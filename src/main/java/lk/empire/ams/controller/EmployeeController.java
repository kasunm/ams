package lk.empire.ams.controller;

import lk.empire.ams.model.dto.EmployeeDTO;
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
 * <p>Title         : EmployeeController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Employee. An Employee of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/employees", value = "EmployeeController", description = "All services related to EmployeeController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

 

    public EmployeeController(EmployeeService employeeService  ){
        this.employeeService = employeeService;
 
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all employees
     * @return ResponseEntity<List<EmployeeDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getEmployees(){
        logger.debug("Request to get all Employees");
        List<EmployeeDTO> employees = employeeService.getEmployees().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(employees == null || employees.size() < 1) throw new ResourceNotFoundException("Unable to find any Employees");
        return new ResponseEntity(employees, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific employee by id
     * @param id Long
     * @return ResponseEntity<EmployeeDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<EmployeeDTO> getEmployees(@PathVariable Long id) {
        logger.debug("Request to get a Employee by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Employee> employee = employeeService.getByID(id);
        if(employee != null && employee.isPresent()) return new ResponseEntity(convertToDTO(employee.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Employee with id " + id);
    }


    /**
     * Persist employee. if id > 0 is present expects valid employee object already present, and update it by
     * replacing values. Otherwise simply creates a new employee and id is returned
     * @param employee EmployeeDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody @Valid EmployeeDTO employee) throws Exception{
        logger.debug("Request to save Employee");
        Employee existingEmployee = new Employee();
        if(employee.getId() != null && employee.getId() > 0) {
            //Updating existing employee - Check item with matching ID present
            Optional<Employee> savedEmployee = employeeService.getByID(employee.getId());
            if(savedEmployee != null && savedEmployee.isPresent()) existingEmployee = savedEmployee.get();
            else throw new ResourceNotFoundException("In order to update Employee " + employee.getId() + ", existing Employee must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Employee saveEmployee = copyToEmployee(employee, existingEmployee);
        Employee savedEmployee = employeeService.saveEmployee(saveEmployee);
        if(savedEmployee.getId() != null && savedEmployee.getId() > 0){
            logger.info("Saved Employee with id " + saveEmployee.getId());
            EmployeeDTO savedEmployeeDTo = convertToDTO(savedEmployee);
            return  ResponseEntity.created (new URI("/employees/" + savedEmployee.getId())).body(savedEmployeeDTo);
        }
        else{
            throw new PersistenceException("Employee not persisted: " + new Gson().toJson(savedEmployee));
        }
    }

   /**
     * Delete a employee by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Employee with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Employee> employee = employeeService.getByID(id);
        if(employee == null || !employee.isPresent()) throw new ResourceNotFoundException("In order to delete  Employee " + id + ", existing  Employee must be available with same ID");
        employeeService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  employees by firstName
     * @param firstName String     
     * @return List<Employee>
     */
    @GetMapping(path = "firstName/{firstName}")
    public ResponseEntity<List<Employee>> findAllByFirstName(@PathVariable String firstName){
        logger.debug("findAllByFirstName(" + firstName + ")");
		Assert.notNull(firstName, "Expects a valid firstName");

        List<Employee> employees =  employeeService.findAllByFirstName(firstName);
        if(employees == null || employees.size() < 1) throw new ResourceNotFoundException("Unable to find any employees matching criteria");
        return new ResponseEntity(getDTOs(employees), HttpStatus.ACCEPTED);
    }
	/**
     * Find  employees by email
     * @param email String     
     * @return List<Employee>
     */
    @GetMapping(path = "email/{email}")
    public ResponseEntity<List<Employee>> findAllByEmail(@PathVariable String email){
        logger.debug("findAllByEmail(" + email + ")");
		Assert.notNull(email, "Expects a valid email");

        List<Employee> employees =  employeeService.findAllByEmail(email);
        if(employees == null || employees.size() < 1) throw new ResourceNotFoundException("Unable to find any employees matching criteria");
        return new ResponseEntity(getDTOs(employees), HttpStatus.ACCEPTED);
    }
	/**
     * Find  employees by nic
     * @param nic String     
     * @return List<Employee>
     */
    @GetMapping(path = "nic/{nic}")
    public ResponseEntity<List<Employee>> findAllByNic(@PathVariable String nic){
        logger.debug("findAllByNic(" + nic + ")");
		Assert.notNull(nic, "Expects a valid nic");

        List<Employee> employees =  employeeService.findAllByNic(nic);
        if(employees == null || employees.size() < 1) throw new ResourceNotFoundException("Unable to find any employees matching criteria");
        return new ResponseEntity(getDTOs(employees), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private EmployeeDTO convertToDTO(Employee employee){
        return modelMapper.map(employee, EmployeeDTO.class);
    }

     private List<EmployeeDTO> getDTOs(List<Employee> employees){
           if(employees == null) return null;
           List<EmployeeDTO> dtoList = new ArrayList<EmployeeDTO>(employees.size());
           for(Employee employee: employees){
               EmployeeDTO dto = convertToDTO(employee);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Employee copyToEmployee(EmployeeDTO employeeDTO, Employee employee){
 
         modelMapper.map(employeeDTO, employee);
          return employee;
    }

}
