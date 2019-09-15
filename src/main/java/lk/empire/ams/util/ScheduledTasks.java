package lk.empire.ams.util;

import lk.empire.ams.model.entity.Client;
import lk.empire.ams.model.entity.Payment;
import lk.empire.ams.model.entity.Unit;
import lk.empire.ams.model.enums.PaymentMethod;
import lk.empire.ams.model.enums.PaymentStatus;
import lk.empire.ams.service.PaymentService;
import lk.empire.ams.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>Title         : ${FILE_NAME}
 * <p>Project       : Ams
 * <p>Description   :
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private PaymentService paymentService;
    private EmailSender emailSender;
    private UnitService unitService;

    @Value("${email.overdue.subject}")
    private String overdueEmailSubject;

    @Value("${email.overdue.body}")
    private String overdueEmailBody;


    @Value("${email.future.subject}")
    private String futureEmailSubject;

    @Value("${email.future.body}")
    private String futureEmailBody;



    public ScheduledTasks(PaymentService paymentService, EmailSender emailSender, UnitService unitService){
        this.paymentService = paymentService;
        this.emailSender = emailSender;
        this.unitService = unitService;
    }

    /**
     * Execute everyday at 1 am
     * Inform users about
     * 1) Overdue payments last 2 days
     * 2) Due payments today and tomorrow
     */
    @Scheduled(cron = "0 1 * * * *")
    public void dailyTasks(){
        logger.info("Daily task started");

        logger.info("Sending overdue emails");
        List<Payment>  overduePayments = new ArrayList<>();
        List<Payment>  futurePayments = new ArrayList<>();
        paymentService.addPendingPayments(futurePayments, overduePayments);
        for(Payment overdue: overduePayments){
            Client client = overdue.getClient();
            if(client == null) throw new IllegalStateException("Payment " + overdue.getId() + " should have a valid client ");
            String email = client.getEmail();
            if(StringUtils.isEmpty(email)) continue;
            overdueEmailBody.replaceAll("\\$name", client.getFirstName()).replaceAll("\\$unit", overdue.getUnit().getName()).replaceAll("\\amount", String.valueOf(overdue.getAmount()));
            emailSender.sendEmail(email, overdueEmailSubject, overdueEmailBody );
        }
        for(Payment payment: futurePayments){
            Client client = payment.getClient();
            if(client == null) throw new IllegalStateException("Payment " + payment.getId() + " should have a valid client ");
            String email = client.getEmail();
            if(StringUtils.isEmpty(email)) continue;
            futureEmailBody.replaceAll("\\$name", client.getFirstName()).replaceAll("\\$unit", payment.getUnit().getName()).replaceAll("\\amount", String.valueOf(payment.getAmount()));
            emailSender.sendEmail(email, futureEmailSubject, futureEmailBody );
        }

    }

    /**
     * Execute 1 am on December 31st
     * Generate repeating payments
     */
    public void anualTasks(){
        List<Unit> units = unitService.getUnits();
        for(Unit unit: units){
            double amount = 10000; //@TODO calculate amount based on unit type
            Payment payment = new Payment();
            payment.setPaymentMethod(PaymentMethod.Cash);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 30);
            payment.setDate(LocalDate.now());
            TimeZone tz = calendar.getTimeZone();
            ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
            payment.setAmount(amount);
            payment.setDueDate(LocalDateTime.ofInstant(calendar.toInstant(), zid).toLocalDate());
            payment.setClient(unit.getOwner());
            payment.setUnit(unit);
            payment.setRecurringInterval(365L);
            payment.setStatus(PaymentStatus.Pending);
            paymentService.savePayment(payment);
        }
    }



}
