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
 * <p>Title         : MaintenanceDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class MaintenanceDTO  {
    private Long id;


         
	@Size(min = 0, max = 2000, message="{Maintenance.description.length}")
    private String description;
     
	@Size(min = 0, max = 50, message="{Maintenance.blockName.length}")
    private String blockName;
     
	@Size(min = 0, max = 50, message="{Maintenance.doneBy.length}")
    private String doneBy;
     
    private MaintenanceType maintenanceType;
     
    private MaintenanceStatus status;
     
    private LocalDate startDate;
     
    private LocalDate endDate;
	@NotNull
   private Long contractorId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String contractorFirstName;


	@NotNull
   private Long floorId;




}
