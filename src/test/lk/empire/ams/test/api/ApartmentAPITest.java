package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.ApartmentController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.ApartmentDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainApartmentService;
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
 * <p>Title         : Apartment API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Apartment. An apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, ApartmentController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApartmentAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainApartmentService apartmentService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private ApartmentRepository apartmentRepository;
    //No war
    private List<Apartment> apartments = new ArrayList<>();

      


    private final ModelMapper modelMapper = new ModelMapper();

    public ApartmentAPITest(){


    }

    @Before
    public void initData(){
         
        apartments.add( new Apartment(0L, "Aloka", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "e Samnit"));
		apartments.add( new Apartment(0L, "Tenuki", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps"));
		apartments.add( new Apartment(0L, "Nayani", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basi"));
        try {
        apartmentRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Apartment savedApartment = apartmentRepository.save(apartments.get(0));
        Assert.assertNotNull(savedApartment);
        Assert.assertTrue(savedApartment.getId() > 0);
        apartments.get(0).setId(savedApartment.getId());
        savedApartment = apartmentRepository.save(apartments.get(1));
        Assert.assertNotNull(savedApartment);
        Assert.assertTrue(savedApartment.getId() > 0);
        apartments.get(1).setId(savedApartment.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetApartmentByIDPresent() throws Exception{
        mockMvc.perform(get("/apartments/{id}",apartments.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(apartments.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetApartmentByIDNotPresent() throws Exception{
        mockMvc.perform(get("/apartments/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetApartmentByIDZero() throws Exception{
        mockMvc.perform(get("/apartments/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllApartments() throws Exception{
        mockMvc.perform(get("/apartments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveApartmentSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(apartments.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveApartmentExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(apartments.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/apartments/" + apartments.get(1).getId()));
    }


    @Test
    public void verifySaveApartmentFailValidation() throws Exception{
        Apartment invalid =   new Apartment(6L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
        mockMvc.perform(MockMvcRequestBuilders.post("/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveApartmentExistingNotFound() throws Exception{
        Apartment nonExisting =   new Apartment(5L, "Thamasha", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "n on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] ");
        mockMvc.perform(MockMvcRequestBuilders.post("/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/apartments/{id}", apartments.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/apartments/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/apartments/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ApartmentDTO getDTO(Apartment apartment){
        return modelMapper.map(apartment, ApartmentDTO.class);
    }

}
