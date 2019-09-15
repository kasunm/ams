package lk.empire.ams.repo;

import lk.empire.ams.model.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : ParkingSlot Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

 List<ParkingSlot> findAllByName(String name);
List<ParkingSlot> findAllByUnit_Id(Long unit);
List<ParkingSlot> findAllByVehicleNumber(String vehicleNumber);


}
