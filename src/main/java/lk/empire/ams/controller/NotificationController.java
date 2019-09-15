package lk.empire.ams.controller;

import lk.empire.ams.model.dto.NotificationDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.service.*;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import javax.persistence.PersistenceException;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.*;
import java.util.stream.Collectors;
/**
 * <p>Title         : NotificationController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Notification. A Notification of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/notifications", value = "NotificationController", description = "All services related to NotificationController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

 

    public NotificationController(NotificationService notificationService  ){
        this.notificationService = notificationService;
 
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all notifications
     * @return ResponseEntity<List<NotificationDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getNotifications(){
        logger.debug("Request to get all Notifications");
        List<NotificationDTO> notifications = notificationService.getNotifications().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(notifications == null || notifications.size() < 1) throw new ResourceNotFoundException("Unable to find any Notifications");
        return new ResponseEntity(notifications, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific notification by id
     * @param id Long
     * @return ResponseEntity<NotificationDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<NotificationDTO> getNotifications(@PathVariable Long id) {
        logger.debug("Request to get a Notification by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Notification> notification = notificationService.getByID(id);
        if(notification != null && notification.isPresent()) return new ResponseEntity(convertToDTO(notification.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Notification with id " + id);
    }


    /**
     * Persist notification. if id > 0 is present expects valid notification object already present, and update it by
     * replacing values. Otherwise simply creates a new notification and id is returned
     * @param notification NotificationDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<NotificationDTO> saveNotification(@RequestBody @Valid NotificationDTO notification) throws Exception{
        logger.debug("Request to save Notification");
        Notification existingNotification = new Notification();
        if(notification.getId() != null && notification.getId() > 0) {
            //Updating existing notification - Check item with matching ID present
            Optional<Notification> savedNotification = notificationService.getByID(notification.getId());
            if(savedNotification != null && savedNotification.isPresent()) existingNotification = savedNotification.get();
            else throw new ResourceNotFoundException("In order to update Notification " + notification.getId() + ", existing Notification must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Notification saveNotification = copyToNotification(notification, existingNotification);
        Notification savedNotification = notificationService.saveNotification(saveNotification);
        if(savedNotification.getId() != null && savedNotification.getId() > 0){
            logger.info("Saved Notification with id " + saveNotification.getId());
            NotificationDTO savedNotificationDTo = convertToDTO(savedNotification);
            return  ResponseEntity.created (new URI("/notifications/" + savedNotification.getId())).body(savedNotificationDTo);
        }
        else{
            throw new PersistenceException("Notification not persisted: " + new Gson().toJson(savedNotification));
        }
    }

   /**
     * Delete a notification by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Notification with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Notification> notification = notificationService.getByID(id);
        if(notification == null || !notification.isPresent()) throw new ResourceNotFoundException("In order to delete  Notification " + id + ", existing  Notification must be available with same ID");
        notificationService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  notifications by description
     * @param description String     
     * @return List<Notification>
     */
    @GetMapping(path = "description/{description}")
    public ResponseEntity<List<Notification>> findAllByDescription(@PathVariable String description){
        logger.debug("findAllByDescription(" + description + ")");
		Assert.notNull(description, "Expects a valid description");

        List<Notification> notifications =  notificationService.findAllByDescription(description);
        if(notifications == null || notifications.size() < 1) throw new ResourceNotFoundException("Unable to find any notifications matching criteria");
        return new ResponseEntity(getDTOs(notifications), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private NotificationDTO convertToDTO(Notification notification){
        return modelMapper.map(notification, NotificationDTO.class);
    }

     private List<NotificationDTO> getDTOs(List<Notification> notifications){
           if(notifications == null) return null;
           List<NotificationDTO> dtoList = new ArrayList<NotificationDTO>(notifications.size());
           for(Notification notification: notifications){
               NotificationDTO dto = convertToDTO(notification);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Notification copyToNotification(NotificationDTO notificationDTO, Notification notification){
 
         modelMapper.map(notificationDTO, notification);
          return notification;
    }

}
