package lk.empire.ams.service;


import lk.empire.ams.model.entity.Vehicle;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : VehicleService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Vehicle. A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface VehicleService {

    /**
     * Get all vehicles
     * @return List<Vehicle>
     */
    List<Vehicle> getVehicles();

    /**
     * Get a specific vehicle by id
     * @param id Long
     * @return Optional<Vehicle>
     */
    Optional<Vehicle> getByID(Long id);

    /**
     * Save vehicle and set id to passed vehicle
     * @param vehicle Vehicle
     * @return ServiceStatus
     */
    Vehicle saveVehicle(Vehicle vehicle);

    /**
     * Delete a vehicle by ID
     * @param vehicleID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long vehicleID);



	/**
     * Find  vehicles by number
     * @param number String     
     * @return List<Vehicle>
     */
    public List<Vehicle> findAllByNumber(String number);

	/**
     * Find  vehicles by unit
     * @param unit Long     
     * @return List<Vehicle>
     */
    public List<Vehicle> findAllByUnit_Id(Long unit);




}
