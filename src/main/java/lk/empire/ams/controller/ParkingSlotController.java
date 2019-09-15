package lk.empire.ams.controller;

import lk.empire.ams.model.dto.ParkingSlotDTO;
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
 * <p>Title         : ParkingSlotController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/parkingslots", value = "ParkingSlotController", description = "All services related to ParkingSlotController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/parkingslots")
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(ParkingSlotController.class);

	private final UnitService unitService;


    public ParkingSlotController(ParkingSlotService parkingSlotService , UnitService unitService){
        this.parkingSlotService = parkingSlotService;
		this.unitService = unitService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all parkingSlots
     * @return ResponseEntity<List<ParkingSlotDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getParkingSlots(){
        logger.debug("Request to get all Parking slots");
        List<ParkingSlotDTO> parkingSlots = parkingSlotService.getParkingSlots().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(parkingSlots == null || parkingSlots.size() < 1) throw new ResourceNotFoundException("Unable to find any Parking slots");
        return new ResponseEntity(parkingSlots, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific parkingSlot by id
     * @param id Long
     * @return ResponseEntity<ParkingSlotDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<ParkingSlotDTO> getParkingSlots(@PathVariable Long id) {
        logger.debug("Request to get a ParkingSlot by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<ParkingSlot> parkingSlot = parkingSlotService.getByID(id);
        if(parkingSlot != null && parkingSlot.isPresent()) return new ResponseEntity(convertToDTO(parkingSlot.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Parking slot with id " + id);
    }


    /**
     * Persist parkingSlot. if id > 0 is present expects valid parkingSlot object already present, and update it by
     * replacing values. Otherwise simply creates a new parkingSlot and id is returned
     * @param parkingSlot ParkingSlotDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<ParkingSlotDTO> saveParkingSlot(@RequestBody @Valid ParkingSlotDTO parkingSlot) throws Exception{
        logger.debug("Request to save Parking slot");
        ParkingSlot existingParkingSlot = new ParkingSlot();
        if(parkingSlot.getId() != null && parkingSlot.getId() > 0) {
            //Updating existing parkingSlot - Check item with matching ID present
            Optional<ParkingSlot> savedParkingSlot = parkingSlotService.getByID(parkingSlot.getId());
            if(savedParkingSlot != null && savedParkingSlot.isPresent()) existingParkingSlot = savedParkingSlot.get();
            else throw new ResourceNotFoundException("In order to update Parking slot " + parkingSlot.getId() + ", existing Parking slot must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        ParkingSlot saveParkingSlot = copyToParkingSlot(parkingSlot, existingParkingSlot);
        ParkingSlot savedParkingSlot = parkingSlotService.saveParkingSlot(saveParkingSlot);
        if(savedParkingSlot.getId() != null && savedParkingSlot.getId() > 0){
            logger.info("Saved Parking slot with id " + saveParkingSlot.getId());
            ParkingSlotDTO savedParkingSlotDTo = convertToDTO(savedParkingSlot);
            return  ResponseEntity.created (new URI("/parkingslots/" + savedParkingSlot.getId())).body(savedParkingSlotDTo);
        }
        else{
            throw new PersistenceException("ParkingSlot not persisted: " + new Gson().toJson(savedParkingSlot));
        }
    }

   /**
     * Delete a parkingSlot by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete ParkingSlot with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<ParkingSlot> parkingSlot = parkingSlotService.getByID(id);
        if(parkingSlot == null || !parkingSlot.isPresent()) throw new ResourceNotFoundException("In order to delete  Parking slot " + id + ", existing  Parking slot must be available with same ID");
        parkingSlotService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  parkingSlots by name
     * @param name String     
     * @return List<ParkingSlot>
     */
    @GetMapping(path = "name/{name}")
    public ResponseEntity<List<ParkingSlot>> findAllByName(@PathVariable String name){
        logger.debug("findAllByName(" + name + ")");
		Assert.notNull(name, "Expects a valid name");

        List<ParkingSlot> parkingSlots =  parkingSlotService.findAllByName(name);
        if(parkingSlots == null || parkingSlots.size() < 1) throw new ResourceNotFoundException("Unable to find any parkingSlots matching criteria");
        return new ResponseEntity(getDTOs(parkingSlots), HttpStatus.ACCEPTED);
    }
	/**
     * Find  parkingSlots by unit
     * @param unit Long     
     * @return List<ParkingSlot>
     */
    @GetMapping(path = "unit/{unit}")
    public ResponseEntity<List<ParkingSlot>> findAllByUnit_Id(@PathVariable Long unit){
        logger.debug("findAllByUnit_Id(" + unit + ")");
		Assert.notNull(unit, "Expects a valid unit");
		Assert.isTrue(unit > 0, "Expects a valid unit > 0");

        List<ParkingSlot> parkingSlots =  parkingSlotService.findAllByUnit_Id(unit);
        if(parkingSlots == null || parkingSlots.size() < 1) throw new ResourceNotFoundException("Unable to find any parkingSlots matching criteria");
        return new ResponseEntity(getDTOs(parkingSlots), HttpStatus.ACCEPTED);
    }
	/**
     * Find  parkingSlots by vehicleNumber
     * @param vehicleNumber String     
     * @return List<ParkingSlot>
     */
    @GetMapping(path = "vehicleNumber/{vehicleNumber}")
    public ResponseEntity<List<ParkingSlot>> findAllByVehicleNumber(@PathVariable String vehicleNumber){
        logger.debug("findAllByVehicleNumber(" + vehicleNumber + ")");
		Assert.notNull(vehicleNumber, "Expects a valid vehicleNumber");

        List<ParkingSlot> parkingSlots =  parkingSlotService.findAllByVehicleNumber(vehicleNumber);
        if(parkingSlots == null || parkingSlots.size() < 1) throw new ResourceNotFoundException("Unable to find any parkingSlots matching criteria");
        return new ResponseEntity(getDTOs(parkingSlots), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private ParkingSlotDTO convertToDTO(ParkingSlot parkingSlot){
        return modelMapper.map(parkingSlot, ParkingSlotDTO.class);
    }

     private List<ParkingSlotDTO> getDTOs(List<ParkingSlot> parkingSlots){
           if(parkingSlots == null) return null;
           List<ParkingSlotDTO> dtoList = new ArrayList<ParkingSlotDTO>(parkingSlots.size());
           for(ParkingSlot parkingSlot: parkingSlots){
               ParkingSlotDTO dto = convertToDTO(parkingSlot);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private ParkingSlot copyToParkingSlot(ParkingSlotDTO parkingSlotDTO, ParkingSlot parkingSlot){
		if(parkingSlotDTO.getUnitId() != null && parkingSlotDTO.getUnitId() > 0){
            parkingSlot.setUnit( unitService.getByID(parkingSlotDTO.getUnitId()).get());
        }

         modelMapper.map(parkingSlotDTO, parkingSlot);
          return parkingSlot;
    }

}
