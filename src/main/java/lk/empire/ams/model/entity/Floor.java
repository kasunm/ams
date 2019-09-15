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
 * <p>Title         : Floor entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class Floor  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(max = 20, message="{Floor.color.length}")
    private String color;
     
	@Size(min = 0, max = 20, message="{Floor.name.length}")
	@NotNull(message = "{Floor.name.required}")
    private String name;
     
	@NotNull(message = "{Floor.floorNumber.required}")
    private Integer floorNumber;
     
	@NotNull(message = "{Floor.apartment.required}")
	@ManyToOne
    private Apartment apartment;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Floor>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
