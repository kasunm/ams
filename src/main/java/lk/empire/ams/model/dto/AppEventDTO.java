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
 * <p>Title         : AppEventDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class AppEventDTO  {
    private Long id;


         
	@Size(min = 3, max = 150, message="{AppEvent.name.length}")
	@NotNull(message = "{AppEvent.name.required}")
    private String name;
     
    private Long repeatInterval;
     
	@Size(min = 0, max = 2000, message="{AppEvent.description.length}")
    private String description;
     
    private EventStatus status;
     
    private LocalDate date;
     
    private LocalTime startTime;
     
    private LocalTime endTime;
     
	@Size(min = 0, max = 100, message="{AppEvent.eventType.length}")
	@NotNull(message = "{AppEvent.eventType.required}")
    private String eventType;
     
	@Size(min = 0, max = 200, message="{AppEvent.allowedGroup.length}")
    private String allowedGroup;
	@NotNull
   private Long apartmentId;
     
	@Size(min = 3, max = 100, message="{Apartment.name.length}")
	@NotNull(message = "{Apartment.name.required}")
    private String apartmentName;

	@NotNull
   private Long userId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String userFirstName;


	@NotNull
   private Long employeeId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String employeeFirstName;





}
