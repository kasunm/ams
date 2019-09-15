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
 * <p>Title         : VehicleParkingDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class VehicleParkingDTO  {
    private Long id;


         
	@Size(min = 5, max = 100, message="{VehicleParking.driverName.length}")
    private String driverName;
     
	@Size(min = 0, max = 25, message="{VehicleParking.driverID.length}")
    private String driverID;
     
	@NotNull(message = "{VehicleParking.inDate.required}")
    private LocalDate inDate;
     
	@NotNull(message = "{VehicleParking.inTime.required}")
    private LocalTime inTime;
     
    private LocalDate outDate;
     
	@NotNull(message = "{VehicleParking.outTime.required}")
    private LocalTime outTime;
	@NotNull
   private Long vehicleId;
     
	@Size(min = 3, max = 20, message="{Vehicle.number.length}")
	@NotNull(message = "{Vehicle.number.required}")
    private String vehicleNumber;

	@NotNull
   private Long parkingSlotId;
     
	@Size(min = 3, max = 20, message="{ParkingSlot.name.length}")
	@NotNull(message = "{ParkingSlot.name.required}")
    private String parkingSlotName;

     
	@Size(min = 0, max = 500, message="{VehicleParking.remarks.length}")
    private String remarks;



}
