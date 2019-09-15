package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainVehicleService;
import lk.empire.ams.service.VehicleService;
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
 * <p>Title         : VehicleService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Vehicle. A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class VehicleServiceTest {

    @MockBean
    private VehicleRepository repository;


    @Autowired
    private VehicleService vehicleService;

    private List<Vehicle> vehicles = new ArrayList<>();

     	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
	

    @TestConfiguration
    static class VehicleServiceImplTestContextConfiguration {
        @Bean
        public VehicleService employeeService() {
            return new MainVehicleService();
        }
    }

    @Before
    public void setUp(){
        unit0 =   new Unit(0L, "Nayani", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(1L, "Kusal", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 

		vehicles.add( new Vehicle(1L, "w536664018854", unit1, new HasSet <VehicleParking>(), "orn on ", " was born on 8 December 65 BC[nb 4] in the ")); //ID 1
        vehicles.add( new Vehicle(2L, "y0416577126053258", unit1, new HasSet <VehicleParking>(), "He was born on 8 December", "as born on 8 Dece")); //ID 2
		vehicles.add( new Vehicle(3L, "h6087263620436", unit1, new HasSet <VehicleParking>(), "He was born on 8 December 65 ", "orn on 8 December 65 BC[nb 4] in the Samn")); //ID 3

        given(repository.findAll()).willReturn(vehicles);
        given(repository.findById(1L)).willReturn(Optional.of(vehicles.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetVehiclesSuccess(){
        List<Vehicle> vehicles = vehicleService.getVehicles();
        Assert.assertNotNull(vehicles);
        Assert.assertTrue("Expect 3 vehicles in result", vehicles.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", vehicles == this.vehicles);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Vehicle> vehicle = null;
        vehicle = vehicleService.getByID(111L);
        Assert.assertTrue("No match found", !vehicle.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Vehicle> vehicle = null;
        try {
            vehicle = vehicleService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", vehicle == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Vehicle> vehicle = vehicleService.getByID(1L);
        Assert.assertNotNull(vehicles);
        Assert.assertTrue("Expect a vehicles in result", vehicle.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", vehicles.get(0) == vehicle.get());
    }

    @Test
    public void verifySaveVehicleSuccess() throws Exception{
        Vehicle newVehicle =   new Vehicle(1L, "w536664018854", unit1, new HasSet <VehicleParking>(), "orn on ", " was born on 8 December 65 BC[nb 4] in the ");
        Vehicle savedVehicle4 = new Vehicle();
        BeanUtils.copyProperties(newVehicle, savedVehicle4);
        savedVehicle4.setId(5L);
        given(repository.save(newVehicle)).willReturn(savedVehicle4);
        given(repository.saveAndFlush(newVehicle)).willReturn(savedVehicle4);

        Vehicle vehicle = vehicleService.saveVehicle(newVehicle);
        Assert.assertNotNull(vehicle);
        Assert.assertTrue("Expect valid ID in returned vehicle", vehicle.getId() != null && vehicle.getId() > 0);
    }

    @Test
    public void verifySaveVehicleNull() throws Exception{
        Vehicle vehicle = null;
        try {
            vehicle = vehicleService.saveVehicle(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned vehicle", !StringUtils.isEmpty(e.getMessage()) && vehicle == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveVehicleRepoIDNotReturned() throws Exception{
        Vehicle newVehicle =  new Vehicle(0L, "z64660115806", unit1, new HasSet <VehicleParking>(), " was born", "He was born on 8 December 65 BC[nb 4] in the Samn");
        given(repository.save(newVehicle)).willReturn(newVehicle);
        given(repository.saveAndFlush(newVehicle)).willReturn(newVehicle);

        Vehicle vehicle = vehicleService.saveVehicle(newVehicle);
        Assert.assertNotNull(vehicle);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", vehicle.getId() == null || vehicle.getId() == 0);
    }

    @Test
    public void verifySaveVehicleRepoException() throws Exception{
        Vehicle newVehicle =  new Vehicle(0L, "z64660115806", unit1, new HasSet <VehicleParking>(), " was born", "He was born on 8 December 65 BC[nb 4] in the Samn");
        given(repository.save(newVehicle)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newVehicle)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Vehicle vehicle = null;
        try {
            vehicle = vehicleService.saveVehicle(newVehicle);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && vehicle == null);
        }

    }

    @Test
    public void verifyDeleteVehicleSuccess() throws Exception{
        ServiceStatus status = vehicleService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete vehicle operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteVehicleIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = vehicleService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByNumberTest() throws Exception{
        ArrayList<Vehicle> matchedVehicles = new ArrayList<>(1);
        matchedVehicles.add(this.vehicles.get(0));
        given(repository.findAllByNumber(any())).willReturn(matchedVehicles);
        List <Vehicle> resultVehicles = vehicleService.findAllByNumber(users.get(0).getNumber());
        Assert.assertNotNull(resultVehicles);
        Assert.assertTrue(resultVehicles.size() > 0);
        Assert.assertTrue(resultVehicles.get(0).getId() == this.vehicles.get(0).getId());

    }

    @Test
    public void findAllByNumberInvalidParamTest() throws Exception{
        ArrayList<Vehicle> matchedVehicles = new ArrayList<>(1);
        matchedVehicles.add(this.vehicles.get(0));
        given(repository.findAllByNumber(any())).willReturn(matchedVehicles);
        List <Vehicle> resultVehicles = null;
        try {
            resultVehicles = vehicleService.findAllByNumber(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicles == null);
        }
    }
 	
	@Test
    public void findAllByUnit_IdTest() throws Exception{
        ArrayList<Vehicle> matchedVehicles = new ArrayList<>(1);
        matchedVehicles.add(this.vehicles.get(0));
        given(repository.findAllByUnit_Id(any())).willReturn(matchedVehicles);
        List <Vehicle> resultVehicles = vehicleService.findAllByUnit_Id(users.get(0).getUnit());
        Assert.assertNotNull(resultVehicles);
        Assert.assertTrue(resultVehicles.size() > 0);
        Assert.assertTrue(resultVehicles.get(0).getId() == this.vehicles.get(0).getId());

    }

    @Test
    public void findAllByUnit_IdInvalidParamTest() throws Exception{
        ArrayList<Vehicle> matchedVehicles = new ArrayList<>(1);
        matchedVehicles.add(this.vehicles.get(0));
        given(repository.findAllByUnit_Id(any())).willReturn(matchedVehicles);
        List <Vehicle> resultVehicles = null;
        try {
            resultVehicles = vehicleService.findAllByUnit_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultVehicles == null);
        }
    }




}
