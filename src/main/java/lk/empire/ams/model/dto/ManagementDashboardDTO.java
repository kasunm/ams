package lk.empire.ams.model.dto;

import lk.empire.ams.model.entity.AppEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>Title         : ${FILE_NAME}
 * <p>Project       : Ams
 * <p>Description   :
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class ManagementDashboardDTO {
    private Long openInquiryCount;
    private Long pendingPaymentCount;
    private Long overduePaymentCount;
    private Long contractorCount;
    private List<MaintenanceDTO> onGoingMaintenance;
    private List<MaintenanceDTO> pendingMaintenance;
    private List<AppEventDTO> eventList;
    private Long ownersCount;
    private Long rentersCount;
    private Long unitCount;


}
