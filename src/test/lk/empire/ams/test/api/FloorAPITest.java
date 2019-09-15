package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.FloorController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.FloorDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainFloorService;
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
 * <p>Title         : Floor API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Floor. A floor of an apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, FloorController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FloorAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainFloorService floorService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private FloorRepository floorRepository;
    //No war
    private List<Floor> floors = new ArrayList<>();

      	@Autowired
	ApartmentRepository apartmentRepository; 	private Apartment apartment0;
		private Apartment apartment1;
		private Apartment apartment2;
	


    private final ModelMapper modelMapper = new ModelMapper();

    public FloorAPITest(){


    }

    @Before
    public void initData(){
        apartment0 =   new Apartment(0L, "Nayani", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "5 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the ar");
	apartment1 =   new Apartment(0L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trad");
	Apartment savedapartment1 = apartmentRepository.save(apartment1);
	apartment1 = savedapartment1;
apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	 
        floors.add( new Floor(0L, "8 D", "Kusal", 764239, apartment1));
		floors.add( new Floor(0L, " was born ", "Supun", 156724, apartment1));
		floors.add( new Floor(0L, "s", "Aloka", 126689, apartment1));
        try {
        floorRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Floor savedFloor = floorRepository.save(floors.get(0));
        Assert.assertNotNull(savedFloor);
        Assert.assertTrue(savedFloor.getId() > 0);
        floors.get(0).setId(savedFloor.getId());
        savedFloor = floorRepository.save(floors.get(1));
        Assert.assertNotNull(savedFloor);
        Assert.assertTrue(savedFloor.getId() > 0);
        floors.get(1).setId(savedFloor.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetFloorByIDPresent() throws Exception{
        mockMvc.perform(get("/floors/{id}",floors.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(floors.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetFloorByIDNotPresent() throws Exception{
        mockMvc.perform(get("/floors/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetFloorByIDZero() throws Exception{
        mockMvc.perform(get("/floors/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllFloors() throws Exception{
        mockMvc.perform(get("/floors").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveFloorSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(floors.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveFloorExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(floors.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/floors/" + floors.get(1).getId()));
    }


    @Test
    public void verifySaveFloorFailValidation() throws Exception{
        Floor invalid =   new Floor(6L, "He was born on 8 Decem", "He was born on 8 Decem", 351966, apartment2);
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveFloorExistingNotFound() throws Exception{
        Floor nonExisting =   new Floor(5L, "on 8 D", "Aloka", 338251, apartment1);
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/floors/{id}", floors.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/floors/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/floors/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private FloorDTO getDTO(Floor floor){
        return modelMapper.map(floor, FloorDTO.class);
    }

}
