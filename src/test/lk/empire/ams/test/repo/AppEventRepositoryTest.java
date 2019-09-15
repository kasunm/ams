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
 * <p>Title         : AppEventRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class AppEventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppEventRepository repository;

    private List<AppEvent> appEvents = new ArrayList<>();

     	@Autowired
	ApartmentRepository apartmentRepository; 	private Apartment apartment0;
		private Apartment apartment1;
		private Apartment apartment2;
		@Autowired
	ClientRepository clientRepository; 	private Client user0;
		private Client user1;
		private Client user2;
		@Autowired
	EmployeeRepository employeeRepository; 	private Employee employee0;
		private Employee employee1;
		private Employee employee2;
	

    @Before
    public void setup(){
         apartment0 =   new Apartment(0L, "Kasun", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "ecember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area a");
	apartment1 =   new Apartment(0L, "Supun", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "rn on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apul");
	Apartment savedapartment1 = apartmentRepository.save(apartment1);
	apartment1 = savedapartment1;
apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	user0 =   new Client(0L, "Aloka", "me town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata).", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	user1 =   new Client(0L, "Tenuki", "talic", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client saveduser1 = clientRepository.save(user1);
	user1 = saveduser1;
user2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	employee0 =   new Employee(0L, new ArrayList<AppEvent>());
	employee1 =   new Employee(0L, new ArrayList<AppEvent>());
	Employee savedemployee1 = employeeRepository.save(employee1);
	employee1 = savedemployee1;
employee2 =   new Employee(2L, new ArrayList<AppEvent>());
	 

        appEvents.add( new AppEvent(0L, "Tenuki", 189564, "ith Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a referen", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), " BC[nb 4] in the Samnite south of Italy.[5] His home town, V", " was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region betwee", apartment1, user1, employee1));
		appEvents.add( new AppEvent(0L, "Nayani", 355169, "on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his ", EventStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), " of Italy.[5] His home town, Venusia, ", " born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route", apartment1, user1, employee1));
		appEvents.add( new AppEvent(0L, "Sulaiman", 289596, "r was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and ma", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "orn on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia", "65 BC[nb 4] in the Samnite south of Italy.[5] Hi", apartment1, user1, employee1));
        AppEvent appEvent =  repository.save(appEvents.get(0));
        if(appEvent != null) appEvents.get(0).setId(appEvent.getId());
         appEvent =  repository.save(appEvents.get(1));
        if(appEvent != null) appEvents.get(1).setId(appEvent.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = appEvents.get(1).getId();
       Optional<AppEvent> appEvent = repository.findById(id);
       Assert.assertNotNull(appEvent);
       Assert.assertTrue("Expects a valid appEvent", appEvent.isPresent());
       Assert.assertTrue("Expects correct appEvent ID", appEvent.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<AppEvent> appEvent = repository.findById(999L);
        Assert.assertNotNull(appEvent);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !appEvent.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<AppEvent> appEvent = null;
        try {
            appEvent = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<AppEvent> appEvents = repository.findAll();
        Assert.assertNotNull(appEvents);
        Assert.assertTrue("Expect saved data to be returned", appEvents.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        AppEvent appEvent =  repository.saveAndFlush(appEvents.get(2));
        Assert.assertNotNull(appEvent);
        Assert.assertTrue("Expects a valid ID for saving", appEvent.getId() != null && appEvent.getId() > 0);
        Optional<AppEvent> check = repository.findById(appEvent.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = appEvents.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of appEvent", true);
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
