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
 * <p>Title         : Maintenance entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class Maintenance  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 0, max = 2000, message="{Maintenance.description.length}")
    private String description;
     
	@Size(min = 0, max = 50, message="{Maintenance.blockName.length}")
    private String blockName;
     
	@Size(min = 0, max = 50, message="{Maintenance.doneBy.length}")
    private String doneBy;
     
    private MaintenanceType maintenanceType;
     
    private MaintenanceStatus status;
     
    private LocalDate startDate;
     
    private LocalDate endDate;
     
	@ManyToOne
    private Contractor contractor;
     
	@ManyToOne
    private Floor floor;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Maintenance>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
