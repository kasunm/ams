package lk.empire.ams.service;


import lk.empire.ams.model.entity.Payment;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.PaymentRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : PaymentService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Payment. A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainPaymentService implements PaymentService{
    @Autowired
    PaymentRepository paymentRepository;

    /**
     * Get all payments
     * @return List<Payment>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Payment> getPayments(){
        return paymentRepository.findAll();
    }

    /**
     * Get a specific payment by id
     * @param id Long
     * @return Optional<Payment>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Payment> getByID(Long id){
         return paymentRepository.findById(id);
    }

    /**
     * Save payment and set id to passed payment
     * @param payment Payment
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Payment savePayment(Payment payment){
        Assert.notNull(payment, "Payment parameter expected");
        Assert.isTrue(payment.isValid(), "Valid Payment is expected");
        Payment savedPayment = paymentRepository.saveAndFlush(payment);
        if(savedPayment != null && savedPayment.getId() != null && savedPayment.getId() > 0) {
            payment.setId(savedPayment.getId());
        }
        if(payment.getStatus() == PaymentStatus.Completed && payment.getRecurringInterval() != null && payment.getRecurringInterval() > 0){
            //When a recurring payment is completed create a new one
           Payment nextPayment = new Payment();
            BeanUtils.copyProperties(payment, nextPayment);
            nextPayment.setUnit(payment.getUnit());
            nextPayment.setClient(payment.getClient());
            LocalDate nextDate = payment.getDueDate().plusDays(payment.getRecurringInterval());
            if(nextPayment.getEndDate() != null && nextDate.isAfter(nextPayment.getEndDate())){
                //Since next date is after end date task wont be created
            }else {
                nextPayment.setDueDate(nextDate);
                nextPayment.setDate(LocalDate.now());
                paymentRepository.save(nextPayment);
            }
        }
        return payment;
    }

    /**
     * Delete a payment by ID
     * @param paymentID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long paymentID){
        paymentRepository.deleteById(paymentID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  payments by client
     * @param client Long     
     * @return List<Payment>
     */
    public List<Payment> findPaymentsByClient_Id(Long client){
		Assert.notNull(client, "Expects a valid client");
		Assert.isTrue(client > 0, "Expects a valid client > 0");

        return paymentRepository.findPaymentsByClient_Id(client);
    }
	/**
     * Find  payments by status
     * @param status PaymentStatus     
     * @return List<Payment>
     */
    public List<Payment> findAllByStatus(PaymentStatus status){
		Assert.notNull(status, "Expects a valid status");

        return paymentRepository.findAllByStatus(status);
    }
	/**
     * Find  payments by dueDate
     * @param dueDate LocalDate     
     * @return List<Payment>
     */
    public List<Payment> findAllByDueDate(LocalDate dueDate){
		Assert.notNull(dueDate, "Expects a valid dueDate");

        return paymentRepository.findAllByDueDate(dueDate);
    }
	/**
     * Find  payments by unit
     * @param unit Long     
     * @return List<Payment>
     */
    public List<Payment> findAllByUnit_Id(Long unit){
		Assert.notNull(unit, "Expects a valid unit");
		Assert.isTrue(unit > 0, "Expects a valid unit > 0");

        return paymentRepository.findAllByUnit_Id(unit);
    }

	/**
     * Find  payments by status,client
     * @param status PaymentStatus
     * @param client Long     
     * @return List<Payment>
     */
    public List<Payment> findAllByStatusAndClient_Id(PaymentStatus status, Long client){
		Assert.notNull(status, "Expects a valid status");
		Assert.notNull(client, "Expects a valid client");
		Assert.isTrue(client > 0, "Expects a valid client > 0");

        return paymentRepository.findAllByStatusAndClient_Id(status,client);
    }


    /**
     * Find  all overdue payments for a client

     * @param client Long
     * @return List<Payment>
     */
    public List<Payment> findAllByStatusAndClient_Id(Long client){
        Assert.notNull(client, "Expects a valid client");
        Assert.isTrue(client > 0, "Expects a valid client > 0");
        return paymentRepository.findAllByStatusAndClient_IdAndDueDateBefore(PaymentStatus.Pending,client, LocalDate.now());
    }

    /**
     * Find  total overdue payments for a client

     * @param client Long
     * @return Long
     */
    public Long findAllOverdueCountForClient(Long client){
        Assert.notNull(client, "Expects a valid client");
        Assert.isTrue(client > 0, "Expects a valid client > 0");
        return paymentRepository.findAllOverdueCount(PaymentStatus.Pending,client, LocalDate.now());
    }

    /**
     * Find  total overdue payments count
     * @return Long
     */
    public Long findAllOverdueCount(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return paymentRepository.countByStatusAndDueDateBefore(PaymentStatus.Pending, calendar.getTime());
    }

    /**
     * Find  total overdue payments count
     * @return Long
     */
    public Long findAllPendingCount(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return paymentRepository.countByStatus(PaymentStatus.Pending);
    }

    /**
     * Add overdue payments for last 2 days and payment to be made for next 2 days
     * @param dueToday List<Payment> payment to be made for next 2 days
     * @param overdue List<Payment> overdue payments for last 2 days
     */
    public void addPendingPayments(List<Payment> dueToday, List<Payment> overdue){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrowMorning = calendar.getTime();
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.add(Calendar.DAY_OF_YEAR, -4);
        Date yesterdayNight = calendar.getTime();

        List <Payment> overduePayments = paymentRepository.findAllByStatusAndDueDateBetween(PaymentStatus.Pending, yesterdayNight, new Date());
        if(overduePayments != null && overduePayments.size() > 0) overdue.addAll(overduePayments);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        List <Payment> todayPayments = paymentRepository.findAllByStatusAndDueDateBetween(PaymentStatus.Pending, calendar.getTime(), tomorrowMorning);
        if(todayPayments != null && todayPayments.size() > 0) dueToday.addAll(todayPayments);
    }



    @PostConstruct
    public void initDB(){

    }
}
