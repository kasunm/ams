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
 * <p>Title         : Unit entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A Unit of of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class Unit  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 3, max = 100, message="{Unit.name.length}")
	@NotNull(message = "{Unit.name.required}")
    private String name;
     
	@ManyToOne
    private Client owner;
     
	@ManyToOne
    private Client renter;
     
	@OneToMany
    private List <ParkingSlot> parkingSlots;
     
	@OneToMany
    private List <Vehicle> vehicles;
     
	@ManyToOne
    private Floor floor;
     
	@NotNull(message = "{Unit.availability.required}")
    private Availability availability;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Unit>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }

}
