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
 * <p>Title         : ParkingSlotRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class ParkingSlotRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParkingSlotRepository repository;

    private List<ParkingSlot> parkingSlots = new ArrayList<>();

     	@Autowired
	UnitRepository unitRepository; 	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
	

    @Before
    public void setup(){
         unit0 =   new Unit(0L, "Arun", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(0L, "Nuwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	Unit savedunit1 = unitRepository.save(unit1);
	unit1 = savedunit1;
unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 

        parkingSlots.add( new ParkingSlot(0L, "Aloka", unit1, new HasSet <VehicleParking>(), "p", Availability.Available));
		parkingSlots.add( new ParkingSlot(0L, "Supun", unit1, new HasSet <VehicleParking>(), "o203", Availability.Available));
		parkingSlots.add( new ParkingSlot(0L, "Ruwan", unit1, new HasSet <VehicleParking>(), "n7855", Availability.Available));
        ParkingSlot parkingSlot =  repository.save(parkingSlots.get(0));
        if(parkingSlot != null) parkingSlots.get(0).setId(parkingSlot.getId());
         parkingSlot =  repository.save(parkingSlots.get(1));
        if(parkingSlot != null) parkingSlots.get(1).setId(parkingSlot.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = parkingSlots.get(1).getId();
       Optional<ParkingSlot> parkingSlot = repository.findById(id);
       Assert.assertNotNull(parkingSlot);
       Assert.assertTrue("Expects a valid parkingSlot", parkingSlot.isPresent());
       Assert.assertTrue("Expects correct parkingSlot ID", parkingSlot.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<ParkingSlot> parkingSlot = repository.findById(999L);
        Assert.assertNotNull(parkingSlot);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !parkingSlot.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<ParkingSlot> parkingSlot = null;
        try {
            parkingSlot = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<ParkingSlot> parkingSlots = repository.findAll();
        Assert.assertNotNull(parkingSlots);
        Assert.assertTrue("Expect saved data to be returned", parkingSlots.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        ParkingSlot parkingSlot =  repository.saveAndFlush(parkingSlots.get(2));
        Assert.assertNotNull(parkingSlot);
        Assert.assertTrue("Expects a valid ID for saving", parkingSlot.getId() != null && parkingSlot.getId() > 0);
        Optional<ParkingSlot> check = repository.findById(parkingSlot.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = parkingSlots.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of parkingSlot", true);
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
