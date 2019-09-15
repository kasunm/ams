package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.ContractorController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.ContractorDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainContractorService;
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
 * <p>Title         : Contractor API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Contractor. A Contractor of apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, ContractorController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContractorAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainContractorService contractorService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private ContractorRepository contractorRepository;
    //No war
    private List<Contractor> contractors = new ArrayList<>();

      


    private final ModelMapper modelMapper = new ModelMapper();

    public ContractorAPITest(){


    }

    @Before
    public void initData(){
         
        contractors.add( new Contractor(0L, "Nuwan", new ArrayList<Maintenance>()));
		contractors.add( new Contractor(0L, "Arun", new ArrayList<Maintenance>()));
		contractors.add( new Contractor(0L, "Kusal", new ArrayList<Maintenance>()));
        try {
        contractorRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Contractor savedContractor = contractorRepository.save(contractors.get(0));
        Assert.assertNotNull(savedContractor);
        Assert.assertTrue(savedContractor.getId() > 0);
        contractors.get(0).setId(savedContractor.getId());
        savedContractor = contractorRepository.save(contractors.get(1));
        Assert.assertNotNull(savedContractor);
        Assert.assertTrue(savedContractor.getId() > 0);
        contractors.get(1).setId(savedContractor.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetContractorByIDPresent() throws Exception{
        mockMvc.perform(get("/contractors/{id}",contractors.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(contractors.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetContractorByIDNotPresent() throws Exception{
        mockMvc.perform(get("/contractors/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetContractorByIDZero() throws Exception{
        mockMvc.perform(get("/contractors/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllContractors() throws Exception{
        mockMvc.perform(get("/contractors").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveContractorSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(contractors.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveContractorExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(contractors.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/contractors/" + contractors.get(1).getId()));
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
        Contractor nonExisting =   new Contractor(5L, "Kasun", new ArrayList<Maintenance>());
        mockMvc.perform(MockMvcRequestBuilders.post("/contractors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/contractors/{id}", contractors.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/contractors/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/contractors/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ContractorDTO getDTO(Contractor contractor){
        return modelMapper.map(contractor, ContractorDTO.class);
    }

}
