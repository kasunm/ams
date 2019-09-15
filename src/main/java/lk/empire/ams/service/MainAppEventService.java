package lk.empire.ams.service;


import lk.empire.ams.model.entity.AppEvent;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.AppEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : AppEventService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainAppEventService implements AppEventService{
    @Autowired
    AppEventRepository appEventRepository;

    /**
     * Get all appEvents
     * @return List<AppEvent>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AppEvent> getAppEvents(){
        return appEventRepository.findAll();
    }

    /**
     * Get a specific appEvent by id
     * @param id Long
     * @return Optional<AppEvent>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<AppEvent> getByID(Long id){
         return appEventRepository.findById(id);
    }

    /**
     * Save appEvent and set id to passed appEvent
     * @param appEvent AppEvent
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public AppEvent saveAppEvent(AppEvent appEvent){
        Assert.notNull(appEvent, "AppEvent parameter expected");
        Assert.isTrue(appEvent.isValid(), "Valid AppEvent is expected");
        AppEvent savedAppEvent = appEventRepository.saveAndFlush(appEvent);
        if(savedAppEvent != null && savedAppEvent.getId() != null && savedAppEvent.getId() > 0) {
            appEvent.setId(savedAppEvent.getId());
        }
        return appEvent;
    }

    /**
     * Delete a appEvent by ID
     * @param appEventID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long appEventID){
        appEventRepository.deleteById(appEventID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  appEvents by name
     * @param name String     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByName(String name){
		Assert.notNull(name, "Expects a valid name");

        return appEventRepository.findAllByName(name);
    }
	/**
     * Find  appEvents by date
     * @param date LocalDate     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByDate(LocalDate date){
		Assert.notNull(date, "Expects a valid date");

        return appEventRepository.findAllByDate(date);
    }
	/**
     * Find  appEvents by eventType
     * @param eventType String     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByEventType(String eventType){
		Assert.notNull(eventType, "Expects a valid eventType");

        return appEventRepository.findAllByEventType(eventType);
    }
	/**
     * Find  appEvents by apartment
     * @param apartment Long     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByApartment_Id(Long apartment){
		Assert.notNull(apartment, "Expects a valid apartment");
		Assert.isTrue(apartment > 0, "Expects a valid apartment > 0");

        return appEventRepository.findAllByApartment_Id(apartment);
    }
	/**
     * Find  appEvents by user
     * @param user Long     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByUser_Id(Long user){
		Assert.notNull(user, "Expects a valid user");
		Assert.isTrue(user > 0, "Expects a valid user > 0");

        return appEventRepository.findAllByUser_Id(user);
    }
	/**
     * Find  appEvents by employee
     * @param employee Long     
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllByEmployee_Id(Long employee){
		Assert.notNull(employee, "Expects a valid employee");
		Assert.isTrue(employee > 0, "Expects a valid employee > 0");

        return appEventRepository.findAllByEmployee_Id(employee);
    }

    /**
     * Find  appEvents by employee
     * @param startDate Date
     * @param endDate Date
     * @return List<AppEvent>
     */
    public List<AppEvent> findAllPendingEvents(Date startDate, Date endDate){
        Assert.notNull(startDate, "Expects a valid startDate");
        Assert.notNull(endDate, "Expects a valid endDate");

        return appEventRepository.findAllByStatusAndDateBetween(EventStatus.Pending, startDate,  endDate);
    }

    @Override
    public Long getByUserIdAndDates(Long userID, Date start, Date end) {
        Assert.notNull(userID, "Expects a valid userID");
        Assert.notNull(start, "Expects a valid start date");
        Assert.notNull(end, "Expects a end date");
        return appEventRepository.countByUser_IdAndDateBetween(userID, start, end);

    }

    @PostConstruct
    public void initDB(){

    }
}
