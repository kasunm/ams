package lk.empire.ams.controller;

import lk.empire.ams.model.dto.VehicleParkingDTO;
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
 * <p>Title         : VehicleParkingController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/vehicle-parking", value = "VehicleParkingController", description = "All services related to VehicleParkingController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/vehicle-parking")
public class VehicleParkingController {

    private final VehicleParkingService vehicleParkingService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(VehicleParkingController.class);

	private final VehicleService vehicleService;
	private final ParkingSlotService parkingSlotService;


    public VehicleParkingController(VehicleParkingService vehicleParkingService , VehicleService vehicleService, ParkingSlotService parkingSlotService){
        this.vehicleParkingService = vehicleParkingService;
		this.vehicleService = vehicleService;
		this.parkingSlotService = parkingSlotService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all vehicleParkings
     * @return ResponseEntity<List<VehicleParkingDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getVehicleParkings(){
        logger.debug("Request to get all Vehicle Parkings");
        List<VehicleParkingDTO> vehicleParkings = vehicleParkingService.getVehicleParkings().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(vehicleParkings == null || vehicleParkings.size() < 1) throw new ResourceNotFoundException("Unable to find any Vehicle Parkings");
        return new ResponseEntity(vehicleParkings, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific vehicleParking by id
     * @param id Long
     * @return ResponseEntity<VehicleParkingDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<VehicleParkingDTO> getVehicleParkings(@PathVariable Long id) {
        logger.debug("Request to get a VehicleParking by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<VehicleParking> vehicleParking = vehicleParkingService.getByID(id);
        if(vehicleParking != null && vehicleParking.isPresent()) return new ResponseEntity(convertToDTO(vehicleParking.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Vehicle Parking with id " + id);
    }


    /**
     * Persist vehicleParking. if id > 0 is present expects valid vehicleParking object already present, and update it by
     * replacing values. Otherwise simply creates a new vehicleParking and id is returned
     * @param vehicleParking VehicleParkingDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<VehicleParkingDTO> saveVehicleParking(@RequestBody @Valid VehicleParkingDTO vehicleParking) throws Exception{
        logger.debug("Request to save Vehicle Parking");
        VehicleParking existingVehicleParking = new VehicleParking();
        if(vehicleParking.getId() != null && vehicleParking.getId() > 0) {
            //Updating existing vehicleParking - Check item with matching ID present
            Optional<VehicleParking> savedVehicleParking = vehicleParkingService.getByID(vehicleParking.getId());
            if(savedVehicleParking != null && savedVehicleParking.isPresent()) existingVehicleParking = savedVehicleParking.get();
            else throw new ResourceNotFoundException("In order to update Vehicle Parking " + vehicleParking.getId() + ", existing Vehicle Parking must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        VehicleParking saveVehicleParking = copyToVehicleParking(vehicleParking, existingVehicleParking);
        VehicleParking savedVehicleParking = vehicleParkingService.saveVehicleParking(saveVehicleParking);
        if(savedVehicleParking.getId() != null && savedVehicleParking.getId() > 0){
            logger.info("Saved Vehicle Parking with id " + saveVehicleParking.getId());
            VehicleParkingDTO savedVehicleParkingDTo = convertToDTO(savedVehicleParking);
            return  ResponseEntity.created (new URI("/vehicle-parking/" + savedVehicleParking.getId())).body(savedVehicleParkingDTo);
        }
        else{
            throw new PersistenceException("VehicleParking not persisted: " + new Gson().toJson(savedVehicleParking));
        }
    }

   /**
     * Delete a vehicleParking by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete VehicleParking with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<VehicleParking> vehicleParking = vehicleParkingService.getByID(id);
        if(vehicleParking == null || !vehicleParking.isPresent()) throw new ResourceNotFoundException("In order to delete  Vehicle Parking " + id + ", existing  Vehicle Parking must be available with same ID");
        vehicleParkingService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  vehicleParkings by parkingSlot
     * @param parkingSlot Long     
     * @return List<VehicleParking>
     */
    @GetMapping(path = "parkingSlot/{parkingSlot}")
    public ResponseEntity<List<VehicleParking>> findAllByParkingSlot_Id(@PathVariable Long parkingSlot){
        logger.debug("findAllByParkingSlot_Id(" + parkingSlot + ")");
		Assert.notNull(parkingSlot, "Expects a valid parkingSlot");
		Assert.isTrue(parkingSlot > 0, "Expects a valid parkingSlot > 0");

        List<VehicleParking> vehicleParkings =  vehicleParkingService.findAllByParkingSlot_Id(parkingSlot);
        if(vehicleParkings == null || vehicleParkings.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicleParkings matching criteria");
        return new ResponseEntity(getDTOs(vehicleParkings), HttpStatus.ACCEPTED);
    }
	/**
     * Find  vehicleParkings by driverID
     * @param driverID String     
     * @return List<VehicleParking>
     */
    @GetMapping(path = "driverID/{driverID}")
    public ResponseEntity<List<VehicleParking>> findAllByDriverID(@PathVariable String driverID){
        logger.debug("findAllByDriverID(" + driverID + ")");
		Assert.notNull(driverID, "Expects a valid driverID");

        List<VehicleParking> vehicleParkings =  vehicleParkingService.findAllByDriverID(driverID);
        if(vehicleParkings == null || vehicleParkings.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicleParkings matching criteria");
        return new ResponseEntity(getDTOs(vehicleParkings), HttpStatus.ACCEPTED);
    }
	/**
     * Find  vehicleParkings by inDate
     * @param inDate LocalDate     
     * @return List<VehicleParking>
     */
    @GetMapping(path = "inDate/{inDate}")
    public ResponseEntity<List<VehicleParking>> findAllByInDate(@PathVariable LocalDate inDate){
        logger.debug("findAllByInDate(" + inDate + ")");
		Assert.notNull(inDate, "Expects a valid inDate");

        List<VehicleParking> vehicleParkings =  vehicleParkingService.findAllByInDate(inDate);
        if(vehicleParkings == null || vehicleParkings.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicleParkings matching criteria");
        return new ResponseEntity(getDTOs(vehicleParkings), HttpStatus.ACCEPTED);
    }
	/**
     * Find  vehicleParkings by outDate
     * @param outDate LocalDate     
     * @return List<VehicleParking>
     */
    @GetMapping(path = "outDate/{outDate}")
    public ResponseEntity<List<VehicleParking>> findAllByOutDate(@PathVariable LocalDate outDate){
        logger.debug("findAllByOutDate(" + outDate + ")");
		Assert.notNull(outDate, "Expects a valid outDate");

        List<VehicleParking> vehicleParkings =  vehicleParkingService.findAllByOutDate(outDate);
        if(vehicleParkings == null || vehicleParkings.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicleParkings matching criteria");
        return new ResponseEntity(getDTOs(vehicleParkings), HttpStatus.ACCEPTED);
    }
	/**
     * Find  vehicleParkings by vehicle
     * @param vehicle Long     
     * @return List<VehicleParking>
     */
    @GetMapping(path = "vehicle-id/{vehicle}")
    public ResponseEntity<List<VehicleParking>> findAllByVehicle_Id(@PathVariable Long vehicle){
        logger.debug("findAllByVehicle_Id(" + vehicle + ")");
		Assert.notNull(vehicle, "Expects a valid vehicle");
		Assert.isTrue(vehicle > 0, "Expects a valid vehicle > 0");

        List<VehicleParking> vehicleParkings =  vehicleParkingService.findAllByVehicle_Id(vehicle);
        if(vehicleParkings == null || vehicleParkings.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicleParkings matching criteria");
        return new ResponseEntity(getDTOs(vehicleParkings), HttpStatus.ACCEPTED);
    }
	/**
     * Find  vehicleParkings by vehicle
     * @param vehicle Long     
     * @return List<VehicleParking>
     */
    @GetMapping(path = "vehicle-number/{vehicle}")
    public ResponseEntity<List<VehicleParking>> findAllByVehicle_number(@PathVariable Long vehicle){
        logger.debug("findAllByVehicle_number(" + vehicle + ")");
		Assert.notNull(vehicle, "Expects a valid vehicle");
		Assert.isTrue(vehicle > 0, "Expects a valid vehicle > 0");

        List<VehicleParking> vehicleParkings =  vehicleParkingService.findAllByVehicle_number(vehicle);
        if(vehicleParkings == null || vehicleParkings.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicleParkings matching criteria");
        return new ResponseEntity(getDTOs(vehicleParkings), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private VehicleParkingDTO convertToDTO(VehicleParking vehicleParking){
        return modelMapper.map(vehicleParking, VehicleParkingDTO.class);
    }

     private List<VehicleParkingDTO> getDTOs(List<VehicleParking> vehicleParkings){
           if(vehicleParkings == null) return null;
           List<VehicleParkingDTO> dtoList = new ArrayList<VehicleParkingDTO>(vehicleParkings.size());
           for(VehicleParking vehicleParking: vehicleParkings){
               VehicleParkingDTO dto = convertToDTO(vehicleParking);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private VehicleParking copyToVehicleParking(VehicleParkingDTO vehicleParkingDTO, VehicleParking vehicleParking){
		if(vehicleParkingDTO.getVehicleId() != null && vehicleParkingDTO.getVehicleId() > 0){
            vehicleParking.setVehicle( vehicleService.getByID(vehicleParkingDTO.getVehicleId()).get());
        }
		if(vehicleParkingDTO.getParkingSlotId() != null && vehicleParkingDTO.getParkingSlotId() > 0){
            vehicleParking.setParkingSlot( parkingSlotService.getByID(vehicleParkingDTO.getParkingSlotId()).get());
        }

         modelMapper.map(vehicleParkingDTO, vehicleParking);
          return vehicleParking;
    }

}
