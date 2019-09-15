package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.VehicleParkingController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.VehicleParkingDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.VehicleParkingService;
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
 * <p>Title         : VehicleParkingController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for VehicleParking. A parking duration of vehicle
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, VehicleParkingController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class VehicleParkingControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private VehicleParkingService vehicleParkingService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<VehicleParking> vehicleParkings = new ArrayList<>();

     	private Vehicle vehicle0;
		private Vehicle vehicle1;
		private Vehicle vehicle2;
		private ParkingSlot parkingSlot0;
		private ParkingSlot parkingSlot1;
		private ParkingSlot parkingSlot2;
	

    @Before
    public void setup() {
        vehicle0 =   new Vehicle(0L, "d2385736645232", unit1, new HasSet <VehicleParking>(), " ", "orn on 8 December 65 BC[nb 4] ");
	vehicle1 =   new Vehicle(1L, "i125076", unit1, new HasSet <VehicleParking>(), "8 December 65", " was born on 8 December 65 BC[nb 4] in the ");
	vehicle2 =   new Vehicle(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born on 8 December 65 BC[", "He was born on 8 December 65 BC[nb 4] in the Samnite");
	parkingSlot0 =   new ParkingSlot(0L, "Omega", unit1, new HasSet <VehicleParking>(), "x52378", Availability.Available);
	parkingSlot1 =   new ParkingSlot(1L, "Aloka", unit1, new HasSet <VehicleParking>(), "f30", Availability.Available);
	parkingSlot2 =   new ParkingSlot(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born ", Availability.Available);
	 

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        vehicle0 =   new Vehicle(0L, "d2385736645232", unit1, new HasSet <VehicleParking>(), " ", "orn on 8 December 65 BC[nb 4] ");
	vehicle1 =   new Vehicle(1L, "i125076", unit1, new HasSet <VehicleParking>(), "8 December 65", " was born on 8 December 65 BC[nb 4] in the ");
	vehicle2 =   new Vehicle(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born on 8 December 65 BC[", "He was born on 8 December 65 BC[nb 4] in the Samnite");
	parkingSlot0 =   new ParkingSlot(0L, "Omega", unit1, new HasSet <VehicleParking>(), "x52378", Availability.Available);
	parkingSlot1 =   new ParkingSlot(1L, "Aloka", unit1, new HasSet <VehicleParking>(), "f30", Availability.Available);
	parkingSlot2 =   new ParkingSlot(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born ", Availability.Available);
	 
        vehicleParkings.add( new VehicleParking(1L, "Sulaiman", "was born on 8 Dece", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "for language. He could have been familiar with Greek words even as a young boy and later he poked fun at ")); //ID 1
        vehicleParkings.add( new VehicleParking(2L, "Nayani", "H", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "th of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he")); //ID 2
		vehicleParkings.add( new VehicleParking(0L, "Sumudu", "e was born on 8 Dece", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, " was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the"));//New item
        given(vehicleParkingService.getByID(1L)).willReturn(Optional.of(vehicleParkings.get(0)));
        given(vehicleParkingService.getByID(2L)).willReturn(Optional.of(vehicleParkings.get(1)));
        given(vehicleParkingService.getVehicleParkings()).willReturn(vehicleParkings);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(vehicleParkingService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(vehicleParkingService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveVehicleParkingSuccess() throws Exception{
        VehicleParking savedVehicleParking = new VehicleParking();
        BeanUtils.copyProperties(vehicleParkings.get(2), savedVehicleParking);
        savedVehicleParking.setId(5L);
        given(vehicleParkingService.saveVehicleParking(any())).willReturn(savedVehicleParking);
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(vehicleParkings.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/vehicle-parking/5"));
    }

    @Test
    public void verifySaveVehicleParkingExistingSuccess() throws Exception{
        given(vehicleParkingService.saveVehicleParking(any())).willReturn(vehicleParkings.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(vehicleParkings.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/vehicle-parking/2"));
    }


    @Test
    public void verifySaveVehicleParkingFailValidation() throws Exception{
        VehicleParking invalid =   new VehicleParking(6L, "H", "He was born on 8 December 6", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle2, parkingSlot2, "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyss");
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveVehicleParkingExistingNotFound() throws Exception{
        VehicleParking nonExisting =   new VehicleParking(5L, "Thamasha", "rn o", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "eek words");
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveVehicleParkingPersistenceIDNotSet() throws Exception{
        VehicleParking idNotSet =   new VehicleParking(0L, "Sumudu", "e was born on 8 Dece", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, " was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the");;
        given(vehicleParkingService.saveVehicleParking(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveVehicleParkingDataException() throws Exception{
        VehicleParking exceptVehicleParking = new VehicleParking();
        BeanUtils.copyProperties(vehicleParkings.get(2), exceptVehicleParking);

        given(vehicleParkingService.saveVehicleParking(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptVehicleParking)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveVehicleParkingArgumentException() throws Exception{
        VehicleParking exceptVehicleParking = new VehicleParking();
        BeanUtils.copyProperties(vehicleParkings.get(2), exceptVehicleParking);

        given(vehicleParkingService.saveVehicleParking(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptVehicleParking)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(vehicleParkingService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle-parking/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(vehicleParkingService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle-parking/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle-parking/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(vehicleParkingService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle-parking/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByParkingSlot_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByParkingSlot_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/parkingSlot/{parkingSlot}" , users.get(0).getParkingSlot())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByParkingSlot_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByParkingSlot_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/parkingSlot/{parkingSlot}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByParkingSlot_IdNoMatchTest() throws Exception{
        given(userService.findAllByParkingSlot_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/parkingSlot/{parkingSlot}"  , users.get(0).getParkingSlot())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByDriverIDTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDriverID(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/driverID/{driverID}" , users.get(0).getDriverID())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByDriverIDInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDriverID(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/driverID/{driverID}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByDriverIDNoMatchTest() throws Exception{
        given(userService.findAllByDriverID(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/driverID/{driverID}"  , users.get(0).getDriverID())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByInDateTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByInDate(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/inDate/{inDate}" , users.get(0).getInDate())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByInDateInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByInDate(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/inDate/{inDate}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByInDateNoMatchTest() throws Exception{
        given(userService.findAllByInDate(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/inDate/{inDate}"  , users.get(0).getInDate())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByOutDateTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByOutDate(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/outDate/{outDate}" , users.get(0).getOutDate())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByOutDateInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByOutDate(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/outDate/{outDate}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByOutDateNoMatchTest() throws Exception{
        given(userService.findAllByOutDate(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/outDate/{outDate}"  , users.get(0).getOutDate())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByVehicle_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByVehicle_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/vehicle-id/{vehicle}" , users.get(0).getVehicle())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByVehicle_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByVehicle_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/vehicle-id/{vehicle}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByVehicle_IdNoMatchTest() throws Exception{
        given(userService.findAllByVehicle_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/vehicle-id/{vehicle}"  , users.get(0).getVehicle())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByVehicle_numberTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByVehicle_number(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/vehicle-number/{vehicle}" , users.get(0).getVehicle())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByVehicle_numberInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByVehicle_number(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/vehicle-number/{vehicle}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByVehicle_numberNoMatchTest() throws Exception{
        given(userService.findAllByVehicle_number(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle-parking/vehicle-number/{vehicle}"  , users.get(0).getVehicle())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private VehicleParkingDTO getDTO(VehicleParking vehicleParking){
        return modelMapper.map(vehicleParking, VehicleParkingDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
