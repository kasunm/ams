package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.UserController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.UserDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainUserService;
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
 * <p>Title         : User API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for User. A User of apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, UserController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainUserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private UserRepository userRepository;
    //No war
    private List<User> users = new ArrayList<>();

      


    private final ModelMapper modelMapper = new ModelMapper();

    public UserAPITest(){


    }

    @Before
    public void initData(){
         
        users.add( new User(0L, "Sulaiman", "Omega", "e was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town", "ut", "in the Samnite south of Italy.[5] His home town, Venusia, la", "as ", "e ", "mber 65 ", UserRole.Tenant));
		users.add( new User(0L, "Ruwan", "Omega", "on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home", "o", "as born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His h", "He was bo", " ", "cember 6", UserRole.Tenant));
		users.add( new User(0L, "Kusal", "Supun", "er 65 BC[nb 4] in the Samnite sou", "e was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home tow", " 4] in the Samnite so", "wa", "He was bo", "8 ", UserRole.Tenant));
        try {
        userRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        User savedUser = userRepository.save(users.get(0));
        Assert.assertNotNull(savedUser);
        Assert.assertTrue(savedUser.getId() > 0);
        users.get(0).setId(savedUser.getId());
        savedUser = userRepository.save(users.get(1));
        Assert.assertNotNull(savedUser);
        Assert.assertTrue(savedUser.getId() > 0);
        users.get(1).setId(savedUser.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetUserByIDPresent() throws Exception{
        mockMvc.perform(get("/users/{id}",users.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(users.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetUserByIDNotPresent() throws Exception{
        mockMvc.perform(get("/users/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetUserByIDZero() throws Exception{
        mockMvc.perform(get("/users/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllUsers() throws Exception{
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveUserSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(users.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveUserExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(users.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/users/" + users.get(1).getId()));
    }


    @Test
    public void verifySaveUserFailValidation() throws Exception{
        User invalid =   new User(6L, "H", "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on", "He was born ", "He was born ", "He was born on 8 December 65 BC[", UserRole.Tenant);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveUserExistingNotFound() throws Exception{
        User nonExisting =   new User(5L, "Arun", "Supun", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, ", "in the Samnite south of I", "8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusi", "was b", "e was bo", "8 December", UserRole.Tenant);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", users.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserDTO getDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }

}
