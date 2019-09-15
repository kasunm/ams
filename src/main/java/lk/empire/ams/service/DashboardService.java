package lk.empire.ams.service;

import lk.empire.ams.model.dto.ManagementDashboardDTO;
import lk.empire.ams.model.dto.UserDashboardDTO;

/**
 * <p>Title         : DashboardService
 * <p>Project       : Ams
 * <p>Description   : DashboardService
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
public interface DashboardService {
    UserDashboardDTO getUserDashboard(Long userID);
    ManagementDashboardDTO getManagementDashboard(Long userID);
}
