package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainFloorService;
import lk.empire.ams.service.FloorService;
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
 * <p>Title         : FloorService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FloorServiceTest {

    @MockBean
    private FloorRepository repository;


    @Autowired
    private FloorService floorService;

    private List<Floor> floors = new ArrayList<>();

     	private Apartment apartment0;
		private Apartment apartment1;
		private Apartment apartment2;
	

    @TestConfiguration
    static class FloorServiceImplTestContextConfiguration {
        @Bean
        public FloorService employeeService() {
            return new MainFloorService();
        }
    }

    @Before
    public void setUp(){
        apartment0 =   new Apartment(0L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "rea and thi");
	apartment1 =   new Apartment(1L, "Kusal", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "th of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basili");
	apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	 

		floors.add( new Floor(1L, "e was born on", "Nayani", 311753, apartment1)); //ID 1
        floors.add( new Floor(2L, "was ", "Kusal", 441868, apartment1)); //ID 2
		floors.add( new Floor(3L, "s born ", "Nayani", 67828, apartment1)); //ID 3

        given(repository.findAll()).willReturn(floors);
        given(repository.findById(1L)).willReturn(Optional.of(floors.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetFloorsSuccess(){
        List<Floor> floors = floorService.getFloors();
        Assert.assertNotNull(floors);
        Assert.assertTrue("Expect 3 floors in result", floors.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", floors == this.floors);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Floor> floor = null;
        floor = floorService.getByID(111L);
        Assert.assertTrue("No match found", !floor.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Floor> floor = null;
        try {
            floor = floorService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", floor == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Floor> floor = floorService.getByID(1L);
        Assert.assertNotNull(floors);
        Assert.assertTrue("Expect a floors in result", floor.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", floors.get(0) == floor.get());
    }

    @Test
    public void verifySaveFloorSuccess() throws Exception{
        Floor newFloor =   new Floor(1L, "e was born on", "Nayani", 311753, apartment1);
        Floor savedFloor4 = new Floor();
        BeanUtils.copyProperties(newFloor, savedFloor4);
        savedFloor4.setId(5L);
        given(repository.save(newFloor)).willReturn(savedFloor4);
        given(repository.saveAndFlush(newFloor)).willReturn(savedFloor4);

        Floor floor = floorService.saveFloor(newFloor);
        Assert.assertNotNull(floor);
        Assert.assertTrue("Expect valid ID in returned floor", floor.getId() != null && floor.getId() > 0);
    }

    @Test
    public void verifySaveFloorNull() throws Exception{
        Floor floor = null;
        try {
            floor = floorService.saveFloor(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned floor", !StringUtils.isEmpty(e.getMessage()) && floor == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveFloorRepoIDNotReturned() throws Exception{
        Floor newFloor =  new Floor(0L, "was born on 8 ", "Arun", 527702, apartment1);
        given(repository.save(newFloor)).willReturn(newFloor);
        given(repository.saveAndFlush(newFloor)).willReturn(newFloor);

        Floor floor = floorService.saveFloor(newFloor);
        Assert.assertNotNull(floor);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", floor.getId() == null || floor.getId() == 0);
    }

    @Test
    public void verifySaveFloorRepoException() throws Exception{
        Floor newFloor =  new Floor(0L, "was born on 8 ", "Arun", 527702, apartment1);
        given(repository.save(newFloor)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newFloor)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Floor floor = null;
        try {
            floor = floorService.saveFloor(newFloor);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && floor == null);
        }

    }

    @Test
    public void verifyDeleteFloorSuccess() throws Exception{
        ServiceStatus status = floorService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete floor operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteFloorIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = floorService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByNameTest() throws Exception{
        ArrayList<Floor> matchedFloors = new ArrayList<>(1);
        matchedFloors.add(this.floors.get(0));
        given(repository.findAllByName(any())).willReturn(matchedFloors);
        List <Floor> resultFloors = floorService.findAllByName(users.get(0).getName());
        Assert.assertNotNull(resultFloors);
        Assert.assertTrue(resultFloors.size() > 0);
        Assert.assertTrue(resultFloors.get(0).getId() == this.floors.get(0).getId());

    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<Floor> matchedFloors = new ArrayList<>(1);
        matchedFloors.add(this.floors.get(0));
        given(repository.findAllByName(any())).willReturn(matchedFloors);
        List <Floor> resultFloors = null;
        try {
            resultFloors = floorService.findAllByName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultFloors == null);
        }
    }
 	
	@Test
    public void findAllByFloorNumberTest() throws Exception{
        ArrayList<Floor> matchedFloors = new ArrayList<>(1);
        matchedFloors.add(this.floors.get(0));
        given(repository.findAllByFloorNumber(any())).willReturn(matchedFloors);
        List <Floor> resultFloors = floorService.findAllByFloorNumber(users.get(0).getFloorNumber());
        Assert.assertNotNull(resultFloors);
        Assert.assertTrue(resultFloors.size() > 0);
        Assert.assertTrue(resultFloors.get(0).getId() == this.floors.get(0).getId());

    }

    @Test
    public void findAllByFloorNumberInvalidParamTest() throws Exception{
        ArrayList<Floor> matchedFloors = new ArrayList<>(1);
        matchedFloors.add(this.floors.get(0));
        given(repository.findAllByFloorNumber(any())).willReturn(matchedFloors);
        List <Floor> resultFloors = null;
        try {
            resultFloors = floorService.findAllByFloorNumber(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultFloors == null);
        }
    }
 	
	@Test
    public void findAllByApartment_IdTest() throws Exception{
        ArrayList<Floor> matchedFloors = new ArrayList<>(1);
        matchedFloors.add(this.floors.get(0));
        given(repository.findAllByApartment_Id(any())).willReturn(matchedFloors);
        List <Floor> resultFloors = floorService.findAllByApartment_Id(users.get(0).getApartment());
        Assert.assertNotNull(resultFloors);
        Assert.assertTrue(resultFloors.size() > 0);
        Assert.assertTrue(resultFloors.get(0).getId() == this.floors.get(0).getId());

    }

    @Test
    public void findAllByApartment_IdInvalidParamTest() throws Exception{
        ArrayList<Floor> matchedFloors = new ArrayList<>(1);
        matchedFloors.add(this.floors.get(0));
        given(repository.findAllByApartment_Id(any())).willReturn(matchedFloors);
        List <Floor> resultFloors = null;
        try {
            resultFloors = floorService.findAllByApartment_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultFloors == null);
        }
    }




}
