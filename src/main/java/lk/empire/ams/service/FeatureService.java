package lk.empire.ams.service;


import lk.empire.ams.model.entity.Feature;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : FeatureService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Feature. A Feature of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface FeatureService {

    /**
     * Get all features
     * @return List<Feature>
     */
    List<Feature> getFeatures();

    /**
     * Get a specific feature by id
     * @param id Long
     * @return Optional<Feature>
     */
    Optional<Feature> getByID(Long id);

    /**
     * Save feature and set id to passed feature
     * @param feature Feature
     * @return ServiceStatus
     */
    Feature saveFeature(Feature feature);

    /**
     * Delete a feature by ID
     * @param featureID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long featureID);



	/**
     * Find  features by name
     * @param name String     
     * @return List<Feature>
     */
    public List<Feature> findAllByName(String name);




}
