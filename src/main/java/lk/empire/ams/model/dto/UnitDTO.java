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
 * <p>Title         : UnitDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for Unit. A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class UnitDTO  {
    private Long id;


         
	@Size(min = 3, max = 100, message="{Unit.name.length}")
	@NotNull(message = "{Unit.name.required}")
    private String name;
	@NotNull
   private Long ownerId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String ownerFirstName;


	@NotNull
   private Long renterId;
     
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String renterFirstName;
     
	@Size(min = 3, max = 100, message="{User.lastName.length}")
    private String renterLastName;


 
 
	@NotNull
   private Long floorId;
     
	@Size(min = 0, max = 20, message="{Floor.name.length}")
	@NotNull(message = "{Floor.name.required}")
    private String floorName;

     
	@NotNull(message = "{Unit.availability.required}")
    private Availability availability;



}
