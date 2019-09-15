package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Employee Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Employee. An Employee of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

 List<Employee> findAllByFirstName(String firstName);
List<Employee> findAllByEmail(String email);
List<Employee> findAllByNic(String nic);


}
