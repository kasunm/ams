package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;

import java.util.Date;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Maintenance Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

 List<Maintenance> findAllByDescription(String description);
List<Maintenance> findAllByBlockName(String blockName);
List<Maintenance> findAllByDoneBy(String doneBy);
List<Maintenance> findAllByMaintenanceType(MaintenanceType maintenanceType);
List<Maintenance> findAllByStatus(MaintenanceStatus status);
List<Maintenance> findAllByContractor_Id(Long contractor);


}
