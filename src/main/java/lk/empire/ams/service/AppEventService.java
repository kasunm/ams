package lk.empire.ams.service;


import lk.empire.ams.model.entity.AppEvent;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : AppEventService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface AppEventService {

    /**
     * Get all appEvents
     * @return List<AppEvent>
     */
    List<AppEvent> getAppEvents();

    /**
     * Get a specific appEvent by id
     * @param id Long
     * @return Optional<AppEvent>
     */
    Optional<AppEvent> getByID(Long id);

    /**
     * Save appEvent and set id to passed appEvent
     * @param appEvent AppEvent
     * @return ServiceStatus
     */
    AppEvent saveAppEvent(AppEvent appEvent);

    /**
     * Delete a appEvent by ID
     * @param appEventID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long appEventID);



	/**
     * Find  appEvents by name
     * @param name String     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByName(String name);

	/**
     * Find  appEvents by date
     * @param date LocalDate     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByDate(LocalDate date);

	/**
     * Find  appEvents by eventType
     * @param eventType String     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByEventType(String eventType);

	/**
     * Find  appEvents by apartment
     * @param apartment Long     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByApartment_Id(Long apartment);

	/**
     * Find  appEvents by user
     * @param user Long     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByUser_Id(Long user);

	/**
     * Find  appEvents by employee
     * @param employee Long     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByEmployee_Id(Long employee);

    public List<AppEvent> findAllPendingEvents(Date startDate, Date endDate);

    public Long getByUserIdAndDates(Long userID, Date start, Date end);

}
