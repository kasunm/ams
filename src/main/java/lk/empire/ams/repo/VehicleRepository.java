package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Vehicle Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Vehicle. A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

 List<Vehicle> findAllByNumber(String number);
List<Vehicle> findAllByUnit_Id(Long unit);


}
