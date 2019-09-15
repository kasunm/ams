package lk.empire.ams.service;


import lk.empire.ams.model.entity.Unit;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : UnitService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Unit. A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainUnitService implements UnitService{
    @Autowired
    UnitRepository unitsRepository;

    /**
     * Get all unitss
     * @return List<Unit>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Unit> getUnits(){
        return unitsRepository.findAll();
    }

    /**
     * Get a specific units by id
     * @param id Long
     * @return Optional<Unit>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Unit> getByID(Long id){
         return unitsRepository.findById(id);
    }

    /**
     * Save units and set id to passed units
     * @param units Unit
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Unit saveUnit(Unit units){
        Assert.notNull(units, "Unit parameter expected");
        Assert.isTrue(units.isValid(), "Valid Unit is expected");
        Unit savedUnit = unitsRepository.saveAndFlush(units);
        if(savedUnit != null && savedUnit.getId() != null && savedUnit.getId() > 0) {
            units.setId(savedUnit.getId());
        }
        return units;
    }

    /**
     * Delete a units by ID
     * @param unitsID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long unitsID){
        unitsRepository.deleteById(unitsID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  unitss by name
     * @param name String     
     * @return List<Unit>
     */
    public List<Unit> findAllByName(String name){
		Assert.notNull(name, "Expects a valid name");

        return unitsRepository.findAllByName(name);
    }
	/**
     * Find  unitss by owner
     * @param owner Long     
     * @return List<Unit>
     */
    public List<Unit> findAllByOwner_Id(Long owner){
		Assert.notNull(owner, "Expects a valid owner");
		Assert.isTrue(owner > 0, "Expects a valid owner > 0");

        return unitsRepository.findAllByOwner_Id(owner);
    }
	/**
     * Find  unitss by renter
     * @param renter Long     
     * @return List<Unit>
     */
    public List<Unit> findAllByRenter_Id(Long renter){
		Assert.notNull(renter, "Expects a valid renter");
		Assert.isTrue(renter > 0, "Expects a valid renter > 0");

        return unitsRepository.findAllByRenter_Id(renter);
    }
	/**
     * Find  unitss by availability
     * @param availability Availability     
     * @return List<Unit>
     */
    public List<Unit> findAllByAvailability(Availability availability){
		Assert.notNull(availability, "Expects a valid availability");

        return unitsRepository.findAllByAvailability(availability);
    }

    public Long getCount(){
        return unitsRepository.count();
    }

    public Long getRenterCount(){
        return unitsRepository.getCountOfRenters();
    }



    @PostConstruct
    public void initDB(){

    }
}
