package lk.empire.ams.controller;

import lk.empire.ams.model.dto.UserDTO;
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
 * <p>Title         : UserController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/users", value = "UserController", description = "All services related to UserController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

 

    public UserController(UserService userService  ){
        this.userService = userService;
 
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all users
     * @return ResponseEntity<List<UserDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getUsers(){
        logger.debug("Request to get all Users");
        List<UserDTO> users = userService.getUsers().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(users == null || users.size() < 1) throw new ResourceNotFoundException("Unable to find any Users");
        return new ResponseEntity(users, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific user by id
     * @param id Long
     * @return ResponseEntity<UserDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<UserDTO> getUsers(@PathVariable Long id) {
        logger.debug("Request to get a User by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<User> user = userService.getByID(id);
        if(user != null && user.isPresent()) return new ResponseEntity(convertToDTO(user.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any User with id " + id);
    }


    /**
     * Persist user. if id > 0 is present expects valid user object already present, and update it by
     * replacing values. Otherwise simply creates a new user and id is returned
     * @param user UserDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<UserDTO> saveUser(@RequestBody @Valid UserDTO user) throws Exception{
        logger.debug("Request to save User");
        User existingUser = new User();
        if(user.getId() != null && user.getId() > 0) {
            //Updating existing user - Check item with matching ID present
            Optional<User> savedUser = userService.getByID(user.getId());
            if(savedUser != null && savedUser.isPresent()) existingUser = savedUser.get();
            else throw new ResourceNotFoundException("In order to update User " + user.getId() + ", existing User must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        User saveUser = copyToUser(user, existingUser);
        User savedUser = userService.saveUser(saveUser);
        if(savedUser.getId() != null && savedUser.getId() > 0){
            logger.info("Saved User with id " + saveUser.getId());
            UserDTO savedUserDTo = convertToDTO(savedUser);
            return  ResponseEntity.created (new URI("/users/" + savedUser.getId())).body(savedUserDTo);
        }
        else{
            throw new PersistenceException("User not persisted: " + new Gson().toJson(savedUser));
        }
    }

   /**
     * Delete a user by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete User with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<User> user = userService.getByID(id);
        if(user == null || !user.isPresent()) throw new ResourceNotFoundException("In order to delete  User " + id + ", existing  User must be available with same ID");
        userService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  users by userRole
     * @param userRole UserRole     
     * @return List<User>
     */
    @GetMapping(path = "userRole/{userRole}")
    public ResponseEntity<List<User>> findAllByUserRole(@PathVariable UserRole userRole){
        logger.debug("findAllByUserRole(" + userRole + ")");
		Assert.notNull(userRole, "Expects a valid userRole");

        List<User> users =  userService.findAllByUserRole(userRole);
        if(users == null || users.size() < 1) throw new ResourceNotFoundException("Unable to find any users matching criteria");
        return new ResponseEntity(getDTOs(users), HttpStatus.ACCEPTED);
    }
	/**
     * Find  users by firstName
     * @param firstName String     
     * @return List<User>
     */
    @GetMapping(path = "firstName/{firstName}")
    public ResponseEntity<List<User>> findAllByFirstName(@PathVariable String firstName){
        logger.debug("findAllByFirstName(" + firstName + ")");
		Assert.notNull(firstName, "Expects a valid firstName");

        List<User> users =  userService.findAllByFirstName(firstName);
        if(users == null || users.size() < 1) throw new ResourceNotFoundException("Unable to find any users matching criteria");
        return new ResponseEntity(getDTOs(users), HttpStatus.ACCEPTED);
    }
	/**
     * Find  users by email
     * @param email String     
     * @return List<User>
     */
    @GetMapping(path = "email/{email}")
    public ResponseEntity<List<User>> findAllByEmail(@PathVariable String email){
        logger.debug("findAllByEmail(" + email + ")");
		Assert.notNull(email, "Expects a valid email");

        List<User> users =  userService.findAllByEmail(email);
        if(users == null || users.size() < 1) throw new ResourceNotFoundException("Unable to find any users matching criteria");
        return new ResponseEntity(getDTOs(users), HttpStatus.ACCEPTED);
    }
	/**
     * Find  users by contact1
     * @param contact1 String     
     * @return List<User>
     */
    @GetMapping(path = "contact1/{contact1}")
    public ResponseEntity<List<User>> findAllByContact1(@PathVariable String contact1){
        logger.debug("findAllByContact1(" + contact1 + ")");
		Assert.notNull(contact1, "Expects a valid contact1");

        List<User> users =  userService.findAllByContact1(contact1);
        if(users == null || users.size() < 1) throw new ResourceNotFoundException("Unable to find any users matching criteria");
        return new ResponseEntity(getDTOs(users), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private UserDTO convertToDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }

     private List<UserDTO> getDTOs(List<User> users){
           if(users == null) return null;
           List<UserDTO> dtoList = new ArrayList<UserDTO>(users.size());
           for(User user: users){
               UserDTO dto = convertToDTO(user);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private User copyToUser(UserDTO userDTO, User user){
 
         modelMapper.map(userDTO, user);
          return user;
    }

}
