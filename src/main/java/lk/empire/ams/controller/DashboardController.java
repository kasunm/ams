package lk.empire.ams.controller;

import com.sun.deploy.net.HttpResponse;
import io.swagger.annotations.Api;
import lk.empire.ams.model.dto.ManagementDashboardDTO;
import lk.empire.ams.model.dto.UserDashboardDTO;
import lk.empire.ams.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title         : ${FILE_NAME}
 * <p>Project       : Ams
 * <p>Description   :
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/dashboard", value = "DashboardController", description = "All services related to Dashboards", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/dashboard")
public class DashboardController {
    private DashboardService service;
    private final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    public DashboardController(DashboardService service){
        this.service = service;
    }

    /**
     * Get user's dashboard
     * @param userID Long
     * @return UserDashboardDTO
     */
    @GetMapping(path = "/user/{userID}")
    public ResponseEntity<UserDashboardDTO> getUserDashboard(Long userID){
        logger.debug("Request to get a user's dashboard by userID: " + userID);
        if(userID == null || userID == 0) throw new IllegalArgumentException("expects a valid userID");
        UserDashboardDTO dashboardDTO = service.getUserDashboard(userID);
        if(dashboardDTO == null)  throw new ResourceNotFoundException("Unable to find any user with id " + userID);
        return new ResponseEntity<UserDashboardDTO>(dashboardDTO, HttpStatus.ACCEPTED);
    }

    /**
     * Get manager's dashboard
     * @param userID Long
     * @return UserDashboardDTO
     */
    @GetMapping(path = "/manager/{userID}")
    public ResponseEntity<ManagementDashboardDTO> getManagerDashboard(Long userID){
        logger.debug("Request to get a user's dashboard by userID: " + userID);
        if(userID == null || userID == 0) throw new IllegalArgumentException("expects a valid userID");
        ManagementDashboardDTO dashboardDTO = service.getManagementDashboard(userID);
        if(dashboardDTO == null)  throw new ResourceNotFoundException("Unable to find any manager with id " + userID);
        return new ResponseEntity<ManagementDashboardDTO>(dashboardDTO, HttpStatus.ACCEPTED);
    }
}
