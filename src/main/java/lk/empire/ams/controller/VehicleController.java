package lk.empire.ams.controller;

import lk.empire.ams.model.dto.VehicleDTO;
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
 * <p>Title         : VehicleController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Vehicle. A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/vehicles", value = "VehicleController", description = "All services related to VehicleController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	private final UnitService unitService;


    public VehicleController(VehicleService vehicleService , UnitService unitService){
        this.vehicleService = vehicleService;
		this.unitService = unitService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all vehicles
     * @return ResponseEntity<List<VehicleDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getVehicles(){
        logger.debug("Request to get all Vehicles");
        List<VehicleDTO> vehicles = vehicleService.getVehicles().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(vehicles == null || vehicles.size() < 1) throw new ResourceNotFoundException("Unable to find any Vehicles");
        return new ResponseEntity(vehicles, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific vehicle by id
     * @param id Long
     * @return ResponseEntity<VehicleDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<VehicleDTO> getVehicles(@PathVariable Long id) {
        logger.debug("Request to get a Vehicle by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Vehicle> vehicle = vehicleService.getByID(id);
        if(vehicle != null && vehicle.isPresent()) return new ResponseEntity(convertToDTO(vehicle.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Vehicle with id " + id);
    }


    /**
     * Persist vehicle. if id > 0 is present expects valid vehicle object already present, and update it by
     * replacing values. Otherwise simply creates a new vehicle and id is returned
     * @param vehicle VehicleDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<VehicleDTO> saveVehicle(@RequestBody @Valid VehicleDTO vehicle) throws Exception{
        logger.debug("Request to save Vehicle");
        Vehicle existingVehicle = new Vehicle();
        if(vehicle.getId() != null && vehicle.getId() > 0) {
            //Updating existing vehicle - Check item with matching ID present
            Optional<Vehicle> savedVehicle = vehicleService.getByID(vehicle.getId());
            if(savedVehicle != null && savedVehicle.isPresent()) existingVehicle = savedVehicle.get();
            else throw new ResourceNotFoundException("In order to update Vehicle " + vehicle.getId() + ", existing Vehicle must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Vehicle saveVehicle = copyToVehicle(vehicle, existingVehicle);
        Vehicle savedVehicle = vehicleService.saveVehicle(saveVehicle);
        if(savedVehicle.getId() != null && savedVehicle.getId() > 0){
            logger.info("Saved Vehicle with id " + saveVehicle.getId());
            VehicleDTO savedVehicleDTo = convertToDTO(savedVehicle);
            return  ResponseEntity.created (new URI("/vehicles/" + savedVehicle.getId())).body(savedVehicleDTo);
        }
        else{
            throw new PersistenceException("Vehicle not persisted: " + new Gson().toJson(savedVehicle));
        }
    }

   /**
     * Delete a vehicle by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Vehicle with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Vehicle> vehicle = vehicleService.getByID(id);
        if(vehicle == null || !vehicle.isPresent()) throw new ResourceNotFoundException("In order to delete  Vehicle " + id + ", existing  Vehicle must be available with same ID");
        vehicleService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  vehicles by number
     * @param number String     
     * @return List<Vehicle>
     */
    @GetMapping(path = "number/{number}")
    public ResponseEntity<List<Vehicle>> findAllByNumber(@PathVariable String number){
        logger.debug("findAllByNumber(" + number + ")");
		Assert.notNull(number, "Expects a valid number");

        List<Vehicle> vehicles =  vehicleService.findAllByNumber(number);
        if(vehicles == null || vehicles.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicles matching criteria");
        return new ResponseEntity(getDTOs(vehicles), HttpStatus.ACCEPTED);
    }
	/**
     * Find  vehicles by unit
     * @param unit Long     
     * @return List<Vehicle>
     */
    @GetMapping(path = "unit/{unit}")
    public ResponseEntity<List<Vehicle>> findAllByUnit_Id(@PathVariable Long unit){
        logger.debug("findAllByUnit_Id(" + unit + ")");
		Assert.notNull(unit, "Expects a valid unit");
		Assert.isTrue(unit > 0, "Expects a valid unit > 0");

        List<Vehicle> vehicles =  vehicleService.findAllByUnit_Id(unit);
        if(vehicles == null || vehicles.size() < 1) throw new ResourceNotFoundException("Unable to find any vehicles matching criteria");
        return new ResponseEntity(getDTOs(vehicles), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private VehicleDTO convertToDTO(Vehicle vehicle){
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

     private List<VehicleDTO> getDTOs(List<Vehicle> vehicles){
           if(vehicles == null) return null;
           List<VehicleDTO> dtoList = new ArrayList<VehicleDTO>(vehicles.size());
           for(Vehicle vehicle: vehicles){
               VehicleDTO dto = convertToDTO(vehicle);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Vehicle copyToVehicle(VehicleDTO vehicleDTO, Vehicle vehicle){
		if(vehicleDTO.getUnitId() != null && vehicleDTO.getUnitId() > 0){
            vehicle.setUnit( unitService.getByID(vehicleDTO.getUnitId()).get());
        }

         modelMapper.map(vehicleDTO, vehicle);
          return vehicle;
    }

}
