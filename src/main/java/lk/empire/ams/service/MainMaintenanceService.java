package lk.empire.ams.service;


import lk.empire.ams.model.entity.Maintenance;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : MaintenanceService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainMaintenanceService implements MaintenanceService{
    @Autowired
    MaintenanceRepository maintenanceRepository;

    /**
     * Get all maintenances
     * @return List<Maintenance>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Maintenance> getMaintenances(){
        return maintenanceRepository.findAll();
    }

    /**
     * Get a specific maintenance by id
     * @param id Long
     * @return Optional<Maintenance>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Maintenance> getByID(Long id){
         return maintenanceRepository.findById(id);
    }

    /**
     * Save maintenance and set id to passed maintenance
     * @param maintenance Maintenance
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Maintenance saveMaintenance(Maintenance maintenance){
        Assert.notNull(maintenance, "Maintenance parameter expected");
        Assert.isTrue(maintenance.isValid(), "Valid Maintenance is expected");
        Maintenance savedMaintenance = maintenanceRepository.saveAndFlush(maintenance);
        if(savedMaintenance != null && savedMaintenance.getId() != null && savedMaintenance.getId() > 0) {
            maintenance.setId(savedMaintenance.getId());
        }
        return maintenance;
    }

    /**
     * Delete a maintenance by ID
     * @param maintenanceID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long maintenanceID){
        maintenanceRepository.deleteById(maintenanceID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  maintenances by description
     * @param description String     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByDescription(String description){
		Assert.notNull(description, "Expects a valid description");

        return maintenanceRepository.findAllByDescription(description);
    }
	/**
     * Find  maintenances by blockName
     * @param blockName String     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByBlockName(String blockName){
		Assert.notNull(blockName, "Expects a valid blockName");

        return maintenanceRepository.findAllByBlockName(blockName);
    }
	/**
     * Find  maintenances by doneBy
     * @param doneBy String     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByDoneBy(String doneBy){
		Assert.notNull(doneBy, "Expects a valid doneBy");

        return maintenanceRepository.findAllByDoneBy(doneBy);
    }
	/**
     * Find  maintenances by maintenanceType
     * @param maintenanceType MaintenanceType     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByMaintenanceType(MaintenanceType maintenanceType){
		Assert.notNull(maintenanceType, "Expects a valid maintenanceType");

        return maintenanceRepository.findAllByMaintenanceType(maintenanceType);
    }
	/**
     * Find  maintenances by status
     * @param status MaintenanceStatus     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByStatus(MaintenanceStatus status){
		Assert.notNull(status, "Expects a valid status");

        return maintenanceRepository.findAllByStatus(status);
    }
	/**
     * Find  maintenances by contractor
     * @param contractor Long     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByContractor_Id(Long contractor){
		Assert.notNull(contractor, "Expects a valid contractor");
		Assert.isTrue(contractor > 0, "Expects a valid contractor > 0");

        return maintenanceRepository.findAllByContractor_Id(contractor);
    }




    @PostConstruct
    public void initDB(){

    }
}
