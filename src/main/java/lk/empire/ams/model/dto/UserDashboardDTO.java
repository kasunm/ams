package lk.empire.ams.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title         : UserDashboardDTO
 * <p>Project       : Ams
 * <p>Description   : UserDashboardDTO
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class UserDashboardDTO {
    private UserDTO user;
    private List<UnitDTO> ownedUnits;
    private List<UnitDTO> rentedUnits;
    private Set<String> parkingSlotNames;
    private Set<String> userVehicleNumbers;
    private Long totalPayable;
    private Long inquiryCount;
    private Long eventCount;
    private List<CommonAreaDTO> commonAreas;


}
