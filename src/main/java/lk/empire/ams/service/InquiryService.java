package lk.empire.ams.service;


import lk.empire.ams.model.entity.Inquiry;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : InquiryService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Inquiry. An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface InquiryService {

    /**
     * Get all inquirys
     * @return List<Inquiry>
     */
    List<Inquiry> getInquirys();

    /**
     * Get a specific inquiry by id
     * @param id Long
     * @return Optional<Inquiry>
     */
    Optional<Inquiry> getByID(Long id);

    /**
     * Save inquiry and set id to passed inquiry
     * @param inquiry Inquiry
     * @return ServiceStatus
     */
    Inquiry saveInquiry(Inquiry inquiry);

    /**
     * Delete a inquiry by ID
     * @param inquiryID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long inquiryID);



	/**
     * Find  inquirys by description
     * @param description String     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByDescription(String description);

	/**
     * Find  inquirys by type
     * @param type InquiryType     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByType(InquiryType type);

	/**
     * Find  inquirys by action
     * @param action InquiryAction     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByAction(InquiryAction action);

	/**
     * Find  inquirys by client
     * @param client Long     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByClient_Id(Long client);

    public List<Inquiry> findAllByStatus(InquiryStatus status);



	/**
     * Find  inquirys by employee
     * @param employee Long     
     * @return List<Inquiry>
     */
    public List<Inquiry> findAllByEmployee_Id(Long employee);

    public Long getCountByClientIdAndStatus(Long clientId, InquiryStatus status);

    public Long getCountByClientIdAndAction(Long clientId, InquiryAction action);


}
