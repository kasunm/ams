package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.FeatureController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.FeatureDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainFeatureService;
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
 * <p>Title         : Feature API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Feature. A Feature of an apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, FeatureController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FeatureAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainFeatureService featureService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private FeatureRepository featureRepository;
    //No war
    private List<Feature> features = new ArrayList<>();

      


    private final ModelMapper modelMapper = new ModelMapper();

    public FeatureAPITest(){


    }

    @Before
    public void initData(){
         
        features.add( new Feature(0L, "Nuwan", " 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in", "a, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects wer"));
		features.add( new Feature(0L, "Sumudu", "talic dialects were spoken in the area and this perhaps en", "4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region bet"));
		features.add( new Feature(0L, "Tenuki", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps en", "was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enr"));
        try {
        featureRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Feature savedFeature = featureRepository.save(features.get(0));
        Assert.assertNotNull(savedFeature);
        Assert.assertTrue(savedFeature.getId() > 0);
        features.get(0).setId(savedFeature.getId());
        savedFeature = featureRepository.save(features.get(1));
        Assert.assertNotNull(savedFeature);
        Assert.assertTrue(savedFeature.getId() > 0);
        features.get(1).setId(savedFeature.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetFeatureByIDPresent() throws Exception{
        mockMvc.perform(get("/features/{id}",features.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(features.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetFeatureByIDNotPresent() throws Exception{
        mockMvc.perform(get("/features/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetFeatureByIDZero() throws Exception{
        mockMvc.perform(get("/features/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllFeatures() throws Exception{
        mockMvc.perform(get("/features").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveFeatureSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(features.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveFeatureExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(features.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/features/" + features.get(1).getId()));
    }


    @Test
    public void verifySaveFeatureFailValidation() throws Exception{
        Feature invalid =   new Feature(6L, "He was born on 8 December 65 BC[nb 4] in the Samnite south of ", "n 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the a", "nb 4] in the Sam");
        mockMvc.perform(MockMvcRequestBuilders.post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveFeatureExistingNotFound() throws Exception{
        Feature nonExisting =   new Feature(5L, "Aloka", " of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spo", " Various Italic dialects were spo");
        mockMvc.perform(MockMvcRequestBuilders.post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/features/{id}", features.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/features/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/features/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private FeatureDTO getDTO(Feature feature){
        return modelMapper.map(feature, FeatureDTO.class);
    }

}
