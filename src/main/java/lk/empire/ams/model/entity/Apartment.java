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
 * <p>Title         : Apartment entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class Apartment  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 3, max = 100, message="{Apartment.name.length}")
	@NotNull(message = "{Apartment.name.required}")
    private String name;
     
	@OneToMany
    private List <Maintenance> maintenanceList;
     
	@OneToMany
    private List <Floor> floors;
     
	@OneToMany
    private List <AppEvent> events;
     
	@NotNull(message = "{Apartment.logo.required}")
    private String logo;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Apartment>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
