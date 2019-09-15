package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.AppEventController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.AppEventDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.AppEventService;
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
 * <p>Title         : AppEventController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for AppEvent. An event of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, AppEventController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class AppEventControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private AppEventService appEventService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<AppEvent> appEvents = new ArrayList<>();

     	private Apartment apartment0;
		private Apartment apartment1;
		private Apartment apartment2;
		private Client user0;
		private Client user1;
		private Client user2;
		private Employee employee0;
		private Employee employee1;
		private Employee employee2;
	

    @Before
    public void setup() {
        apartment0 =   new Apartment(0L, "Kusal", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "in the Samnite south ");
	apartment1 =   new Apartment(1L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps e");
	apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	user0 =   new Client(0L, "Sulaiman", "in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Itali", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	user1 =   new Client(1L, "Ruwan", "te south of Italy.[5] His home town, Venu", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	user2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	employee0 =   new Employee(0L, new ArrayList<AppEvent>());
	employee1 =   new Employee(1L, new ArrayList<AppEvent>());
	employee2 =   new Employee(2L, new ArrayList<AppEvent>());
	 

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        apartment0 =   new Apartment(0L, "Kusal", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "in the Samnite south ");
	apartment1 =   new Apartment(1L, "Omega", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps e");
	apartment2 =   new Apartment(2L, "H", new ArrayList<Maintenance>(), new ArrayList<Floor>(), new ArrayList<AppEvent>(), "null");
	user0 =   new Client(0L, "Sulaiman", "in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Itali", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	user1 =   new Client(1L, "Ruwan", "te south of Italy.[5] His home town, Venu", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	user2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	employee0 =   new Employee(0L, new ArrayList<AppEvent>());
	employee1 =   new Employee(1L, new ArrayList<AppEvent>());
	employee2 =   new Employee(2L, new ArrayList<AppEvent>());
	 
        appEvents.add( new AppEvent(1L, "Arun", 471621, "n the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a referen", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), " south of Italy.[5] His home tow", "trade route in the border region between Ap", apartment1, user1, employee1)); //ID 1
        appEvents.add( new AppEvent(2L, "Ruwan", 660535, "he third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his free", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, l", "home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Vari", apartment1, user1, employee1)); //ID 2
		appEvents.add( new AppEvent(0L, "Sumudu", 694772, "nusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his chil", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "He was born on 8 December 65 BC[nb 4] in ", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata", apartment1, user1, employee1));//New item
        given(appEventService.getByID(1L)).willReturn(Optional.of(appEvents.get(0)));
        given(appEventService.getByID(2L)).willReturn(Optional.of(appEvents.get(1)));
        given(appEventService.getAppEvents()).willReturn(appEvents);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/events/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(appEventService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/events/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(appEventService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/events/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveAppEventSuccess() throws Exception{
        AppEvent savedAppEvent = new AppEvent();
        BeanUtils.copyProperties(appEvents.get(2), savedAppEvent);
        savedAppEvent.setId(5L);
        given(appEventService.saveAppEvent(any())).willReturn(savedAppEvent);
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(appEvents.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/events/5"));
    }

    @Test
    public void verifySaveAppEventExistingSuccess() throws Exception{
        given(appEventService.saveAppEvent(any())).willReturn(appEvents.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(appEvents.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/events/2"));
    }


    @Test
    public void verifySaveAppEventFailValidation() throws Exception{
        AppEvent invalid =   new AppEvent(6L, "H", 48645, "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a reference to 'coactor argentar", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", apartment2, user2, employee2);
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveAppEventExistingNotFound() throws Exception{
        AppEvent nonExisting =   new AppEvent(5L, "Aloka", 501561, "Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Hor", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "orn on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town", "s home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic ", apartment1, user1, employee1);
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveAppEventPersistenceIDNotSet() throws Exception{
        AppEvent idNotSet =   new AppEvent(0L, "Sumudu", 694772, "nusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his chil", EventStatus.Approved, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), LocalTime.of((int)(Math.random() * 23),(int)(Math.random() * 59),(int)(Math.random() * 59),0), "He was born on 8 December 65 BC[nb 4] in ", " on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata", apartment1, user1, employee1);;
        given(appEventService.saveAppEvent(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveAppEventDataException() throws Exception{
        AppEvent exceptAppEvent = new AppEvent();
        BeanUtils.copyProperties(appEvents.get(2), exceptAppEvent);

        given(appEventService.saveAppEvent(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptAppEvent)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveAppEventArgumentException() throws Exception{
        AppEvent exceptAppEvent = new AppEvent();
        BeanUtils.copyProperties(appEvents.get(2), exceptAppEvent);

        given(appEventService.saveAppEvent(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptAppEvent)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(appEventService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(appEventService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(appEventService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/name/{name}" , users.get(0).getName())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/name/{name}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByNameNoMatchTest() throws Exception{
        given(userService.findAllByName(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/name/{name}"  , users.get(0).getName())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByDateTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDate(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/date/{date}" , users.get(0).getDate())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByDateInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDate(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/date/{date}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByDateNoMatchTest() throws Exception{
        given(userService.findAllByDate(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/date/{date}"  , users.get(0).getDate())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByEventTypeTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEventType(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/eventType/{eventType}" , users.get(0).getEventType())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByEventTypeInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEventType(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/eventType/{eventType}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByEventTypeNoMatchTest() throws Exception{
        given(userService.findAllByEventType(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/eventType/{eventType}"  , users.get(0).getEventType())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByApartment_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByApartment_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/apartment/{apartment}" , users.get(0).getApartment())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByApartment_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByApartment_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/apartment/{apartment}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByApartment_IdNoMatchTest() throws Exception{
        given(userService.findAllByApartment_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/apartment/{apartment}"  , users.get(0).getApartment())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByUser_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByUser_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/user/{user}" , users.get(0).getUser())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByUser_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByUser_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/user/{user}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByUser_IdNoMatchTest() throws Exception{
        given(userService.findAllByUser_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/user/{user}"  , users.get(0).getUser())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByEmployee_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEmployee_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/employee/{employee}" , users.get(0).getEmployee())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByEmployee_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByEmployee_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/employee/{employee}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByEmployee_IdNoMatchTest() throws Exception{
        given(userService.findAllByEmployee_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/employee/{employee}"  , users.get(0).getEmployee())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private AppEventDTO getDTO(AppEvent appEvent){
        return modelMapper.map(appEvent, AppEventDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
