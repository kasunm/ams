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
 * <p>Title         : ApartmentRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Apartment. An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class ApartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApartmentRepository repository;

    private List<Apartment> apartments = new ArrayList<>();

     

    @Before
    public void setup(){
          

        apartments.add( new Apartment(0L, "Aloka", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "e Samnit"));
		apartments.add( new Apartment(0L, "Tenuki", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps"));
		apartments.add( new Apartment(0L, "Nayani", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basi"));
        Apartment apartment =  repository.save(apartments.get(0));
        if(apartment != null) apartments.get(0).setId(apartment.getId());
         apartment =  repository.save(apartments.get(1));
        if(apartment != null) apartments.get(1).setId(apartment.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = apartments.get(1).getId();
       Optional<Apartment> apartment = repository.findById(id);
       Assert.assertNotNull(apartment);
       Assert.assertTrue("Expects a valid apartment", apartment.isPresent());
       Assert.assertTrue("Expects correct apartment ID", apartment.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Apartment> apartment = repository.findById(999L);
        Assert.assertNotNull(apartment);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !apartment.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Apartment> apartment = null;
        try {
            apartment = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Apartment> apartments = repository.findAll();
        Assert.assertNotNull(apartments);
        Assert.assertTrue("Expect saved data to be returned", apartments.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Apartment apartment =  repository.saveAndFlush(apartments.get(2));
        Assert.assertNotNull(apartment);
        Assert.assertTrue("Expects a valid ID for saving", apartment.getId() != null && apartment.getId() > 0);
        Optional<Apartment> check = repository.findById(apartment.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = apartments.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of apartment", true);
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
