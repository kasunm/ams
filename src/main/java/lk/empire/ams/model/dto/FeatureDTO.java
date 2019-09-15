package lk.empire.ams.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import lk.empire.ams.model.enums.*;
import javax.validation.constraints.*;
import java.time.*;
import java.util.*;

/**
 * <p>Title         : FeatureDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Feature. A Feature of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class FeatureDTO  {
    private Long id;


         
	@Size(min = 0, max = 60, message="{Feature.name.length}")
	@NotNull(message = "{Feature.name.required}")
    private String name;
     
    private String description;
     
    private byte[] image;



}
