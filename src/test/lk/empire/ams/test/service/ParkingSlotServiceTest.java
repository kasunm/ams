package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainParkingSlotService;
import lk.empire.ams.service.ParkingSlotService;
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
 * <p>Title         : ParkingSlotService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ParkingSlotServiceTest {

    @MockBean
    private ParkingSlotRepository repository;


    @Autowired
    private ParkingSlotService parkingSlotService;

    private List<ParkingSlot> parkingSlots = new ArrayList<>();

     	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
	

    @TestConfiguration
    static class ParkingSlotServiceImplTestContextConfiguration {
        @Bean
        public ParkingSlotService employeeService() {
            return new MainParkingSlotService();
        }
    }

    @Before
    public void setUp(){
        unit0 =   new Unit(0L, "Sumudu", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(1L, "Ruwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 

		parkingSlots.add( new ParkingSlot(1L, "Sulaiman", unit1, new HasSet <VehicleParking>(), "z20", Availability.Available)); //ID 1
        parkingSlots.add( new ParkingSlot(2L, "Arun", unit1, new HasSet <VehicleParking>(), "z350", Availability.Available)); //ID 2
		parkingSlots.add( new ParkingSlot(3L, "Kasun", unit1, new HasSet <VehicleParking>(), "z52867", Availability.Available)); //ID 3

        given(repository.findAll()).willReturn(parkingSlots);
        given(repository.findById(1L)).willReturn(Optional.of(parkingSlots.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetParkingSlotsSuccess(){
        List<ParkingSlot> parkingSlots = parkingSlotService.getParkingSlots();
        Assert.assertNotNull(parkingSlots);
        Assert.assertTrue("Expect 3 parkingSlots in result", parkingSlots.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", parkingSlots == this.parkingSlots);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<ParkingSlot> parkingSlot = null;
        parkingSlot = parkingSlotService.getByID(111L);
        Assert.assertTrue("No match found", !parkingSlot.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<ParkingSlot> parkingSlot = null;
        try {
            parkingSlot = parkingSlotService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", parkingSlot == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<ParkingSlot> parkingSlot = parkingSlotService.getByID(1L);
        Assert.assertNotNull(parkingSlots);
        Assert.assertTrue("Expect a parkingSlots in result", parkingSlot.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", parkingSlots.get(0) == parkingSlot.get());
    }

    @Test
    public void verifySaveParkingSlotSuccess() throws Exception{
        ParkingSlot newParkingSlot =   new ParkingSlot(1L, "Sulaiman", unit1, new HasSet <VehicleParking>(), "z20", Availability.Available);
        ParkingSlot savedParkingSlot4 = new ParkingSlot();
        BeanUtils.copyProperties(newParkingSlot, savedParkingSlot4);
        savedParkingSlot4.setId(5L);
        given(repository.save(newParkingSlot)).willReturn(savedParkingSlot4);
        given(repository.saveAndFlush(newParkingSlot)).willReturn(savedParkingSlot4);

        ParkingSlot parkingSlot = parkingSlotService.saveParkingSlot(newParkingSlot);
        Assert.assertNotNull(parkingSlot);
        Assert.assertTrue("Expect valid ID in returned parkingSlot", parkingSlot.getId() != null && parkingSlot.getId() > 0);
    }

    @Test
    public void verifySaveParkingSlotNull() throws Exception{
        ParkingSlot parkingSlot = null;
        try {
            parkingSlot = parkingSlotService.saveParkingSlot(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned parkingSlot", !StringUtils.isEmpty(e.getMessage()) && parkingSlot == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveParkingSlotRepoIDNotReturned() throws Exception{
        ParkingSlot newParkingSlot =  new ParkingSlot(0L, "Kusal", unit1, new HasSet <VehicleParking>(), "t", Availability.Available);
        given(repository.save(newParkingSlot)).willReturn(newParkingSlot);
        given(repository.saveAndFlush(newParkingSlot)).willReturn(newParkingSlot);

        ParkingSlot parkingSlot = parkingSlotService.saveParkingSlot(newParkingSlot);
        Assert.assertNotNull(parkingSlot);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", parkingSlot.getId() == null || parkingSlot.getId() == 0);
    }

    @Test
    public void verifySaveParkingSlotRepoException() throws Exception{
        ParkingSlot newParkingSlot =  new ParkingSlot(0L, "Kusal", unit1, new HasSet <VehicleParking>(), "t", Availability.Available);
        given(repository.save(newParkingSlot)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newParkingSlot)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        ParkingSlot parkingSlot = null;
        try {
            parkingSlot = parkingSlotService.saveParkingSlot(newParkingSlot);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && parkingSlot == null);
        }

    }

    @Test
    public void verifyDeleteParkingSlotSuccess() throws Exception{
        ServiceStatus status = parkingSlotService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete parkingSlot operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteParkingSlotIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = parkingSlotService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByNameTest() throws Exception{
        ArrayList<ParkingSlot> matchedParkingSlots = new ArrayList<>(1);
        matchedParkingSlots.add(this.parkingSlots.get(0));
        given(repository.findAllByName(any())).willReturn(matchedParkingSlots);
        List <ParkingSlot> resultParkingSlots = parkingSlotService.findAllByName(users.get(0).getName());
        Assert.assertNotNull(resultParkingSlots);
        Assert.assertTrue(resultParkingSlots.size() > 0);
        Assert.assertTrue(resultParkingSlots.get(0).getId() == this.parkingSlots.get(0).getId());

    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<ParkingSlot> matchedParkingSlots = new ArrayList<>(1);
        matchedParkingSlots.add(this.parkingSlots.get(0));
        given(repository.findAllByName(any())).willReturn(matchedParkingSlots);
        List <ParkingSlot> resultParkingSlots = null;
        try {
            resultParkingSlots = parkingSlotService.findAllByName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultParkingSlots == null);
        }
    }
 	
	@Test
    public void findAllByUnit_IdTest() throws Exception{
        ArrayList<ParkingSlot> matchedParkingSlots = new ArrayList<>(1);
        matchedParkingSlots.add(this.parkingSlots.get(0));
        given(repository.findAllByUnit_Id(any())).willReturn(matchedParkingSlots);
        List <ParkingSlot> resultParkingSlots = parkingSlotService.findAllByUnit_Id(users.get(0).getUnit());
        Assert.assertNotNull(resultParkingSlots);
        Assert.assertTrue(resultParkingSlots.size() > 0);
        Assert.assertTrue(resultParkingSlots.get(0).getId() == this.parkingSlots.get(0).getId());

    }

    @Test
    public void findAllByUnit_IdInvalidParamTest() throws Exception{
        ArrayList<ParkingSlot> matchedParkingSlots = new ArrayList<>(1);
        matchedParkingSlots.add(this.parkingSlots.get(0));
        given(repository.findAllByUnit_Id(any())).willReturn(matchedParkingSlots);
        List <ParkingSlot> resultParkingSlots = null;
        try {
            resultParkingSlots = parkingSlotService.findAllByUnit_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultParkingSlots == null);
        }
    }
 	
	@Test
    public void findAllByVehicleNumberTest() throws Exception{
        ArrayList<ParkingSlot> matchedParkingSlots = new ArrayList<>(1);
        matchedParkingSlots.add(this.parkingSlots.get(0));
        given(repository.findAllByVehicleNumber(any())).willReturn(matchedParkingSlots);
        List <ParkingSlot> resultParkingSlots = parkingSlotService.findAllByVehicleNumber(users.get(0).getVehicleNumber());
        Assert.assertNotNull(resultParkingSlots);
        Assert.assertTrue(resultParkingSlots.size() > 0);
        Assert.assertTrue(resultParkingSlots.get(0).getId() == this.parkingSlots.get(0).getId());

    }

    @Test
    public void findAllByVehicleNumberInvalidParamTest() throws Exception{
        ArrayList<ParkingSlot> matchedParkingSlots = new ArrayList<>(1);
        matchedParkingSlots.add(this.parkingSlots.get(0));
        given(repository.findAllByVehicleNumber(any())).willReturn(matchedParkingSlots);
        List <ParkingSlot> resultParkingSlots = null;
        try {
            resultParkingSlots = parkingSlotService.findAllByVehicleNumber(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultParkingSlots == null);
        }
    }




}
