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
 * <p>Title         : UserDTO
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : DTO for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class UserDTO  {
    private Long id;


         
	@Size(min = 3, max = 100, message="{User.firstName.length}")
	@NotNull(message = "{User.firstName.required}")
    private String firstName;
     
	@Size(min = 3, max = 100, message="{User.lastName.length}")
    private String lastName;
     
	@Size(min = 0, max = 100, message="{User.email.length}")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "{User.email.pattern}")
    private String email;
     
	@Size(min = 0, max = 100, message="{User.addressLine1.length}")
    private String addressLine1;
     
	@Size(min = 0, max = 100, message="{User.addressLine2.length}")
    private String addressLine2;
     
	@Size(min = 0, max = 10, message="{User.contact1.length}")
	@Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "{User.contact1.pattern}")
    private String contact1;
     
	@Size(min = 0, max = 10, message="{User.contact2.length}")
	@Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "{User.contact2.pattern}")
    private String contact2;
     
	@Size(min = 0, max = 30, message="{User.nic.length}")
	@Pattern(regexp = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$", message = "{User.nic.pattern}")
    private String nic;
     
	@NotNull(message = "{User.userRole.required}")
    private UserRole userRole;



}
