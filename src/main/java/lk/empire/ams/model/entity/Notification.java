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
import javax.xml.bind.annotation.XmlType;
import java.util.*;
import java.time.*;

import lk.empire.ams.model.enums.*;

/**
 * <p>Title         : Notification entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A Notification of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlType(namespace = "http://www.example.org/notification")
public class Notification  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; 

         
	@Size(min = 0, max = 2000, message="{Notification.description.length}")
	@NotNull(message = "{Notification.description.required}")
    private String description;
     
	@Size(min = 0, max = 20, message="{Notification.name.length}")
	@NotNull(message = "{Notification.name.required}")
    private String name;
     
    private LocalDate startDate;
     
    private LocalDate expiredDate;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Notification>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
