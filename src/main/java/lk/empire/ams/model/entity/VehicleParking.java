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
 * <p>Title         : VehicleParking entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class VehicleParking  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 5, max = 100, message="{VehicleParking.driverName.length}")
    private String driverName;
     
	@Size(min = 0, max = 25, message="{VehicleParking.driverID.length}")
    private String driverID;
     
	@NotNull(message = "{VehicleParking.inDate.required}")
    private LocalDate inDate;
     
	@NotNull(message = "{VehicleParking.inTime.required}")
    private LocalTime inTime;
     
    private LocalDate outDate;
     
	@NotNull(message = "{VehicleParking.outTime.required}")
    private LocalTime outTime;
     
	@ManyToOne
    private Vehicle vehicle;
     
	@ManyToOne
    private ParkingSlot parkingSlot;
     
	@Size(min = 0, max = 500, message="{VehicleParking.remarks.length}")
    private String remarks;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<VehicleParking>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
