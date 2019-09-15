package lk.empire.ams.service;


import lk.empire.ams.model.entity.Floor;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.FloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : FloorService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainFloorService implements FloorService{
    @Autowired
    FloorRepository floorRepository;

    /**
     * Get all floors
     * @return List<Floor>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Floor> getFloors(){
        return floorRepository.findAll();
    }

    /**
     * Get a specific floor by id
     * @param id Long
     * @return Optional<Floor>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Floor> getByID(Long id){
         return floorRepository.findById(id);
    }

    /**
     * Save floor and set id to passed floor
     * @param floor Floor
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Floor saveFloor(Floor floor){
        Assert.notNull(floor, "Floor parameter expected");
        Assert.isTrue(floor.isValid(), "Valid Floor is expected");
        Floor savedFloor = floorRepository.saveAndFlush(floor);
        if(savedFloor != null && savedFloor.getId() != null && savedFloor.getId() > 0) {
            floor.setId(savedFloor.getId());
        }
        return floor;
    }

    /**
     * Delete a floor by ID
     * @param floorID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long floorID){
        floorRepository.deleteById(floorID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  floors by name
     * @param name String     
     * @return List<Floor>
     */
    public List<Floor> findAllByName(String name){
		Assert.notNull(name, "Expects a valid name");

        return floorRepository.findAllByName(name);
    }
	/**
     * Find  floors by floorNumber
     * @param floorNumber Integer     
     * @return List<Floor>
     */
    public List<Floor> findAllByFloorNumber(Integer floorNumber){
		Assert.notNull(floorNumber, "Expects a valid floorNumber");
		Assert.isTrue(floorNumber > 0, "Expects a valid floorNumber > 0");

        return floorRepository.findAllByFloorNumber(floorNumber);
    }
	/**
     * Find  floors by apartment
     * @param apartment Long     
     * @return List<Floor>
     */
    public List<Floor> findAllByApartment_Id(Long apartment){
		Assert.notNull(apartment, "Expects a valid apartment");
		Assert.isTrue(apartment > 0, "Expects a valid apartment > 0");

        return floorRepository.findAllByApartment_Id(apartment);
    }



    @PostConstruct
    public void initDB(){

    }
}
