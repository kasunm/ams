package lk.empire.ams.service;


import lk.empire.ams.model.entity.Employee;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : EmployeeService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Employee. An Employee of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface EmployeeService {

    /**
     * Get all employees
     * @return List<Employee>
     */
    List<Employee> getEmployees();

    /**
     * Get a specific employee by id
     * @param id Long
     * @return Optional<Employee>
     */
    Optional<Employee> getByID(Long id);

    /**
     * Save employee and set id to passed employee
     * @param employee Employee
     * @return ServiceStatus
     */
    Employee saveEmployee(Employee employee);

    /**
     * Delete a employee by ID
     * @param employeeID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long employeeID);



	/**
     * Find  employees by firstName
     * @param firstName String     
     * @return List<Employee>
     */
    public List<Employee> findAllByFirstName(String firstName);

	/**
     * Find  employees by email
     * @param email String     
     * @return List<Employee>
     */
    public List<Employee> findAllByEmail(String email);

	/**
     * Find  employees by nic
     * @param nic String     
     * @return List<Employee>
     */
    public List<Employee> findAllByNic(String nic);




}
