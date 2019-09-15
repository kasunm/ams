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
 * <p>Title         : Contractor entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Contractor") 
public class Contractor  extends User{
     

         
	@Size(min = 0, max = 30, message="{Contractor.companyName.length}")
    private String companyName;
     
	@OneToMany
    private List <Maintenance> maintenances;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Contractor>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
