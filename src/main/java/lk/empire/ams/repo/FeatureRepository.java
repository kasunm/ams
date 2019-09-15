package lk.empire.ams.repo;

import lk.empire.ams.model.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lk.empire.ams.model.enums.*;
import java.util.List;
import java.time.*;

/**
 * <p>Title         : Feature Repository
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Repository support for Feature. A Feature of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {

 List<Feature> findAllByName(String name);


}
