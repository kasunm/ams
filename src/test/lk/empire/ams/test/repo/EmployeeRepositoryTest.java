package lk.empire.ams.test.repo;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>Title         : EmployeeRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Employee. An Employee of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository repository;

    private List<Employee> employees = new ArrayList<>();

     

    @Before
    public void setup(){
          

        employees.add( new Employee(0L, new ArrayList<AppEvent>()));
		employees.add( new Employee(0L, new ArrayList<AppEvent>()));
		employees.add( new Employee(0L, new ArrayList<AppEvent>()));
        Employee employee =  repository.save(employees.get(0));
        if(employee != null) employees.get(0).setId(employee.getId());
         employee =  repository.save(employees.get(1));
        if(employee != null) employees.get(1).setId(employee.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = employees.get(1).getId();
       Optional<Employee> employee = repository.findById(id);
       Assert.assertNotNull(employee);
       Assert.assertTrue("Expects a valid employee", employee.isPresent());
       Assert.assertTrue("Expects correct employee ID", employee.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Employee> employee = repository.findById(999L);
        Assert.assertNotNull(employee);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !employee.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Employee> employee = null;
        try {
            employee = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Employee> employees = repository.findAll();
        Assert.assertNotNull(employees);
        Assert.assertTrue("Expect saved data to be returned", employees.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Employee employee =  repository.saveAndFlush(employees.get(2));
        Assert.assertNotNull(employee);
        Assert.assertTrue("Expects a valid ID for saving", employee.getId() != null && employee.getId() > 0);
        Optional<Employee> check = repository.findById(employee.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = employees.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of employee", true);
    }

    @Test
    public void verifyDeleteByIDNonExisting(){
        try {
            repository.deleteById(888L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Unable to delete not existing item", e != null);
        }
    }

    @Test
    public void verifyDeleteByIDNull(){
        try {
            repository.deleteById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Invalid param zero", e != null);
        }
    }

 

}
