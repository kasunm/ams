package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.ParkingSlotController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.ParkingSlotDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainParkingSlotService;
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
 * <p>Title         : ParkingSlot API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for ParkingSlot. A parking slot
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, ParkingSlotController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ParkingSlotAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainParkingSlotService parkingSlotService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    //No war
    private List<ParkingSlot> parkingSlots = new ArrayList<>();

      	@Autowired
	UnitRepository unitRepository; 	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
	


    private final ModelMapper modelMapper = new ModelMapper();

    public ParkingSlotAPITest(){


    }

    @Before
    public void initData(){
        unit0 =   new Unit(0L, "Arun", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(0L, "Nuwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	Unit savedunit1 = unitRepository.save(unit1);
	unit1 = savedunit1;
unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 
        parkingSlots.add( new ParkingSlot(0L, "Aloka", unit1, new HasSet <VehicleParking>(), "p", Availability.Available));
		parkingSlots.add( new ParkingSlot(0L, "Supun", unit1, new HasSet <VehicleParking>(), "o203", Availability.Available));
		parkingSlots.add( new ParkingSlot(0L, "Ruwan", unit1, new HasSet <VehicleParking>(), "n7855", Availability.Available));
        try {
        parkingSlotRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        ParkingSlot savedParkingSlot = parkingSlotRepository.save(parkingSlots.get(0));
        Assert.assertNotNull(savedParkingSlot);
        Assert.assertTrue(savedParkingSlot.getId() > 0);
        parkingSlots.get(0).setId(savedParkingSlot.getId());
        savedParkingSlot = parkingSlotRepository.save(parkingSlots.get(1));
        Assert.assertNotNull(savedParkingSlot);
        Assert.assertTrue(savedParkingSlot.getId() > 0);
        parkingSlots.get(1).setId(savedParkingSlot.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetParkingSlotByIDPresent() throws Exception{
        mockMvc.perform(get("/parkingslots/{id}",parkingSlots.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(parkingSlots.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetParkingSlotByIDNotPresent() throws Exception{
        mockMvc.perform(get("/parkingslots/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetParkingSlotByIDZero() throws Exception{
        mockMvc.perform(get("/parkingslots/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllParkingSlots() throws Exception{
        mockMvc.perform(get("/parkingslots").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveParkingSlotSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(parkingSlots.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveParkingSlotExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(parkingSlots.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/parkingslots/" + parkingSlots.get(1).getId()));
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
        ParkingSlot nonExisting =   new ParkingSlot(5L, "Supun", unit1, new HasSet <VehicleParking>(), "j2044000", Availability.Available);
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingslots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/parkingslots/{id}", parkingSlots.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/parkingslots/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/parkingslots/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ParkingSlotDTO getDTO(ParkingSlot parkingSlot){
        return modelMapper.map(parkingSlot, ParkingSlotDTO.class);
    }

}
