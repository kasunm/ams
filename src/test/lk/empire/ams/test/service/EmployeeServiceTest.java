package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainEmployeeService;
import lk.empire.ams.service.EmployeeService;
import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * <p>Title         : EmployeeService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Employee. An Employee of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeServiceTest {

    @MockBean
    private EmployeeRepository repository;


    @Autowired
    private EmployeeService employeeService;

    private List<Employee> employees = new ArrayList<>();

     

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public EmployeeService employeeService() {
            return new MainEmployeeService();
        }
    }

    @Before
    public void setUp(){
         

		employees.add( new Employee(1L, new ArrayList<AppEvent>())); //ID 1
        employees.add( new Employee(2L, new ArrayList<AppEvent>())); //ID 2
		employees.add( new Employee(3L, new ArrayList<AppEvent>())); //ID 3

        given(repository.findAll()).willReturn(employees);
        given(repository.findById(1L)).willReturn(Optional.of(employees.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetEmployeesSuccess(){
        List<Employee> employees = employeeService.getEmployees();
        Assert.assertNotNull(employees);
        Assert.assertTrue("Expect 3 employees in result", employees.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", employees == this.employees);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Employee> employee = null;
        employee = employeeService.getByID(111L);
        Assert.assertTrue("No match found", !employee.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Employee> employee = null;
        try {
            employee = employeeService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", employee == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Employee> employee = employeeService.getByID(1L);
        Assert.assertNotNull(employees);
        Assert.assertTrue("Expect a employees in result", employee.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", employees.get(0) == employee.get());
    }

    @Test
    public void verifySaveEmployeeSuccess() throws Exception{
        Employee newEmployee =   new Employee(1L, new ArrayList<AppEvent>());
        Employee savedEmployee4 = new Employee();
        BeanUtils.copyProperties(newEmployee, savedEmployee4);
        savedEmployee4.setId(5L);
        given(repository.save(newEmployee)).willReturn(savedEmployee4);
        given(repository.saveAndFlush(newEmployee)).willReturn(savedEmployee4);

        Employee employee = employeeService.saveEmployee(newEmployee);
        Assert.assertNotNull(employee);
        Assert.assertTrue("Expect valid ID in returned employee", employee.getId() != null && employee.getId() > 0);
    }

    @Test
    public void verifySaveEmployeeNull() throws Exception{
        Employee employee = null;
        try {
            employee = employeeService.saveEmployee(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned employee", !StringUtils.isEmpty(e.getMessage()) && employee == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveEmployeeRepoIDNotReturned() throws Exception{
        Employee newEmployee =  new Employee(0L, new ArrayList<AppEvent>());
        given(repository.save(newEmployee)).willReturn(newEmployee);
        given(repository.saveAndFlush(newEmployee)).willReturn(newEmployee);

        Employee employee = employeeService.saveEmployee(newEmployee);
        Assert.assertNotNull(employee);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", employee.getId() == null || employee.getId() == 0);
    }

    @Test
    public void verifySaveEmployeeRepoException() throws Exception{
        Employee newEmployee =  new Employee(0L, new ArrayList<AppEvent>());
        given(repository.save(newEmployee)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newEmployee)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Employee employee = null;
        try {
            employee = employeeService.saveEmployee(newEmployee);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && employee == null);
        }

    }

    @Test
    public void verifyDeleteEmployeeSuccess() throws Exception{
        ServiceStatus status = employeeService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete employee operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteEmployeeIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = employeeService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByFirstNameTest() throws Exception{
        ArrayList<Employee> matchedEmployees = new ArrayList<>(1);
        matchedEmployees.add(this.employees.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedEmployees);
        List <Employee> resultEmployees = employeeService.findAllByFirstName(users.get(0).getFirstName());
        Assert.assertNotNull(resultEmployees);
        Assert.assertTrue(resultEmployees.size() > 0);
        Assert.assertTrue(resultEmployees.get(0).getId() == this.employees.get(0).getId());

    }

    @Test
    public void findAllByFirstNameInvalidParamTest() throws Exception{
        ArrayList<Employee> matchedEmployees = new ArrayList<>(1);
        matchedEmployees.add(this.employees.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedEmployees);
        List <Employee> resultEmployees = null;
        try {
            resultEmployees = employeeService.findAllByFirstName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultEmployees == null);
        }
    }
 	
	@Test
    public void findAllByEmailTest() throws Exception{
        ArrayList<Employee> matchedEmployees = new ArrayList<>(1);
        matchedEmployees.add(this.employees.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedEmployees);
        List <Employee> resultEmployees = employeeService.findAllByEmail(users.get(0).getEmail());
        Assert.assertNotNull(resultEmployees);
        Assert.assertTrue(resultEmployees.size() > 0);
        Assert.assertTrue(resultEmployees.get(0).getId() == this.employees.get(0).getId());

    }

    @Test
    public void findAllByEmailInvalidParamTest() throws Exception{
        ArrayList<Employee> matchedEmployees = new ArrayList<>(1);
        matchedEmployees.add(this.employees.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedEmployees);
        List <Employee> resultEmployees = null;
        try {
            resultEmployees = employeeService.findAllByEmail(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultEmployees == null);
        }
    }
 	
	@Test
    public void findAllByNicTest() throws Exception{
        ArrayList<Employee> matchedEmployees = new ArrayList<>(1);
        matchedEmployees.add(this.employees.get(0));
        given(repository.findAllByNic(any())).willReturn(matchedEmployees);
        List <Employee> resultEmployees = employeeService.findAllByNic(users.get(0).getNic());
        Assert.assertNotNull(resultEmployees);
        Assert.assertTrue(resultEmployees.size() > 0);
        Assert.assertTrue(resultEmployees.get(0).getId() == this.employees.get(0).getId());

    }

    @Test
    public void findAllByNicInvalidParamTest() throws Exception{
        ArrayList<Employee> matchedEmployees = new ArrayList<>(1);
        matchedEmployees.add(this.employees.get(0));
        given(repository.findAllByNic(any())).willReturn(matchedEmployees);
        List <Employee> resultEmployees = null;
        try {
            resultEmployees = employeeService.findAllByNic(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultEmployees == null);
        }
    }




}
