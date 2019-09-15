package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Unit Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Unit. A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

 List<Unit> findAllByName(String name);
List<Unit> findAllByOwner_Id(Long owner);
List<Unit> findAllByRenter_Id(Long renter);
List<Unit> findAllByAvailability(Availability availability);

@Query(value = "select count(u.id) from Unit u where u.renter is not null")
Long getCountOfRenters();

}
