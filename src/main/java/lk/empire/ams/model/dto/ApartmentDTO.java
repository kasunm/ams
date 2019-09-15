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
 * <p>Title         : ApartmentDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Apartment. An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class ApartmentDTO  {
    private Long id;


         
	@Size(min = 3, max = 100, message="{Apartment.name.length}")
	@NotNull(message = "{Apartment.name.required}")
    private String name;
 
 
 
     
	@NotNull(message = "{Apartment.logo.required}")
    private String logo;



}
