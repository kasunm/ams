package lk.empire.ams.repo;

import lk.empire.ams.model.entity.CommonArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : CommonArea Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface CommonAreaRepository extends JpaRepository<CommonArea, Long> {

 List<CommonArea> findAllByType(String type);
List<CommonArea> findAllByAvailability(Availability availability);
List<CommonArea> findAllByFloor_Name(Long floor);


}
