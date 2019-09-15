package lk.empire.ams.controller;

import lk.empire.ams.model.dto.MaintenanceDTO;
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
 * <p>Title         : MaintenanceController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/maintenance", value = "MaintenanceController", description = "All services related to MaintenanceController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);

	private final ContractorService contractorService;
	private final FloorService floorService;


    public MaintenanceController(MaintenanceService maintenanceService , ContractorService contractorService, FloorService floorService){
        this.maintenanceService = maintenanceService;
		this.contractorService = contractorService;
		this.floorService = floorService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all maintenances
     * @return ResponseEntity<List<MaintenanceDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getMaintenances(){
        logger.debug("Request to get all Maintenances");
        List<MaintenanceDTO> maintenances = maintenanceService.getMaintenances().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(maintenances == null || maintenances.size() < 1) throw new ResourceNotFoundException("Unable to find any Maintenances");
        return new ResponseEntity(maintenances, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific maintenance by id
     * @param id Long
     * @return ResponseEntity<MaintenanceDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<MaintenanceDTO> getMaintenances(@PathVariable Long id) {
        logger.debug("Request to get a Maintenance by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Maintenance> maintenance = maintenanceService.getByID(id);
        if(maintenance != null && maintenance.isPresent()) return new ResponseEntity(convertToDTO(maintenance.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Maintenance with id " + id);
    }


    /**
     * Persist maintenance. if id > 0 is present expects valid maintenance object already present, and update it by
     * replacing values. Otherwise simply creates a new maintenance and id is returned
     * @param maintenance MaintenanceDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<MaintenanceDTO> saveMaintenance(@RequestBody @Valid MaintenanceDTO maintenance) throws Exception{
        logger.debug("Request to save Maintenance");
        Maintenance existingMaintenance = new Maintenance();
        if(maintenance.getId() != null && maintenance.getId() > 0) {
            //Updating existing maintenance - Check item with matching ID present
            Optional<Maintenance> savedMaintenance = maintenanceService.getByID(maintenance.getId());
            if(savedMaintenance != null && savedMaintenance.isPresent()) existingMaintenance = savedMaintenance.get();
            else throw new ResourceNotFoundException("In order to update Maintenance " + maintenance.getId() + ", existing Maintenance must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Maintenance saveMaintenance = copyToMaintenance(maintenance, existingMaintenance);
        Maintenance savedMaintenance = maintenanceService.saveMaintenance(saveMaintenance);
        if(savedMaintenance.getId() != null && savedMaintenance.getId() > 0){
            logger.info("Saved Maintenance with id " + saveMaintenance.getId());
            MaintenanceDTO savedMaintenanceDTo = convertToDTO(savedMaintenance);
            return  ResponseEntity.created (new URI("/maintenance/" + savedMaintenance.getId())).body(savedMaintenanceDTo);
        }
        else{
            throw new PersistenceException("Maintenance not persisted: " + new Gson().toJson(savedMaintenance));
        }
    }

   /**
     * Delete a maintenance by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Maintenance with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Maintenance> maintenance = maintenanceService.getByID(id);
        if(maintenance == null || !maintenance.isPresent()) throw new ResourceNotFoundException("In order to delete  Maintenance " + id + ", existing  Maintenance must be available with same ID");
        maintenanceService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  maintenances by description
     * @param description String     
     * @return List<Maintenance>
     */
    @GetMapping(path = "description/{description}")
    public ResponseEntity<List<Maintenance>> findAllByDescription(@PathVariable String description){
        logger.debug("findAllByDescription(" + description + ")");
		Assert.notNull(description, "Expects a valid description");

        List<Maintenance> maintenances =  maintenanceService.findAllByDescription(description);
        if(maintenances == null || maintenances.size() < 1) throw new ResourceNotFoundException("Unable to find any maintenances matching criteria");
        return new ResponseEntity(getDTOs(maintenances), HttpStatus.ACCEPTED);
    }
	/**
     * Find  maintenances by blockName
     * @param blockName String     
     * @return List<Maintenance>
     */
    @GetMapping(path = "blockName/{blockName}")
    public ResponseEntity<List<Maintenance>> findAllByBlockName(@PathVariable String blockName){
        logger.debug("findAllByBlockName(" + blockName + ")");
		Assert.notNull(blockName, "Expects a valid blockName");

        List<Maintenance> maintenances =  maintenanceService.findAllByBlockName(blockName);
        if(maintenances == null || maintenances.size() < 1) throw new ResourceNotFoundException("Unable to find any maintenances matching criteria");
        return new ResponseEntity(getDTOs(maintenances), HttpStatus.ACCEPTED);
    }
	/**
     * Find  maintenances by doneBy
     * @param doneBy String     
     * @return List<Maintenance>
     */
    @GetMapping(path = "doneBy/{doneBy}")
    public ResponseEntity<List<Maintenance>> findAllByDoneBy(@PathVariable String doneBy){
        logger.debug("findAllByDoneBy(" + doneBy + ")");
		Assert.notNull(doneBy, "Expects a valid doneBy");

        List<Maintenance> maintenances =  maintenanceService.findAllByDoneBy(doneBy);
        if(maintenances == null || maintenances.size() < 1) throw new ResourceNotFoundException("Unable to find any maintenances matching criteria");
        return new ResponseEntity(getDTOs(maintenances), HttpStatus.ACCEPTED);
    }
	/**
     * Find  maintenances by maintenanceType
     * @param maintenanceType MaintenanceType     
     * @return List<Maintenance>
     */
    @GetMapping(path = "maintenanceType/{maintenanceType}")
    public ResponseEntity<List<Maintenance>> findAllByMaintenanceType(@PathVariable MaintenanceType maintenanceType){
        logger.debug("findAllByMaintenanceType(" + maintenanceType + ")");
		Assert.notNull(maintenanceType, "Expects a valid maintenanceType");

        List<Maintenance> maintenances =  maintenanceService.findAllByMaintenanceType(maintenanceType);
        if(maintenances == null || maintenances.size() < 1) throw new ResourceNotFoundException("Unable to find any maintenances matching criteria");
        return new ResponseEntity(getDTOs(maintenances), HttpStatus.ACCEPTED);
    }
	/**
     * Find  maintenances by status
     * @param status MaintenanceStatus     
     * @return List<Maintenance>
     */
    @GetMapping(path = "status/{status}")
    public ResponseEntity<List<Maintenance>> findAllByStatus(@PathVariable MaintenanceStatus status){
        logger.debug("findAllByStatus(" + status + ")");
		Assert.notNull(status, "Expects a valid status");

        List<Maintenance> maintenances =  maintenanceService.findAllByStatus(status);
        if(maintenances == null || maintenances.size() < 1) throw new ResourceNotFoundException("Unable to find any maintenances matching criteria");
        return new ResponseEntity(getDTOs(maintenances), HttpStatus.ACCEPTED);
    }
	/**
     * Find  maintenances by contractor
     * @param contractor Long     
     * @return List<Maintenance>
     */
    @GetMapping(path = "contractor/{contractor}")
    public ResponseEntity<List<Maintenance>> findAllByContractor_Id(@PathVariable Long contractor){
        logger.debug("findAllByContractor_Id(" + contractor + ")");
		Assert.notNull(contractor, "Expects a valid contractor");
		Assert.isTrue(contractor > 0, "Expects a valid contractor > 0");

        List<Maintenance> maintenances =  maintenanceService.findAllByContractor_Id(contractor);
        if(maintenances == null || maintenances.size() < 1) throw new ResourceNotFoundException("Unable to find any maintenances matching criteria");
        return new ResponseEntity(getDTOs(maintenances), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private MaintenanceDTO convertToDTO(Maintenance maintenance){
        return modelMapper.map(maintenance, MaintenanceDTO.class);
    }

     private List<MaintenanceDTO> getDTOs(List<Maintenance> maintenances){
           if(maintenances == null) return null;
           List<MaintenanceDTO> dtoList = new ArrayList<MaintenanceDTO>(maintenances.size());
           for(Maintenance maintenance: maintenances){
               MaintenanceDTO dto = convertToDTO(maintenance);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Maintenance copyToMaintenance(MaintenanceDTO maintenanceDTO, Maintenance maintenance){
		if(maintenanceDTO.getContractorId() != null && maintenanceDTO.getContractorId() > 0){
            maintenance.setContractor( contractorService.getByID(maintenanceDTO.getContractorId()).get());
        }
		if(maintenanceDTO.getFloorId() != null && maintenanceDTO.getFloorId() > 0){
            maintenance.setFloor( floorService.getByID(maintenanceDTO.getFloorId()).get());
        }

         modelMapper.map(maintenanceDTO, maintenance);
          return maintenance;
    }

}
