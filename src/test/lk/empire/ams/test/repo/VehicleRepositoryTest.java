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
 * <p>Title         : VehicleRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Vehicle. A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class VehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepository repository;

    private List<Vehicle> vehicles = new ArrayList<>();

     	@Autowired
	UnitRepository unitRepository; 	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
	

    @Before
    public void setup(){
         unit0 =   new Unit(0L, "Aloka", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(0L, "Ruwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	Unit savedunit1 = unitRepository.save(unit1);
	unit1 = savedunit1;
unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 

        vehicles.add( new Vehicle(0L, "d2160351335250", unit1, new HasSet <VehicleParking>(), "8 Dec", "He was born on 8 December 65 BC[nb 4] in the Samn"));
		vehicles.add( new Vehicle(0L, "w85", unit1, new HasSet <VehicleParking>(), "8 December 65", "as born on 8 December 65 BC[nb 4]"));
		vehicles.add( new Vehicle(0L, "v808877", unit1, new HasSet <VehicleParking>(), "rn on 8 Decembe", "rn on 8 December 65 "));
        Vehicle vehicle =  repository.save(vehicles.get(0));
        if(vehicle != null) vehicles.get(0).setId(vehicle.getId());
         vehicle =  repository.save(vehicles.get(1));
        if(vehicle != null) vehicles.get(1).setId(vehicle.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = vehicles.get(1).getId();
       Optional<Vehicle> vehicle = repository.findById(id);
       Assert.assertNotNull(vehicle);
       Assert.assertTrue("Expects a valid vehicle", vehicle.isPresent());
       Assert.assertTrue("Expects correct vehicle ID", vehicle.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Vehicle> vehicle = repository.findById(999L);
        Assert.assertNotNull(vehicle);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !vehicle.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Vehicle> vehicle = null;
        try {
            vehicle = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Vehicle> vehicles = repository.findAll();
        Assert.assertNotNull(vehicles);
        Assert.assertTrue("Expect saved data to be returned", vehicles.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Vehicle vehicle =  repository.saveAndFlush(vehicles.get(2));
        Assert.assertNotNull(vehicle);
        Assert.assertTrue("Expects a valid ID for saving", vehicle.getId() != null && vehicle.getId() > 0);
        Optional<Vehicle> check = repository.findById(vehicle.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = vehicles.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of vehicle", true);
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
