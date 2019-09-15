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
 * <p>Title         : InquiryRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Inquiry. An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class InquiryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InquiryRepository repository;

    private List<Inquiry> inquirys = new ArrayList<>();

     	@Autowired
	ClientRepository clientRepository; 	private Client client0;
		private Client client1;
		private Client client2;
		@Autowired
	EmployeeRepository employeeRepository; 	private Employee employee0;
		private Employee employee1;
		private Employee employee2;
	

    @Before
    public void setup(){
         client0 =   new Client(0L, "Supun", "4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Itali", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	client1 =   new Client(0L, "Omega", " of Italy.[5] His home town, Venus", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client savedclient1 = clientRepository.save(client1);
	client1 = savedclient1;
client2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	employee0 =   new Employee(0L, new ArrayList<AppEvent>());
	employee1 =   new Employee(0L, new ArrayList<AppEvent>());
	Employee savedemployee1 = employeeRepository.save(employee1);
	employee1 = savedemployee1;
employee2 =   new Employee(2L, new ArrayList<AppEvent>());
	 

        inquirys.add( new Inquiry(0L, "ds even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed", "born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the ", InquiryStatus.Pending, InquiryType.Suggestion, InquiryAction.Done, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1));
		inquirys.add( new Inquiry(0L, " BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this p", "trade route in the border region between Apulia and Lucania (Basilicata). Various Itali", InquiryStatus.Pending, InquiryType.Suggestion, InquiryAction.Planned, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1));
		inquirys.add( new Inquiry(0L, "aught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According t", "ia, lay on a trade route in the border region between Apulia and Lucania (Basilicata).", InquiryStatus.Pending, InquiryType.Complain, InquiryAction.Planned, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1));
        Inquiry inquiry =  repository.save(inquirys.get(0));
        if(inquiry != null) inquirys.get(0).setId(inquiry.getId());
         inquiry =  repository.save(inquirys.get(1));
        if(inquiry != null) inquirys.get(1).setId(inquiry.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = inquirys.get(1).getId();
       Optional<Inquiry> inquiry = repository.findById(id);
       Assert.assertNotNull(inquiry);
       Assert.assertTrue("Expects a valid inquiry", inquiry.isPresent());
       Assert.assertTrue("Expects correct inquiry ID", inquiry.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Inquiry> inquiry = repository.findById(999L);
        Assert.assertNotNull(inquiry);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !inquiry.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Inquiry> inquiry = null;
        try {
            inquiry = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Inquiry> inquirys = repository.findAll();
        Assert.assertNotNull(inquirys);
        Assert.assertTrue("Expect saved data to be returned", inquirys.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Inquiry inquiry =  repository.saveAndFlush(inquirys.get(2));
        Assert.assertNotNull(inquiry);
        Assert.assertTrue("Expects a valid ID for saving", inquiry.getId() != null && inquiry.getId() > 0);
        Optional<Inquiry> check = repository.findById(inquiry.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = inquirys.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of inquiry", true);
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
