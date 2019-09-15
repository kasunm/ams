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
 * <p>Title         : Client entity
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : A Client of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Client") 
public class Client  extends User{
     

         
	@Size(min = 3, max = 20, message="{Client.userName.length}")
	@NotNull(message = "{Client.userName.required}")
    private String userName;
     
	@Size(min = 0, max = 200, message="{Client.password.length}")
	@NotNull(message = "{Client.password.required}")
    private String password;
     
	@OneToMany
    private Set <Unit> ownedUnits;
     
	@OneToMany
    private Set <Unit> rentedUnits;
     
	@OneToMany
    private Set <Payment> payments;
     
	@OneToMany
    private Set <Notification> notificationList;
     
	@OneToMany
    private List <AppEvent> events;


    @JsonIgnore
    @Transient
    public boolean isValid(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Client>> violationSet =  validator.validate(this);
        return violationSet == null || violationSet.size() < 1;
    }
}
