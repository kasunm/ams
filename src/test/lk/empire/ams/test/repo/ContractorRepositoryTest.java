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
 * <p>Title         : ContractorRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class ContractorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContractorRepository repository;

    private List<Contractor> contractors = new ArrayList<>();

     

    @Before
    public void setup(){
          

        contractors.add( new Contractor(0L, "Nuwan", new ArrayList<Maintenance>()));
		contractors.add( new Contractor(0L, "Arun", new ArrayList<Maintenance>()));
		contractors.add( new Contractor(0L, "Kusal", new ArrayList<Maintenance>()));
        Contractor contractor =  repository.save(contractors.get(0));
        if(contractor != null) contractors.get(0).setId(contractor.getId());
         contractor =  repository.save(contractors.get(1));
        if(contractor != null) contractors.get(1).setId(contractor.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = contractors.get(1).getId();
       Optional<Contractor> contractor = repository.findById(id);
       Assert.assertNotNull(contractor);
       Assert.assertTrue("Expects a valid contractor", contractor.isPresent());
       Assert.assertTrue("Expects correct contractor ID", contractor.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Contractor> contractor = repository.findById(999L);
        Assert.assertNotNull(contractor);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !contractor.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Contractor> contractor = null;
        try {
            contractor = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Contractor> contractors = repository.findAll();
        Assert.assertNotNull(contractors);
        Assert.assertTrue("Expect saved data to be returned", contractors.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Contractor contractor =  repository.saveAndFlush(contractors.get(2));
        Assert.assertNotNull(contractor);
        Assert.assertTrue("Expects a valid ID for saving", contractor.getId() != null && contractor.getId() > 0);
        Optional<Contractor> check = repository.findById(contractor.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = contractors.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of contractor", true);
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
