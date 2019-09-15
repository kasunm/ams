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
 * <p>Title         : InquiryDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Inquiry. An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class InquiryDTO  {
    private Long id;


         
	@Size(min = 0, max = 2000, message="{Inquiry.description.length}")
    private String description;
     
    private byte[] image;
     
    private InquiryStatus status;
     
    private InquiryType type;
     
    private InquiryAction action;
     
    private LocalDate openDate;
     
    private LocalDate closeDate;
	@NotNull
   private Long clientId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String clientFirstName;


	@NotNull
   private Long employeeId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String employeeFirstName;





}
