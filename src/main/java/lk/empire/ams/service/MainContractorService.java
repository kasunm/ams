package lk.empire.ams.service;


import lk.empire.ams.model.entity.Contractor;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.ContractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : ContractorService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainContractorService implements ContractorService{
    @Autowired
    ContractorRepository contractorRepository;

    /**
     * Get all contractors
     * @return List<Contractor>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Contractor> getContractors(){
        return contractorRepository.findAll();
    }

    /**
     * Get a specific contractor by id
     * @param id Long
     * @return Optional<Contractor>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Contractor> getByID(Long id){
         return contractorRepository.findById(id);
    }

    /**
     * Save contractor and set id to passed contractor
     * @param contractor Contractor
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Contractor saveContractor(Contractor contractor){
        Assert.notNull(contractor, "Contractor parameter expected");
        Assert.isTrue(contractor.isValid(), "Valid Contractor is expected");
        Contractor savedContractor = contractorRepository.saveAndFlush(contractor);
        if(savedContractor != null && savedContractor.getId() != null && savedContractor.getId() > 0) {
            contractor.setId(savedContractor.getId());
        }
        return contractor;
    }

    /**
     * Delete a contractor by ID
     * @param contractorID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long contractorID){
        contractorRepository.deleteById(contractorID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  contractors by companyName
     * @param companyName String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByCompanyName(String companyName){
		Assert.notNull(companyName, "Expects a valid companyName");

        return contractorRepository.findAllByCompanyName(companyName);
    }
	/**
     * Find  contractors by firstName
     * @param firstName String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByFirstName(String firstName){
		Assert.notNull(firstName, "Expects a valid firstName");

        return contractorRepository.findAllByFirstName(firstName);
    }
	/**
     * Find  contractors by email
     * @param email String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByEmail(String email){
		Assert.notNull(email, "Expects a valid email");

        return contractorRepository.findAllByEmail(email);
    }
	/**
     * Find  contractors by nic
     * @param nic String     
     * @return List<Contractor>
     */
    public List<Contractor> findAllByNic(String nic){
		Assert.notNull(nic, "Expects a valid nic");

        return contractorRepository.findAllByNic(nic);
    }

    public Long getCount(){
        return contractorRepository.count();
    }



    @PostConstruct
    public void initDB(){

    }
}
