package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Inquiry Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Inquiry. An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

 List<Inquiry> findAllByDescription(String description);
List<Inquiry> findAllByType(InquiryType type);
List<Inquiry> findAllByAction(InquiryAction action);
 List<Inquiry> findAllByStatus(InquiryStatus status);
List<Inquiry> findAllByClient_Id(Long client);
List<Inquiry> findAllByEmployee_Id(Long employee);
Long countByClient_IdAndStatus(Long clientID, InquiryStatus status);
Long countByClient_IdAndAction(Long clientID, InquiryAction status);


}
