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
 * <p>Title         : PaymentRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Payment. A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository repository;

    private List<Payment> payments = new ArrayList<>();

     	@Autowired
	UnitRepository unitRepository; 	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
		@Autowired
	ClientRepository clientRepository; 	private Client client0;
		private Client client1;
		private Client client2;
	

    @Before
    public void setup(){
         unit0 =   new Unit(0L, "Tenuki", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(0L, "Kusal", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	Unit savedunit1 = unitRepository.save(unit1);
	unit1 = savedunit1;
unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	client0 =   new Client(0L, "Nuwan", "gion between Apulia and Lucania (Basilicata). Various Italic", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	client1 =   new Client(0L, "Tenuki", " 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client savedclient1 = clientRepository.save(client1);
	client1 = savedclient1;
client2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	 

        payments.add( new Payment(0L, PaymentMethod.BankTransfer, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 766458, 176799, 102277.97602660464, unit1, client1));
		payments.add( new Payment(0L, PaymentMethod.Cheque, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 796516, 934168, 528123.9102237523, unit1, client1));
		payments.add( new Payment(0L, PaymentMethod.BankTransfer, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 783708, 991145, 363306.5945699274, unit1, client1));
        Payment payment =  repository.save(payments.get(0));
        if(payment != null) payments.get(0).setId(payment.getId());
         payment =  repository.save(payments.get(1));
        if(payment != null) payments.get(1).setId(payment.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = payments.get(1).getId();
       Optional<Payment> payment = repository.findById(id);
       Assert.assertNotNull(payment);
       Assert.assertTrue("Expects a valid payment", payment.isPresent());
       Assert.assertTrue("Expects correct payment ID", payment.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Payment> payment = repository.findById(999L);
        Assert.assertNotNull(payment);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !payment.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Payment> payment = null;
        try {
            payment = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Payment> payments = repository.findAll();
        Assert.assertNotNull(payments);
        Assert.assertTrue("Expect saved data to be returned", payments.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Payment payment =  repository.saveAndFlush(payments.get(2));
        Assert.assertNotNull(payment);
        Assert.assertTrue("Expects a valid ID for saving", payment.getId() != null && payment.getId() > 0);
        Optional<Payment> check = repository.findById(payment.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = payments.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of payment", true);
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
