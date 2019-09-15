package lk.empire.ams.service;


import lk.empire.ams.model.entity.ParkingSlot;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : ParkingSlotService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface ParkingSlotService {

    /**
     * Get all parkingSlots
     * @return List<ParkingSlot>
     */
    List<ParkingSlot> getParkingSlots();

    /**
     * Get a specific parkingSlot by id
     * @param id Long
     * @return Optional<ParkingSlot>
     */
    Optional<ParkingSlot> getByID(Long id);

    /**
     * Save parkingSlot and set id to passed parkingSlot
     * @param parkingSlot ParkingSlot
     * @return ServiceStatus
     */
    ParkingSlot saveParkingSlot(ParkingSlot parkingSlot);

    /**
     * Delete a parkingSlot by ID
     * @param parkingSlotID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long parkingSlotID);



	/**
     * Find  parkingSlots by name
     * @param name String     
     * @return List<ParkingSlot>
     */
    public List<ParkingSlot> findAllByName(String name);

	/**
     * Find  parkingSlots by unit
     * @param unit Long     
     * @return List<ParkingSlot>
     */
    public List<ParkingSlot> findAllByUnit_Id(Long unit);

	/**
     * Find  parkingSlots by vehicleNumber
     * @param vehicleNumber String     
     * @return List<ParkingSlot>
     */
    public List<ParkingSlot> findAllByVehicleNumber(String vehicleNumber);




}
