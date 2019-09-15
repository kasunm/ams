package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainInquiryService;
import lk.empire.ams.service.InquiryService;
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
 * <p>Title         : InquiryService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Inquiry. An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InquiryServiceTest {

    @MockBean
    private InquiryRepository repository;


    @Autowired
    private InquiryService inquiryService;

    private List<Inquiry> inquirys = new ArrayList<>();

     	private Client client0;
		private Client client1;
		private Client client2;
		private Employee employee0;
		private Employee employee1;
		private Employee employee2;
	

    @TestConfiguration
    static class InquiryServiceImplTestContextConfiguration {
        @Bean
        public InquiryService employeeService() {
            return new MainInquiryService();
        }
    }

    @Before
    public void setUp(){
        client0 =   new Client(0L, "Ruwan", "ome town, Venusia, lay on a trade route in the", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	client1 =   new Client(1L, "Nayani", "s born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic ", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	client2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	employee0 =   new Employee(0L, new ArrayList<AppEvent>());
	employee1 =   new Employee(1L, new ArrayList<AppEvent>());
	employee2 =   new Employee(2L, new ArrayList<AppEvent>());
	 

		inquirys.add( new Inquiry(1L, " have always been devoted to their home towns, even after success in the wider world, and Horace ", "Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various ", InquiryStatus.Pending, InquiryType.Suggestion, InquiryAction.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1)); //ID 1
        inquirys.add( new Inquiry(2L, " south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the", "n 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade rou", InquiryStatus.Pending, InquiryType.Suggestion, InquiryAction.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1)); //ID 2
		inquirys.add( new Inquiry(3L, "rs. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could d", "cember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a", InquiryStatus.Pending, InquiryType.Suggestion, InquiryAction.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1)); //ID 3

        given(repository.findAll()).willReturn(inquirys);
        given(repository.findById(1L)).willReturn(Optional.of(inquirys.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetInquirysSuccess(){
        List<Inquiry> inquirys = inquiryService.getInquirys();
        Assert.assertNotNull(inquirys);
        Assert.assertTrue("Expect 3 inquirys in result", inquirys.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", inquirys == this.inquirys);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Inquiry> inquiry = null;
        inquiry = inquiryService.getByID(111L);
        Assert.assertTrue("No match found", !inquiry.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Inquiry> inquiry = null;
        try {
            inquiry = inquiryService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", inquiry == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Inquiry> inquiry = inquiryService.getByID(1L);
        Assert.assertNotNull(inquirys);
        Assert.assertTrue("Expect a inquirys in result", inquiry.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", inquirys.get(0) == inquiry.get());
    }

    @Test
    public void verifySaveInquirySuccess() throws Exception{
        Inquiry newInquiry =   new Inquiry(1L, " have always been devoted to their home towns, even after success in the wider world, and Horace ", "Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various ", InquiryStatus.Pending, InquiryType.Suggestion, InquiryAction.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1);
        Inquiry savedInquiry4 = new Inquiry();
        BeanUtils.copyProperties(newInquiry, savedInquiry4);
        savedInquiry4.setId(5L);
        given(repository.save(newInquiry)).willReturn(savedInquiry4);
        given(repository.saveAndFlush(newInquiry)).willReturn(savedInquiry4);

        Inquiry inquiry = inquiryService.saveInquiry(newInquiry);
        Assert.assertNotNull(inquiry);
        Assert.assertTrue("Expect valid ID in returned inquiry", inquiry.getId() != null && inquiry.getId() > 0);
    }

    @Test
    public void verifySaveInquiryNull() throws Exception{
        Inquiry inquiry = null;
        try {
            inquiry = inquiryService.saveInquiry(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned inquiry", !StringUtils.isEmpty(e.getMessage()) && inquiry == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveInquiryRepoIDNotReturned() throws Exception{
        Inquiry newInquiry =  new Inquiry(0L, "chool was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' co", " town, Venusia, lay on a trade route in the border region between Apulia", InquiryStatus.Pending, InquiryType.Complain, InquiryAction.Done, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1);
        given(repository.save(newInquiry)).willReturn(newInquiry);
        given(repository.saveAndFlush(newInquiry)).willReturn(newInquiry);

        Inquiry inquiry = inquiryService.saveInquiry(newInquiry);
        Assert.assertNotNull(inquiry);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", inquiry.getId() == null || inquiry.getId() == 0);
    }

    @Test
    public void verifySaveInquiryRepoException() throws Exception{
        Inquiry newInquiry =  new Inquiry(0L, "chool was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' co", " town, Venusia, lay on a trade route in the border region between Apulia", InquiryStatus.Pending, InquiryType.Complain, InquiryAction.Done, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), client1, employee1);
        given(repository.save(newInquiry)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newInquiry)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Inquiry inquiry = null;
        try {
            inquiry = inquiryService.saveInquiry(newInquiry);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && inquiry == null);
        }

    }

    @Test
    public void verifyDeleteInquirySuccess() throws Exception{
        ServiceStatus status = inquiryService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete inquiry operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteInquiryIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = inquiryService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByDescriptionTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByDescription(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = inquiryService.findAllByDescription(users.get(0).getDescription());
        Assert.assertNotNull(resultInquirys);
        Assert.assertTrue(resultInquirys.size() > 0);
        Assert.assertTrue(resultInquirys.get(0).getId() == this.inquirys.get(0).getId());

    }

    @Test
    public void findAllByDescriptionInvalidParamTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByDescription(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = null;
        try {
            resultInquirys = inquiryService.findAllByDescription(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultInquirys == null);
        }
    }
 	
	@Test
    public void findAllByTypeTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByType(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = inquiryService.findAllByType(users.get(0).getType());
        Assert.assertNotNull(resultInquirys);
        Assert.assertTrue(resultInquirys.size() > 0);
        Assert.assertTrue(resultInquirys.get(0).getId() == this.inquirys.get(0).getId());

    }

    @Test
    public void findAllByTypeInvalidParamTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByType(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = null;
        try {
            resultInquirys = inquiryService.findAllByType(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultInquirys == null);
        }
    }
 	
	@Test
    public void findAllByActionTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByAction(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = inquiryService.findAllByAction(users.get(0).getAction());
        Assert.assertNotNull(resultInquirys);
        Assert.assertTrue(resultInquirys.size() > 0);
        Assert.assertTrue(resultInquirys.get(0).getId() == this.inquirys.get(0).getId());

    }

    @Test
    public void findAllByActionInvalidParamTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByAction(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = null;
        try {
            resultInquirys = inquiryService.findAllByAction(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultInquirys == null);
        }
    }
 	
	@Test
    public void findAllByClient_IdTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByClient_Id(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = inquiryService.findAllByClient_Id(users.get(0).getClient());
        Assert.assertNotNull(resultInquirys);
        Assert.assertTrue(resultInquirys.size() > 0);
        Assert.assertTrue(resultInquirys.get(0).getId() == this.inquirys.get(0).getId());

    }

    @Test
    public void findAllByClient_IdInvalidParamTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByClient_Id(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = null;
        try {
            resultInquirys = inquiryService.findAllByClient_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultInquirys == null);
        }
    }
 	
	@Test
    public void findAllByEmployee_IdTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByEmployee_Id(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = inquiryService.findAllByEmployee_Id(users.get(0).getEmployee());
        Assert.assertNotNull(resultInquirys);
        Assert.assertTrue(resultInquirys.size() > 0);
        Assert.assertTrue(resultInquirys.get(0).getId() == this.inquirys.get(0).getId());

    }

    @Test
    public void findAllByEmployee_IdInvalidParamTest() throws Exception{
        ArrayList<Inquiry> matchedInquirys = new ArrayList<>(1);
        matchedInquirys.add(this.inquirys.get(0));
        given(repository.findAllByEmployee_Id(any())).willReturn(matchedInquirys);
        List <Inquiry> resultInquirys = null;
        try {
            resultInquirys = inquiryService.findAllByEmployee_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultInquirys == null);
        }
    }




}
