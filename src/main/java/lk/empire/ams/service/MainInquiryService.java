package lk.empire.ams.service;


import lk.empire.ams.model.entity.Inquiry;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : InquiryService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Inquiry. An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainInquiryService implements InquiryService{
    @Autowired
    InquiryRepository inquiryRepository;

    /**
     * Get all inquirys
     * @return List<Inquiry>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Inquiry> getInquirys(){
        return inquiryRepository.findAll();
    }

    /**
     * Get a specific inquiry by id
     * @param id Long
     * @return Optional<Inquiry>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Inquiry> getByID(Long id){
         return inquiryRepository.findById(id);
    }

    /**
     * Save inquiry and set id to passed inquiry
     * @param inquiry Inquiry
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Inquiry saveInquiry(Inquiry inquiry){
        Assert.notNull(inquiry, "Inquiry parameter expected");
        Assert.isTrue(inquiry.isValid(), "Valid Inquiry is expected");
        Inquiry savedInquiry = inquiryRepository.saveAndFlush(inquiry);
        if(savedInquiry != null && savedInquiry.getId() != null && savedInquiry.getId() > 0) {
            inquiry.setId(savedInquiry.getId());
        }
        return inquiry;
    }

    /**
     * Delete a inquiry by ID
     * @param inquiryID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long inquiryID){
        inquiryRepository.deleteById(inquiryID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  inquirys by description
     * @param description String     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByDescription(String description){
		Assert.notNull(description, "Expects a valid description");

        return inquiryRepository.findAllByDescription(description);
    }
	/**
     * Find  inquirys by type
     * @param type InquiryType     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByType(InquiryType type){
		Assert.notNull(type, "Expects a valid type");

        return inquiryRepository.findAllByType(type);
    }
	/**
     * Find  inquirys by action
     * @param action InquiryAction     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByAction(InquiryAction action){
		Assert.notNull(action, "Expects a valid action");

        return inquiryRepository.findAllByAction(action);
    }
	/**
     * Find  inquirys by client
     * @param client Long     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByClient_Id(Long client){
		Assert.notNull(client, "Expects a valid client");
		Assert.isTrue(client > 0, "Expects a valid client > 0");

        return inquiryRepository.findAllByClient_Id(client);
    }

    /**
     * Find  inquirys by status
     * @param status InquiryStatus
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByStatus(InquiryStatus status){
        Assert.notNull(status, "Expects a valid inquiry status");

        return inquiryRepository.findAllByStatus(status);
    }

	/**
     * Find  inquirys by employee
     * @param employee Long     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByEmployee_Id(Long employee){
		Assert.notNull(employee, "Expects a valid employee");
		Assert.isTrue(employee > 0, "Expects a valid employee > 0");

        return inquiryRepository.findAllByEmployee_Id(employee);
    }

    public Long getCountByClientIdAndStatus(Long clientId, InquiryStatus status){
        Assert.notNull(clientId, "Expects a valid client");
        Assert.isTrue(clientId > 0, "Expects a valid client > 0");
        Assert.notNull(status, "Expects a valid status");
        return inquiryRepository.countByClient_IdAndStatus(clientId, status);
    }

    public Long getCountByClientIdAndAction(Long clientId, InquiryAction action){
        Assert.notNull(clientId, "Expects a valid client");
        Assert.isTrue(clientId > 0, "Expects a valid client > 0");
        Assert.notNull(action, "Expects a valid action");
        return inquiryRepository.countByClient_IdAndAction(clientId, action);
    }



    @PostConstruct
    public void initDB(){

    }
}
