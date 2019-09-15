package lk.empire.ams.controller;

import lk.empire.ams.model.dto.CommonAreaDTO;
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
 * <p>Title         : CommonAreaController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/commonAreas", value = "CommonAreaController", description = "All services related to CommonAreaController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/commonAreas")
public class CommonAreaController {

    private final CommonAreaService commonAreaService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(CommonAreaController.class);

	private final FloorService floorService;


    public CommonAreaController(CommonAreaService commonAreaService , FloorService floorService){
        this.commonAreaService = commonAreaService;
		this.floorService = floorService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all commonAreas
     * @return ResponseEntity<List<CommonAreaDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getCommonAreas(){
        logger.debug("Request to get all Common Areas");
        List<CommonAreaDTO> commonAreas = commonAreaService.getCommonAreas().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(commonAreas == null || commonAreas.size() < 1) throw new ResourceNotFoundException("Unable to find any Common Areas");
        return new ResponseEntity(commonAreas, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific commonArea by id
     * @param id Long
     * @return ResponseEntity<CommonAreaDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<CommonAreaDTO> getCommonAreas(@PathVariable Long id) {
        logger.debug("Request to get a CommonArea by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<CommonArea> commonArea = commonAreaService.getByID(id);
        if(commonArea != null && commonArea.isPresent()) return new ResponseEntity(convertToDTO(commonArea.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Common Area with id " + id);
    }


    /**
     * Persist commonArea. if id > 0 is present expects valid commonArea object already present, and update it by
     * replacing values. Otherwise simply creates a new commonArea and id is returned
     * @param commonArea CommonAreaDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<CommonAreaDTO> saveCommonArea(@RequestBody @Valid CommonAreaDTO commonArea) throws Exception{
        logger.debug("Request to save Common Area");
        CommonArea existingCommonArea = new CommonArea();
        if(commonArea.getId() != null && commonArea.getId() > 0) {
            //Updating existing commonArea - Check item with matching ID present
            Optional<CommonArea> savedCommonArea = commonAreaService.getByID(commonArea.getId());
            if(savedCommonArea != null && savedCommonArea.isPresent()) existingCommonArea = savedCommonArea.get();
            else throw new ResourceNotFoundException("In order to update Common Area " + commonArea.getId() + ", existing Common Area must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        CommonArea saveCommonArea = copyToCommonArea(commonArea, existingCommonArea);
        CommonArea savedCommonArea = commonAreaService.saveCommonArea(saveCommonArea);
        if(savedCommonArea.getId() != null && savedCommonArea.getId() > 0){
            logger.info("Saved Common Area with id " + saveCommonArea.getId());
            CommonAreaDTO savedCommonAreaDTo = convertToDTO(savedCommonArea);
            return  ResponseEntity.created (new URI("/commonAreas/" + savedCommonArea.getId())).body(savedCommonAreaDTo);
        }
        else{
            throw new PersistenceException("CommonArea not persisted: " + new Gson().toJson(savedCommonArea));
        }
    }

   /**
     * Delete a commonArea by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete CommonArea with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<CommonArea> commonArea = commonAreaService.getByID(id);
        if(commonArea == null || !commonArea.isPresent()) throw new ResourceNotFoundException("In order to delete  Common Area " + id + ", existing  Common Area must be available with same ID");
        commonAreaService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  commonAreas by type
     * @param type String     
     * @return List<CommonArea>
     */
    @GetMapping(path = "type/{type}")
    public ResponseEntity<List<CommonArea>> findAllByType(@PathVariable String type){
        logger.debug("findAllByType(" + type + ")");
		Assert.notNull(type, "Expects a valid type");

        List<CommonArea> commonAreas =  commonAreaService.findAllByType(type);
        if(commonAreas == null || commonAreas.size() < 1) throw new ResourceNotFoundException("Unable to find any commonAreas matching criteria");
        return new ResponseEntity(getDTOs(commonAreas), HttpStatus.ACCEPTED);
    }
	/**
     * Find  commonAreas by availability
     * @param availability Availability     
     * @return List<CommonArea>
     */
    @GetMapping(path = "availability/{availability}")
    public ResponseEntity<List<CommonArea>> findAllByAvailability(@PathVariable Availability availability){
        logger.debug("findAllByAvailability(" + availability + ")");
		Assert.notNull(availability, "Expects a valid availability");

        List<CommonArea> commonAreas =  commonAreaService.findAllByAvailability(availability);
        if(commonAreas == null || commonAreas.size() < 1) throw new ResourceNotFoundException("Unable to find any commonAreas matching criteria");
        return new ResponseEntity(getDTOs(commonAreas), HttpStatus.ACCEPTED);
    }
	/**
     * Find  commonAreas by floor
     * @param floor Long     
     * @return List<CommonArea>
     */
    @GetMapping(path = "floor/{floor}")
    public ResponseEntity<List<CommonArea>> findAllByFloor_Name(@PathVariable Long floor){
        logger.debug("findAllByFloor_Name(" + floor + ")");
		Assert.notNull(floor, "Expects a valid floor");
		Assert.isTrue(floor > 0, "Expects a valid floor > 0");

        List<CommonArea> commonAreas =  commonAreaService.findAllByFloor_Name(floor);
        if(commonAreas == null || commonAreas.size() < 1) throw new ResourceNotFoundException("Unable to find any commonAreas matching criteria");
        return new ResponseEntity(getDTOs(commonAreas), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private CommonAreaDTO convertToDTO(CommonArea commonArea){
        return modelMapper.map(commonArea, CommonAreaDTO.class);
    }

     private List<CommonAreaDTO> getDTOs(List<CommonArea> commonAreas){
           if(commonAreas == null) return null;
           List<CommonAreaDTO> dtoList = new ArrayList<CommonAreaDTO>(commonAreas.size());
           for(CommonArea commonArea: commonAreas){
               CommonAreaDTO dto = convertToDTO(commonArea);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private CommonArea copyToCommonArea(CommonAreaDTO commonAreaDTO, CommonArea commonArea){
		if(commonAreaDTO.getFloorId() != null && commonAreaDTO.getFloorId() > 0){
            commonArea.setFloor( floorService.getByID(commonAreaDTO.getFloorId()).get());
        }

         modelMapper.map(commonAreaDTO, commonArea);
          return commonArea;
    }

}
