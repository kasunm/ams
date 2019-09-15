package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Payment Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Payment. A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

 List<Payment> findPaymentsByClient_Id(Long client);
List<Payment> findAllByStatus(PaymentStatus status);
List<Payment> findAllByDueDate(LocalDate dueDate);
List<Payment> findAllByStatusAndDueDateBefore(PaymentStatus status, LocalDate dueDate);
List<Payment> findAllByStatusAndDueDateBetween(PaymentStatus status, Date startDate, Date endDate);
List<Payment> findAllByUnit_Id(Long unit);
List<Payment> findAllByStatusAndClient_Id(PaymentStatus status, Long client);
List<Payment> findAllByStatusAndClient_IdAndDueDateBefore(PaymentStatus status, Long client, LocalDate date);
@Query(value = "select count(p.amount) from Payment p JOIN p.client c where p.status = ?1 and c.id = ?2 and p.dueDate <= ?3")
Long findAllOverdueCount(PaymentStatus status, Long client, LocalDate date);
Long countByStatusAndDueDateBefore(PaymentStatus status, Date date);
Long countByStatus(PaymentStatus status);



}
