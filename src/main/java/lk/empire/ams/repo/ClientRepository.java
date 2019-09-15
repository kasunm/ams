package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Client Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Client. A Client of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

 List<Client> findAllByFirstName(String firstName);
List<Client> findAllByEmail(String email);
List<Client> findAllByNic(String nic);


}
