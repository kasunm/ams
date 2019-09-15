package lk.empire.ams.service;


import lk.empire.ams.model.entity.User;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : UserService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface UserService {

    /**
     * Get all users
     * @return List<User>
     */
    List<User> getUsers();

    /**
     * Get a specific user by id
     * @param id Long
     * @return Optional<User>
     */
    Optional<User> getByID(Long id);

    /**
     * Save user and set id to passed user
     * @param user User
     * @return ServiceStatus
     */
    User saveUser(User user);

    /**
     * Delete a user by ID
     * @param userID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long userID);



	/**
     * Find  users by userRole
     * @param userRole UserRole     
     * @return List<User>
     */
    public List<User> findAllByUserRole(UserRole userRole);

	/**
     * Find  users by firstName
     * @param firstName String     
     * @return List<User>
     */
    public List<User> findAllByFirstName(String firstName);

	/**
     * Find  users by email
     * @param email String     
     * @return List<User>
     */
    public List<User> findAllByEmail(String email);

	/**
     * Find  users by contact1
     * @param contact1 String     
     * @return List<User>
     */
    public List<User> findAllByContact1(String contact1);




}
