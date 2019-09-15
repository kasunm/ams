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
 * <p>Title         : VehicleParkingRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class VehicleParkingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleParkingRepository repository;

    private List<VehicleParking> vehicleParkings = new ArrayList<>();

     	@Autowired
	VehicleRepository vehicleRepository; 	private Vehicle vehicle0;
		private Vehicle vehicle1;
		private Vehicle vehicle2;
		@Autowired
	ParkingSlotRepository parkingSlotRepository; 	private ParkingSlot parkingSlot0;
		private ParkingSlot parkingSlot1;
		private ParkingSlot parkingSlot2;
	

    @Before
    public void setup(){
         vehicle0 =   new Vehicle(0L, "x00703664", unit1, new HasSet <VehicleParking>(), "as born on 8 December 65 ", "born on 8 December 65 BC[nb 4] in the Samn");
	vehicle1 =   new Vehicle(0L, "j050", unit1, new HasSet <VehicleParking>(), " 6", "H");
	Vehicle savedvehicle1 = vehicleRepository.save(vehicle1);
	vehicle1 = savedvehicle1;
vehicle2 =   new Vehicle(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born on 8 December 65 BC[", "He was born on 8 December 65 BC[nb 4] in the Samnite");
	parkingSlot0 =   new ParkingSlot(0L, "Supun", unit1, new HasSet <VehicleParking>(), "m57", Availability.Available);
	parkingSlot1 =   new ParkingSlot(0L, "Tenuki", unit1, new HasSet <VehicleParking>(), "j0", Availability.Available);
	ParkingSlot savedparkingSlot1 = parkingSlotRepository.save(parkingSlot1);
	parkingSlot1 = savedparkingSlot1;
parkingSlot2 =   new ParkingSlot(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born ", Availability.Available);
	 

        vehicleParkings.add( new VehicleParking(0L, "Sumudu", "orn on 8 Decem", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, " born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in"));
		vehicleParkings.add( new VehicleParking(0L, "Kasun", " was born ", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "nite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of"));
		vehicleParkings.add( new VehicleParking(0L, "Nuwan", " born on 8", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "amnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was th"));
        VehicleParking vehicleParking =  repository.save(vehicleParkings.get(0));
        if(vehicleParking != null) vehicleParkings.get(0).setId(vehicleParking.getId());
         vehicleParking =  repository.save(vehicleParkings.get(1));
        if(vehicleParking != null) vehicleParkings.get(1).setId(vehicleParking.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = vehicleParkings.get(1).getId();
       Optional<VehicleParking> vehicleParking = repository.findById(id);
       Assert.assertNotNull(vehicleParking);
       Assert.assertTrue("Expects a valid vehicleParking", vehicleParking.isPresent());
       Assert.assertTrue("Expects correct vehicleParking ID", vehicleParking.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<VehicleParking> vehicleParking = repository.findById(999L);
        Assert.assertNotNull(vehicleParking);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !vehicleParking.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<VehicleParking> vehicleParking = null;
        try {
            vehicleParking = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<VehicleParking> vehicleParkings = repository.findAll();
        Assert.assertNotNull(vehicleParkings);
        Assert.assertTrue("Expect saved data to be returned", vehicleParkings.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        VehicleParking vehicleParking =  repository.saveAndFlush(vehicleParkings.get(2));
        Assert.assertNotNull(vehicleParking);
        Assert.assertTrue("Expects a valid ID for saving", vehicleParking.getId() != null && vehicleParking.getId() > 0);
        Optional<VehicleParking> check = repository.findById(vehicleParking.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = vehicleParkings.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of vehicleParking", true);
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
