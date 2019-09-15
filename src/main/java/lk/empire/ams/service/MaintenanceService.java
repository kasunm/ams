package lk.empire.ams.service;


import lk.empire.ams.model.entity.Maintenance;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : MaintenanceService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface MaintenanceService {

    /**
     * Get all maintenances
     * @return List<Maintenance>
     */
    List<Maintenance> getMaintenances();

    /**
     * Get a specific maintenance by id
     * @param id Long
     * @return Optional<Maintenance>
     */
    Optional<Maintenance> getByID(Long id);

    /**
     * Save maintenance and set id to passed maintenance
     * @param maintenance Maintenance
     * @return ServiceStatus
     */
    Maintenance saveMaintenance(Maintenance maintenance);

    /**
     * Delete a maintenance by ID
     * @param maintenanceID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long maintenanceID);



	/**
     * Find  maintenances by description
     * @param description String     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByDescription(String description);

	/**
     * Find  maintenances by blockName
     * @param blockName String     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByBlockName(String blockName);

	/**
     * Find  maintenances by doneBy
     * @param doneBy String     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByDoneBy(String doneBy);

	/**
     * Find  maintenances by maintenanceType
     * @param maintenanceType MaintenanceType     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByMaintenanceType(MaintenanceType maintenanceType);

	/**
     * Find  maintenances by status
     * @param status MaintenanceStatus     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByStatus(MaintenanceStatus status);

	/**
     * Find  maintenances by contractor
     * @param contractor Long     
     * @return List<Maintenance>
     */
    public List<Maintenance> findAllByContractor_Id(Long contractor);




}
