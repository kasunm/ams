package lk.empire.ams.controller;

import lk.empire.ams.model.dto.InquiryDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.service.*;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import javax.persistence.PersistenceException;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.*;
import java.util.stream.Collectors;
/**
 * <p>Title         : InquiryController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Inquiry. An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/inquiries", value = "InquiryController", description = "All services related to InquiryController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(InquiryController.class);

	private final ClientService clientService;
	private final EmployeeService employeeService;


    public InquiryController(InquiryService inquiryService , ClientService clientService, EmployeeService employeeService){
        this.inquiryService = inquiryService;
		this.clientService = clientService;
		this.employeeService = employeeService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all inquirys
     * @return ResponseEntity<List<InquiryDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getInquirys(){
        logger.debug("Request to get all Inquirys");
        List<InquiryDTO> inquirys = inquiryService.getInquirys().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(inquirys == null || inquirys.size() < 1) throw new ResourceNotFoundException("Unable to find any Inquirys");
        return new ResponseEntity(inquirys, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific inquiry by id
     * @param id Long
     * @return ResponseEntity<InquiryDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<InquiryDTO> getInquirys(@PathVariable Long id) {
        logger.debug("Request to get a Inquiry by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Inquiry> inquiry = inquiryService.getByID(id);
        if(inquiry != null && inquiry.isPresent()) return new ResponseEntity(convertToDTO(inquiry.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Inquiry with id " + id);
    }


    /**
     * Persist inquiry. if id > 0 is present expects valid inquiry object already present, and update it by
     * replacing values. Otherwise simply creates a new inquiry and id is returned
     * @param inquiry InquiryDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<InquiryDTO> saveInquiry(@RequestBody @Valid InquiryDTO inquiry) throws Exception{
        logger.debug("Request to save Inquiry");
        Inquiry existingInquiry = new Inquiry();
        if(inquiry.getId() != null && inquiry.getId() > 0) {
            //Updating existing inquiry - Check item with matching ID present
            Optional<Inquiry> savedInquiry = inquiryService.getByID(inquiry.getId());
            if(savedInquiry != null && savedInquiry.isPresent()) existingInquiry = savedInquiry.get();
            else throw new ResourceNotFoundException("In order to update Inquiry " + inquiry.getId() + ", existing Inquiry must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Inquiry saveInquiry = copyToInquiry(inquiry, existingInquiry);
        Inquiry savedInquiry = inquiryService.saveInquiry(saveInquiry);
        if(savedInquiry.getId() != null && savedInquiry.getId() > 0){
            logger.info("Saved Inquiry with id " + saveInquiry.getId());
            InquiryDTO savedInquiryDTo = convertToDTO(savedInquiry);
            return  ResponseEntity.created (new URI("/inquiries/" + savedInquiry.getId())).body(savedInquiryDTo);
        }
        else{
            throw new PersistenceException("Inquiry not persisted: " + new Gson().toJson(savedInquiry));
        }
    }

   /**
     * Delete a inquiry by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Inquiry with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Inquiry> inquiry = inquiryService.getByID(id);
        if(inquiry == null || !inquiry.isPresent()) throw new ResourceNotFoundException("In order to delete  Inquiry " + id + ", existing  Inquiry must be available with same ID");
        inquiryService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  inquirys by description
     * @param description String     
     * @return List<Inquiry>
     */
    @GetMapping(path = "description/{description}")
    public ResponseEntity<List<Inquiry>> findAllByDescription(@PathVariable String description){
        logger.debug("findAllByDescription(" + description + ")");
		Assert.notNull(description, "Expects a valid description");

        List<Inquiry> inquirys =  inquiryService.findAllByDescription(description);
        if(inquirys == null || inquirys.size() < 1) throw new ResourceNotFoundException("Unable to find any inquirys matching criteria");
        return new ResponseEntity(getDTOs(inquirys), HttpStatus.ACCEPTED);
    }
	/**
     * Find  inquirys by type
     * @param type InquiryType     
     * @return List<Inquiry>
     */
    @GetMapping(path = "type/{type}")
    public ResponseEntity<List<Inquiry>> findAllByType(@PathVariable InquiryType type){
        logger.debug("findAllByType(" + type + ")");
		Assert.notNull(type, "Expects a valid type");

        List<Inquiry> inquirys =  inquiryService.findAllByType(type);
        if(inquirys == null || inquirys.size() < 1) throw new ResourceNotFoundException("Unable to find any inquirys matching criteria");
        return new ResponseEntity(getDTOs(inquirys), HttpStatus.ACCEPTED);
    }
	/**
     * Find  inquirys by action
     * @param action InquiryAction     
     * @return List<Inquiry>
     */
    @GetMapping(path = "action/{action}")
    public ResponseEntity<List<Inquiry>> findAllByAction(@PathVariable InquiryAction action){
        logger.debug("findAllByAction(" + action + ")");
		Assert.notNull(action, "Expects a valid action");

        List<Inquiry> inquirys =  inquiryService.findAllByAction(action);
        if(inquirys == null || inquirys.size() < 1) throw new ResourceNotFoundException("Unable to find any inquirys matching criteria");
        return new ResponseEntity(getDTOs(inquirys), HttpStatus.ACCEPTED);
    }
	/**
     * Find  inquirys by client
     * @param client Long     
     * @return List<Inquiry>
     */
    @GetMapping(path = "client/{client}")
    public ResponseEntity<List<Inquiry>> findAllByClient_Id(@PathVariable Long client){
        logger.debug("findAllByClient_Id(" + client + ")");
		Assert.notNull(client, "Expects a valid client");
		Assert.isTrue(client > 0, "Expects a valid client > 0");

        List<Inquiry> inquirys =  inquiryService.findAllByClient_Id(client);
        if(inquirys == null || inquirys.size() < 1) throw new ResourceNotFoundException("Unable to find any inquirys matching criteria");
        return new ResponseEntity(getDTOs(inquirys), HttpStatus.ACCEPTED);
    }
	/**
     * Find  inquirys by employee
     * @param employee Long     
     * @return List<Inquiry>
     */
    @GetMapping(path = "employee/{employee}")
    public ResponseEntity<List<Inquiry>> findAllByEmployee_Id(@PathVariable Long employee){
        logger.debug("findAllByEmployee_Id(" + employee + ")");
		Assert.notNull(employee, "Expects a valid employee");
		Assert.isTrue(employee > 0, "Expects a valid employee > 0");

        List<Inquiry> inquirys =  inquiryService.findAllByEmployee_Id(employee);
        if(inquirys == null || inquirys.size() < 1) throw new ResourceNotFoundException("Unable to find any inquirys matching criteria");
        return new ResponseEntity(getDTOs(inquirys), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private InquiryDTO convertToDTO(Inquiry inquiry){
        return modelMapper.map(inquiry, InquiryDTO.class);
    }

     private List<InquiryDTO> getDTOs(List<Inquiry> inquirys){
           if(inquirys == null) return null;
           List<InquiryDTO> dtoList = new ArrayList<InquiryDTO>(inquirys.size());
           for(Inquiry inquiry: inquirys){
               InquiryDTO dto = convertToDTO(inquiry);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Inquiry copyToInquiry(InquiryDTO inquiryDTO, Inquiry inquiry){
		if(inquiryDTO.getClientId() != null && inquiryDTO.getClientId() > 0){
            inquiry.setClient( clientService.getByID(inquiryDTO.getClientId()).get());
        }
		if(inquiryDTO.getEmployeeId() != null && inquiryDTO.getEmployeeId() > 0){
            inquiry.setEmployee( employeeService.getByID(inquiryDTO.getEmployeeId()).get());
        }

         modelMapper.map(inquiryDTO, inquiry);
          return inquiry;
    }

}
