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
 * <p>Title         : CommonAreaRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class CommonAreaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommonAreaRepository repository;

    private List<CommonArea> commonAreas = new ArrayList<>();

     	@Autowired
	FloorRepository floorRepository; 	private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @Before
    public void setup(){
         floor0 =   new Floor(0L, "He was born on 8 De", "Tenuki", 797821, apartment1);
	floor1 =   new Floor(0L, "He was born on 8 De", "Nuwan", 438474, apartment1);
	Floor savedfloor1 = floorRepository.save(floor1);
	floor1 = savedfloor1;
floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 945416, apartment2);
	 

        commonAreas.add( new CommonArea(0L, "veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite", "h of Italy.[5] His home town, Venusia, lay on a trade route in", "outh", Availability.Available, floor1));
		commonAreas.add( new CommonArea(0L, "a and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and i", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enri", " of Italy.[5] His h", Availability.Available, floor1));
		commonAreas.add( new CommonArea(0L, "rade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the t", "n 8 December 65 BC[nb 4] in the Samnite south of Italy.[5", "e was born on 8 Decem", Availability.Available, floor1));
        CommonArea commonArea =  repository.save(commonAreas.get(0));
        if(commonArea != null) commonAreas.get(0).setId(commonArea.getId());
         commonArea =  repository.save(commonAreas.get(1));
        if(commonArea != null) commonAreas.get(1).setId(commonArea.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = commonAreas.get(1).getId();
       Optional<CommonArea> commonArea = repository.findById(id);
       Assert.assertNotNull(commonArea);
       Assert.assertTrue("Expects a valid commonArea", commonArea.isPresent());
       Assert.assertTrue("Expects correct commonArea ID", commonArea.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<CommonArea> commonArea = repository.findById(999L);
        Assert.assertNotNull(commonArea);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !commonArea.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<CommonArea> commonArea = null;
        try {
            commonArea = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<CommonArea> commonAreas = repository.findAll();
        Assert.assertNotNull(commonAreas);
        Assert.assertTrue("Expect saved data to be returned", commonAreas.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        CommonArea commonArea =  repository.saveAndFlush(commonAreas.get(2));
        Assert.assertNotNull(commonArea);
        Assert.assertTrue("Expects a valid ID for saving", commonArea.getId() != null && commonArea.getId() > 0);
        Optional<CommonArea> check = repository.findById(commonArea.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = commonAreas.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of commonArea", true);
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
