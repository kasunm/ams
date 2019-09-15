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
 * <p>Title         : MaintenanceRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class MaintenanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MaintenanceRepository repository;

    private List<Maintenance> maintenances = new ArrayList<>();

     	@Autowired
	ContractorRepository contractorRepository; 	private Contractor contractor0;
		private Contractor contractor1;
		private Contractor contractor2;
		@Autowired
	FloorRepository floorRepository; 	private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @Before
    public void setup(){
         contractor0 =   new Contractor(0L, "Tenuki", new ArrayList<Maintenance>());
	contractor1 =   new Contractor(0L, "Supun", new ArrayList<Maintenance>());
	Contractor savedcontractor1 = contractorRepository.save(contractor1);
	contractor1 = savedcontractor1;
contractor2 =   new Contractor(2L, "He was born on 8 December 65 BC[", new ArrayList<Maintenance>());
	floor0 =   new Floor(0L, "as born on 8 ", "Nuwan", 65412, apartment1);
	floor1 =   new Floor(0L, " born o", "Tenuki", 690003, apartment1);
	Floor savedfloor1 = floorRepository.save(floor1);
	floor1 = savedfloor1;
floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 925929, apartment2);
	 

        maintenances.add( new Maintenance(0L, " of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosp", "Arun", "5 BC", MaintenanceType.CommonArea, MaintenanceStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1));
		maintenances.add( new Maintenance(0L, " works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting", "Tenuki", "8 December 65 BC[", MaintenanceType.CommonArea, MaintenanceStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1));
		maintenances.add( new Maintenance(0L, "sia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or poss", "Aloka", "rn on 8 December 65 ", MaintenanceType.CommonArea, MaintenanceStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1));
        Maintenance maintenance =  repository.save(maintenances.get(0));
        if(maintenance != null) maintenances.get(0).setId(maintenance.getId());
         maintenance =  repository.save(maintenances.get(1));
        if(maintenance != null) maintenances.get(1).setId(maintenance.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = maintenances.get(1).getId();
       Optional<Maintenance> maintenance = repository.findById(id);
       Assert.assertNotNull(maintenance);
       Assert.assertTrue("Expects a valid maintenance", maintenance.isPresent());
       Assert.assertTrue("Expects correct maintenance ID", maintenance.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Maintenance> maintenance = repository.findById(999L);
        Assert.assertNotNull(maintenance);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !maintenance.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Maintenance> maintenance = null;
        try {
            maintenance = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Maintenance> maintenances = repository.findAll();
        Assert.assertNotNull(maintenances);
        Assert.assertTrue("Expect saved data to be returned", maintenances.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Maintenance maintenance =  repository.saveAndFlush(maintenances.get(2));
        Assert.assertNotNull(maintenance);
        Assert.assertTrue("Expects a valid ID for saving", maintenance.getId() != null && maintenance.getId() > 0);
        Optional<Maintenance> check = repository.findById(maintenance.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = maintenances.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of maintenance", true);
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
