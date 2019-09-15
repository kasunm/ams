package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainUserService;
import lk.empire.ams.service.UserService;
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
 * <p>Title         : UserService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @MockBean
    private UserRepository repository;


    @Autowired
    private UserService userService;

    private List<User> users = new ArrayList<>();

     

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService employeeService() {
            return new MainUserService();
        }
    }

    @Before
    public void setUp(){
         

		users.add( new User(1L, "Aloka", "Thamasha", "the Samnite south of Italy.[5] His home town, Venusia, la", "] in the Samnite south of Italy.[5]", " 65 BC[nb 4] in the Samnite south of I", "e was", "s bo", "was born on 8 Decembe", UserRole.Tenant)); //ID 1
        users.add( new User(2L, "Kusal", "Nayani", "e south of Italy", "n on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay", "the Samnite south of Italy.[5] His home town, Venusia, la", "He ", "He was bo", "orn on 8 December 65 ", UserRole.Tenant)); //ID 2
		users.add( new User(3L, "Aloka", "Omega", "e was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay", "BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, l", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home t", "He ", "was ", "n 8 De", UserRole.Tenant)); //ID 3

        given(repository.findAll()).willReturn(users);
        given(repository.findById(1L)).willReturn(Optional.of(users.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetUsersSuccess(){
        List<User> users = userService.getUsers();
        Assert.assertNotNull(users);
        Assert.assertTrue("Expect 3 users in result", users.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", users == this.users);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<User> user = null;
        user = userService.getByID(111L);
        Assert.assertTrue("No match found", !user.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<User> user = null;
        try {
            user = userService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", user == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<User> user = userService.getByID(1L);
        Assert.assertNotNull(users);
        Assert.assertTrue("Expect a users in result", user.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", users.get(0) == user.get());
    }

    @Test
    public void verifySaveUserSuccess() throws Exception{
        User newUser =   new User(1L, "Aloka", "Thamasha", "the Samnite south of Italy.[5] His home town, Venusia, la", "] in the Samnite south of Italy.[5]", " 65 BC[nb 4] in the Samnite south of I", "e was", "s bo", "was born on 8 Decembe", UserRole.Tenant);
        User savedUser4 = new User();
        BeanUtils.copyProperties(newUser, savedUser4);
        savedUser4.setId(5L);
        given(repository.save(newUser)).willReturn(savedUser4);
        given(repository.saveAndFlush(newUser)).willReturn(savedUser4);

        User user = userService.saveUser(newUser);
        Assert.assertNotNull(user);
        Assert.assertTrue("Expect valid ID in returned user", user.getId() != null && user.getId() > 0);
    }

    @Test
    public void verifySaveUserNull() throws Exception{
        User user = null;
        try {
            user = userService.saveUser(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned user", !StringUtils.isEmpty(e.getMessage()) && user == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveUserRepoIDNotReturned() throws Exception{
        User newUser =  new User(0L, "Kasun", "Tenuki", "the Samnite south of Italy.[5] His home town, Venusia", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, l", "ecember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay", "w", "was b", " was born on 8 December ", UserRole.Tenant);
        given(repository.save(newUser)).willReturn(newUser);
        given(repository.saveAndFlush(newUser)).willReturn(newUser);

        User user = userService.saveUser(newUser);
        Assert.assertNotNull(user);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", user.getId() == null || user.getId() == 0);
    }

    @Test
    public void verifySaveUserRepoException() throws Exception{
        User newUser =  new User(0L, "Kasun", "Tenuki", "the Samnite south of Italy.[5] His home town, Venusia", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, l", "ecember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay", "w", "was b", " was born on 8 December ", UserRole.Tenant);
        given(repository.save(newUser)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newUser)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        User user = null;
        try {
            user = userService.saveUser(newUser);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && user == null);
        }

    }

    @Test
    public void verifyDeleteUserSuccess() throws Exception{
        ServiceStatus status = userService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete user operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteUserIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = userService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByUserRoleTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByUserRole(any())).willReturn(matchedUsers);
        List <User> resultUsers = userService.findAllByUserRole(users.get(0).getUserRole());
        Assert.assertNotNull(resultUsers);
        Assert.assertTrue(resultUsers.size() > 0);
        Assert.assertTrue(resultUsers.get(0).getId() == this.users.get(0).getId());

    }

    @Test
    public void findAllByUserRoleInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByUserRole(any())).willReturn(matchedUsers);
        List <User> resultUsers = null;
        try {
            resultUsers = userService.findAllByUserRole(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUsers == null);
        }
    }
 	
	@Test
    public void findAllByFirstNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedUsers);
        List <User> resultUsers = userService.findAllByFirstName(users.get(0).getFirstName());
        Assert.assertNotNull(resultUsers);
        Assert.assertTrue(resultUsers.size() > 0);
        Assert.assertTrue(resultUsers.get(0).getId() == this.users.get(0).getId());

    }

    @Test
    public void findAllByFirstNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedUsers);
        List <User> resultUsers = null;
        try {
            resultUsers = userService.findAllByFirstName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUsers == null);
        }
    }
 	
	@Test
    public void findAllByEmailTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedUsers);
        List <User> resultUsers = userService.findAllByEmail(users.get(0).getEmail());
        Assert.assertNotNull(resultUsers);
        Assert.assertTrue(resultUsers.size() > 0);
        Assert.assertTrue(resultUsers.get(0).getId() == this.users.get(0).getId());

    }

    @Test
    public void findAllByEmailInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedUsers);
        List <User> resultUsers = null;
        try {
            resultUsers = userService.findAllByEmail(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUsers == null);
        }
    }
 	
	@Test
    public void findAllByContact1Test() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByContact1(any())).willReturn(matchedUsers);
        List <User> resultUsers = userService.findAllByContact1(users.get(0).getContact1());
        Assert.assertNotNull(resultUsers);
        Assert.assertTrue(resultUsers.size() > 0);
        Assert.assertTrue(resultUsers.get(0).getId() == this.users.get(0).getId());

    }

    @Test
    public void findAllByContact1InvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(repository.findAllByContact1(any())).willReturn(matchedUsers);
        List <User> resultUsers = null;
        try {
            resultUsers = userService.findAllByContact1(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUsers == null);
        }
    }




}
