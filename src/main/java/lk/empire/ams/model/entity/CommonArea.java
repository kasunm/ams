package lk.empire.ams.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;
import java.util.*;
import java.time.*;

import lk.empire.ams.model.enums.*;

/**
 * <p>Title         : CommonArea entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class CommonArea  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 0, max = 2000, message="{CommonArea.description.length}")
    private String description;
     
    private String image;
     
	@Size(min = 0, max = 100, message="{CommonArea.type.length}")
    private String type;
     
    private Availability availability;
     
	@NotNull(message = "{CommonArea.floor.required}")
	@ManyToOne
    private Floor floor;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CommonArea>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
