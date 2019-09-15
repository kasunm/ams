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
 * <p>Title         : ParkingSlot entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class ParkingSlot  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 3, max = 20, message="{ParkingSlot.name.length}")
	@NotNull(message = "{ParkingSlot.name.required}")
    private String name;
     
	@ManyToOne
    private Unit unit;
     
	@OneToMany
    private Set <VehicleParking> vehicleParking;
     
	@Size(min = 0, max = 10, message="{ParkingSlot.vehicleNumber.length}")
    private String vehicleNumber;
     
	@NotNull(message = "{ParkingSlot.availability.required}")
    private Availability availability;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ParkingSlot>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
