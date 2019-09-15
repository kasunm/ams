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
 * <p>Title         : FloorDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class FloorDTO  {
    private Long id;


         
	@Size(max = 20, message="{Floor.color.length}")
    private String color;
     
	@Size(min = 0, max = 20, message="{Floor.name.length}")
	@NotNull(message = "{Floor.name.required}")
    private String name;
     
	@NotNull(message = "{Floor.floorNumber.required}")
    private Integer floorNumber;
	@NotNull
   private Long apartmentId;
     
	@Size(min = 3, max = 100, message="{Apartment.name.length}")
	@NotNull(message = "{Apartment.name.required}")
    private String apartmentName;




}
