package lk.empire.ams.service;


import lk.empire.ams.model.entity.Contractor;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : ContractorService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface ContractorService {

    /**
     * Get all contractors
     * @return List<Contractor>
     */
    List<Contractor> getContractors();

    /**
     * Get a specific contractor by id
     * @param id Long
     * @return Optional<Contractor>
     */
    Optional<Contractor> getByID(Long id);

    /**
     * Save contractor and set id to passed contractor
     * @param contractor Contractor
     * @return ServiceStatus
     */
    Contractor saveContractor(Contractor contractor);

    /**
     * Delete a contractor by ID
     * @param contractorID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long contractorID);



	/**
     * Find  contractors by companyName
     * @param companyName String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByCompanyName(String companyName);

	/**
     * Find  contractors by firstName
     * @param firstName String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByFirstName(String firstName);

	/**
     * Find  contractors by email
     * @param email String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByEmail(String email);

	/**
     * Find  contractors by nic
     * @param nic String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByNic(String nic);

    public Long getCount();




}
