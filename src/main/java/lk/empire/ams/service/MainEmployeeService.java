package lk.empire.ams.service;


import lk.empire.ams.model.entity.Employee;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : EmployeeService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Employee. An Employee of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainEmployeeService implements EmployeeService{
    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * Get all employees
     * @return List<Employee>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    /**
     * Get a specific employee by id
     * @param id Long
     * @return Optional<Employee>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Employee> getByID(Long id){
         return employeeRepository.findById(id);
    }

    /**
     * Save employee and set id to passed employee
     * @param employee Employee
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Employee saveEmployee(Employee employee){
        Assert.notNull(employee, "Employee parameter expected");
        Assert.isTrue(employee.isValid(), "Valid Employee is expected");
        Employee savedEmployee = employeeRepository.saveAndFlush(employee);
        if(savedEmployee != null && savedEmployee.getId() != null && savedEmployee.getId() > 0) {
            employee.setId(savedEmployee.getId());
        }
        return employee;
    }

    /**
     * Delete a employee by ID
     * @param employeeID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long employeeID){
        employeeRepository.deleteById(employeeID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  employees by firstName
     * @param firstName String     
     * @return List<Employee>
     */
    public List<Employee> findAllByFirstName(String firstName){
		Assert.notNull(firstName, "Expects a valid firstName");

        return employeeRepository.findAllByFirstName(firstName);
    }
	/**
     * Find  employees by email
     * @param email String     
     * @return List<Employee>
     */
    public List<Employee> findAllByEmail(String email){
		Assert.notNull(email, "Expects a valid email");

        return employeeRepository.findAllByEmail(email);
    }
	/**
     * Find  employees by nic
     * @param nic String     
     * @return List<Employee>
     */
    public List<Employee> findAllByNic(String nic){
		Assert.notNull(nic, "Expects a valid nic");

        return employeeRepository.findAllByNic(nic);
    }



    @PostConstruct
    public void initDB(){

    }
}
