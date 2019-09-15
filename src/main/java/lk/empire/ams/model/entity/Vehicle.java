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
 * <p>Title         : Vehicle entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class Vehicle  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 3, max = 20, message="{Vehicle.number.length}")
	@NotNull(message = "{Vehicle.number.required}")
    private String number;
     
	@ManyToOne
    private Unit unit;
     
	@OneToMany
    private Set <VehicleParking> vehicleParking;
     
	@Size(min = 0, max = 30, message="{Vehicle.make.length}")
    private String make;
     
	@Size(min = 0, max = 50, message="{Vehicle.model.length}")
    private String model;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Vehicle>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
