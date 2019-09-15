package lk.empire.ams.controller;

import lk.empire.ams.model.dto.FloorDTO;
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
 * <p>Title         : FloorController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/floors", value = "FloorController", description = "All services related to FloorController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/floors")
public class FloorController {

    private final FloorService floorService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(FloorController.class);

	private final ApartmentService apartmentService;


    public FloorController(FloorService floorService , ApartmentService apartmentService){
        this.floorService = floorService;
		this.apartmentService = apartmentService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all floors
     * @return ResponseEntity<List<FloorDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getFloors(){
        logger.debug("Request to get all Floors");
        List<FloorDTO> floors = floorService.getFloors().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(floors == null || floors.size() < 1) throw new ResourceNotFoundException("Unable to find any Floors");
        return new ResponseEntity(floors, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific floor by id
     * @param id Long
     * @return ResponseEntity<FloorDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<FloorDTO> getFloors(@PathVariable Long id) {
        logger.debug("Request to get a Floor by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Floor> floor = floorService.getByID(id);
        if(floor != null && floor.isPresent()) return new ResponseEntity(convertToDTO(floor.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Floor with id " + id);
    }


    /**
     * Persist floor. if id > 0 is present expects valid floor object already present, and update it by
     * replacing values. Otherwise simply creates a new floor and id is returned
     * @param floor FloorDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<FloorDTO> saveFloor(@RequestBody @Valid FloorDTO floor) throws Exception{
        logger.debug("Request to save Floor");
        Floor existingFloor = new Floor();
        if(floor.getId() != null && floor.getId() > 0) {
            //Updating existing floor - Check item with matching ID present
            Optional<Floor> savedFloor = floorService.getByID(floor.getId());
            if(savedFloor != null && savedFloor.isPresent()) existingFloor = savedFloor.get();
            else throw new ResourceNotFoundException("In order to update Floor " + floor.getId() + ", existing Floor must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Floor saveFloor = copyToFloor(floor, existingFloor);
        Floor savedFloor = floorService.saveFloor(saveFloor);
        if(savedFloor.getId() != null && savedFloor.getId() > 0){
            logger.info("Saved Floor with id " + saveFloor.getId());
            FloorDTO savedFloorDTo = convertToDTO(savedFloor);
            return  ResponseEntity.created (new URI("/floors/" + savedFloor.getId())).body(savedFloorDTo);
        }
        else{
            throw new PersistenceException("Floor not persisted: " + new Gson().toJson(savedFloor));
        }
    }

   /**
     * Delete a floor by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Floor with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Floor> floor = floorService.getByID(id);
        if(floor == null || !floor.isPresent()) throw new ResourceNotFoundException("In order to delete  Floor " + id + ", existing  Floor must be available with same ID");
        floorService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  floors by name
     * @param name String     
     * @return List<Floor>
     */
    @GetMapping(path = "name/{name}")
    public ResponseEntity<List<Floor>> findAllByName(@PathVariable String name){
        logger.debug("findAllByName(" + name + ")");
		Assert.notNull(name, "Expects a valid name");

        List<Floor> floors =  floorService.findAllByName(name);
        if(floors == null || floors.size() < 1) throw new ResourceNotFoundException("Unable to find any floors matching criteria");
        return new ResponseEntity(getDTOs(floors), HttpStatus.ACCEPTED);
    }
	/**
     * Find  floors by floorNumber
     * @param floorNumber Integer     
     * @return List<Floor>
     */
    @GetMapping(path = "floorNumber/{floorNumber}")
    public ResponseEntity<List<Floor>> findAllByFloorNumber(@PathVariable Integer floorNumber){
        logger.debug("findAllByFloorNumber(" + floorNumber + ")");
		Assert.notNull(floorNumber, "Expects a valid floorNumber");
		Assert.isTrue(floorNumber > 0, "Expects a valid floorNumber > 0");

        List<Floor> floors =  floorService.findAllByFloorNumber(floorNumber);
        if(floors == null || floors.size() < 1) throw new ResourceNotFoundException("Unable to find any floors matching criteria");
        return new ResponseEntity(getDTOs(floors), HttpStatus.ACCEPTED);
    }
	/**
     * Find  floors by apartment
     * @param apartment Long     
     * @return List<Floor>
     */
    @GetMapping(path = "apartment/{apartment}")
    public ResponseEntity<List<Floor>> findAllByApartment_Id(@PathVariable Long apartment){
        logger.debug("findAllByApartment_Id(" + apartment + ")");
		Assert.notNull(apartment, "Expects a valid apartment");
		Assert.isTrue(apartment > 0, "Expects a valid apartment > 0");

        List<Floor> floors =  floorService.findAllByApartment_Id(apartment);
        if(floors == null || floors.size() < 1) throw new ResourceNotFoundException("Unable to find any floors matching criteria");
        return new ResponseEntity(getDTOs(floors), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private FloorDTO convertToDTO(Floor floor){
        return modelMapper.map(floor, FloorDTO.class);
    }

     private List<FloorDTO> getDTOs(List<Floor> floors){
           if(floors == null) return null;
           List<FloorDTO> dtoList = new ArrayList<FloorDTO>(floors.size());
           for(Floor floor: floors){
               FloorDTO dto = convertToDTO(floor);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Floor copyToFloor(FloorDTO floorDTO, Floor floor){
		if(floorDTO.getApartmentId() != null && floorDTO.getApartmentId() > 0){
            floor.setApartment( apartmentService.getByID(floorDTO.getApartmentId()).get());
        }

         modelMapper.map(floorDTO, floor);
          return floor;
    }

}
