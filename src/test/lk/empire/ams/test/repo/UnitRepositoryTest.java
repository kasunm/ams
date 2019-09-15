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
 * <p>Title         : UnitRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Unit. A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class UnitRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UnitRepository repository;

    private List<Unit> unitss = new ArrayList<>();

     	@Autowired
	ClientRepository clientRepository; 	private Client owner0;
		private Client owner1;
		private Client owner2;
		@Autowired
	ClientRepository clientRepository; 	private Client renter0;
		private Client renter1;
		private Client renter2;
		@Autowired
	FloorRepository floorRepository; 	private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @Before
    public void setup(){
         owner0 =   new Client(0L, "Sumudu", "born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	owner1 =   new Client(0L, "Kusal", "5 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Bas", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client savedowner1 = clientRepository.save(owner1);
	owner1 = savedowner1;
owner2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	renter0 =   new Client(0L, "Omega", " on a trade route in the border region between Apulia and Lucania (Basilicata). Various It", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	renter1 =   new Client(0L, "Arun", "as born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). ", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client savedrenter1 = clientRepository.save(renter1);
	renter1 = savedrenter1;
renter2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	floor0 =   new Floor(0L, "e was born on 8 ", "Nuwan", 43989, apartment1);
	floor1 =   new Floor(0L, "He", "Arun", 371098, apartment1);
	Floor savedfloor1 = floorRepository.save(floor1);
	floor1 = savedfloor1;
floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 671848, apartment2);
	 

        unitss.add( new Unit(0L, "Nayani", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available));
		unitss.add( new Unit(0L, "Kusal", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available));
		unitss.add( new Unit(0L, "Sumudu", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available));
        Unit units =  repository.save(unitss.get(0));
        if(units != null) unitss.get(0).setId(units.getId());
         units =  repository.save(unitss.get(1));
        if(units != null) unitss.get(1).setId(units.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = unitss.get(1).getId();
       Optional<Unit> units = repository.findById(id);
       Assert.assertNotNull(units);
       Assert.assertTrue("Expects a valid units", units.isPresent());
       Assert.assertTrue("Expects correct units ID", units.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Unit> units = repository.findById(999L);
        Assert.assertNotNull(units);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !units.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Unit> units = null;
        try {
            units = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Unit> unitss = repository.findAll();
        Assert.assertNotNull(unitss);
        Assert.assertTrue("Expect saved data to be returned", unitss.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Unit units =  repository.saveAndFlush(unitss.get(2));
        Assert.assertNotNull(units);
        Assert.assertTrue("Expects a valid ID for saving", units.getId() != null && units.getId() > 0);
        Optional<Unit> check = repository.findById(units.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = unitss.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of units", true);
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
