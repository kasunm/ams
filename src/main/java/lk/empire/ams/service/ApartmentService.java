package lk.empire.ams.service;


import lk.empire.ams.model.entity.Apartment;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : ApartmentService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Apartment. An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface ApartmentService {

    /**
     * Get all apartments
     * @return List<Apartment>
     */
    List<Apartment> getApartments();

    /**
     * Get a specific apartment by id
     * @param id Long
     * @return Optional<Apartment>
     */
    Optional<Apartment> getByID(Long id);

    /**
     * Save apartment and set id to passed apartment
     * @param apartment Apartment
     * @return ServiceStatus
     */
    Apartment saveApartment(Apartment apartment);

    /**
     * Delete a apartment by ID
     * @param apartmentID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long apartmentID);



	/**
     * Find  apartments by name
     * @param name String     
     * @return List<Apartment>
     */
    public List<Apartment> findAllByName(String name);




}
