package lk.empire.ams.service;


import lk.empire.ams.model.entity.VehicleParking;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : VehicleParkingService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface VehicleParkingService {

    /**
     * Get all vehicleParkings
     * @return List<VehicleParking>
     */
    List<VehicleParking> getVehicleParkings();

    /**
     * Get a specific vehicleParking by id
     * @param id Long
     * @return Optional<VehicleParking>
     */
    Optional<VehicleParking> getByID(Long id);

    /**
     * Save vehicleParking and set id to passed vehicleParking
     * @param vehicleParking VehicleParking
     * @return ServiceStatus
     */
    VehicleParking saveVehicleParking(VehicleParking vehicleParking);

    /**
     * Delete a vehicleParking by ID
     * @param vehicleParkingID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long vehicleParkingID);



	/**
     * Find  vehicleParkings by parkingSlot
     * @param parkingSlot Long     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByParkingSlot_Id(Long parkingSlot);

	/**
     * Find  vehicleParkings by driverID
     * @param driverID String     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByDriverID(String driverID);

	/**
     * Find  vehicleParkings by inDate
     * @param inDate LocalDate     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByInDate(LocalDate inDate);

	/**
     * Find  vehicleParkings by outDate
     * @param outDate LocalDate     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByOutDate(LocalDate outDate);

	/**
     * Find  vehicleParkings by vehicle
     * @param vehicle Long     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByVehicle_Id(Long vehicle);

	/**
     * Find  vehicleParkings by vehicle
     * @param vehicle Long     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByVehicle_number(Long vehicle);




}
