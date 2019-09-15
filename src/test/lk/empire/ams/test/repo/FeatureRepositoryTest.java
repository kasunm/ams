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
 * <p>Title         : FeatureRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Feature. A Feature of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class FeatureRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeatureRepository repository;

    private List<Feature> features = new ArrayList<>();

     

    @Before
    public void setup(){
          

        features.add( new Feature(0L, "Nuwan", " 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in", "a, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects wer"));
		features.add( new Feature(0L, "Sumudu", "talic dialects were spoken in the area and this perhaps en", "4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region bet"));
		features.add( new Feature(0L, "Tenuki", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps en", "was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enr"));
        Feature feature =  repository.save(features.get(0));
        if(feature != null) features.get(0).setId(feature.getId());
         feature =  repository.save(features.get(1));
        if(feature != null) features.get(1).setId(feature.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = features.get(1).getId();
       Optional<Feature> feature = repository.findById(id);
       Assert.assertNotNull(feature);
       Assert.assertTrue("Expects a valid feature", feature.isPresent());
       Assert.assertTrue("Expects correct feature ID", feature.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Feature> feature = repository.findById(999L);
        Assert.assertNotNull(feature);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !feature.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Feature> feature = null;
        try {
            feature = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Feature> features = repository.findAll();
        Assert.assertNotNull(features);
        Assert.assertTrue("Expect saved data to be returned", features.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Feature feature =  repository.saveAndFlush(features.get(2));
        Assert.assertNotNull(feature);
        Assert.assertTrue("Expects a valid ID for saving", feature.getId() != null && feature.getId() > 0);
        Optional<Feature> check = repository.findById(feature.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = features.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of feature", true);
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
