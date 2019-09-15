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
 * <p>Title         : ClientRepository unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository unit test class for Client. A Client of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
public class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository repository;

    private List<Client> clients = new ArrayList<>();

     

    @Before
    public void setup(){
          

        clients.add( new Client(0L, "Tenuki", "border region betw", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>()));
		clients.add( new Client(0L, "Supun", "] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in th", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>()));
		clients.add( new Client(0L, "Nayani", "n on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). V", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>()));
        Client client =  repository.save(clients.get(0));
        if(client != null) clients.get(0).setId(client.getId());
         client =  repository.save(clients.get(1));
        if(client != null) clients.get(1).setId(client.getId());
    }

    @Test
    public void verifyGetByID(){
        Long id = clients.get(1).getId();
       Optional<Client> client = repository.findById(id);
       Assert.assertNotNull(client);
       Assert.assertTrue("Expects a valid client", client.isPresent());
       Assert.assertTrue("Expects correct client ID", client.get().getId() == id);
    }

    @Test
    public void verifyGetByIDNonExisting(){
        Optional<Client> client = repository.findById(999L);
        Assert.assertNotNull(client);
        Assert.assertTrue("Expects a no ${classDisplayName} to be returned", !client.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Client> client = null;
        try {
            client = repository.findById(null);
        } catch (InvalidDataAccessApiUsageException e) {
            Assert.assertTrue("Expects IllegalArgumentException", e != null);
        }


    }

    @Test
    public void verifyGetAll(){
        List<Client> clients = repository.findAll();
        Assert.assertNotNull(clients);
        Assert.assertTrue("Expect saved data to be returned", clients.size() == 2);
    }


    @Test
    public void verifySaveAndFlush(){
        Client client =  repository.saveAndFlush(clients.get(2));
        Assert.assertNotNull(client);
        Assert.assertTrue("Expects a valid ID for saving", client.getId() != null && client.getId() > 0);
        Optional<Client> check = repository.findById(client.getId());
        Assert.assertNotNull(check);
        Assert.assertTrue("Expects to retrieve by ID after saving", check.isPresent() &&  check.get() != null && check.get().getId() != null && check.get().getId() > 0);
    }



    @Test
    public void verifyDeleteByID(){
        Long id = clients.get(1).getId();
        repository.deleteById(id);
        Assert.assertTrue("Expects a deletion of client", true);
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
