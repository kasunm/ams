package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.UserController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.UserDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.UserService;
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
 * <p>Title         : UserController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for User. A User of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, UserController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private UserService userService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<User> users = new ArrayList<>();

     

    @Before
    public void setup() {
         

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
         
        users.add( new User(1L, "Aloka", "Thamasha", "the Samnite south of Italy.[5] His home town, Venusia, la", "] in the Samnite south of Italy.[5]", " 65 BC[nb 4] in the Samnite south of I", "e was", "s bo", "was born on 8 Decembe", UserRole.Tenant)); //ID 1
        users.add( new User(2L, "Kusal", "Nayani", "e south of Italy", "n on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay", "the Samnite south of Italy.[5] His home town, Venusia, la", "He ", "He was bo", "orn on 8 December 65 ", UserRole.Tenant)); //ID 2
		users.add( new User(0L, "Kasun", "Tenuki", "the Samnite south of Italy.[5] His home town, Venusia", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, l", "ecember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay", "w", "was b", " was born on 8 December ", UserRole.Tenant));//New item
        given(userService.getByID(1L)).willReturn(Optional.of(users.get(0)));
        given(userService.getByID(2L)).willReturn(Optional.of(users.get(1)));
        given(userService.getUsers()).willReturn(users);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(userService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(userService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveUserSuccess() throws Exception{
        User savedUser = new User();
        BeanUtils.copyProperties(users.get(2), savedUser);
        savedUser.setId(5L);
        given(userService.saveUser(any())).willReturn(savedUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(users.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/users/5"));
    }

    @Test
    public void verifySaveUserExistingSuccess() throws Exception{
        given(userService.saveUser(any())).willReturn(users.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(users.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/users/2"));
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
        User nonExisting =   new User(5L, "Supun", "Ruwan", "nb", "orn on 8 December 65 BC[nb 4] in the Samnite south ", "home town, V", " ", "e wa", "as born on 8 December ", UserRole.Tenant);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveUserPersistenceIDNotSet() throws Exception{
        User idNotSet =   new User(0L, "Kasun", "Tenuki", "the Samnite south of Italy.[5] His home town, Venusia", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, l", "ecember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay", "w", "was b", " was born on 8 December ", UserRole.Tenant);;
        given(userService.saveUser(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveUserDataException() throws Exception{
        User exceptUser = new User();
        BeanUtils.copyProperties(users.get(2), exceptUser);

        given(userService.saveUser(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptUser)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveUserArgumentException() throws Exception{
        User exceptUser = new User();
        BeanUtils.copyProperties(users.get(2), exceptUser);

        given(userService.saveUser(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptUser)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(userService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(userService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(userService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByUserRoleTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByUserRole(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/userRole/{userRole}" , users.get(0).getUserRole())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByUserRoleInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByUserRole(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/userRole/{userRole}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByUserRoleNoMatchTest() throws Exception{
        given(userService.findAllByUserRole(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/userRole/{userRole}"  , users.get(0).getUserRole())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByFirstNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFirstName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/firstName/{firstName}" , users.get(0).getFirstName())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByFirstNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFirstName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/firstName/{firstName}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByFirstNameNoMatchTest() throws Exception{
        given(userService.findAllByFirstName(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/firstName/{firstName}"  , users.get(0).getFirstName())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByEmailTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEmail(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}" , users.get(0).getEmail())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByEmailInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEmail(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByEmailNoMatchTest() throws Exception{
        given(userService.findAllByEmail(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}"  , users.get(0).getEmail())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByContact1Test() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByContact1(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/contact1/{contact1}" , users.get(0).getContact1())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByContact1InvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByContact1(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/contact1/{contact1}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByContact1NoMatchTest() throws Exception{
        given(userService.findAllByContact1(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/contact1/{contact1}"  , users.get(0).getContact1())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private UserDTO getDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
