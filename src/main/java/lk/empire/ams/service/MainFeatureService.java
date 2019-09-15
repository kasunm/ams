package lk.empire.ams.service;


import lk.empire.ams.model.entity.Feature;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : FeatureService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Feature. A Feature of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainFeatureService implements FeatureService{
    @Autowired
    FeatureRepository featureRepository;

    /**
     * Get all features
     * @return List<Feature>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Feature> getFeatures(){
        return featureRepository.findAll();
    }

    /**
     * Get a specific feature by id
     * @param id Long
     * @return Optional<Feature>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Feature> getByID(Long id){
         return featureRepository.findById(id);
    }

    /**
     * Save feature and set id to passed feature
     * @param feature Feature
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Feature saveFeature(Feature feature){
        Assert.notNull(feature, "Feature parameter expected");
        Assert.isTrue(feature.isValid(), "Valid Feature is expected");
        Feature savedFeature = featureRepository.saveAndFlush(feature);
        if(savedFeature != null && savedFeature.getId() != null && savedFeature.getId() > 0) {
            feature.setId(savedFeature.getId());
        }
        return feature;
    }

    /**
     * Delete a feature by ID
     * @param featureID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long featureID){
        featureRepository.deleteById(featureID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  features by name
     * @param name String     
     * @return List<Feature>
     */
    public List<Feature> findAllByName(String name){
		Assert.notNull(name, "Expects a valid name");

        return featureRepository.findAllByName(name);
    }



    @PostConstruct
    public void initDB(){

    }
}
