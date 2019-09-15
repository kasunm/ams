package lk.empire.ams.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * <p>Title         : ${className}Service
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Object representing error response JSON
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private int status;
    private String message;
    private List<String> details;
}
