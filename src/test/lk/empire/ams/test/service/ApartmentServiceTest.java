package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainApartmentService;
import lk.empire.ams.service.ApartmentService;
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
 * <p>Title         : ApartmentService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Apartment. An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ApartmentServiceTest {

    @MockBean
    private ApartmentRepository repository;


    @Autowired
    private ApartmentService apartmentService;

    private List<Apartment> apartments = new ArrayList<>();

     

    @TestConfiguration
    static class ApartmentServiceImplTestContextConfiguration {
        @Bean
        public ApartmentService employeeService() {
            return new MainApartmentService();
        }
    }

    @Before
    public void setUp(){
         

		apartments.add( new Apartment(1L, "Aloka", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this pe")); //ID 1
        apartments.add( new Apartment(2L, "Supun", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), " in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and ")); //ID 2
		apartments.add( new Apartment(3L, "Thamasha", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "er 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucan")); //ID 3

        given(repository.findAll()).willReturn(apartments);
        given(repository.findById(1L)).willReturn(Optional.of(apartments.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetApartmentsSuccess(){
        List<Apartment> apartments = apartmentService.getApartments();
        Assert.assertNotNull(apartments);
        Assert.assertTrue("Expect 3 apartments in result", apartments.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", apartments == this.apartments);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Apartment> apartment = null;
        apartment = apartmentService.getByID(111L);
        Assert.assertTrue("No match found", !apartment.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Apartment> apartment = null;
        try {
            apartment = apartmentService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", apartment == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Apartment> apartment = apartmentService.getByID(1L);
        Assert.assertNotNull(apartments);
        Assert.assertTrue("Expect a apartments in result", apartment.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", apartments.get(0) == apartment.get());
    }

    @Test
    public void verifySaveApartmentSuccess() throws Exception{
        Apartment newApartment =   new Apartment(1L, "Aloka", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this pe");
        Apartment savedApartment4 = new Apartment();
        BeanUtils.copyProperties(newApartment, savedApartment4);
        savedApartment4.setId(5L);
        given(repository.save(newApartment)).willReturn(savedApartment4);
        given(repository.saveAndFlush(newApartment)).willReturn(savedApartment4);

        Apartment apartment = apartmentService.saveApartment(newApartment);
        Assert.assertNotNull(apartment);
        Assert.assertTrue("Expect valid ID in returned apartment", apartment.getId() != null && apartment.getId() > 0);
    }

    @Test
    public void verifySaveApartmentNull() throws Exception{
        Apartment apartment = null;
        try {
            apartment = apartmentService.saveApartment(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned apartment", !StringUtils.isEmpty(e.getMessage()) && apartment == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveApartmentRepoIDNotReturned() throws Exception{
        Apartment newApartment =  new Apartment(0L, "Aloka", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "between Apulia and Lucania (Basilicata). Vari");
        given(repository.save(newApartment)).willReturn(newApartment);
        given(repository.saveAndFlush(newApartment)).willReturn(newApartment);

        Apartment apartment = apartmentService.saveApartment(newApartment);
        Assert.assertNotNull(apartment);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", apartment.getId() == null || apartment.getId() == 0);
    }

    @Test
    public void verifySaveApartmentRepoException() throws Exception{
        Apartment newApartment =  new Apartment(0L, "Aloka", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "between Apulia and Lucania (Basilicata). Vari");
        given(repository.save(newApartment)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newApartment)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Apartment apartment = null;
        try {
            apartment = apartmentService.saveApartment(newApartment);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && apartment == null);
        }

    }

    @Test
    public void verifyDeleteApartmentSuccess() throws Exception{
        ServiceStatus status = apartmentService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete apartment operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteApartmentIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = apartmentService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByNameTest() throws Exception{
        ArrayList<Apartment> matchedApartments = new ArrayList<>(1);
        matchedApartments.add(this.apartments.get(0));
        given(repository.findAllByName(any())).willReturn(matchedApartments);
        List <Apartment> resultApartments = apartmentService.findAllByName(users.get(0).getName());
        Assert.assertNotNull(resultApartments);
        Assert.assertTrue(resultApartments.size() > 0);
        Assert.assertTrue(resultApartments.get(0).getId() == this.apartments.get(0).getId());

    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<Apartment> matchedApartments = new ArrayList<>(1);
        matchedApartments.add(this.apartments.get(0));
        given(repository.findAllByName(any())).willReturn(matchedApartments);
        List <Apartment> resultApartments = null;
        try {
            resultApartments = apartmentService.findAllByName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultApartments == null);
        }
    }




}
