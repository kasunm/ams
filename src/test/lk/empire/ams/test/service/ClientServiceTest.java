package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainClientService;
import lk.empire.ams.service.ClientService;
import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * <p>Title         : ClientService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Client. A Client of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ClientServiceTest {

    @MockBean
    private ClientRepository repository;


    @Autowired
    private ClientService clientService;

    private List<Client> clients = new ArrayList<>();

     

    @TestConfiguration
    static class ClientServiceImplTestContextConfiguration {
        @Bean
        public ClientService employeeService() {
            return new MainClientService();
        }
    }

    @Before
    public void setUp(){
         

		clients.add( new Client(1L, "Aloka", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border ", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>())); //ID 1
        clients.add( new Client(2L, "Ruwan", "b 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Itali", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>())); //ID 2
		clients.add( new Client(3L, "Nuwan", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>())); //ID 3

        given(repository.findAll()).willReturn(clients);
        given(repository.findById(1L)).willReturn(Optional.of(clients.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetClientsSuccess(){
        List<Client> clients = clientService.getClients();
        Assert.assertNotNull(clients);
        Assert.assertTrue("Expect 3 clients in result", clients.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", clients == this.clients);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Client> client = null;
        client = clientService.getByID(111L);
        Assert.assertTrue("No match found", !client.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Client> client = null;
        try {
            client = clientService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", client == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Client> client = clientService.getByID(1L);
        Assert.assertNotNull(clients);
        Assert.assertTrue("Expect a clients in result", client.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", clients.get(0) == client.get());
    }

    @Test
    public void verifySaveClientSuccess() throws Exception{
        Client newClient =   new Client(1L, "Aloka", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border ", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
        Client savedClient4 = new Client();
        BeanUtils.copyProperties(newClient, savedClient4);
        savedClient4.setId(5L);
        given(repository.save(newClient)).willReturn(savedClient4);
        given(repository.saveAndFlush(newClient)).willReturn(savedClient4);

        Client client = clientService.saveClient(newClient);
        Assert.assertNotNull(client);
        Assert.assertTrue("Expect valid ID in returned client", client.getId() != null && client.getId() > 0);
    }

    @Test
    public void verifySaveClientNull() throws Exception{
        Client client = null;
        try {
            client = clientService.saveClient(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned client", !StringUtils.isEmpty(e.getMessage()) && client == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveClientRepoIDNotReturned() throws Exception{
        Client newClient =  new Client(0L, "Omega", " south of Italy.[5] His home town, Venusia, lay on a trade route in the border re", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
        given(repository.save(newClient)).willReturn(newClient);
        given(repository.saveAndFlush(newClient)).willReturn(newClient);

        Client client = clientService.saveClient(newClient);
        Assert.assertNotNull(client);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", client.getId() == null || client.getId() == 0);
    }

    @Test
    public void verifySaveClientRepoException() throws Exception{
        Client newClient =  new Client(0L, "Omega", " south of Italy.[5] His home town, Venusia, lay on a trade route in the border re", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
        given(repository.save(newClient)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newClient)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Client client = null;
        try {
            client = clientService.saveClient(newClient);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && client == null);
        }

    }

    @Test
    public void verifyDeleteClientSuccess() throws Exception{
        ServiceStatus status = clientService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete client operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteClientIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = clientService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByFirstNameTest() throws Exception{
        ArrayList<Client> matchedClients = new ArrayList<>(1);
        matchedClients.add(this.clients.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedClients);
        List <Client> resultClients = clientService.findAllByFirstName(users.get(0).getFirstName());
        Assert.assertNotNull(resultClients);
        Assert.assertTrue(resultClients.size() > 0);
        Assert.assertTrue(resultClients.get(0).getId() == this.clients.get(0).getId());

    }

    @Test
    public void findAllByFirstNameInvalidParamTest() throws Exception{
        ArrayList<Client> matchedClients = new ArrayList<>(1);
        matchedClients.add(this.clients.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedClients);
        List <Client> resultClients = null;
        try {
            resultClients = clientService.findAllByFirstName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultClients == null);
        }
    }
 	
	@Test
    public void findAllByEmailTest() throws Exception{
        ArrayList<Client> matchedClients = new ArrayList<>(1);
        matchedClients.add(this.clients.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedClients);
        List <Client> resultClients = clientService.findAllByEmail(users.get(0).getEmail());
        Assert.assertNotNull(resultClients);
        Assert.assertTrue(resultClients.size() > 0);
        Assert.assertTrue(resultClients.get(0).getId() == this.clients.get(0).getId());

    }

    @Test
    public void findAllByEmailInvalidParamTest() throws Exception{
        ArrayList<Client> matchedClients = new ArrayList<>(1);
        matchedClients.add(this.clients.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedClients);
        List <Client> resultClients = null;
        try {
            resultClients = clientService.findAllByEmail(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultClients == null);
        }
    }
 	
	@Test
    public void findAllByNicTest() throws Exception{
        ArrayList<Client> matchedClients = new ArrayList<>(1);
        matchedClients.add(this.clients.get(0));
        given(repository.findAllByNic(any())).willReturn(matchedClients);
        List <Client> resultClients = clientService.findAllByNic(users.get(0).getNic());
        Assert.assertNotNull(resultClients);
        Assert.assertTrue(resultClients.size() > 0);
        Assert.assertTrue(resultClients.get(0).getId() == this.clients.get(0).getId());

    }

    @Test
    public void findAllByNicInvalidParamTest() throws Exception{
        ArrayList<Client> matchedClients = new ArrayList<>(1);
        matchedClients.add(this.clients.get(0));
        given(repository.findAllByNic(any())).willReturn(matchedClients);
        List <Client> resultClients = null;
        try {
            resultClients = clientService.findAllByNic(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultClients == null);
        }
    }




}
