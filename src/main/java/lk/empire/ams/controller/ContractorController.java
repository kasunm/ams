package lk.empire.ams.controller;

import lk.empire.ams.model.dto.ContractorDTO;
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
 * <p>Title         : ContractorController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/contractors", value = "ContractorController", description = "All services related to ContractorController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/contractors")
public class ContractorController {

    private final ContractorService contractorService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(ContractorController.class);

 

    public ContractorController(ContractorService contractorService  ){
        this.contractorService = contractorService;
 
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all contractors
     * @return ResponseEntity<List<ContractorDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getContractors(){
        logger.debug("Request to get all Contractors");
        List<ContractorDTO> contractors = contractorService.getContractors().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(contractors == null || contractors.size() < 1) throw new ResourceNotFoundException("Unable to find any Contractors");
        return new ResponseEntity(contractors, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific contractor by id
     * @param id Long
     * @return ResponseEntity<ContractorDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<ContractorDTO> getContractors(@PathVariable Long id) {
        logger.debug("Request to get a Contractor by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Contractor> contractor = contractorService.getByID(id);
        if(contractor != null && contractor.isPresent()) return new ResponseEntity(convertToDTO(contractor.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Contractor with id " + id);
    }


    /**
     * Persist contractor. if id > 0 is present expects valid contractor object already present, and update it by
     * replacing values. Otherwise simply creates a new contractor and id is returned
     * @param contractor ContractorDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<ContractorDTO> saveContractor(@RequestBody @Valid ContractorDTO contractor) throws Exception{
        logger.debug("Request to save Contractor");
        Contractor existingContractor = new Contractor();
        if(contractor.getId() != null && contractor.getId() > 0) {
            //Updating existing contractor - Check item with matching ID present
            Optional<Contractor> savedContractor = contractorService.getByID(contractor.getId());
            if(savedContractor != null && savedContractor.isPresent()) existingContractor = savedContractor.get();
            else throw new ResourceNotFoundException("In order to update Contractor " + contractor.getId() + ", existing Contractor must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Contractor saveContractor = copyToContractor(contractor, existingContractor);
        Contractor savedContractor = contractorService.saveContractor(saveContractor);
        if(savedContractor.getId() != null && savedContractor.getId() > 0){
            logger.info("Saved Contractor with id " + saveContractor.getId());
            ContractorDTO savedContractorDTo = convertToDTO(savedContractor);
            return  ResponseEntity.created (new URI("/contractors/" + savedContractor.getId())).body(savedContractorDTo);
        }
        else{
            throw new PersistenceException("Contractor not persisted: " + new Gson().toJson(savedContractor));
        }
    }

   /**
     * Delete a contractor by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Contractor with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Contractor> contractor = contractorService.getByID(id);
        if(contractor == null || !contractor.isPresent()) throw new ResourceNotFoundException("In order to delete  Contractor " + id + ", existing  Contractor must be available with same ID");
        contractorService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  contractors by companyName
     * @param companyName String     
     * @return List<Contractor>
     */
    @GetMapping(path = "companyName/{companyName}")
    public ResponseEntity<List<Contractor>> findAllByCompanyName(@PathVariable String companyName){
        logger.debug("findAllByCompanyName(" + companyName + ")");
		Assert.notNull(companyName, "Expects a valid companyName");

        List<Contractor> contractors =  contractorService.findAllByCompanyName(companyName);
        if(contractors == null || contractors.size() < 1) throw new ResourceNotFoundException("Unable to find any contractors matching criteria");
        return new ResponseEntity(getDTOs(contractors), HttpStatus.ACCEPTED);
    }
	/**
     * Find  contractors by firstName
     * @param firstName String     
     * @return List<Contractor>
     */
    @GetMapping(path = "firstName/{firstName}")
    public ResponseEntity<List<Contractor>> findAllByFirstName(@PathVariable String firstName){
        logger.debug("findAllByFirstName(" + firstName + ")");
		Assert.notNull(firstName, "Expects a valid firstName");

        List<Contractor> contractors =  contractorService.findAllByFirstName(firstName);
        if(contractors == null || contractors.size() < 1) throw new ResourceNotFoundException("Unable to find any contractors matching criteria");
        return new ResponseEntity(getDTOs(contractors), HttpStatus.ACCEPTED);
    }
	/**
     * Find  contractors by email
     * @param email String     
     * @return List<Contractor>
     */
    @GetMapping(path = "email/{email}")
    public ResponseEntity<List<Contractor>> findAllByEmail(@PathVariable String email){
        logger.debug("findAllByEmail(" + email + ")");
		Assert.notNull(email, "Expects a valid email");

        List<Contractor> contractors =  contractorService.findAllByEmail(email);
        if(contractors == null || contractors.size() < 1) throw new ResourceNotFoundException("Unable to find any contractors matching criteria");
        return new ResponseEntity(getDTOs(contractors), HttpStatus.ACCEPTED);
    }
	/**
     * Find  contractors by nic
     * @param nic String     
     * @return List<Contractor>
     */
    @GetMapping(path = "nic/{nic}")
    public ResponseEntity<List<Contractor>> findAllByNic(@PathVariable String nic){
        logger.debug("findAllByNic(" + nic + ")");
		Assert.notNull(nic, "Expects a valid nic");

        List<Contractor> contractors =  contractorService.findAllByNic(nic);
        if(contractors == null || contractors.size() < 1) throw new ResourceNotFoundException("Unable to find any contractors matching criteria");
        return new ResponseEntity(getDTOs(contractors), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private ContractorDTO convertToDTO(Contractor contractor){
        return modelMapper.map(contractor, ContractorDTO.class);
    }

     private List<ContractorDTO> getDTOs(List<Contractor> contractors){
           if(contractors == null) return null;
           List<ContractorDTO> dtoList = new ArrayList<ContractorDTO>(contractors.size());
           for(Contractor contractor: contractors){
               ContractorDTO dto = convertToDTO(contractor);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Contractor copyToContractor(ContractorDTO contractorDTO, Contractor contractor){
 
         modelMapper.map(contractorDTO, contractor);
          return contractor;
    }

}
