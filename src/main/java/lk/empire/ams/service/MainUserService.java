package lk.empire.ams.service;


import lk.empire.ams.model.entity.User;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : UserService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainUserService implements UserService{
    @Autowired
    UserRepository userRepository;

    /**
     * Get all users
     * @return List<User>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    /**
     * Get a specific user by id
     * @param id Long
     * @return Optional<User>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<User> getByID(Long id){
         return userRepository.findById(id);
    }

    /**
     * Save user and set id to passed user
     * @param user User
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public User saveUser(User user){
        Assert.notNull(user, "User parameter expected");
        Assert.isTrue(user.isValid(), "Valid User is expected");
        User savedUser = userRepository.saveAndFlush(user);
        if(savedUser != null && savedUser.getId() != null && savedUser.getId() > 0) {
            user.setId(savedUser.getId());
        }
        return user;
    }

    /**
     * Delete a user by ID
     * @param userID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long userID){
        userRepository.deleteById(userID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  users by userRole
     * @param userRole UserRole     
     * @return List<User>
     */
    public List<User> findAllByUserRole(UserRole userRole){
		Assert.notNull(userRole, "Expects a valid userRole");

        return userRepository.findAllByUserRole(userRole);
    }
	/**
     * Find  users by firstName
     * @param firstName String     
     * @return List<User>
     */
    public List<User> findAllByFirstName(String firstName){
		Assert.notNull(firstName, "Expects a valid firstName");

        return userRepository.findAllByFirstName(firstName);
    }
	/**
     * Find  users by email
     * @param email String     
     * @return List<User>
     */
    public List<User> findAllByEmail(String email){
		Assert.notNull(email, "Expects a valid email");

        return userRepository.findAllByEmail(email);
    }
	/**
     * Find  users by contact1
     * @param contact1 String     
     * @return List<User>
     */
    public List<User> findAllByContact1(String contact1){
		Assert.notNull(contact1, "Expects a valid contact1");

        return userRepository.findAllByContact1(contact1);
    }



    @PostConstruct
    public void initDB(){

    }
}
