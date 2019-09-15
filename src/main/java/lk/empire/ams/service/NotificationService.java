package lk.empire.ams.service;


import lk.empire.ams.model.entity.Notification;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : NotificationService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Notification. A Notification of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface NotificationService {

    /**
     * Get all notifications
     * @return List<Notification>
     */
    List<Notification> getNotifications();

    /**
     * Get a specific notification by id
     * @param id Long
     * @return Optional<Notification>
     */
    Optional<Notification> getByID(Long id);

    /**
     * Save notification and set id to passed notification
     * @param notification Notification
     * @return ServiceStatus
     */
    Notification saveNotification(Notification notification);

    /**
     * Delete a notification by ID
     * @param notificationID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long notificationID);



	/**
     * Find  notifications by description
     * @param description String     
     * @return List<Notification>
     */
    public List<Notification> findAllByDescription(String description);




}
