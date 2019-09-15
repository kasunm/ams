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
 * <p>Title         : ParkingSlotDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class ParkingSlotDTO  {
    private Long id;


         
	@Size(min = 3, max = 20, message="{ParkingSlot.name.length}")
	@NotNull(message = "{ParkingSlot.name.required}")
    private String name;
	@NotNull
   private Long unitId;
     
	@Size(min = 3, max = 100, message="{Unit.name.length}")
	@NotNull(message = "{Unit.name.required}")
    private String unitName;

 
     
	@Size(min = 0, max = 10, message="{ParkingSlot.vehicleNumber.length}")
    private String vehicleNumber;
     
	@NotNull(message = "{ParkingSlot.availability.required}")
    private Availability availability;



}
