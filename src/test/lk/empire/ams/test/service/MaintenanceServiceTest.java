package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainMaintenanceService;
import lk.empire.ams.service.MaintenanceService;
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
 * <p>Title         : MaintenanceService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MaintenanceServiceTest {

    @MockBean
    private MaintenanceRepository repository;


    @Autowired
    private MaintenanceService maintenanceService;

    private List<Maintenance> maintenances = new ArrayList<>();

     	private Contractor contractor0;
		private Contractor contractor1;
		private Contractor contractor2;
		private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @TestConfiguration
    static class MaintenanceServiceImplTestContextConfiguration {
        @Bean
        public MaintenanceService employeeService() {
            return new MainMaintenanceService();
        }
    }

    @Before
    public void setUp(){
        contractor0 =   new Contractor(0L, "Sulaiman", new ArrayList<Maintenance>());
	contractor1 =   new Contractor(1L, "Ruwan", new ArrayList<Maintenance>());
	contractor2 =   new Contractor(2L, "He was born on 8 December 65 BC[", new ArrayList<Maintenance>());
	floor0 =   new Floor(0L, " ", "Sulaiman", 337460, apartment1);
	floor1 =   new Floor(1L, "orn on 8 De", "Nayani", 10512, apartment1);
	floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 789122, apartment2);
	 

		maintenances.add( new Maintenance(1L, "en familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition r", "Nayani", " ", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1)); //ID 1
        maintenances.add( new Maintenance(2L, "ave been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself ", "Nayani", "He was born on 8 December 65 BC[nb 4] in the S", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1)); //ID 2
		maintenances.add( new Maintenance(3L, "boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his ", "Nayani", "er 65 BC[nb 4] i", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1)); //ID 3

        given(repository.findAll()).willReturn(maintenances);
        given(repository.findById(1L)).willReturn(Optional.of(maintenances.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetMaintenancesSuccess(){
        List<Maintenance> maintenances = maintenanceService.getMaintenances();
        Assert.assertNotNull(maintenances);
        Assert.assertTrue("Expect 3 maintenances in result", maintenances.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", maintenances == this.maintenances);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Maintenance> maintenance = null;
        maintenance = maintenanceService.getByID(111L);
        Assert.assertTrue("No match found", !maintenance.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Maintenance> maintenance = null;
        try {
            maintenance = maintenanceService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", maintenance == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Maintenance> maintenance = maintenanceService.getByID(1L);
        Assert.assertNotNull(maintenances);
        Assert.assertTrue("Expect a maintenances in result", maintenance.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", maintenances.get(0) == maintenance.get());
    }

    @Test
    public void verifySaveMaintenanceSuccess() throws Exception{
        Maintenance newMaintenance =   new Maintenance(1L, "en familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition r", "Nayani", " ", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1);
        Maintenance savedMaintenance4 = new Maintenance();
        BeanUtils.copyProperties(newMaintenance, savedMaintenance4);
        savedMaintenance4.setId(5L);
        given(repository.save(newMaintenance)).willReturn(savedMaintenance4);
        given(repository.saveAndFlush(newMaintenance)).willReturn(savedMaintenance4);

        Maintenance maintenance = maintenanceService.saveMaintenance(newMaintenance);
        Assert.assertNotNull(maintenance);
        Assert.assertTrue("Expect valid ID in returned maintenance", maintenance.getId() != null && maintenance.getId() > 0);
    }

    @Test
    public void verifySaveMaintenanceNull() throws Exception{
        Maintenance maintenance = null;
        try {
            maintenance = maintenanceService.saveMaintenance(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned maintenance", !StringUtils.isEmpty(e.getMessage()) && maintenance == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveMaintenanceRepoIDNotReturned() throws Exception{
        Maintenance newMaintenance =  new Maintenance(0L, "ponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success", "Ruwan", "8 December 65 BC[nb 4] in the S", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1);
        given(repository.save(newMaintenance)).willReturn(newMaintenance);
        given(repository.saveAndFlush(newMaintenance)).willReturn(newMaintenance);

        Maintenance maintenance = maintenanceService.saveMaintenance(newMaintenance);
        Assert.assertNotNull(maintenance);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", maintenance.getId() == null || maintenance.getId() == 0);
    }

    @Test
    public void verifySaveMaintenanceRepoException() throws Exception{
        Maintenance newMaintenance =  new Maintenance(0L, "ponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success", "Ruwan", "8 December 65 BC[nb 4] in the S", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1);
        given(repository.save(newMaintenance)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newMaintenance)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Maintenance maintenance = null;
        try {
            maintenance = maintenanceService.saveMaintenance(newMaintenance);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && maintenance == null);
        }

    }

    @Test
    public void verifyDeleteMaintenanceSuccess() throws Exception{
        ServiceStatus status = maintenanceService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete maintenance operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteMaintenanceIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = maintenanceService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByDescriptionTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByDescription(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = maintenanceService.findAllByDescription(users.get(0).getDescription());
        Assert.assertNotNull(resultMaintenances);
        Assert.assertTrue(resultMaintenances.size() > 0);
        Assert.assertTrue(resultMaintenances.get(0).getId() == this.maintenances.get(0).getId());

    }

    @Test
    public void findAllByDescriptionInvalidParamTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByDescription(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = null;
        try {
            resultMaintenances = maintenanceService.findAllByDescription(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultMaintenances == null);
        }
    }
 	
	@Test
    public void findAllByBlockNameTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByBlockName(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = maintenanceService.findAllByBlockName(users.get(0).getBlockName());
        Assert.assertNotNull(resultMaintenances);
        Assert.assertTrue(resultMaintenances.size() > 0);
        Assert.assertTrue(resultMaintenances.get(0).getId() == this.maintenances.get(0).getId());

    }

    @Test
    public void findAllByBlockNameInvalidParamTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByBlockName(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = null;
        try {
            resultMaintenances = maintenanceService.findAllByBlockName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultMaintenances == null);
        }
    }
 	
	@Test
    public void findAllByDoneByTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByDoneBy(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = maintenanceService.findAllByDoneBy(users.get(0).getDoneBy());
        Assert.assertNotNull(resultMaintenances);
        Assert.assertTrue(resultMaintenances.size() > 0);
        Assert.assertTrue(resultMaintenances.get(0).getId() == this.maintenances.get(0).getId());

    }

    @Test
    public void findAllByDoneByInvalidParamTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByDoneBy(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = null;
        try {
            resultMaintenances = maintenanceService.findAllByDoneBy(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultMaintenances == null);
        }
    }
 	
	@Test
    public void findAllByMaintenanceTypeTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByMaintenanceType(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = maintenanceService.findAllByMaintenanceType(users.get(0).getMaintenanceType());
        Assert.assertNotNull(resultMaintenances);
        Assert.assertTrue(resultMaintenances.size() > 0);
        Assert.assertTrue(resultMaintenances.get(0).getId() == this.maintenances.get(0).getId());

    }

    @Test
    public void findAllByMaintenanceTypeInvalidParamTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByMaintenanceType(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = null;
        try {
            resultMaintenances = maintenanceService.findAllByMaintenanceType(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultMaintenances == null);
        }
    }
 	
	@Test
    public void findAllByStatusTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByStatus(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = maintenanceService.findAllByStatus(users.get(0).getStatus());
        Assert.assertNotNull(resultMaintenances);
        Assert.assertTrue(resultMaintenances.size() > 0);
        Assert.assertTrue(resultMaintenances.get(0).getId() == this.maintenances.get(0).getId());

    }

    @Test
    public void findAllByStatusInvalidParamTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByStatus(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = null;
        try {
            resultMaintenances = maintenanceService.findAllByStatus(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultMaintenances == null);
        }
    }
 	
	@Test
    public void findAllByContractor_IdTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByContractor_Id(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = maintenanceService.findAllByContractor_Id(users.get(0).getContractor());
        Assert.assertNotNull(resultMaintenances);
        Assert.assertTrue(resultMaintenances.size() > 0);
        Assert.assertTrue(resultMaintenances.get(0).getId() == this.maintenances.get(0).getId());

    }

    @Test
    public void findAllByContractor_IdInvalidParamTest() throws Exception{
        ArrayList<Maintenance> matchedMaintenances = new ArrayList<>(1);
        matchedMaintenances.add(this.maintenances.get(0));
        given(repository.findAllByContractor_Id(any())).willReturn(matchedMaintenances);
        List <Maintenance> resultMaintenances = null;
        try {
            resultMaintenances = maintenanceService.findAllByContractor_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultMaintenances == null);
        }
    }




}
