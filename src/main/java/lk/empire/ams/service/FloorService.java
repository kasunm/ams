package lk.empire.ams.service;


import lk.empire.ams.model.entity.Floor;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : FloorService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface FloorService {

    /**
     * Get all floors
     * @return List<Floor>
     */
    List<Floor> getFloors();

    /**
     * Get a specific floor by id
     * @param id Long
     * @return Optional<Floor>
     */
    Optional<Floor> getByID(Long id);

    /**
     * Save floor and set id to passed floor
     * @param floor Floor
     * @return ServiceStatus
     */
    Floor saveFloor(Floor floor);

    /**
     * Delete a floor by ID
     * @param floorID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long floorID);



	/**
     * Find  floors by name
     * @param name String     
     * @return List<Floor>
     */
    public List<Floor> findAllByName(String name);

	/**
     * Find  floors by floorNumber
     * @param floorNumber Integer     
     * @return List<Floor>
     */
    public List<Floor> findAllByFloorNumber(Integer floorNumber);

	/**
     * Find  floors by apartment
     * @param apartment Long     
     * @return List<Floor>
     */
    public List<Floor> findAllByApartment_Id(Long apartment);




}
