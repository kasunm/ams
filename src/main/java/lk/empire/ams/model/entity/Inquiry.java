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
 * <p>Title         : Inquiry entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : An Inquiry for apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class Inquiry  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 0, max = 2000, message="{Inquiry.description.length}")
    private String description;
     
	@Lob
    private byte[] image;
     
    private InquiryStatus status;
     
    private InquiryType type;
     
    private InquiryAction action;
     
    private LocalDate openDate;
     
    private LocalDate closeDate;
     
	@ManyToOne
    private Client client;
     
	@ManyToOne
    private Employee employee;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Inquiry>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
