package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.VehicleParkingController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.VehicleParkingDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainVehicleParkingService;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;



/**
 * <p>Title         : VehicleParking API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for VehicleParking. A parking duration of vehicle
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, VehicleParkingController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VehicleParkingAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainVehicleParkingService vehicleParkingService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private VehicleParkingRepository vehicleParkingRepository;
    //No war
    private List<VehicleParking> vehicleParkings = new ArrayList<>();

      	@Autowired
	VehicleRepository vehicleRepository; 	private Vehicle vehicle0;
		private Vehicle vehicle1;
		private Vehicle vehicle2;
		@Autowired
	ParkingSlotRepository parkingSlotRepository; 	private ParkingSlot parkingSlot0;
		private ParkingSlot parkingSlot1;
		private ParkingSlot parkingSlot2;
	


    private final ModelMapper modelMapper = new ModelMapper();

    public VehicleParkingAPITest(){


    }

    @Before
    public void initData(){
        vehicle0 =   new Vehicle(0L, "x00703664", unit1, new HasSet <VehicleParking>(), "as born on 8 December 65 ", "born on 8 December 65 BC[nb 4] in the Samn");
	vehicle1 =   new Vehicle(0L, "j050", unit1, new HasSet <VehicleParking>(), " 6", "H");
	Vehicle savedvehicle1 = vehicleRepository.save(vehicle1);
	vehicle1 = savedvehicle1;
vehicle2 =   new Vehicle(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born on 8 December 65 BC[", "He was born on 8 December 65 BC[nb 4] in the Samnite");
	parkingSlot0 =   new ParkingSlot(0L, "Supun", unit1, new HasSet <VehicleParking>(), "m57", Availability.Available);
	parkingSlot1 =   new ParkingSlot(0L, "Tenuki", unit1, new HasSet <VehicleParking>(), "j0", Availability.Available);
	ParkingSlot savedparkingSlot1 = parkingSlotRepository.save(parkingSlot1);
	parkingSlot1 = savedparkingSlot1;
parkingSlot2 =   new ParkingSlot(2L, "H", unit2, new HasSet <VehicleParking>(), "He was born ", Availability.Available);
	 
        vehicleParkings.add( new VehicleParking(0L, "Sumudu", "orn on 8 Decem", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, " born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in"));
		vehicleParkings.add( new VehicleParking(0L, "Kasun", " was born ", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "nite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of"));
		vehicleParkings.add( new VehicleParking(0L, "Nuwan", " born on 8", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "amnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was th"));
        try {
        vehicleParkingRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        VehicleParking savedVehicleParking = vehicleParkingRepository.save(vehicleParkings.get(0));
        Assert.assertNotNull(savedVehicleParking);
        Assert.assertTrue(savedVehicleParking.getId() > 0);
        vehicleParkings.get(0).setId(savedVehicleParking.getId());
        savedVehicleParking = vehicleParkingRepository.save(vehicleParkings.get(1));
        Assert.assertNotNull(savedVehicleParking);
        Assert.assertTrue(savedVehicleParking.getId() > 0);
        vehicleParkings.get(1).setId(savedVehicleParking.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetVehicleParkingByIDPresent() throws Exception{
        mockMvc.perform(get("/vehicle-parking/{id}",vehicleParkings.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(vehicleParkings.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetVehicleParkingByIDNotPresent() throws Exception{
        mockMvc.perform(get("/vehicle-parking/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetVehicleParkingByIDZero() throws Exception{
        mockMvc.perform(get("/vehicle-parking/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllVehicleParkings() throws Exception{
        mockMvc.perform(get("/vehicle-parking").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveVehicleParkingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(vehicleParkings.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveVehicleParkingExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(vehicleParkings.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/vehicle-parking/" + vehicleParkings.get(1).getId()));
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
        VehicleParking nonExisting =   new VehicleParking(5L, "Ruwan", " was born on 8", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), vehicle1, parkingSlot1, "n at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was t");
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle-parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle-parking/{id}", vehicleParkings.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle-parking/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle-parking/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VehicleParkingDTO getDTO(VehicleParking vehicleParking){
        return modelMapper.map(vehicleParking, VehicleParkingDTO.class);
    }

}
