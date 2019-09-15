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
 * <p>Title         : Payment entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class Payment  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@NotNull(message = "{Payment.paymentMethod.required}")
    private PaymentMethod paymentMethod;
     
	@NotNull(message = "{Payment.status.required}")
    private PaymentStatus status;
     
    private LocalDate date;
     
    private LocalDate dueDate;
     
    private LocalDate endDate;
     
	@Min( value = 0, message="{Payment.recurringInterval.min}")
    private Long recurringInterval;
     
	@Min( value = 0, message="{Payment.gracePeriod.min}")
    private Long gracePeriod;
     
    private Double amount;
     
	@ManyToOne
    private Unit unit;
     
	@ManyToOne
    private Client client;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Payment>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
