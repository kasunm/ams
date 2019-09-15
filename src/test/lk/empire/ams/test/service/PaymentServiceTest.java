package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainPaymentService;
import lk.empire.ams.service.PaymentService;
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
 * <p>Title         : PaymentService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Payment. A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentServiceTest {

    @MockBean
    private PaymentRepository repository;


    @Autowired
    private PaymentService paymentService;

    private List<Payment> payments = new ArrayList<>();

     	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
		private Client client0;
		private Client client1;
		private Client client2;
	

    @TestConfiguration
    static class PaymentServiceImplTestContextConfiguration {
        @Bean
        public PaymentService employeeService() {
            return new MainPaymentService();
        }
    }

    @Before
    public void setUp(){
        unit0 =   new Unit(0L, "Sumudu", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(1L, "Nayani", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	client0 =   new Client(0L, "Sumudu", "ite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	client1 =   new Client(1L, "Omega", " in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region betwee", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	client2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	 

		payments.add( new Payment(1L, PaymentMethod.Cash, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 399619, 956645, 687961.6658473716, unit1, client1)); //ID 1
        payments.add( new Payment(2L, PaymentMethod.Cheque, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 337395, 818850, 918849.0147831113, unit1, client1)); //ID 2
		payments.add( new Payment(3L, PaymentMethod.Cash, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 925259, 253708, 967621.7575155144, unit1, client1)); //ID 3

        given(repository.findAll()).willReturn(payments);
        given(repository.findById(1L)).willReturn(Optional.of(payments.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetPaymentsSuccess(){
        List<Payment> payments = paymentService.getPayments();
        Assert.assertNotNull(payments);
        Assert.assertTrue("Expect 3 payments in result", payments.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", payments == this.payments);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Payment> payment = null;
        payment = paymentService.getByID(111L);
        Assert.assertTrue("No match found", !payment.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Payment> payment = null;
        try {
            payment = paymentService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", payment == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Payment> payment = paymentService.getByID(1L);
        Assert.assertNotNull(payments);
        Assert.assertTrue("Expect a payments in result", payment.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", payments.get(0) == payment.get());
    }

    @Test
    public void verifySavePaymentSuccess() throws Exception{
        Payment newPayment =   new Payment(1L, PaymentMethod.Cash, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 399619, 956645, 687961.6658473716, unit1, client1);
        Payment savedPayment4 = new Payment();
        BeanUtils.copyProperties(newPayment, savedPayment4);
        savedPayment4.setId(5L);
        given(repository.save(newPayment)).willReturn(savedPayment4);
        given(repository.saveAndFlush(newPayment)).willReturn(savedPayment4);

        Payment payment = paymentService.savePayment(newPayment);
        Assert.assertNotNull(payment);
        Assert.assertTrue("Expect valid ID in returned payment", payment.getId() != null && payment.getId() > 0);
    }

    @Test
    public void verifySavePaymentNull() throws Exception{
        Payment payment = null;
        try {
            payment = paymentService.savePayment(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned payment", !StringUtils.isEmpty(e.getMessage()) && payment == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySavePaymentRepoIDNotReturned() throws Exception{
        Payment newPayment =  new Payment(0L, PaymentMethod.Cheque, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 985693, 276854, 751347.6289998706, unit1, client1);
        given(repository.save(newPayment)).willReturn(newPayment);
        given(repository.saveAndFlush(newPayment)).willReturn(newPayment);

        Payment payment = paymentService.savePayment(newPayment);
        Assert.assertNotNull(payment);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", payment.getId() == null || payment.getId() == 0);
    }

    @Test
    public void verifySavePaymentRepoException() throws Exception{
        Payment newPayment =  new Payment(0L, PaymentMethod.Cheque, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 985693, 276854, 751347.6289998706, unit1, client1);
        given(repository.save(newPayment)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newPayment)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Payment payment = null;
        try {
            payment = paymentService.savePayment(newPayment);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && payment == null);
        }

    }

    @Test
    public void verifyDeletePaymentSuccess() throws Exception{
        ServiceStatus status = paymentService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete payment operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeletePaymentIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = paymentService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findPaymentsByClient_IdTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findPaymentsByClient_Id(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = paymentService.findPaymentsByClient_Id(users.get(0).getClient());
        Assert.assertNotNull(resultPayments);
        Assert.assertTrue(resultPayments.size() > 0);
        Assert.assertTrue(resultPayments.get(0).getId() == this.payments.get(0).getId());

    }

    @Test
    public void findPaymentsByClient_IdInvalidParamTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findPaymentsByClient_Id(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = null;
        try {
            resultPayments = paymentService.findPaymentsByClient_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultPayments == null);
        }
    }
 	
	@Test
    public void findAllByStatusTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByStatus(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = paymentService.findAllByStatus(users.get(0).getStatus());
        Assert.assertNotNull(resultPayments);
        Assert.assertTrue(resultPayments.size() > 0);
        Assert.assertTrue(resultPayments.get(0).getId() == this.payments.get(0).getId());

    }

    @Test
    public void findAllByStatusInvalidParamTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByStatus(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = null;
        try {
            resultPayments = paymentService.findAllByStatus(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultPayments == null);
        }
    }
 	
	@Test
    public void findAllByDueDateTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByDueDate(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = paymentService.findAllByDueDate(users.get(0).getDueDate());
        Assert.assertNotNull(resultPayments);
        Assert.assertTrue(resultPayments.size() > 0);
        Assert.assertTrue(resultPayments.get(0).getId() == this.payments.get(0).getId());

    }

    @Test
    public void findAllByDueDateInvalidParamTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByDueDate(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = null;
        try {
            resultPayments = paymentService.findAllByDueDate(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultPayments == null);
        }
    }
 	
	@Test
    public void findAllByUnit_IdTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByUnit_Id(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = paymentService.findAllByUnit_Id(users.get(0).getUnit());
        Assert.assertNotNull(resultPayments);
        Assert.assertTrue(resultPayments.size() > 0);
        Assert.assertTrue(resultPayments.get(0).getId() == this.payments.get(0).getId());

    }

    @Test
    public void findAllByUnit_IdInvalidParamTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByUnit_Id(any())).willReturn(matchedPayments);
        List <Payment> resultPayments = null;
        try {
            resultPayments = paymentService.findAllByUnit_Id(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultPayments == null);
        }
    }
 	
	@Test
    public void findAllByStatusAndClient_IdTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByStatusAndClient_Id(any() ,any())).willReturn(matchedPayments);
        List <Payment> resultPayments = paymentService.findAllByStatusAndClient_Id(users.get(0).getStatus() ,users.get(0).getClient());
        Assert.assertNotNull(resultPayments);
        Assert.assertTrue(resultPayments.size() > 0);
        Assert.assertTrue(resultPayments.get(0).getId() == this.payments.get(0).getId());

    }

    @Test
    public void findAllByStatusAndClient_IdInvalidParamTest() throws Exception{
        ArrayList<Payment> matchedPayments = new ArrayList<>(1);
        matchedPayments.add(this.payments.get(0));
        given(repository.findAllByStatusAndClient_Id(any() ,any())).willReturn(matchedPayments);
        List <Payment> resultPayments = null;
        try {
            resultPayments = paymentService.findAllByStatusAndClient_Id(null ,null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultPayments == null);
        }
    }




}
