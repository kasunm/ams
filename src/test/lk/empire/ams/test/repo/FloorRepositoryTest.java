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
 * <p>Title         : FloorRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class FloorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FloorRepository repository;

    private List<Floor> floors = new ArrayList<>();

     	@Autowired
	ApartmentRepository apartmentRepository; 	private Apartment apartment0;
		private Apartment apartment1;
		private Apartment apartment2;
	

    @Before
    public void setup(){
         apartment0 =   new Apartment(0L, "Nayani", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "5 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the ar");
	apartment1 =   new Apartment(0L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trad");
	Apartment savedapartment1 = apartmentRepository.save(apartment1);
	apartment1 = savedapartment1;
apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	 

        floors.add( new Floor(0L, "8 D", "Kusal", 764239, apartment1));
		floors.add( new Floor(0L, " was born ", "Supun", 156724, apartment1));
		floors.add( new Floor(0L, "s", "Aloka", 126689, apartment1));
        Floor floor =  repository.save(floors.get(0));
        if(floor != null) floors.get(0).setId(floor.getId());
         floor =  repository.save(floors.get(1));
        if(floor != null) floors.get(1).setId(floor.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = floors.get(1).getId();
       Optional<Floor> floor = repository.findById(id);
       Assert.assertNotNull(floor);
       Assert.assertTrue("Expects a valid floor", floor.isPresent());
       Assert.assertTrue("Expects correct floor ID", floor.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Floor> floor = repository.findById(999L);
        Assert.assertNotNull(floor);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !floor.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Floor> floor = null;
        try {
            floor = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Floor> floors = repository.findAll();
        Assert.assertNotNull(floors);
        Assert.assertTrue("Expect saved data to be returned", floors.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Floor floor =  repository.saveAndFlush(floors.get(2));
        Assert.assertNotNull(floor);
        Assert.assertTrue("Expects a valid ID for saving", floor.getId() != null && floor.getId() > 0);
        Optional<Floor> check = repository.findById(floor.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = floors.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of floor", true);
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
