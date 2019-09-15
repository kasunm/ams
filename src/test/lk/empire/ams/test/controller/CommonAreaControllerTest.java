package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.CommonAreaController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.CommonAreaDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.CommonAreaService;
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
 * <p>Title         : CommonAreaController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, CommonAreaController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class CommonAreaControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private CommonAreaService commonAreaService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<CommonArea> commonAreas = new ArrayList<>();

     	private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @Before
    public void setup() {
        floor0 =   new Floor(0L, "e was born on", "Aloka", 438136, apartment1);
	floor1 =   new Floor(1L, " bo", "Nayani", 175239, apartment1);
	floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 180557, apartment2);
	 

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        floor0 =   new Floor(0L, "e was born on", "Aloka", 438136, apartment1);
	floor1 =   new Floor(1L, " bo", "Nayani", 175239, apartment1);
	floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 180557, apartment2);
	 
        commonAreas.add( new CommonArea(1L, " town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references t", " 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this per", " December 65 BC[nb 4] i", Availability.Available, floor1)); //ID 1
        commonAreas.add( new CommonArea(2L, "and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly h", " December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in ", " born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Ve", Availability.Available, floor1)); //ID 2
		commonAreas.add( new CommonArea(0L, "e at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholi", "mber 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, la", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home ", Availability.Available, floor1));//New item
        given(commonAreaService.getByID(1L)).willReturn(Optional.of(commonAreas.get(0)));
        given(commonAreaService.getByID(2L)).willReturn(Optional.of(commonAreas.get(1)));
        given(commonAreaService.getCommonAreas()).willReturn(commonAreas);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(commonAreaService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(commonAreaService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveCommonAreaSuccess() throws Exception{
        CommonArea savedCommonArea = new CommonArea();
        BeanUtils.copyProperties(commonAreas.get(2), savedCommonArea);
        savedCommonArea.setId(5L);
        given(commonAreaService.saveCommonArea(any())).willReturn(savedCommonArea);
        mockMvc.perform(MockMvcRequestBuilders.post("/commonAreas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(commonAreas.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/commonAreas/5"));
    }

    @Test
    public void verifySaveCommonAreaExistingSuccess() throws Exception{
        given(commonAreaService.saveCommonArea(any())).willReturn(commonAreas.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/commonAreas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(commonAreas.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/commonAreas/2"));
    }


    @Test
    public void verifySaveCommonAreaFailValidation() throws Exception{
        CommonArea invalid =   new CommonArea(6L, "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a reference to 'coactor argentar", "te in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken ", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on", Availability.Available, floor2);
        mockMvc.perform(MockMvcRequestBuilders.post("/commonAreas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveCommonAreaExistingNotFound() throws Exception{
        CommonArea nonExisting =   new CommonArea(5L, "ts use by Horace[16] was expl", "ecember 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilic", "8 December 65 BC[nb 4] in the ", Availability.Available, floor1);
        mockMvc.perform(MockMvcRequestBuilders.post("/commonAreas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveCommonAreaPersistenceIDNotSet() throws Exception{
        CommonArea idNotSet =   new CommonArea(0L, "e at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholi", "mber 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, la", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home ", Availability.Available, floor1);;
        given(commonAreaService.saveCommonArea(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/commonAreas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveCommonAreaDataException() throws Exception{
        CommonArea exceptCommonArea = new CommonArea();
        BeanUtils.copyProperties(commonAreas.get(2), exceptCommonArea);

        given(commonAreaService.saveCommonArea(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/commonAreas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptCommonArea)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveCommonAreaArgumentException() throws Exception{
        CommonArea exceptCommonArea = new CommonArea();
        BeanUtils.copyProperties(commonAreas.get(2), exceptCommonArea);

        given(commonAreaService.saveCommonArea(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/commonAreas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptCommonArea)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(commonAreaService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/commonAreas/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(commonAreaService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/commonAreas/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/commonAreas/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(commonAreaService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/commonAreas/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByTypeTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByType(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/type/{type}" , users.get(0).getType())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByTypeInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByType(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/type/{type}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByTypeNoMatchTest() throws Exception{
        given(userService.findAllByType(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/type/{type}"  , users.get(0).getType())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByAvailabilityTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByAvailability(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/availability/{availability}" , users.get(0).getAvailability())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByAvailabilityInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByAvailability(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/availability/{availability}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByAvailabilityNoMatchTest() throws Exception{
        given(userService.findAllByAvailability(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/availability/{availability}"  , users.get(0).getAvailability())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByFloor_NameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFloor_Name(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/floor/{floor}" , users.get(0).getFloor())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByFloor_NameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByFloor_Name(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/floor/{floor}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByFloor_NameNoMatchTest() throws Exception{
        given(userService.findAllByFloor_Name(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/commonAreas/floor/{floor}"  , users.get(0).getFloor())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private CommonAreaDTO getDTO(CommonArea commonArea){
        return modelMapper.map(commonArea, CommonAreaDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
