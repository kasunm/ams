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
 * <p>Title         : ContractorDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class ContractorDTO  extends UserDTO {
     


         
	@Size(min = 0, max = 30, message="{Contractor.companyName.length}")
    private String companyName;
 



}
