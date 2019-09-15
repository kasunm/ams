package lk.empire.ams.service;


import lk.empire.ams.model.entity.Payment;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : PaymentService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Payment. A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface PaymentService {

    /**
     * Get all payments
     * @return List<Payment>
     */
    List<Payment> getPayments();

    /**
     * Get a specific payment by id
     * @param id Long
     * @return Optional<Payment>
     */
    Optional<Payment> getByID(Long id);

    /**
     * Save payment and set id to passed payment
     * @param payment Payment
     * @return ServiceStatus
     */
    Payment savePayment(Payment payment);

    /**
     * Delete a payment by ID
     * @param paymentID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long paymentID);



	/**
     * Find  payments by client
     * @param client Long     
     * @return List<Payment>
     */
    public List<Payment> findPaymentsByClient_Id(Long client);

	/**
     * Find  payments by status
     * @param status PaymentStatus     
     * @return List<Payment>
     */
    public List<Payment> findAllByStatus(PaymentStatus status);

	/**
     * Find  payments by dueDate
     * @param dueDate LocalDate     
     * @return List<Payment>
     */
    public List<Payment> findAllByDueDate(LocalDate dueDate);

	/**
     * Find  payments by unit
     * @param unit Long     
     * @return List<Payment>
     */
    public List<Payment> findAllByUnit_Id(Long unit);

	/**
     * Find  payments by status,client
     * @param status PaymentStatus
     * @param client Long     
     * @return List<Payment>
     */
    public List<Payment> findAllByStatusAndClient_Id(PaymentStatus status, Long client);

    public Long findAllOverdueCountForClient(Long client);

    public Long findAllOverdueCount();

    public Long findAllPendingCount();


    public void addPendingPayments(List<Payment> dueToday, List<Payment> overdue);


}
