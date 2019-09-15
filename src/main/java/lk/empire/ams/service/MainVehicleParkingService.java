package lk.empire.ams.service;


import lk.empire.ams.model.entity.VehicleParking;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.VehicleParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : VehicleParkingService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainVehicleParkingService implements VehicleParkingService{
    @Autowired
    VehicleParkingRepository vehicleParkingRepository;

    /**
     * Get all vehicleParkings
     * @return List<VehicleParking>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VehicleParking> getVehicleParkings(){
        return vehicleParkingRepository.findAll();
    }

    /**
     * Get a specific vehicleParking by id
     * @param id Long
     * @return Optional<VehicleParking>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<VehicleParking> getByID(Long id){
         return vehicleParkingRepository.findById(id);
    }

    /**
     * Save vehicleParking and set id to passed vehicleParking
     * @param vehicleParking VehicleParking
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public VehicleParking saveVehicleParking(VehicleParking vehicleParking){
        Assert.notNull(vehicleParking, "Vehicle Parking parameter expected");
        Assert.isTrue(vehicleParking.isValid(), "Valid Vehicle Parking is expected");
        VehicleParking savedVehicleParking = vehicleParkingRepository.saveAndFlush(vehicleParking);
        if(savedVehicleParking != null && savedVehicleParking.getId() != null && savedVehicleParking.getId() > 0) {
            vehicleParking.setId(savedVehicleParking.getId());
        }
        return vehicleParking;
    }

    /**
     * Delete a vehicleParking by ID
     * @param vehicleParkingID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long vehicleParkingID){
        vehicleParkingRepository.deleteById(vehicleParkingID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  vehicleParkings by parkingSlot
     * @param parkingSlot Long     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByParkingSlot_Id(Long parkingSlot){
		Assert.notNull(parkingSlot, "Expects a valid parkingSlot");
		Assert.isTrue(parkingSlot > 0, "Expects a valid parkingSlot > 0");

        return vehicleParkingRepository.findAllByParkingSlot_Id(parkingSlot);
    }
	/**
     * Find  vehicleParkings by driverID
     * @param driverID String     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByDriverID(String driverID){
		Assert.notNull(driverID, "Expects a valid driverID");

        return vehicleParkingRepository.findAllByDriverID(driverID);
    }
	/**
     * Find  vehicleParkings by inDate
     * @param inDate LocalDate     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByInDate(LocalDate inDate){
		Assert.notNull(inDate, "Expects a valid inDate");

        return vehicleParkingRepository.findAllByInDate(inDate);
    }
	/**
     * Find  vehicleParkings by outDate
     * @param outDate LocalDate     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByOutDate(LocalDate outDate){
		Assert.notNull(outDate, "Expects a valid outDate");

        return vehicleParkingRepository.findAllByOutDate(outDate);
    }
	/**
     * Find  vehicleParkings by vehicle
     * @param vehicle Long     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByVehicle_Id(Long vehicle){
		Assert.notNull(vehicle, "Expects a valid vehicle");
		Assert.isTrue(vehicle > 0, "Expects a valid vehicle > 0");

        return vehicleParkingRepository.findAllByVehicle_Id(vehicle);
    }
	/**
     * Find  vehicleParkings by vehicle
     * @param vehicle Long     
     * @return List<VehicleParking>
     */
    public List<VehicleParking> findAllByVehicle_number(Long vehicle){
		Assert.notNull(vehicle, "Expects a valid vehicle");
		Assert.isTrue(vehicle > 0, "Expects a valid vehicle > 0");

        return vehicleParkingRepository.findAllByVehicle_number(vehicle);
    }



    @PostConstruct
    public void initDB(){

    }
}
