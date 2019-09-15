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
 * <p>Title         : NotificationDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Notification. A Notification of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class NotificationDTO  {
    private Long id;


         
	@Size(min = 0, max = 2000, message="{Notification.description.length}")
	@NotNull(message = "{Notification.description.required}")
    private String description;
     
	@Size(min = 0, max = 20, message="{Notification.name.length}")
	@NotNull(message = "{Notification.name.required}")
    private String name;
     
    private LocalDate startDate;
     
    private LocalDate expiredDate;



}
