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
 * <p>Title         : VehicleDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Vehicle. A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class VehicleDTO  {
    private Long id;


         
	@Size(min = 3, max = 20, message="{Vehicle.number.length}")
	@NotNull(message = "{Vehicle.number.required}")
    private String number;
	@NotNull
   private Long unitId;
     
	@Size(min = 3, max = 100, message="{Unit.name.length}")
	@NotNull(message = "{Unit.name.required}")
    private String unitName;

 
     
	@Size(min = 0, max = 30, message="{Vehicle.make.length}")
    private String make;
     
	@Size(min = 0, max = 50, message="{Vehicle.model.length}")
    private String model;



}
