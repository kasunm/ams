package lk.empire.ams.service;


import lk.empire.ams.model.entity.Notification;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : NotificationService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Notification. A Notification of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainNotificationService implements NotificationService{
    @Autowired
    NotificationRepository notificationRepository;

    /**
     * Get all notifications
     * @return List<Notification>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Notification> getNotifications(){
        return notificationRepository.findAll();
    }

    /**
     * Get a specific notification by id
     * @param id Long
     * @return Optional<Notification>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Notification> getByID(Long id){
         return notificationRepository.findById(id);
    }

    /**
     * Save notification and set id to passed notification
     * @param notification Notification
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Notification saveNotification(Notification notification){
        Assert.notNull(notification, "Notification parameter expected");
        Assert.isTrue(notification.isValid(), "Valid Notification is expected");
        Notification savedNotification = notificationRepository.saveAndFlush(notification);
        if(savedNotification != null && savedNotification.getId() != null && savedNotification.getId() > 0) {
            notification.setId(savedNotification.getId());
        }
        return notification;
    }

    /**
     * Delete a notification by ID
     * @param notificationID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long notificationID){
        notificationRepository.deleteById(notificationID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  notifications by description
     * @param description String     
     * @return List<Notification>
     */
    public List<Notification> findAllByDescription(String description){
		Assert.notNull(description, "Expects a valid description");

        return notificationRepository.findAllByDescription(description);
    }



    @PostConstruct
    public void initDB(){

    }
}
