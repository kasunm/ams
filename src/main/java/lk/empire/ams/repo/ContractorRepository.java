package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Contractor Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {

 List<Contractor> findAllByCompanyName(String companyName);
List<Contractor> findAllByFirstName(String firstName);
List<Contractor> findAllByEmail(String email);
List<Contractor> findAllByNic(String nic);


}
