package lk.empire.ams.service;


import lk.empire.ams.model.entity.Vehicle;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : VehicleService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Vehicle. A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainVehicleService implements VehicleService{
    @Autowired
    VehicleRepository vehicleRepository;

    /**
     * Get all vehicles
     * @return List<Vehicle>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Vehicle> getVehicles(){
        return vehicleRepository.findAll();
    }

    /**
     * Get a specific vehicle by id
     * @param id Long
     * @return Optional<Vehicle>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Vehicle> getByID(Long id){
         return vehicleRepository.findById(id);
    }

    /**
     * Save vehicle and set id to passed vehicle
     * @param vehicle Vehicle
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Vehicle saveVehicle(Vehicle vehicle){
        Assert.notNull(vehicle, "Vehicle parameter expected");
        Assert.isTrue(vehicle.isValid(), "Valid Vehicle is expected");
        Vehicle savedVehicle = vehicleRepository.saveAndFlush(vehicle);
        if(savedVehicle != null && savedVehicle.getId() != null && savedVehicle.getId() > 0) {
            vehicle.setId(savedVehicle.getId());
        }
        return vehicle;
    }

    /**
     * Delete a vehicle by ID
     * @param vehicleID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long vehicleID){
        vehicleRepository.deleteById(vehicleID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  vehicles by number
     * @param number String     
     * @return List<Vehicle>
     */
    public List<Vehicle> findAllByNumber(String number){
		Assert.notNull(number, "Expects a valid number");

        return vehicleRepository.findAllByNumber(number);
    }
	/**
     * Find  vehicles by unit
     * @param unit Long     
     * @return List<Vehicle>
     */
    public List<Vehicle> findAllByUnit_Id(Long unit){
		Assert.notNull(unit, "Expects a valid unit");
		Assert.isTrue(unit > 0, "Expects a valid unit > 0");

        return vehicleRepository.findAllByUnit_Id(unit);
    }



    @PostConstruct
    public void initDB(){

    }
}
