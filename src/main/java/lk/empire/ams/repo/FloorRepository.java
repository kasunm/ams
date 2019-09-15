package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Floor Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {

 List<Floor> findAllByName(String name);
List<Floor> findAllByFloorNumber(Integer floorNumber);
List<Floor> findAllByApartment_Id(Long apartment);


}
