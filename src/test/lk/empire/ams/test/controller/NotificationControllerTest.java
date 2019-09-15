package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.NotificationController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.NotificationDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.NotificationService;
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
 * <p>Title         : NotificationController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for Notification. A Notification of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, NotificationController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class NotificationControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private NotificationService notificationService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<Notification> notifications = new ArrayList<>();

     

    @Before
    public void setup() {
         

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
         
        notifications.add( new Notification(1L, "h state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his", "Supun", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)))); //ID 1
        notifications.add( new Notification(2L, "shment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[", "Tenuki", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)))); //ID 2
		notifications.add( new Notification(0L, "reek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled t", "Kusal", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27))));//New item
        given(notificationService.getByID(1L)).willReturn(Optional.of(notifications.get(0)));
        given(notificationService.getByID(2L)).willReturn(Optional.of(notifications.get(1)));
        given(notificationService.getNotifications()).willReturn(notifications);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(notificationService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(notificationService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveNotificationSuccess() throws Exception{
        Notification savedNotification = new Notification();
        BeanUtils.copyProperties(notifications.get(2), savedNotification);
        savedNotification.setId(5L);
        given(notificationService.saveNotification(any())).willReturn(savedNotification);
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(notifications.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/notifications/5"));
    }

    @Test
    public void verifySaveNotificationExistingSuccess() throws Exception{
        given(notificationService.saveNotification(any())).willReturn(notifications.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(notifications.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/notifications/2"));
    }


    @Test
    public void verifySaveNotificationFailValidation() throws Exception{
        Notification invalid =   new Notification(6L, "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a reference to 'coactor argentar", "He was born on 8 Decem", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)));
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveNotificationExistingNotFound() throws Exception{
        Notification nonExisting =   new Notification(5L, "f his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by sch", "Sulaiman", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)));
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveNotificationPersistenceIDNotSet() throws Exception{
        Notification idNotSet =   new Notification(0L, "reek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled t", "Kusal", LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)));;
        given(notificationService.saveNotification(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveNotificationDataException() throws Exception{
        Notification exceptNotification = new Notification();
        BeanUtils.copyProperties(notifications.get(2), exceptNotification);

        given(notificationService.saveNotification(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptNotification)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveNotificationArgumentException() throws Exception{
        Notification exceptNotification = new Notification();
        BeanUtils.copyProperties(notifications.get(2), exceptNotification);

        given(notificationService.saveNotification(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptNotification)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(notificationService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(notificationService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(notificationService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByDescriptionTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDescription(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/description/{description}" , users.get(0).getDescription())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByDescriptionInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDescription(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/description/{description}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByDescriptionNoMatchTest() throws Exception{
        given(userService.findAllByDescription(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/description/{description}"  , users.get(0).getDescription())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private NotificationDTO getDTO(Notification notification){
        return modelMapper.map(notification, NotificationDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
