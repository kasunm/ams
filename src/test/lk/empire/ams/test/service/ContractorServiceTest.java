package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainContractorService;
import lk.empire.ams.service.ContractorService;
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
 * <p>Title         : ContractorService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ContractorServiceTest {

    @MockBean
    private ContractorRepository repository;


    @Autowired
    private ContractorService contractorService;

    private List<Contractor> contractors = new ArrayList<>();

     

    @TestConfiguration
    static class ContractorServiceImplTestContextConfiguration {
        @Bean
        public ContractorService employeeService() {
            return new MainContractorService();
        }
    }

    @Before
    public void setUp(){
         

		contractors.add( new Contractor(1L, "Arun", new ArrayList<Maintenance>())); //ID 1
        contractors.add( new Contractor(2L, "Aloka", new ArrayList<Maintenance>())); //ID 2
		contractors.add( new Contractor(3L, "Kasun", new ArrayList<Maintenance>())); //ID 3

        given(repository.findAll()).willReturn(contractors);
        given(repository.findById(1L)).willReturn(Optional.of(contractors.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetContractorsSuccess(){
        List<Contractor> contractors = contractorService.getContractors();
        Assert.assertNotNull(contractors);
        Assert.assertTrue("Expect 3 contractors in result", contractors.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", contractors == this.contractors);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Contractor> contractor = null;
        contractor = contractorService.getByID(111L);
        Assert.assertTrue("No match found", !contractor.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Contractor> contractor = null;
        try {
            contractor = contractorService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", contractor == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Contractor> contractor = contractorService.getByID(1L);
        Assert.assertNotNull(contractors);
        Assert.assertTrue("Expect a contractors in result", contractor.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", contractors.get(0) == contractor.get());
    }

    @Test
    public void verifySaveContractorSuccess() throws Exception{
        Contractor newContractor =   new Contractor(1L, "Arun", new ArrayList<Maintenance>());
        Contractor savedContractor4 = new Contractor();
        BeanUtils.copyProperties(newContractor, savedContractor4);
        savedContractor4.setId(5L);
        given(repository.save(newContractor)).willReturn(savedContractor4);
        given(repository.saveAndFlush(newContractor)).willReturn(savedContractor4);

        Contractor contractor = contractorService.saveContractor(newContractor);
        Assert.assertNotNull(contractor);
        Assert.assertTrue("Expect valid ID in returned contractor", contractor.getId() != null && contractor.getId() > 0);
    }

    @Test
    public void verifySaveContractorNull() throws Exception{
        Contractor contractor = null;
        try {
            contractor = contractorService.saveContractor(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned contractor", !StringUtils.isEmpty(e.getMessage()) && contractor == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveContractorRepoIDNotReturned() throws Exception{
        Contractor newContractor =  new Contractor(0L, "Tenuki", new ArrayList<Maintenance>());
        given(repository.save(newContractor)).willReturn(newContractor);
        given(repository.saveAndFlush(newContractor)).willReturn(newContractor);

        Contractor contractor = contractorService.saveContractor(newContractor);
        Assert.assertNotNull(contractor);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", contractor.getId() == null || contractor.getId() == 0);
    }

    @Test
    public void verifySaveContractorRepoException() throws Exception{
        Contractor newContractor =  new Contractor(0L, "Tenuki", new ArrayList<Maintenance>());
        given(repository.save(newContractor)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newContractor)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Contractor contractor = null;
        try {
            contractor = contractorService.saveContractor(newContractor);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && contractor == null);
        }

    }

    @Test
    public void verifyDeleteContractorSuccess() throws Exception{
        ServiceStatus status = contractorService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete contractor operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteContractorIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = contractorService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByCompanyNameTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByCompanyName(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = contractorService.findAllByCompanyName(users.get(0).getCompanyName());
        Assert.assertNotNull(resultContractors);
        Assert.assertTrue(resultContractors.size() > 0);
        Assert.assertTrue(resultContractors.get(0).getId() == this.contractors.get(0).getId());

    }

    @Test
    public void findAllByCompanyNameInvalidParamTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByCompanyName(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = null;
        try {
            resultContractors = contractorService.findAllByCompanyName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultContractors == null);
        }
    }
 	
	@Test
    public void findAllByFirstNameTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = contractorService.findAllByFirstName(users.get(0).getFirstName());
        Assert.assertNotNull(resultContractors);
        Assert.assertTrue(resultContractors.size() > 0);
        Assert.assertTrue(resultContractors.get(0).getId() == this.contractors.get(0).getId());

    }

    @Test
    public void findAllByFirstNameInvalidParamTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByFirstName(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = null;
        try {
            resultContractors = contractorService.findAllByFirstName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultContractors == null);
        }
    }
 	
	@Test
    public void findAllByEmailTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = contractorService.findAllByEmail(users.get(0).getEmail());
        Assert.assertNotNull(resultContractors);
        Assert.assertTrue(resultContractors.size() > 0);
        Assert.assertTrue(resultContractors.get(0).getId() == this.contractors.get(0).getId());

    }

    @Test
    public void findAllByEmailInvalidParamTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByEmail(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = null;
        try {
            resultContractors = contractorService.findAllByEmail(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultContractors == null);
        }
    }
 	
	@Test
    public void findAllByNicTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByNic(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = contractorService.findAllByNic(users.get(0).getNic());
        Assert.assertNotNull(resultContractors);
        Assert.assertTrue(resultContractors.size() > 0);
        Assert.assertTrue(resultContractors.get(0).getId() == this.contractors.get(0).getId());

    }

    @Test
    public void findAllByNicInvalidParamTest() throws Exception{
        ArrayList<Contractor> matchedContractors = new ArrayList<>(1);
        matchedContractors.add(this.contractors.get(0));
        given(repository.findAllByNic(any())).willReturn(matchedContractors);
        List <Contractor> resultContractors = null;
        try {
            resultContractors = contractorService.findAllByNic(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultContractors == null);
        }
    }




}
