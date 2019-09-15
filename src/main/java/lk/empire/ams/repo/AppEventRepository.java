package lk.empire.ams.repo;

import lk.empire.ams.model.entity.AppEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;

import java.util.Date;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : AppEvent Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface AppEventRepository extends JpaRepository<AppEvent, Long> {

 List<AppEvent> findAllByName(String name);
List<AppEvent> findAllByDate(LocalDate date);
List<AppEvent> findAllByEventType(String eventType);
List<AppEvent> findAllByApartment_Id(Long apartment);
List<AppEvent> findAllByUser_Id(Long user);
List<AppEvent> findAllByEmployee_Id(Long employee);
 List<AppEvent> findAllByStatusAndDateBetween(EventStatus status, Date start, Date end);
Long countByUser_IdAndDateBetween(Long userID, Date startDate, Date endDate);

}
