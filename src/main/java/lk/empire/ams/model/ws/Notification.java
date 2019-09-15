package lk.empire.ams.model.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;
import java.util.Set;



@XmlType(namespace = "http://www.kasun.com/notification")
public class Notification{

    private Long id;



    @Size(min = 0, max = 2000, message="{Notification.description.length}")
    @NotNull(message = "{Notification.description.required}")

    private String description;

    @Size(min = 0, max = 20, message="{Notification.name.length}")
    @NotNull(message = "{Notification.name.required}")

    private String name;

//    private LocalDate startDate;
//
//    private LocalDate expiredDate;


    public Notification() {
    }

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}