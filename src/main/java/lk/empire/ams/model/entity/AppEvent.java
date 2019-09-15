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
 * <p>Title         : AppEvent entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class AppEvent  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 3, max = 150, message="{AppEvent.name.length}")
	@NotNull(message = "{AppEvent.name.required}")
    private String name;
     
    private Long repeatInterval;
     
	@Size(min = 0, max = 2000, message="{AppEvent.description.length}")
    private String description;
     
    private EventStatus status;
     
    private LocalDate date;
     
    private LocalTime startTime;
     
    private LocalTime endTime;
     
	@Size(min = 0, max = 100, message="{AppEvent.eventType.length}")
	@NotNull(message = "{AppEvent.eventType.required}")
    private String eventType;
     
	@Size(min = 0, max = 200, message="{AppEvent.allowedGroup.length}")
    private String allowedGroup;
     
	@NotNull(message = "{AppEvent.apartment.required}")
	@ManyToOne
    private Apartment apartment;
     
	@ManyToOne
    private Client user;
     
	@ManyToOne
    private Employee employee;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AppEvent>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
