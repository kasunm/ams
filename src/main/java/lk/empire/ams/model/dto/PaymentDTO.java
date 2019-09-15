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
 * <p>Title         : PaymentDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Payment. A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class PaymentDTO  {
    private Long id;


         
	@NotNull(message = "{Payment.paymentMethod.required}")
    private PaymentMethod paymentMethod;
     
	@NotNull(message = "{Payment.status.required}")
    private PaymentStatus status;
     
    private LocalDate date;
     
    private LocalDate dueDate;
     
    private LocalDate endDate;
     
	@Min( value = 0, message="{Payment.recurringInterval.min}")
    private Long recurringInterval;
     
	@Min( value = 0, message="{Payment.gracePeriod.min}")
    private Long gracePeriod;
     
    private Double amount;
	@NotNull
   private Long unitId;
     
	@Size(min = 3, max = 100, message="{Unit.name.length}")
	@NotNull(message = "{Unit.name.required}")
    private String unitName;

	@NotNull
   private Long clientId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String clientFirstName;
     
	@Size(min = 3, max = 100, message="{User.lastName.length}")
    private String clientLastName;





}
