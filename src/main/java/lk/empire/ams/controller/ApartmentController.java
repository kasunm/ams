package lk.empire.ams.controller;

import lk.empire.ams.model.dto.ApartmentDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.service.*;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import lk.empire.ams.util.EmailSender;
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
 * <p>Title         : ApartmentController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Apartment. An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/apartments", value = "ApartmentController", description = "All services related to ApartmentController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/apartments")
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(ApartmentController.class);
    @Autowired
    private EmailSender emailSender;

 

    public ApartmentController(ApartmentService apartmentService  ){
        this.apartmentService = apartmentService;
 
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all apartments
     * @return ResponseEntity<List<ApartmentDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getApartments(){
        logger.debug("Request to get all Apartments");
      //  emailSender.sendEmail("kasunm@gmail.com", "eee", "asdasdasd ddd");
        List<ApartmentDTO> apartments = apartmentService.getApartments().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(apartments == null || apartments.size() < 1) throw new ResourceNotFoundException("Unable to find any Apartments");
        return new ResponseEntity(apartments, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific apartment by id
     * @param id Long
     * @return ResponseEntity<ApartmentDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<ApartmentDTO> getApartments(@PathVariable Long id) {
        logger.debug("Request to get a Apartment by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Apartment> apartment = apartmentService.getByID(id);
        if(apartment != null && apartment.isPresent()) return new ResponseEntity(convertToDTO(apartment.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Apartment with id " + id);
    }


    /**
     * Persist apartment. if id > 0 is present expects valid apartment object already present, and update it by
     * replacing values. Otherwise simply creates a new apartment and id is returned
     * @param apartment ApartmentDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<ApartmentDTO> saveApartment(@RequestBody @Valid ApartmentDTO apartment) throws Exception{
        logger.debug("Request to save Apartment");
        Apartment existingApartment = new Apartment();
        if(apartment.getId() != null && apartment.getId() > 0) {
            //Updating existing apartment - Check item with matching ID present
            Optional<Apartment> savedApartment = apartmentService.getByID(apartment.getId());
            if(savedApartment != null && savedApartment.isPresent()) existingApartment = savedApartment.get();
            else throw new ResourceNotFoundException("In order to update Apartment " + apartment.getId() + ", existing Apartment must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Apartment saveApartment = copyToApartment(apartment, existingApartment);
        Apartment savedApartment = apartmentService.saveApartment(saveApartment);
        if(savedApartment.getId() != null && savedApartment.getId() > 0){
            logger.info("Saved Apartment with id " + saveApartment.getId());
            ApartmentDTO savedApartmentDTo = convertToDTO(savedApartment);
            return  ResponseEntity.created (new URI("/apartments/" + savedApartment.getId())).body(savedApartmentDTo);
        }
        else{
            throw new PersistenceException("Apartment not persisted: " + new Gson().toJson(savedApartment));
        }
    }

   /**
     * Delete a apartment by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Apartment with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Apartment> apartment = apartmentService.getByID(id);
        if(apartment == null || !apartment.isPresent()) throw new ResourceNotFoundException("In order to delete  Apartment " + id + ", existing  Apartment must be available with same ID");
        apartmentService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  apartments by name
     * @param name String     
     * @return List<Apartment>
     */
    @GetMapping(path = "Name/{name}")
    public ResponseEntity<List<Apartment>> findAllByName(@PathVariable String name){
        logger.debug("findAllByName(" + name + ")");
		Assert.notNull(name, "Expects a valid name");

        List<Apartment> apartments =  apartmentService.findAllByName(name);
        if(apartments == null || apartments.size() < 1) throw new ResourceNotFoundException("Unable to find any apartments matching criteria");
        return new ResponseEntity(getDTOs(apartments), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private ApartmentDTO convertToDTO(Apartment apartment){
        return modelMapper.map(apartment, ApartmentDTO.class);
    }

     private List<ApartmentDTO> getDTOs(List<Apartment> apartments){
           if(apartments == null) return null;
           List<ApartmentDTO> dtoList = new ArrayList<ApartmentDTO>(apartments.size());
           for(Apartment apartment: apartments){
               ApartmentDTO dto = convertToDTO(apartment);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Apartment copyToApartment(ApartmentDTO apartmentDTO, Apartment apartment){
 
         modelMapper.map(apartmentDTO, apartment);
          return apartment;
    }

}
