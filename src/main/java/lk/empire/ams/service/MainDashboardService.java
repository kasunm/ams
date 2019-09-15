package lk.empire.ams.service;

import lk.empire.ams.model.dto.*;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.InquiryAction;
import lk.empire.ams.model.enums.InquiryStatus;
import lk.empire.ams.model.enums.MaintenanceStatus;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * <p>Title         : MainDashboardService
 * <p>Project       : Ams
 * <p>Description   : MainDashboardService
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainDashboardService implements DashboardService{
    private UserService userService;
    private UnitService unitService;

    private  InquiryService inquiryService;
    private  AppEventService eventService;
    private  PaymentService paymentService;
    private  ContractorService contractorService;
    private  CommonAreaService commonAreaService;
    private  MaintenanceService maintenanceService;
    private final ModelMapper modelMapper = new ModelMapper();

    public MainDashboardService(UserService userService, UnitService unitService, InquiryService inquiryService,
                                AppEventService eventService, PaymentService paymentService, CommonAreaService commonAreaService,
                                ContractorService contractorService, MaintenanceService maintenanceService){
        this.userService = userService;
        this.unitService = unitService;
        this.inquiryService = inquiryService;
        this.eventService = eventService;
        this.paymentService = paymentService;
        this.commonAreaService = commonAreaService;
        this.contractorService = contractorService;
        this.maintenanceService = maintenanceService;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }


    @Override
    public UserDashboardDTO getUserDashboard(Long userID) {
        Assert.notNull(userID, "Expects a valid userID");
        UserDashboardDTO userDashboardDTO = new UserDashboardDTO();

        //Get user
        Optional<User> user = userService.getByID(userID);
        if(user == null || !user.isPresent()) return null;
        UserDTO userDTO = new UserDTO();
        modelMapper.map(user.get(), userDTO);
        userDashboardDTO.setUser(userDTO);

        //Get Blocks owned and rented
        List<Unit> allBlocks = new ArrayList<>();
        List<Unit> ownedBlocks = unitService.findAllByOwner_Id(userID);
        if(ownedBlocks != null && ownedBlocks.size() > 0){
            userDashboardDTO.setOwnedUnits(getDTOs(ownedBlocks));
            allBlocks.addAll(ownedBlocks);
        }
        List<Unit> rentedBlocks = unitService.findAllByRenter_Id(userID);
        if(rentedBlocks != null && rentedBlocks.size() > 0){
            userDashboardDTO.setRentedUnits(getDTOs(rentedBlocks));
            allBlocks.addAll(rentedBlocks);
        }

        Set<String> parkingSlotNameList = new HashSet<>();
        Set<String> vehicleNumbers = new HashSet<>();
        //Loop through all owned/rented units and add slot name and vehicle numbers
        for(Unit unit: allBlocks){
            List<ParkingSlot> parkingSlots = unit.getParkingSlots();
            List<Vehicle> vehicles = unit.getVehicles();
            if(parkingSlots != null) {
                for(ParkingSlot slot: parkingSlots){
                    parkingSlotNameList.add(slot.getName());
                }
            }
            if(vehicles != null){
                for(Vehicle vehicle: vehicles){
                    vehicleNumbers.add(vehicle.getNumber());
                }
            }
        }
        userDashboardDTO.setParkingSlotNames(parkingSlotNameList);
        userDashboardDTO.setUserVehicleNumbers(vehicleNumbers);


        userDashboardDTO.setTotalPayable(paymentService.findAllOverdueCountForClient(userID));
        userDashboardDTO.setInquiryCount(inquiryService.getCountByClientIdAndStatus(userID, InquiryStatus.Pending));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        userDashboardDTO.setEventCount(eventService.getByUserIdAndDates(userID, new Date(), calendar.getTime()));

        List<CommonArea> commonAreas = commonAreaService.getCommonAreas();
        if(commonAreas != null) userDashboardDTO.setCommonAreas(getCommanAreaDTOs(commonAreas));
        return userDashboardDTO;
    }

    @Override
    public ManagementDashboardDTO getManagementDashboard(Long userID) {
        ManagementDashboardDTO dashboardDTO = new ManagementDashboardDTO();
        dashboardDTO.setOpenInquiryCount(inquiryService.getCountByClientIdAndAction(userID, InquiryAction.Pending));
        dashboardDTO.setOverduePaymentCount(paymentService.findAllOverdueCount());
        dashboardDTO.setPendingPaymentCount(paymentService.findAllPendingCount());
        dashboardDTO.setContractorCount(contractorService.getCount());
        dashboardDTO.setUnitCount(unitService.getCount());
        dashboardDTO.setOwnersCount(dashboardDTO.getUnitCount());
        dashboardDTO.setRentersCount(unitService.getRenterCount());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        List<AppEvent> events = eventService.findAllPendingEvents(new Date(), calendar.getTime());

        if(events != null && events.size() > 0) dashboardDTO.setEventList(getAppEventDTOs(events));
        List<Maintenance> maintenanceList = maintenanceService.findAllByStatus(MaintenanceStatus.Ongoing);

        if(maintenanceList != null && maintenanceList.size() > 0) dashboardDTO.setOnGoingMaintenance(getMaintenanceDTOs(maintenanceList));
        maintenanceList = maintenanceService.findAllByStatus(MaintenanceStatus.Pending);
        if(maintenanceList != null && maintenanceList.size() > 0) dashboardDTO.setPendingMaintenance(getMaintenanceDTOs(maintenanceList));
        return dashboardDTO;
    }


    /** ------------- Private supportive methods ------------- */

    private UnitDTO convertToDTO(Unit units){
        return modelMapper.map(units, UnitDTO.class);
    }

    private List<UnitDTO> getDTOs(List<Unit> unitss){
        if(unitss == null) return null;
        List<UnitDTO> dtoList = new ArrayList<UnitDTO>(unitss.size());
        for(Unit units: unitss){
            UnitDTO dto = convertToDTO(units);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private CommonAreaDTO convertToDTO(CommonArea commonArea){
        return modelMapper.map(commonArea, CommonAreaDTO.class);
    }

    private List<CommonAreaDTO> getCommanAreaDTOs(List<CommonArea> commonAreas){
        if(commonAreas == null) return null;
        List<CommonAreaDTO> dtoList = new ArrayList<CommonAreaDTO>(commonAreas.size());
        for(CommonArea commonArea: commonAreas){
            CommonAreaDTO dto = convertToDTO(commonArea);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private AppEventDTO convertToDTO(AppEvent appEvent){
        return modelMapper.map(appEvent, AppEventDTO.class);
    }

    private List<AppEventDTO> getAppEventDTOs(List<AppEvent> appEvents){
        if(appEvents == null) return null;
        List<AppEventDTO> dtoList = new ArrayList<AppEventDTO>(appEvents.size());
        for(AppEvent appEvent: appEvents){
            AppEventDTO dto = convertToDTO(appEvent);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private MaintenanceDTO convertToDTO(Maintenance maintenance){
        return modelMapper.map(maintenance, MaintenanceDTO.class);
    }

    private List<MaintenanceDTO> getMaintenanceDTOs(List<Maintenance> maintenances){
        if(maintenances == null) return null;
        List<MaintenanceDTO> dtoList = new ArrayList<MaintenanceDTO>(maintenances.size());
        for(Maintenance maintenance: maintenances){
            MaintenanceDTO dto = convertToDTO(maintenance);
            dtoList.add(dto);
        }
        return dtoList;
    }


}
