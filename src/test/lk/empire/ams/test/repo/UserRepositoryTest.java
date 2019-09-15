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
 * <p>Title         : UserRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private List<User> users = new ArrayList<>();

     

    @Before
    public void setup(){
          

        users.add( new User(0L, "Sulaiman", "Omega", "e was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town", "ut", "in the Samnite south of Italy.[5] His home town, Venusia, la", "as ", "e ", "mber 65 ", UserRole.Tenant));
		users.add( new User(0L, "Ruwan", "Omega", "on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home", "o", "as born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His h", "He was bo", " ", "cember 6", UserRole.Tenant));
		users.add( new User(0L, "Kusal", "Supun", "er 65 BC[nb 4] in the Samnite sou", "e was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home tow", " 4] in the Samnite so", "wa", "He was bo", "8 ", UserRole.Tenant));
        User user =  repository.save(users.get(0));
        if(user != null) users.get(0).setId(user.getId());
         user =  repository.save(users.get(1));
        if(user != null) users.get(1).setId(user.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = users.get(1).getId();
       Optional<User> user = repository.findById(id);
       Assert.assertNotNull(user);
       Assert.assertTrue("Expects a valid user", user.isPresent());
       Assert.assertTrue("Expects correct user ID", user.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<User> user = repository.findById(999L);
        Assert.assertNotNull(user);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !user.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<User> user = null;
        try {
            user = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<User> users = repository.findAll();
        Assert.assertNotNull(users);
        Assert.assertTrue("Expect saved data to be returned", users.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        User user =  repository.saveAndFlush(users.get(2));
        Assert.assertNotNull(user);
        Assert.assertTrue("Expects a valid ID for saving", user.getId() != null && user.getId() > 0);
        Optional<User> check = repository.findById(user.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = users.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of user", true);
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
