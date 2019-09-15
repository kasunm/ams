package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.ParkingSlotController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.ParkingSlotDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.ParkingSlotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.result.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>Title         : ParkingSlotController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for ParkingSlot. A parking slot
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, ParkingSlotController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class ParkingSlotControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private ParkingSlotService parkingSlotService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<ParkingSlot> parkingSlots = new ArrayList<>();

     	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
	

    @Before
    public void setup() {
        unit0 =   new Unit(0L, "Sumudu", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(1L, "Ruwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        unit0 =   new Unit(0L, "Sumudu", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(1L, "Ruwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 
        parkingSlots.add( new ParkingSlot(1L, "Sulaiman", unit1, new HasSet <VehicleParking>(), "z20", Availability.Available)); //ID 1
        parkingSlots.add( new ParkingSlot(2L, "Arun", unit1, new HasSet <VehicleParking>(), "z350", Availability.Available)); //ID 2
		parkingSlots.add( new ParkingSlot(0L, "Kusal", unit1, new HasSet <VehicleParking>(), "t", Availability.Available));//New item
        given(parkingSlotService.getByID(1L)).willReturn(Optional.of(parkingSlots.get(0)));
        given(parkingSlotService.getByID(2L)).willReturn(Optional.of(parkingSlots.get(1)));
        given(parkingSlotService.getParkingSlots()).willReturn(parkingSlots);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(parkingSlotService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(parkingSlotService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveParkingSlotSuccess() throws Exception{
        ParkingSlot savedParkingSlot = new ParkingSlot();
        BeanUtils.copyProperties(parkingSlots.get(2), savedParkingSlot);
        savedParkingSlot.setId(5L);
        given(parkingSlotService.saveParkingSlot(any())).willReturn(savedParkingSlot);
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(parkingSlots.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/parkingslots/5"));
    }

    @Test
    public void verifySaveParkingSlotExistingSuccess() throws Exception{
        given(parkingSlotService.saveParkingSlot(any())).willReturn(parkingSlots.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(parkingSlots.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/parkingslots/2"));
    }


    @Test
    public void verifySaveParkingSlotFailValidation() throws Exception{
        ParkingSlot invalid =   new ParkingSlot(6L, "H", unit2, new HasSet <VehicleParking>(), "He was born ", Availability.Available);
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveParkingSlotExistingNotFound() throws Exception{
        ParkingSlot nonExisting =   new ParkingSlot(5L, "Nayani", unit1, new HasSet <VehicleParking>(), "m", Availability.Available);
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveParkingSlotPersistenceIDNotSet() throws Exception{
        ParkingSlot idNotSet =   new ParkingSlot(0L, "Kusal", unit1, new HasSet <VehicleParking>(), "t", Availability.Available);;
        given(parkingSlotService.saveParkingSlot(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveParkingSlotDataException() throws Exception{
        ParkingSlot exceptParkingSlot = new ParkingSlot();
        BeanUtils.copyProperties(parkingSlots.get(2), exceptParkingSlot);

        given(parkingSlotService.saveParkingSlot(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptParkingSlot)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveParkingSlotArgumentException() throws Exception{
        ParkingSlot exceptParkingSlot = new ParkingSlot();
        BeanUtils.copyProperties(parkingSlots.get(2), exceptParkingSlot);

        given(parkingSlotService.saveParkingSlot(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptParkingSlot)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(parkingSlotService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/parkingslots/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(parkingSlotService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/parkingslots/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/parkingslots/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(parkingSlotService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/parkingslots/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/name/{name}" , users.get(0).getName())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/name/{name}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByNameNoMatchTest() throws Exception{
        given(userService.findAllByName(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/name/{name}"  , users.get(0).getName())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByUnit_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByUnit_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/unit/{unit}" , users.get(0).getUnit())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByUnit_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByUnit_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/unit/{unit}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByUnit_IdNoMatchTest() throws Exception{
        given(userService.findAllByUnit_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/unit/{unit}"  , users.get(0).getUnit())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByVehicleNumberTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByVehicleNumber(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/vehicleNumber/{vehicleNumber}" , users.get(0).getVehicleNumber())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByVehicleNumberInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByVehicleNumber(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/vehicleNumber/{vehicleNumber}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByVehicleNumberNoMatchTest() throws Exception{
        given(userService.findAllByVehicleNumber(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/parkingslots/vehicleNumber/{vehicleNumber}"  , users.get(0).getVehicleNumber())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private ParkingSlotDTO getDTO(ParkingSlot parkingSlot){
        return modelMapper.map(parkingSlot, ParkingSlotDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
