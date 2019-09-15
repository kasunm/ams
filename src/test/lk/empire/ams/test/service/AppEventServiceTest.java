package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainAppEventService;
import lk.empire.ams.service.AppEventService;
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
 * <p>Title         : AppEventService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AppEventServiceTest {

    @MockBean
    private AppEventRepository repository;


    @Autowired
    private AppEventService appEventService;

    private List<AppEvent> appEvents = new ArrayList<>();

     	private Apartment apartment0;
		private Apartment apartment1;
		private Apartment apartment2;
		private Client user0;
		private Client user1;
		private Client user2;
		private Employee employee0;
		private Employee employee1;
		private Employee employee2;
	

    @TestConfiguration
    static class AppEventServiceImplTestContextConfiguration {
        @Bean
        public AppEventService employeeService() {
            return new MainAppEventService();
        }
    }

    @Before
    public void setUp(){
        apartment0 =   new Apartment(0L, "Kusal", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "in the Samnite south ");
	apartment1 =   new Apartment(1L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps e");
	apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	user0 =   new Client(0L, "Sulaiman", "in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Itali", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	user1 =   new Client(1L, "Ruwan", "te south of Italy.[5] His home town, Venu", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	user2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	employee0 =   new Employee(0L, new ArrayList<AppEvent>());
	employee1 =   new Employee(1L, new ArrayList<AppEvent>());
	employee2 =   new Employee(2L, new ArrayList<AppEvent>());
	 

		appEvents.add( new AppEvent(1L, "Arun", 471621, "n the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a referen", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), " south of Italy.[5] His home tow", "trade route in the border region between Ap", apartment1, user1, employee1)); //ID 1
        appEvents.add( new AppEvent(2L, "Ruwan", 660535, "he third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his free", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, l", "home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Vari", apartment1, user1, employee1)); //ID 2
		appEvents.add( new AppEvent(3L, "Sulaiman", 245217, "ial War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social", EventStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "f Italy.[5] His home", "o", apartment1, user1, employee1)); //ID 3

        given(repository.findAll()).willReturn(appEvents);
        given(repository.findById(1L)).willReturn(Optional.of(appEvents.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetAppEventsSuccess(){
        List<AppEvent> appEvents = appEventService.getAppEvents();
        Assert.assertNotNull(appEvents);
        Assert.assertTrue("Expect 3 appEvents in result", appEvents.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", appEvents == this.appEvents);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<AppEvent> appEvent = null;
        appEvent = appEventService.getByID(111L);
        Assert.assertTrue("No match found", !appEvent.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<AppEvent> appEvent = null;
        try {
            appEvent = appEventService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", appEvent == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<AppEvent> appEvent = appEventService.getByID(1L);
        Assert.assertNotNull(appEvents);
        Assert.assertTrue("Expect a appEvents in result", appEvent.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", appEvents.get(0) == appEvent.get());
    }

    @Test
    public void verifySaveAppEventSuccess() throws Exception{
        AppEvent newAppEvent =   new AppEvent(1L, "Arun", 471621, "n the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a referen", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), " south of Italy.[5] His home tow", "trade route in the border region between Ap", apartment1, user1, employee1);
        AppEvent savedAppEvent4 = new AppEvent();
        BeanUtils.copyProperties(newAppEvent, savedAppEvent4);
        savedAppEvent4.setId(5L);
        given(repository.save(newAppEvent)).willReturn(savedAppEvent4);
        given(repository.saveAndFlush(newAppEvent)).willReturn(savedAppEvent4);

        AppEvent appEvent = appEventService.saveAppEvent(newAppEvent);
        Assert.assertNotNull(appEvent);
        Assert.assertTrue("Expect valid ID in returned appEvent", appEvent.getId() != null && appEvent.getId() > 0);
    }

    @Test
    public void verifySaveAppEventNull() throws Exception{
        AppEvent appEvent = null;
        try {
            appEvent = appEventService.saveAppEvent(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned appEvent", !StringUtils.isEmpty(e.getMessage()) && appEvent == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveAppEventRepoIDNotReturned() throws Exception{
        AppEvent newAppEvent =  new AppEvent(0L, "Sumudu", 694772, "nusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his chil", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "He was born on 8 December 65 BC[nb 4] in ", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata", apartment1, user1, employee1);
        given(repository.save(newAppEvent)).willReturn(newAppEvent);
        given(repository.saveAndFlush(newAppEvent)).willReturn(newAppEvent);

        AppEvent appEvent = appEventService.saveAppEvent(newAppEvent);
        Assert.assertNotNull(appEvent);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", appEvent.getId() == null || appEvent.getId() == 0);
    }

    @Test
    public void verifySaveAppEventRepoException() throws Exception{
        AppEvent newAppEvent =  new AppEvent(0L, "Sumudu", 694772, "nusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his chil", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "He was born on 8 December 65 BC[nb 4] in ", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata", apartment1, user1, employee1);
        given(repository.save(newAppEvent)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newAppEvent)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        AppEvent appEvent = null;
        try {
            appEvent = appEventService.saveAppEvent(newAppEvent);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && appEvent == null);
        }

    }

    @Test
    public void verifyDeleteAppEventSuccess() throws Exception{
        ServiceStatus status = appEventService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete appEvent operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteAppEventIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = appEventService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByNameTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByName(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = appEventService.findAllByName(users.get(0).getName());
        Assert.assertNotNull(resultAppEvents);
        Assert.assertTrue(resultAppEvents.size() > 0);
        Assert.assertTrue(resultAppEvents.get(0).getId() == this.appEvents.get(0).getId());

    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByName(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = null;
        try {
            resultAppEvents = appEventService.findAllByName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultAppEvents == null);
        }
    }
 	
	@Test
    public void findAllByDateTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByDate(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = appEventService.findAllByDate(users.get(0).getDate());
        Assert.assertNotNull(resultAppEvents);
        Assert.assertTrue(resultAppEvents.size() > 0);
        Assert.assertTrue(resultAppEvents.get(0).getId() == this.appEvents.get(0).getId());

    }

    @Test
    public void findAllByDateInvalidParamTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByDate(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = null;
        try {
            resultAppEvents = appEventService.findAllByDate(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultAppEvents == null);
        }
    }
 	
	@Test
    public void findAllByEventTypeTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByEventType(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = appEventService.findAllByEventType(users.get(0).getEventType());
        Assert.assertNotNull(resultAppEvents);
        Assert.assertTrue(resultAppEvents.size() > 0);
        Assert.assertTrue(resultAppEvents.get(0).getId() == this.appEvents.get(0).getId());

    }

    @Test
    public void findAllByEventTypeInvalidParamTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByEventType(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = null;
        try {
            resultAppEvents = appEventService.findAllByEventType(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultAppEvents == null);
        }
    }
 	
	@Test
    public void findAllByApartment_IdTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByApartment_Id(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = appEventService.findAllByApartment_Id(users.get(0).getApartment());
        Assert.assertNotNull(resultAppEvents);
        Assert.assertTrue(resultAppEvents.size() > 0);
        Assert.assertTrue(resultAppEvents.get(0).getId() == this.appEvents.get(0).getId());

    }

    @Test
    public void findAllByApartment_IdInvalidParamTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByApartment_Id(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = null;
        try {
            resultAppEvents = appEventService.findAllByApartment_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultAppEvents == null);
        }
    }
 	
	@Test
    public void findAllByUser_IdTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByUser_Id(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = appEventService.findAllByUser_Id(users.get(0).getUser());
        Assert.assertNotNull(resultAppEvents);
        Assert.assertTrue(resultAppEvents.size() > 0);
        Assert.assertTrue(resultAppEvents.get(0).getId() == this.appEvents.get(0).getId());

    }

    @Test
    public void findAllByUser_IdInvalidParamTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByUser_Id(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = null;
        try {
            resultAppEvents = appEventService.findAllByUser_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultAppEvents == null);
        }
    }
 	
	@Test
    public void findAllByEmployee_IdTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByEmployee_Id(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = appEventService.findAllByEmployee_Id(users.get(0).getEmployee());
        Assert.assertNotNull(resultAppEvents);
        Assert.assertTrue(resultAppEvents.size() > 0);
        Assert.assertTrue(resultAppEvents.get(0).getId() == this.appEvents.get(0).getId());

    }

    @Test
    public void findAllByEmployee_IdInvalidParamTest() throws Exception{
        ArrayList<AppEvent> matchedAppEvents = new ArrayList<>(1);
        matchedAppEvents.add(this.appEvents.get(0));
        given(repository.findAllByEmployee_Id(any())).willReturn(matchedAppEvents);
        List <AppEvent> resultAppEvents = null;
        try {
            resultAppEvents = appEventService.findAllByEmployee_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultAppEvents == null);
        }
    }




}
