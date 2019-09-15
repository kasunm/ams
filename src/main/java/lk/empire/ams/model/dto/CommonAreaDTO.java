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
 * <p>Title         : CommonAreaDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class CommonAreaDTO  {
    private Long id;


         
	@Size(min = 0, max = 2000, message="{CommonArea.description.length}")
    private String description;
     
    private String image;
     
	@Size(min = 0, max = 100, message="{CommonArea.type.length}")
    private String type;
     
    private Availability availability;
	@NotNull
   private Long floorId;
     
	@Size(min = 0, max = 20, message="{Floor.name.length}")
	@NotNull(message = "{Floor.name.required}")
    private String floorName;




}
