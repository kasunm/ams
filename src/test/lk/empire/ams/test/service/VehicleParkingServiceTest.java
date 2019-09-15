package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainVehicleParkingService;
import lk.empire.ams.service.VehicleParkingService;
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
 * <p>Title         : VehicleParkingService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class VehicleParkingServiceTest {

    @MockBean
    private VehicleParkingRepository repository;


    @Autowired
    private VehicleParkingService vehicleParkingService;

    private List<VehicleParking> vehicleParkings = new ArrayList<>();

     	private Vehicle vehicle0;
		private Vehicle vehicle1;
		private Vehicle vehicle2;
		private ParkingSlot parkingSlot0;
		private ParkingSlot parkingSlot1;
		private ParkingSlot parkingSlot2;
	

    @TestConfiguration
    static class VehicleParkingServiceImplTestContextConfiguration {
        @Bean
        public VehicleParkingService employeeService() {
            return new MainVehicleParkingService();
        }
    }

    @Before
    public void setUp(){
        vehicle0 =   new Vehicle(0L, "d2385736645232", unit1, new HasSet <VehicleParking>(), " ", "orn on 8 December 65 BC[nb 4] ");
	vehicle1 =   new Vehicle(1L, "i125076", unit1, new HasSet <VehicleParking>(), "8 December 65", " was born on 8 December 65 BC[nb 4] in the ");
	vehicle2 =   new Vehicle(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born on 8 December 65 BC[", "He was born on 8 December 65 BC[nb 4] in the Samnite");
	parkingSlot0 =   new ParkingSlot(0L, "Omega", unit1, new HasSet <VehicleParking>(), "x52378", Availability.Available);
	parkingSlot1 =   new ParkingSlot(1L, "Aloka", unit1, new HasSet <VehicleParking>(), "f30", Availability.Available);
	parkingSlot2 =   new ParkingSlot(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born ", Availability.Available);
	 

		vehicleParkings.add( new VehicleParking(1L, "Sulaiman", "was born on 8 Dece", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "for language. He could have been familiar with Greek words even as a young boy and later he poked fun at ")); //ID 1
        vehicleParkings.add( new VehicleParking(2L, "Nayani", "H", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "th of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he")); //ID 2
		vehicleParkings.add( new VehicleParking(3L, "Tenuki", " on 8 Decembe", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6")); //ID 3

        given(repository.findAll()).willReturn(vehicleParkings);
        given(repository.findById(1L)).willReturn(Optional.of(vehicleParkings.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetVehicleParkingsSuccess(){
        List<VehicleParking> vehicleParkings = vehicleParkingService.getVehicleParkings();
        Assert.assertNotNull(vehicleParkings);
        Assert.assertTrue("Expect 3 vehicleParkings in result", vehicleParkings.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", vehicleParkings == this.vehicleParkings);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<VehicleParking> vehicleParking = null;
        vehicleParking = vehicleParkingService.getByID(111L);
        Assert.assertTrue("No match found", !vehicleParking.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<VehicleParking> vehicleParking = null;
        try {
            vehicleParking = vehicleParkingService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", vehicleParking == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<VehicleParking> vehicleParking = vehicleParkingService.getByID(1L);
        Assert.assertNotNull(vehicleParkings);
        Assert.assertTrue("Expect a vehicleParkings in result", vehicleParking.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", vehicleParkings.get(0) == vehicleParking.get());
    }

    @Test
    public void verifySaveVehicleParkingSuccess() throws Exception{
        VehicleParking newVehicleParking =   new VehicleParking(1L, "Sulaiman", "was born on 8 Dece", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "for language. He could have been familiar with Greek words even as a young boy and later he poked fun at ");
        VehicleParking savedVehicleParking4 = new VehicleParking();
        BeanUtils.copyProperties(newVehicleParking, savedVehicleParking4);
        savedVehicleParking4.setId(5L);
        given(repository.save(newVehicleParking)).willReturn(savedVehicleParking4);
        given(repository.saveAndFlush(newVehicleParking)).willReturn(savedVehicleParking4);

        VehicleParking vehicleParking = vehicleParkingService.saveVehicleParking(newVehicleParking);
        Assert.assertNotNull(vehicleParking);
        Assert.assertTrue("Expect valid ID in returned vehicleParking", vehicleParking.getId() != null && vehicleParking.getId() > 0);
    }

    @Test
    public void verifySaveVehicleParkingNull() throws Exception{
        VehicleParking vehicleParking = null;
        try {
            vehicleParking = vehicleParkingService.saveVehicleParking(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned vehicleParking", !StringUtils.isEmpty(e.getMessage()) && vehicleParking == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveVehicleParkingRepoIDNotReturned() throws Exception{
        VehicleParking newVehicleParking =  new VehicleParking(0L, "Sumudu", "e was born on 8 Dece", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, " was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the");
        given(repository.save(newVehicleParking)).willReturn(newVehicleParking);
        given(repository.saveAndFlush(newVehicleParking)).willReturn(newVehicleParking);

        VehicleParking vehicleParking = vehicleParkingService.saveVehicleParking(newVehicleParking);
        Assert.assertNotNull(vehicleParking);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", vehicleParking.getId() == null || vehicleParking.getId() == 0);
    }

    @Test
    public void verifySaveVehicleParkingRepoException() throws Exception{
        VehicleParking newVehicleParking =  new VehicleParking(0L, "Sumudu", "e was born on 8 Dece", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, " was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the");
        given(repository.save(newVehicleParking)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newVehicleParking)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        VehicleParking vehicleParking = null;
        try {
            vehicleParking = vehicleParkingService.saveVehicleParking(newVehicleParking);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && vehicleParking == null);
        }

    }

    @Test
    public void verifyDeleteVehicleParkingSuccess() throws Exception{
        ServiceStatus status = vehicleParkingService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete vehicleParking operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteVehicleParkingIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = vehicleParkingService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByParkingSlot_IdTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByParkingSlot_Id(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = vehicleParkingService.findAllByParkingSlot_Id(users.get(0).getParkingSlot());
        Assert.assertNotNull(resultVehicleParkings);
        Assert.assertTrue(resultVehicleParkings.size() > 0);
        Assert.assertTrue(resultVehicleParkings.get(0).getId() == this.vehicleParkings.get(0).getId());

    }

    @Test
    public void findAllByParkingSlot_IdInvalidParamTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByParkingSlot_Id(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = null;
        try {
            resultVehicleParkings = vehicleParkingService.findAllByParkingSlot_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicleParkings == null);
        }
    }
 	
	@Test
    public void findAllByDriverIDTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByDriverID(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = vehicleParkingService.findAllByDriverID(users.get(0).getDriverID());
        Assert.assertNotNull(resultVehicleParkings);
        Assert.assertTrue(resultVehicleParkings.size() > 0);
        Assert.assertTrue(resultVehicleParkings.get(0).getId() == this.vehicleParkings.get(0).getId());

    }

    @Test
    public void findAllByDriverIDInvalidParamTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByDriverID(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = null;
        try {
            resultVehicleParkings = vehicleParkingService.findAllByDriverID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicleParkings == null);
        }
    }
 	
	@Test
    public void findAllByInDateTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByInDate(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = vehicleParkingService.findAllByInDate(users.get(0).getInDate());
        Assert.assertNotNull(resultVehicleParkings);
        Assert.assertTrue(resultVehicleParkings.size() > 0);
        Assert.assertTrue(resultVehicleParkings.get(0).getId() == this.vehicleParkings.get(0).getId());

    }

    @Test
    public void findAllByInDateInvalidParamTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByInDate(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = null;
        try {
            resultVehicleParkings = vehicleParkingService.findAllByInDate(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicleParkings == null);
        }
    }
 	
	@Test
    public void findAllByOutDateTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByOutDate(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = vehicleParkingService.findAllByOutDate(users.get(0).getOutDate());
        Assert.assertNotNull(resultVehicleParkings);
        Assert.assertTrue(resultVehicleParkings.size() > 0);
        Assert.assertTrue(resultVehicleParkings.get(0).getId() == this.vehicleParkings.get(0).getId());

    }

    @Test
    public void findAllByOutDateInvalidParamTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByOutDate(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = null;
        try {
            resultVehicleParkings = vehicleParkingService.findAllByOutDate(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicleParkings == null);
        }
    }
 	
	@Test
    public void findAllByVehicle_IdTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByVehicle_Id(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = vehicleParkingService.findAllByVehicle_Id(users.get(0).getVehicle());
        Assert.assertNotNull(resultVehicleParkings);
        Assert.assertTrue(resultVehicleParkings.size() > 0);
        Assert.assertTrue(resultVehicleParkings.get(0).getId() == this.vehicleParkings.get(0).getId());

    }

    @Test
    public void findAllByVehicle_IdInvalidParamTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByVehicle_Id(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = null;
        try {
            resultVehicleParkings = vehicleParkingService.findAllByVehicle_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicleParkings == null);
        }
    }
 	
	@Test
    public void findAllByVehicle_numberTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByVehicle_number(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = vehicleParkingService.findAllByVehicle_number(users.get(0).getVehicle());
        Assert.assertNotNull(resultVehicleParkings);
        Assert.assertTrue(resultVehicleParkings.size() > 0);
        Assert.assertTrue(resultVehicleParkings.get(0).getId() == this.vehicleParkings.get(0).getId());

    }

    @Test
    public void findAllByVehicle_numberInvalidParamTest() throws Exception{
        ArrayList<VehicleParking> matchedVehicleParkings = new ArrayList<>(1);
        matchedVehicleParkings.add(this.vehicleParkings.get(0));
        given(repository.findAllByVehicle_number(any())).willReturn(matchedVehicleParkings);
        List <VehicleParking> resultVehicleParkings = null;
        try {
            resultVehicleParkings = vehicleParkingService.findAllByVehicle_number(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicleParkings == null);
        }
    }




}
