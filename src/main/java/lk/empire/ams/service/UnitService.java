package lk.empire.ams.service;


import lk.empire.ams.model.entity.Unit;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : UnitService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Unit. A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface UnitService {

    /**
     * Get all unitss
     * @return List<Unit>
     */
    List<Unit> getUnits();

    /**
     * Get a specific units by id
     * @param id Long
     * @return Optional<Unit>
     */
    Optional<Unit> getByID(Long id);

    /**
     * Save units and set id to passed units
     * @param units Unit
     * @return ServiceStatus
     */
    Unit saveUnit(Unit units);

    /**
     * Delete a units by ID
     * @param unitsID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long unitsID);



	/**
     * Find  unitss by name
     * @param name String     
     * @return List<Unit>
     */
    public List<Unit> findAllByName(String name);

	/**
     * Find  unitss by owner
     * @param owner Long     
     * @return List<Unit>
     */
    public List<Unit> findAllByOwner_Id(Long owner);

	/**
     * Find  unitss by renter
     * @param renter Long     
     * @return List<Unit>
     */
    public List<Unit> findAllByRenter_Id(Long renter);

	/**
     * Find  unitss by availability
     * @param availability Availability     
     * @return List<Unit>
     */
    public List<Unit> findAllByAvailability(Availability availability);

    public Long getCount();

    public Long getRenterCount();

}
