package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainNotificationService;
import lk.empire.ams.service.NotificationService;
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
 * <p>Title         : NotificationService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Notification. A Notification of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class NotificationServiceTest {

    @MockBean
    private NotificationRepository repository;


    @Autowired
    private NotificationService notificationService;

    private List<Notification> notifications = new ArrayList<>();

     

    @TestConfiguration
    static class NotificationServiceImplTestContextConfiguration {
        @Bean
        public NotificationService employeeService() {
            return new MainNotificationService();
        }
    }

    @Before
    public void setUp(){
         

		notifications.add( new Notification(1L, "h state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his", "Supun", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)))); //ID 1
        notifications.add( new Notification(2L, "shment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[", "Tenuki", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)))); //ID 2
		notifications.add( new Notification(3L, "order region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social positio", "Ruwan", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)))); //ID 3

        given(repository.findAll()).willReturn(notifications);
        given(repository.findById(1L)).willReturn(Optional.of(notifications.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetNotificationsSuccess(){
        List<Notification> notifications = notificationService.getNotifications();
        Assert.assertNotNull(notifications);
        Assert.assertTrue("Expect 3 notifications in result", notifications.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", notifications == this.notifications);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Notification> notification = null;
        notification = notificationService.getByID(111L);
        Assert.assertTrue("No match found", !notification.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Notification> notification = null;
        try {
            notification = notificationService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", notification == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Notification> notification = notificationService.getByID(1L);
        Assert.assertNotNull(notifications);
        Assert.assertTrue("Expect a notifications in result", notification.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", notifications.get(0) == notification.get());
    }

    @Test
    public void verifySaveNotificationSuccess() throws Exception{
        Notification newNotification =   new Notification(1L, "h state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his", "Supun", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)));
        Notification savedNotification4 = new Notification();
        BeanUtils.copyProperties(newNotification, savedNotification4);
        savedNotification4.setId(5L);
        given(repository.save(newNotification)).willReturn(savedNotification4);
        given(repository.saveAndFlush(newNotification)).willReturn(savedNotification4);

        Notification notification = notificationService.saveNotification(newNotification);
        Assert.assertNotNull(notification);
        Assert.assertTrue("Expect valid ID in returned notification", notification.getId() != null && notification.getId() > 0);
    }

    @Test
    public void verifySaveNotificationNull() throws Exception{
        Notification notification = null;
        try {
            notification = notificationService.saveNotification(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned notification", !StringUtils.isEmpty(e.getMessage()) && notification == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveNotificationRepoIDNotReturned() throws Exception{
        Notification newNotification =  new Notification(0L, "reek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled t", "Kusal", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)));
        given(repository.save(newNotification)).willReturn(newNotification);
        given(repository.saveAndFlush(newNotification)).willReturn(newNotification);

        Notification notification = notificationService.saveNotification(newNotification);
        Assert.assertNotNull(notification);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", notification.getId() == null || notification.getId() == 0);
    }

    @Test
    public void verifySaveNotificationRepoException() throws Exception{
        Notification newNotification =  new Notification(0L, "reek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled t", "Kusal", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)));
        given(repository.save(newNotification)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newNotification)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Notification notification = null;
        try {
            notification = notificationService.saveNotification(newNotification);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && notification == null);
        }

    }

    @Test
    public void verifyDeleteNotificationSuccess() throws Exception{
        ServiceStatus status = notificationService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete notification operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteNotificationIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = notificationService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByDescriptionTest() throws Exception{
        ArrayList<Notification> matchedNotifications = new ArrayList<>(1);
        matchedNotifications.add(this.notifications.get(0));
        given(repository.findAllByDescription(any())).willReturn(matchedNotifications);
        List <Notification> resultNotifications = notificationService.findAllByDescription(users.get(0).getDescription());
        Assert.assertNotNull(resultNotifications);
        Assert.assertTrue(resultNotifications.size() > 0);
        Assert.assertTrue(resultNotifications.get(0).getId() == this.notifications.get(0).getId());

    }

    @Test
    public void findAllByDescriptionInvalidParamTest() throws Exception{
        ArrayList<Notification> matchedNotifications = new ArrayList<>(1);
        matchedNotifications.add(this.notifications.get(0));
        given(repository.findAllByDescription(any())).willReturn(matchedNotifications);
        List <Notification> resultNotifications = null;
        try {
            resultNotifications = notificationService.findAllByDescription(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultNotifications == null);
        }
    }




}
