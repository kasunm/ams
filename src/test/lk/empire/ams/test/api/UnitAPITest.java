package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.UnitController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.UnitDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainUnitService;
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
 * <p>Title         : Unit API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Unit. A Unit of of an apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, UnitController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UnitAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainUnitService unitsService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private UnitRepository unitsRepository;
    //No war
    private List<Unit> unitss = new ArrayList<>();

      	@Autowired
	ClientRepository clientRepository; 	private Client owner0;
		private Client owner1;
		private Client owner2;
		@Autowired
	ClientRepository clientRepository; 	private Client renter0;
		private Client renter1;
		private Client renter2;
		@Autowired
	FloorRepository floorRepository; 	private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	


    private final ModelMapper modelMapper = new ModelMapper();

    public UnitAPITest(){


    }

    @Before
    public void initData(){
        owner0 =   new Client(0L, "Sumudu", "born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	owner1 =   new Client(0L, "Kusal", "5 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Bas", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client savedowner1 = clientRepository.save(owner1);
	owner1 = savedowner1;
owner2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	renter0 =   new Client(0L, "Omega", " on a trade route in the border region between Apulia and Lucania (Basilicata). Various It", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	renter1 =   new Client(0L, "Arun", "as born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). ", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client savedrenter1 = clientRepository.save(renter1);
	renter1 = savedrenter1;
renter2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	floor0 =   new Floor(0L, "e was born on 8 ", "Nuwan", 43989, apartment1);
	floor1 =   new Floor(0L, "He", "Arun", 371098, apartment1);
	Floor savedfloor1 = floorRepository.save(floor1);
	floor1 = savedfloor1;
floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 671848, apartment2);
	 
        unitss.add( new Unit(0L, "Nayani", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available));
		unitss.add( new Unit(0L, "Kusal", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available));
		unitss.add( new Unit(0L, "Sumudu", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available));
        try {
        unitsRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Unit savedUnit = unitsRepository.save(unitss.get(0));
        Assert.assertNotNull(savedUnit);
        Assert.assertTrue(savedUnit.getId() > 0);
        unitss.get(0).setId(savedUnit.getId());
        savedUnit = unitsRepository.save(unitss.get(1));
        Assert.assertNotNull(savedUnit);
        Assert.assertTrue(savedUnit.getId() > 0);
        unitss.get(1).setId(savedUnit.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetUnitByIDPresent() throws Exception{
        mockMvc.perform(get("/units/{id}",unitss.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(unitss.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetUnitByIDNotPresent() throws Exception{
        mockMvc.perform(get("/units/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetUnitByIDZero() throws Exception{
        mockMvc.perform(get("/units/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllUnits() throws Exception{
        mockMvc.perform(get("/units").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveUnitSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(unitss.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveUnitExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(unitss.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/units/" + unitss.get(1).getId()));
    }


    @Test
    public void verifySaveUnitFailValidation() throws Exception{
        Unit invalid =   new Unit(6L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
        mockMvc.perform(MockMvcRequestBuilders.post("/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveUnitExistingNotFound() throws Exception{
        Unit nonExisting =   new Unit(5L, "Sulaiman", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
        mockMvc.perform(MockMvcRequestBuilders.post("/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/units/{id}", unitss.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/units/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/units/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UnitDTO getDTO(Unit units){
        return modelMapper.map(units, UnitDTO.class);
    }

}
