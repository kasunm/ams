package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.FloorController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.FloorDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.FloorService;
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
 * <p>Title         : FloorController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for Floor. A floor of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, FloorController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class FloorControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private FloorService floorService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<Floor> floors = new ArrayList<>();

     	private Apartment apartment0;
		private Apartment apartment1;
		private Apartment apartment2;
	

    @Before
    public void setup() {
        apartment0 =   new Apartment(0L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "rea and thi");
	apartment1 =   new Apartment(1L, "Kusal", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "th of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basili");
	apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	 

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        apartment0 =   new Apartment(0L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "rea and thi");
	apartment1 =   new Apartment(1L, "Kusal", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "th of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basili");
	apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	 
        floors.add( new Floor(1L, "e was born on", "Nayani", 311753, apartment1)); //ID 1
        floors.add( new Floor(2L, "was ", "Kusal", 441868, apartment1)); //ID 2
		floors.add( new Floor(0L, "was born on 8 ", "Arun", 527702, apartment1));//New item
        given(floorService.getByID(1L)).willReturn(Optional.of(floors.get(0)));
        given(floorService.getByID(2L)).willReturn(Optional.of(floors.get(1)));
        given(floorService.getFloors()).willReturn(floors);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/floors").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(floorService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(floorService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveFloorSuccess() throws Exception{
        Floor savedFloor = new Floor();
        BeanUtils.copyProperties(floors.get(2), savedFloor);
        savedFloor.setId(5L);
        given(floorService.saveFloor(any())).willReturn(savedFloor);
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(floors.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/floors/5"));
    }

    @Test
    public void verifySaveFloorExistingSuccess() throws Exception{
        given(floorService.saveFloor(any())).willReturn(floors.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(floors.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/floors/2"));
    }


    @Test
    public void verifySaveFloorFailValidation() throws Exception{
        Floor invalid =   new Floor(6L, "He was born on 8 Decem", "He was born on 8 Decem", 337436, apartment2);
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveFloorExistingNotFound() throws Exception{
        Floor nonExisting =   new Floor(5L, " on 8 ", "Kusal", 330135, apartment1);
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveFloorPersistenceIDNotSet() throws Exception{
        Floor idNotSet =   new Floor(0L, "was born on 8 ", "Arun", 527702, apartment1);;
        given(floorService.saveFloor(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveFloorDataException() throws Exception{
        Floor exceptFloor = new Floor();
        BeanUtils.copyProperties(floors.get(2), exceptFloor);

        given(floorService.saveFloor(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptFloor)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveFloorArgumentException() throws Exception{
        Floor exceptFloor = new Floor();
        BeanUtils.copyProperties(floors.get(2), exceptFloor);

        given(floorService.saveFloor(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/floors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptFloor)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(floorService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/floors/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(floorService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/floors/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/floors/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(floorService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/floors/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/name/{name}" , users.get(0).getName())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/name/{name}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByNameNoMatchTest() throws Exception{
        given(userService.findAllByName(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/name/{name}"  , users.get(0).getName())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByFloorNumberTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFloorNumber(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/floorNumber/{floorNumber}" , users.get(0).getFloorNumber())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByFloorNumberInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFloorNumber(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/floorNumber/{floorNumber}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByFloorNumberNoMatchTest() throws Exception{
        given(userService.findAllByFloorNumber(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/floorNumber/{floorNumber}"  , users.get(0).getFloorNumber())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByApartment_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByApartment_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/apartment/{apartment}" , users.get(0).getApartment())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByApartment_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByApartment_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/apartment/{apartment}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByApartment_IdNoMatchTest() throws Exception{
        given(userService.findAllByApartment_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/floors/apartment/{apartment}"  , users.get(0).getApartment())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private FloorDTO getDTO(Floor floor){
        return modelMapper.map(floor, FloorDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
