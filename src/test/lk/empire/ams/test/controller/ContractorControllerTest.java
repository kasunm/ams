package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.ContractorController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.ContractorDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.ContractorService;
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
 * <p>Title         : ContractorController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for Contractor. A Contractor of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, ContractorController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class ContractorControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private ContractorService contractorService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<Contractor> contractors = new ArrayList<>();

     

    @Before
    public void setup() {
         

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
         
        contractors.add( new Contractor(1L, "Arun", new ArrayList<Maintenance>())); //ID 1
        contractors.add( new Contractor(2L, "Aloka", new ArrayList<Maintenance>())); //ID 2
		contractors.add( new Contractor(0L, "Tenuki", new ArrayList<Maintenance>()));//New item
        given(contractorService.getByID(1L)).willReturn(Optional.of(contractors.get(0)));
        given(contractorService.getByID(2L)).willReturn(Optional.of(contractors.get(1)));
        given(contractorService.getContractors()).willReturn(contractors);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(contractorService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(contractorService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveContractorSuccess() throws Exception{
        Contractor savedContractor = new Contractor();
        BeanUtils.copyProperties(contractors.get(2), savedContractor);
        savedContractor.setId(5L);
        given(contractorService.saveContractor(any())).willReturn(savedContractor);
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(contractors.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/contractors/5"));
    }

    @Test
    public void verifySaveContractorExistingSuccess() throws Exception{
        given(contractorService.saveContractor(any())).willReturn(contractors.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(contractors.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/contractors/2"));
    }


    @Test
    public void verifySaveContractorFailValidation() throws Exception{
        Contractor invalid =   new Contractor(6L, "He was born on 8 December 65 BC[", new ArrayList<Maintenance>());
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveContractorExistingNotFound() throws Exception{
        Contractor nonExisting =   new Contractor(5L, "Sumudu", new ArrayList<Maintenance>());
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveContractorPersistenceIDNotSet() throws Exception{
        Contractor idNotSet =   new Contractor(0L, "Tenuki", new ArrayList<Maintenance>());;
        given(contractorService.saveContractor(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveContractorDataException() throws Exception{
        Contractor exceptContractor = new Contractor();
        BeanUtils.copyProperties(contractors.get(2), exceptContractor);

        given(contractorService.saveContractor(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptContractor)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveContractorArgumentException() throws Exception{
        Contractor exceptContractor = new Contractor();
        BeanUtils.copyProperties(contractors.get(2), exceptContractor);

        given(contractorService.saveContractor(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptContractor)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(contractorService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/contractors/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(contractorService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/contractors/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/contractors/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(contractorService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/contractors/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByCompanyNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByCompanyName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/companyName/{companyName}" , users.get(0).getCompanyName())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByCompanyNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByCompanyName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/companyName/{companyName}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByCompanyNameNoMatchTest() throws Exception{
        given(userService.findAllByCompanyName(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/companyName/{companyName}"  , users.get(0).getCompanyName())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByFirstNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFirstName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/firstName/{firstName}" , users.get(0).getFirstName())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByFirstNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFirstName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/firstName/{firstName}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByFirstNameNoMatchTest() throws Exception{
        given(userService.findAllByFirstName(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/firstName/{firstName}"  , users.get(0).getFirstName())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByEmailTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEmail(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/email/{email}" , users.get(0).getEmail())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByEmailInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEmail(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/email/{email}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByEmailNoMatchTest() throws Exception{
        given(userService.findAllByEmail(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/email/{email}"  , users.get(0).getEmail())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByNicTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByNic(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/nic/{nic}" , users.get(0).getNic())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByNicInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByNic(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/nic/{nic}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByNicNoMatchTest() throws Exception{
        given(userService.findAllByNic(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/contractors/nic/{nic}"  , users.get(0).getNic())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private ContractorDTO getDTO(Contractor contractor){
        return modelMapper.map(contractor, ContractorDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
