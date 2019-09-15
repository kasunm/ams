package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainUnitService;
import lk.empire.ams.service.UnitService;
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
 * <p>Title         : UnitService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Unit. A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UnitServiceTest {

    @MockBean
    private UnitRepository repository;


    @Autowired
    private UnitService unitsService;

    private List<Unit> unitss = new ArrayList<>();

     	private Client owner0;
		private Client owner1;
		private Client owner2;
		private Client renter0;
		private Client renter1;
		private Client renter2;
		private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @TestConfiguration
    static class UnitServiceImplTestContextConfiguration {
        @Bean
        public UnitService employeeService() {
            return new MainUnitService();
        }
    }

    @Before
    public void setUp(){
        owner0 =   new Client(0L, "Tenuki", "ecember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various I", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	owner1 =   new Client(1L, "Kasun", "65 BC[nb 4] in the Samnite south of Italy.[5] His home town,", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	owner2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	renter0 =   new Client(0L, "Thamasha", " on a trade route in the border region between Apulia and Lucania (Basilicata). Various ", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	renter1 =   new Client(1L, "Kusal", "8 December 65 BC[nb 4] in the Samnite south of Italy.[5] ", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	renter2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	floor0 =   new Floor(0L, "rn on 8 D", "Ruwan", 831570, apartment1);
	floor1 =   new Floor(1L, "e was born on 8 D", "Nayani", 957126, apartment1);
	floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 93739, apartment2);
	 

		unitss.add( new Unit(1L, "Nuwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available)); //ID 1
        unitss.add( new Unit(2L, "Sumudu", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available)); //ID 2
		unitss.add( new Unit(3L, "Omega", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available)); //ID 3

        given(repository.findAll()).willReturn(unitss);
        given(repository.findById(1L)).willReturn(Optional.of(unitss.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetUnitsSuccess(){
        List<Unit> unitss = unitsService.getUnits();
        Assert.assertNotNull(unitss);
        Assert.assertTrue("Expect 3 unitss in result", unitss.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", unitss == this.unitss);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Unit> units = null;
        units = unitsService.getByID(111L);
        Assert.assertTrue("No match found", !units.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Unit> units = null;
        try {
            units = unitsService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", units == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Unit> units = unitsService.getByID(1L);
        Assert.assertNotNull(unitss);
        Assert.assertTrue("Expect a unitss in result", units.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", unitss.get(0) == units.get());
    }

    @Test
    public void verifySaveUnitSuccess() throws Exception{
        Unit newUnit =   new Unit(1L, "Nuwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
        Unit savedUnit4 = new Unit();
        BeanUtils.copyProperties(newUnit, savedUnit4);
        savedUnit4.setId(5L);
        given(repository.save(newUnit)).willReturn(savedUnit4);
        given(repository.saveAndFlush(newUnit)).willReturn(savedUnit4);

        Unit units = unitsService.saveUnit(newUnit);
        Assert.assertNotNull(units);
        Assert.assertTrue("Expect valid ID in returned units", units.getId() != null && units.getId() > 0);
    }

    @Test
    public void verifySaveUnitNull() throws Exception{
        Unit units = null;
        try {
            units = unitsService.saveUnit(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned units", !StringUtils.isEmpty(e.getMessage()) && units == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveUnitRepoIDNotReturned() throws Exception{
        Unit newUnit =  new Unit(0L, "Aloka", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
        given(repository.save(newUnit)).willReturn(newUnit);
        given(repository.saveAndFlush(newUnit)).willReturn(newUnit);

        Unit units = unitsService.saveUnit(newUnit);
        Assert.assertNotNull(units);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", units.getId() == null || units.getId() == 0);
    }

    @Test
    public void verifySaveUnitRepoException() throws Exception{
        Unit newUnit =  new Unit(0L, "Aloka", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
        given(repository.save(newUnit)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newUnit)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Unit units = null;
        try {
            units = unitsService.saveUnit(newUnit);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && units == null);
        }

    }

    @Test
    public void verifyDeleteUnitSuccess() throws Exception{
        ServiceStatus status = unitsService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete units operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteUnitIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = unitsService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByNameTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByName(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = unitsService.findAllByName(users.get(0).getName());
        Assert.assertNotNull(resultUnits);
        Assert.assertTrue(resultUnits.size() > 0);
        Assert.assertTrue(resultUnits.get(0).getId() == this.unitss.get(0).getId());

    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByName(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = null;
        try {
            resultUnits = unitsService.findAllByName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUnits == null);
        }
    }
 	
	@Test
    public void findAllByOwner_IdTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByOwner_Id(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = unitsService.findAllByOwner_Id(users.get(0).getOwner());
        Assert.assertNotNull(resultUnits);
        Assert.assertTrue(resultUnits.size() > 0);
        Assert.assertTrue(resultUnits.get(0).getId() == this.unitss.get(0).getId());

    }

    @Test
    public void findAllByOwner_IdInvalidParamTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByOwner_Id(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = null;
        try {
            resultUnits = unitsService.findAllByOwner_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUnits == null);
        }
    }
 	
	@Test
    public void findAllByRenter_IdTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByRenter_Id(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = unitsService.findAllByRenter_Id(users.get(0).getRenter());
        Assert.assertNotNull(resultUnits);
        Assert.assertTrue(resultUnits.size() > 0);
        Assert.assertTrue(resultUnits.get(0).getId() == this.unitss.get(0).getId());

    }

    @Test
    public void findAllByRenter_IdInvalidParamTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByRenter_Id(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = null;
        try {
            resultUnits = unitsService.findAllByRenter_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUnits == null);
        }
    }
 	
	@Test
    public void findAllByAvailabilityTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByAvailability(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = unitsService.findAllByAvailability(users.get(0).getAvailability());
        Assert.assertNotNull(resultUnits);
        Assert.assertTrue(resultUnits.size() > 0);
        Assert.assertTrue(resultUnits.get(0).getId() == this.unitss.get(0).getId());

    }

    @Test
    public void findAllByAvailabilityInvalidParamTest() throws Exception{
        ArrayList<Unit> matchedUnits = new ArrayList<>(1);
        matchedUnits.add(this.unitss.get(0));
        given(repository.findAllByAvailability(any())).willReturn(matchedUnits);
        List <Unit> resultUnits = null;
        try {
            resultUnits = unitsService.findAllByAvailability(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultUnits == null);
        }
    }




}
