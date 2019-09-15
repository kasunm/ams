package lk.empire.ams.controller;

import lk.empire.ams.model.dto.UnitDTO;
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
 * <p>Title         : UnitController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Unit. A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/units", value = "UnitController", description = "All services related to UnitController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitsService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(UnitController.class);

	private final ClientService ownerService;
	private final ClientService renterService;
	private final FloorService floorService;


    public UnitController(UnitService unitsService , ClientService ownerService, ClientService renterService, FloorService floorService){
        this.unitsService = unitsService;
		this.ownerService = ownerService;
		this.renterService = renterService;
		this.floorService = floorService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all unitss
     * @return ResponseEntity<List<UnitDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getUnits(){
        logger.debug("Request to get all Units");
        List<UnitDTO> unitss = unitsService.getUnits().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(unitss == null || unitss.size() < 1) throw new ResourceNotFoundException("Unable to find any Units");
        return new ResponseEntity(unitss, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific units by id
     * @param id Long
     * @return ResponseEntity<UnitDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<UnitDTO> getUnits(@PathVariable Long id) {
        logger.debug("Request to get a Unit by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Unit> units = unitsService.getByID(id);
        if(units != null && units.isPresent()) return new ResponseEntity(convertToDTO(units.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Unit with id " + id);
    }


    /**
     * Persist units. if id > 0 is present expects valid units object already present, and update it by
     * replacing values. Otherwise simply creates a new units and id is returned
     * @param units UnitDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<UnitDTO> saveUnit(@RequestBody @Valid UnitDTO units) throws Exception{
        logger.debug("Request to save Unit");
        Unit existingUnit = new Unit();
        if(units.getId() != null && units.getId() > 0) {
            //Updating existing units - Check item with matching ID present
            Optional<Unit> savedUnit = unitsService.getByID(units.getId());
            if(savedUnit != null && savedUnit.isPresent()) existingUnit = savedUnit.get();
            else throw new ResourceNotFoundException("In order to update Unit " + units.getId() + ", existing Unit must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Unit saveUnit = copyToUnit(units, existingUnit);
        Unit savedUnit = unitsService.saveUnit(saveUnit);
        if(savedUnit.getId() != null && savedUnit.getId() > 0){
            logger.info("Saved Unit with id " + saveUnit.getId());
            UnitDTO savedUnitDTo = convertToDTO(savedUnit);
            return  ResponseEntity.created (new URI("/units/" + savedUnit.getId())).body(savedUnitDTo);
        }
        else{
            throw new PersistenceException("Unit not persisted: " + new Gson().toJson(savedUnit));
        }
    }

   /**
     * Delete a units by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Unit with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Unit> units = unitsService.getByID(id);
        if(units == null || !units.isPresent()) throw new ResourceNotFoundException("In order to delete  Unit " + id + ", existing  Unit must be available with same ID");
        unitsService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  unitss by name
     * @param name String     
     * @return List<Unit>
     */
    @GetMapping(path = "name/{name}")
    public ResponseEntity<List<Unit>> findAllByName(@PathVariable String name){
        logger.debug("findAllByName(" + name + ")");
		Assert.notNull(name, "Expects a valid name");

        List<Unit> unitss =  unitsService.findAllByName(name);
        if(unitss == null || unitss.size() < 1) throw new ResourceNotFoundException("Unable to find any unitss matching criteria");
        return new ResponseEntity(getDTOs(unitss), HttpStatus.ACCEPTED);
    }
	/**
     * Find  unitss by owner
     * @param owner Long     
     * @return List<Unit>
     */
    @GetMapping(path = "owner/{owner}")
    public ResponseEntity<List<Unit>> findAllByOwner_Id(@PathVariable Long owner){
        logger.debug("findAllByOwner_Id(" + owner + ")");
		Assert.notNull(owner, "Expects a valid owner");
		Assert.isTrue(owner > 0, "Expects a valid owner > 0");

        List<Unit> unitss =  unitsService.findAllByOwner_Id(owner);
        if(unitss == null || unitss.size() < 1) throw new ResourceNotFoundException("Unable to find any unitss matching criteria");
        return new ResponseEntity(getDTOs(unitss), HttpStatus.ACCEPTED);
    }
	/**
     * Find  unitss by renter
     * @param renter Long     
     * @return List<Unit>
     */
    @GetMapping(path = "renter/{renter}")
    public ResponseEntity<List<Unit>> findAllByRenter_Id(@PathVariable Long renter){
        logger.debug("findAllByRenter_Id(" + renter + ")");
		Assert.notNull(renter, "Expects a valid renter");
		Assert.isTrue(renter > 0, "Expects a valid renter > 0");

        List<Unit> unitss =  unitsService.findAllByRenter_Id(renter);
        if(unitss == null || unitss.size() < 1) throw new ResourceNotFoundException("Unable to find any unitss matching criteria");
        return new ResponseEntity(getDTOs(unitss), HttpStatus.ACCEPTED);
    }
	/**
     * Find  unitss by availability
     * @param availability Availability     
     * @return List<Unit>
     */
    @GetMapping(path = "availability/{availability}")
    public ResponseEntity<List<Unit>> findAllByAvailability(@PathVariable Availability availability){
        logger.debug("findAllByAvailability(" + availability + ")");
		Assert.notNull(availability, "Expects a valid availability");

        List<Unit> unitss =  unitsService.findAllByAvailability(availability);
        if(unitss == null || unitss.size() < 1) throw new ResourceNotFoundException("Unable to find any unitss matching criteria");
        return new ResponseEntity(getDTOs(unitss), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private UnitDTO convertToDTO(Unit units){
        return modelMapper.map(units, UnitDTO.class);
    }

     private List<UnitDTO> getDTOs(List<Unit> unitss){
           if(unitss == null) return null;
           List<UnitDTO> dtoList = new ArrayList<UnitDTO>(unitss.size());
           for(Unit units: unitss){
               UnitDTO dto = convertToDTO(units);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Unit copyToUnit(UnitDTO unitsDTO, Unit units){
		if(unitsDTO.getOwnerId() != null && unitsDTO.getOwnerId() > 0){
            units.setOwner( ownerService.getByID(unitsDTO.getOwnerId()).get());
        }
		if(unitsDTO.getRenterId() != null && unitsDTO.getRenterId() > 0){
            units.setRenter( renterService.getByID(unitsDTO.getRenterId()).get());
        }
		if(unitsDTO.getFloorId() != null && unitsDTO.getFloorId() > 0){
            units.setFloor( floorService.getByID(unitsDTO.getFloorId()).get());
        }

         modelMapper.map(unitsDTO, units);
          return units;
    }

}
