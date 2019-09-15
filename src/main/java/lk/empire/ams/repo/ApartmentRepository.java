package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Apartment Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Apartment. An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

 List<Apartment> findAllByName(String name);


}
