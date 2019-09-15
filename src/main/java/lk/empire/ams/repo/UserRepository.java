package lk.empire.ams.repo;

import lk.empire.ams.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : User Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

 List<User> findAllByUserRole(UserRole userRole);
List<User> findAllByFirstName(String firstName);
List<User> findAllByEmail(String email);
List<User> findAllByContact1(String contact1);


}
