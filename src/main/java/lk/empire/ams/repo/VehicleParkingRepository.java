package lk.empire.ams.repo;

import lk.empire.ams.model.entity.VehicleParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : VehicleParking Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface VehicleParkingRepository extends JpaRepository<VehicleParking, Long> {

 List<VehicleParking> findAllByParkingSlot_Id(Long parkingSlot);
List<VehicleParking> findAllByDriverID(String driverID);
List<VehicleParking> findAllByInDate(LocalDate inDate);
List<VehicleParking> findAllByOutDate(LocalDate outDate);
List<VehicleParking> findAllByVehicle_Id(Long vehicle);
List<VehicleParking> findAllByVehicle_number(Long vehicle);


}
