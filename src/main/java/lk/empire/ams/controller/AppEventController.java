package lk.empire.ams.controller;

import lk.empire.ams.model.dto.AppEventDTO;
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
 * <p>Title         : AppEventController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/events", value = "AppEventController", description = "All services related to AppEventController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/events")
public class AppEventController {

    private final AppEventService appEventService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(AppEventController.class);

	private final ApartmentService apartmentService;
	private final ClientService userService;
	private final EmployeeService employeeService;


    public AppEventController(AppEventService appEventService , ApartmentService apartmentService, ClientService userService, EmployeeService employeeService){
        this.appEventService = appEventService;
		this.apartmentService = apartmentService;
		this.userService = userService;
		this.employeeService = employeeService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all appEvents
     * @return ResponseEntity<List<AppEventDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getAppEvents(){
        logger.debug("Request to get all AppEvents");
        List<AppEventDTO> appEvents = appEventService.getAppEvents().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(appEvents == null || appEvents.size() < 1) throw new ResourceNotFoundException("Unable to find any AppEvents");
        return new ResponseEntity(appEvents, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific appEvent by id
     * @param id Long
     * @return ResponseEntity<AppEventDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<AppEventDTO> getAppEvents(@PathVariable Long id) {
        logger.debug("Request to get a AppEvent by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<AppEvent> appEvent = appEventService.getByID(id);
        if(appEvent != null && appEvent.isPresent()) return new ResponseEntity(convertToDTO(appEvent.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any AppEvent with id " + id);
    }


    /**
     * Persist appEvent. if id > 0 is present expects valid appEvent object already present, and update it by
     * replacing values. Otherwise simply creates a new appEvent and id is returned
     * @param appEvent AppEventDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<AppEventDTO> saveAppEvent(@RequestBody @Valid AppEventDTO appEvent) throws Exception{
        logger.debug("Request to save AppEvent");
        AppEvent existingAppEvent = new AppEvent();
        if(appEvent.getId() != null && appEvent.getId() > 0) {
            //Updating existing appEvent - Check item with matching ID present
            Optional<AppEvent> savedAppEvent = appEventService.getByID(appEvent.getId());
            if(savedAppEvent != null && savedAppEvent.isPresent()) existingAppEvent = savedAppEvent.get();
            else throw new ResourceNotFoundException("In order to update AppEvent " + appEvent.getId() + ", existing AppEvent must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        AppEvent saveAppEvent = copyToAppEvent(appEvent, existingAppEvent);
        AppEvent savedAppEvent = appEventService.saveAppEvent(saveAppEvent);
        if(savedAppEvent.getId() != null && savedAppEvent.getId() > 0){
            logger.info("Saved AppEvent with id " + saveAppEvent.getId());
            AppEventDTO savedAppEventDTo = convertToDTO(savedAppEvent);
            return  ResponseEntity.created (new URI("/events/" + savedAppEvent.getId())).body(savedAppEventDTo);
        }
        else{
            throw new PersistenceException("AppEvent not persisted: " + new Gson().toJson(savedAppEvent));
        }
    }

   /**
     * Delete a appEvent by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete AppEvent with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<AppEvent> appEvent = appEventService.getByID(id);
        if(appEvent == null || !appEvent.isPresent()) throw new ResourceNotFoundException("In order to delete  AppEvent " + id + ", existing  AppEvent must be available with same ID");
        appEventService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  appEvents by name
     * @param name String     
     * @return List<AppEvent>
     */
    @GetMapping(path = "name/{name}")
    public ResponseEntity<List<AppEvent>> findAllByName(@PathVariable String name){
        logger.debug("findAllByName(" + name + ")");
		Assert.notNull(name, "Expects a valid name");

        List<AppEvent> appEvents =  appEventService.findAllByName(name);
        if(appEvents == null || appEvents.size() < 1) throw new ResourceNotFoundException("Unable to find any appEvents matching criteria");
        return new ResponseEntity(getDTOs(appEvents), HttpStatus.ACCEPTED);
    }
	/**
     * Find  appEvents by date
     * @param date LocalDate     
     * @return List<AppEvent>
     */
    @GetMapping(path = "date/{date}")
    public ResponseEntity<List<AppEvent>> findAllByDate(@PathVariable LocalDate date){
        logger.debug("findAllByDate(" + date + ")");
		Assert.notNull(date, "Expects a valid date");

        List<AppEvent> appEvents =  appEventService.findAllByDate(date);
        if(appEvents == null || appEvents.size() < 1) throw new ResourceNotFoundException("Unable to find any appEvents matching criteria");
        return new ResponseEntity(getDTOs(appEvents), HttpStatus.ACCEPTED);
    }
	/**
     * Find  appEvents by eventType
     * @param eventType String     
     * @return List<AppEvent>
     */
    @GetMapping(path = "eventType/{eventType}")
    public ResponseEntity<List<AppEvent>> findAllByEventType(@PathVariable String eventType){
        logger.debug("findAllByEventType(" + eventType + ")");
		Assert.notNull(eventType, "Expects a valid eventType");

        List<AppEvent> appEvents =  appEventService.findAllByEventType(eventType);
        if(appEvents == null || appEvents.size() < 1) throw new ResourceNotFoundException("Unable to find any appEvents matching criteria");
        return new ResponseEntity(getDTOs(appEvents), HttpStatus.ACCEPTED);
    }
	/**
     * Find  appEvents by apartment
     * @param apartment Long     
     * @return List<AppEvent>
     */
    @GetMapping(path = "apartment/{apartment}")
    public ResponseEntity<List<AppEvent>> findAllByApartment_Id(@PathVariable Long apartment){
        logger.debug("findAllByApartment_Id(" + apartment + ")");
		Assert.notNull(apartment, "Expects a valid apartment");
		Assert.isTrue(apartment > 0, "Expects a valid apartment > 0");

        List<AppEvent> appEvents =  appEventService.findAllByApartment_Id(apartment);
        if(appEvents == null || appEvents.size() < 1) throw new ResourceNotFoundException("Unable to find any appEvents matching criteria");
        return new ResponseEntity(getDTOs(appEvents), HttpStatus.ACCEPTED);
    }
	/**
     * Find  appEvents by user
     * @param user Long     
     * @return List<AppEvent>
     */
    @GetMapping(path = "user/{user}")
    public ResponseEntity<List<AppEvent>> findAllByUser_Id(@PathVariable Long user){
        logger.debug("findAllByUser_Id(" + user + ")");
		Assert.notNull(user, "Expects a valid user");
		Assert.isTrue(user > 0, "Expects a valid user > 0");

        List<AppEvent> appEvents =  appEventService.findAllByUser_Id(user);
        if(appEvents == null || appEvents.size() < 1) throw new ResourceNotFoundException("Unable to find any appEvents matching criteria");
        return new ResponseEntity(getDTOs(appEvents), HttpStatus.ACCEPTED);
    }
	/**
     * Find  appEvents by employee
     * @param employee Long     
     * @return List<AppEvent>
     */
    @GetMapping(path = "employee/{employee}")
    public ResponseEntity<List<AppEvent>> findAllByEmployee_Id(@PathVariable Long employee){
        logger.debug("findAllByEmployee_Id(" + employee + ")");
		Assert.notNull(employee, "Expects a valid employee");
		Assert.isTrue(employee > 0, "Expects a valid employee > 0");

        List<AppEvent> appEvents =  appEventService.findAllByEmployee_Id(employee);
        if(appEvents == null || appEvents.size() < 1) throw new ResourceNotFoundException("Unable to find any appEvents matching criteria");
        return new ResponseEntity(getDTOs(appEvents), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private AppEventDTO convertToDTO(AppEvent appEvent){
        return modelMapper.map(appEvent, AppEventDTO.class);
    }

     private List<AppEventDTO> getDTOs(List<AppEvent> appEvents){
           if(appEvents == null) return null;
           List<AppEventDTO> dtoList = new ArrayList<AppEventDTO>(appEvents.size());
           for(AppEvent appEvent: appEvents){
               AppEventDTO dto = convertToDTO(appEvent);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private AppEvent copyToAppEvent(AppEventDTO appEventDTO, AppEvent appEvent){
		if(appEventDTO.getApartmentId() != null && appEventDTO.getApartmentId() > 0){
            appEvent.setApartment( apartmentService.getByID(appEventDTO.getApartmentId()).get());
        }
		if(appEventDTO.getUserId() != null && appEventDTO.getUserId() > 0){
            appEvent.setUser( userService.getByID(appEventDTO.getUserId()).get());
        }
		if(appEventDTO.getEmployeeId() != null && appEventDTO.getEmployeeId() > 0){
            appEvent.setEmployee( employeeService.getByID(appEventDTO.getEmployeeId()).get());
        }

         modelMapper.map(appEventDTO, appEvent);
          return appEvent;
    }

}
