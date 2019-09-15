package lk.empire.ams.service;


import lk.empire.ams.model.entity.ParkingSlot;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : ParkingSlotService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainParkingSlotService implements ParkingSlotService{
    @Autowired
    ParkingSlotRepository parkingSlotRepository;

    /**
     * Get all parkingSlots
     * @return List<ParkingSlot>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ParkingSlot> getParkingSlots(){
        return parkingSlotRepository.findAll();
    }

    /**
     * Get a specific parkingSlot by id
     * @param id Long
     * @return Optional<ParkingSlot>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<ParkingSlot> getByID(Long id){
         return parkingSlotRepository.findById(id);
    }

    /**
     * Save parkingSlot and set id to passed parkingSlot
     * @param parkingSlot ParkingSlot
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ParkingSlot saveParkingSlot(ParkingSlot parkingSlot){
        Assert.notNull(parkingSlot, "Parking slot parameter expected");
        Assert.isTrue(parkingSlot.isValid(), "Valid Parking slot is expected");
        ParkingSlot savedParkingSlot = parkingSlotRepository.saveAndFlush(parkingSlot);
        if(savedParkingSlot != null && savedParkingSlot.getId() != null && savedParkingSlot.getId() > 0) {
            parkingSlot.setId(savedParkingSlot.getId());
        }
        return parkingSlot;
    }

    /**
     * Delete a parkingSlot by ID
     * @param parkingSlotID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long parkingSlotID){
        parkingSlotRepository.deleteById(parkingSlotID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  parkingSlots by name
     * @param name String     
     * @return List<ParkingSlot>
     */
    public List<ParkingSlot> findAllByName(String name){
		Assert.notNull(name, "Expects a valid name");

        return parkingSlotRepository.findAllByName(name);
    }
	/**
     * Find  parkingSlots by unit
     * @param unit Long     
     * @return List<ParkingSlot>
     */
    public List<ParkingSlot> findAllByUnit_Id(Long unit){
		Assert.notNull(unit, "Expects a valid unit");
		Assert.isTrue(unit > 0, "Expects a valid unit > 0");

        return parkingSlotRepository.findAllByUnit_Id(unit);
    }
	/**
     * Find  parkingSlots by vehicleNumber
     * @param vehicleNumber String     
     * @return List<ParkingSlot>
     */
    public List<ParkingSlot> findAllByVehicleNumber(String vehicleNumber){
		Assert.notNull(vehicleNumber, "Expects a valid vehicleNumber");

        return parkingSlotRepository.findAllByVehicleNumber(vehicleNumber);
    }



    @PostConstruct
    public void initDB(){

    }
}
