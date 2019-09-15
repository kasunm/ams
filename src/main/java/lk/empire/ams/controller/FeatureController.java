package lk.empire.ams.controller;

import lk.empire.ams.model.dto.FeatureDTO;
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
 * <p>Title         : FeatureController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Feature. A Feature of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/features", value = "FeatureController", description = "All services related to FeatureController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/features")
public class FeatureController {

    private final FeatureService featureService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(FeatureController.class);

 

    public FeatureController(FeatureService featureService  ){
        this.featureService = featureService;
 
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all features
     * @return ResponseEntity<List<FeatureDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getFeatures(){
        logger.debug("Request to get all Features");
        List<FeatureDTO> features = featureService.getFeatures().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(features == null || features.size() < 1) throw new ResourceNotFoundException("Unable to find any Features");
        return new ResponseEntity(features, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific feature by id
     * @param id Long
     * @return ResponseEntity<FeatureDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<FeatureDTO> getFeatures(@PathVariable Long id) {
        logger.debug("Request to get a Feature by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Feature> feature = featureService.getByID(id);
        if(feature != null && feature.isPresent()) return new ResponseEntity(convertToDTO(feature.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Feature with id " + id);
    }


    /**
     * Persist feature. if id > 0 is present expects valid feature object already present, and update it by
     * replacing values. Otherwise simply creates a new feature and id is returned
     * @param feature FeatureDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<FeatureDTO> saveFeature(@RequestBody @Valid FeatureDTO feature) throws Exception{
        logger.debug("Request to save Feature");
        Feature existingFeature = new Feature();
        if(feature.getId() != null && feature.getId() > 0) {
            //Updating existing feature - Check item with matching ID present
            Optional<Feature> savedFeature = featureService.getByID(feature.getId());
            if(savedFeature != null && savedFeature.isPresent()) existingFeature = savedFeature.get();
            else throw new ResourceNotFoundException("In order to update Feature " + feature.getId() + ", existing Feature must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Feature saveFeature = copyToFeature(feature, existingFeature);
        Feature savedFeature = featureService.saveFeature(saveFeature);
        if(savedFeature.getId() != null && savedFeature.getId() > 0){
            logger.info("Saved Feature with id " + saveFeature.getId());
            FeatureDTO savedFeatureDTo = convertToDTO(savedFeature);
            return  ResponseEntity.created (new URI("/features/" + savedFeature.getId())).body(savedFeatureDTo);
        }
        else{
            throw new PersistenceException("Feature not persisted: " + new Gson().toJson(savedFeature));
        }
    }

   /**
     * Delete a feature by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Feature with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Feature> feature = featureService.getByID(id);
        if(feature == null || !feature.isPresent()) throw new ResourceNotFoundException("In order to delete  Feature " + id + ", existing  Feature must be available with same ID");
        featureService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  features by name
     * @param name String     
     * @return List<Feature>
     */
    @GetMapping(path = "name/{name}")
    public ResponseEntity<List<Feature>> findAllByName(@PathVariable String name){
        logger.debug("findAllByName(" + name + ")");
		Assert.notNull(name, "Expects a valid name");

        List<Feature> features =  featureService.findAllByName(name);
        if(features == null || features.size() < 1) throw new ResourceNotFoundException("Unable to find any features matching criteria");
        return new ResponseEntity(getDTOs(features), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private FeatureDTO convertToDTO(Feature feature){
        return modelMapper.map(feature, FeatureDTO.class);
    }

     private List<FeatureDTO> getDTOs(List<Feature> features){
           if(features == null) return null;
           List<FeatureDTO> dtoList = new ArrayList<FeatureDTO>(features.size());
           for(Feature feature: features){
               FeatureDTO dto = convertToDTO(feature);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Feature copyToFeature(FeatureDTO featureDTO, Feature feature){
 
         modelMapper.map(featureDTO, feature);
          return feature;
    }

}
