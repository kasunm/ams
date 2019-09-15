package lk.empire.ams.test.controller;

import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.MaintenanceController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.MaintenanceDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MaintenanceService;
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
 * <p>Title         : MaintenanceController unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller unit test class for Maintenance. An Maintenance for the apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, MaintenanceController.class})
@Import(CustomizedResponseEntityExceptionHandler.class)
@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class MaintenanceControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @MockBean
    private MaintenanceService maintenanceService;

    private final ModelMapper modelMapper = new ModelMapper();

    private List<Maintenance> maintenances = new ArrayList<>();

     	private Contractor contractor0;
		private Contractor contractor1;
		private Contractor contractor2;
		private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @Before
    public void setup() {
        contractor0 =   new Contractor(0L, "Sulaiman", new ArrayList<Maintenance>());
	contractor1 =   new Contractor(1L, "Ruwan", new ArrayList<Maintenance>());
	contractor2 =   new Contractor(2L, "He was born on 8 December 65 BC[", new ArrayList<Maintenance>());
	floor0 =   new Floor(0L, " ", "Sulaiman", 337460, apartment1);
	floor1 =   new Floor(1L, "orn on 8 De", "Nayani", 10512, apartment1);
	floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 789122, apartment2);
	 

        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(exceptionHandler) .build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        contractor0 =   new Contractor(0L, "Sulaiman", new ArrayList<Maintenance>());
	contractor1 =   new Contractor(1L, "Ruwan", new ArrayList<Maintenance>());
	contractor2 =   new Contractor(2L, "He was born on 8 December 65 BC[", new ArrayList<Maintenance>());
	floor0 =   new Floor(0L, " ", "Sulaiman", 337460, apartment1);
	floor1 =   new Floor(1L, "orn on 8 De", "Nayani", 10512, apartment1);
	floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 789122, apartment2);
	 
        maintenances.add( new Maintenance(1L, "en familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition r", "Nayani", " ", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1)); //ID 1
        maintenances.add( new Maintenance(2L, "ave been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself ", "Nayani", "He was born on 8 December 65 BC[nb 4] in the S", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1)); //ID 2
		maintenances.add( new Maintenance(0L, "ponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success", "Ruwan", "8 December 65 BC[nb 4] in the S", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1));//New item
        given(maintenanceService.getByID(1L)).willReturn(Optional.of(maintenances.get(0)));
        given(maintenanceService.getByID(2L)).willReturn(Optional.of(maintenances.get(1)));
        given(maintenanceService.getMaintenances()).willReturn(maintenances);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Test
    public void verifyGetAll() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void verifyGetByIDMatch() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void verifyGetByIDNotFound() throws Exception{
        given(maintenanceService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/{id}", 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyGetByIDArgumentError() throws Exception{
        given(maintenanceService.getByID(100L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/{id}", "NotID").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveMaintenanceSuccess() throws Exception{
        Maintenance savedMaintenance = new Maintenance();
        BeanUtils.copyProperties(maintenances.get(2), savedMaintenance);
        savedMaintenance.setId(5L);
        given(maintenanceService.saveMaintenance(any())).willReturn(savedMaintenance);
        mockMvc.perform(MockMvcRequestBuilders.post("/maintenance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(maintenances.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/maintenance/5"));
    }

    @Test
    public void verifySaveMaintenanceExistingSuccess() throws Exception{
        given(maintenanceService.saveMaintenance(any())).willReturn(maintenances.get(1));
        mockMvc.perform(MockMvcRequestBuilders.post("/maintenance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(maintenances.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/maintenance/2"));
    }


    @Test
    public void verifySaveMaintenanceFailValidation() throws Exception{
        Maintenance invalid =   new Maintenance(6L, "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholia as a reference to 'coactor argentar", "He was born on 8 December 65 BC[nb 4] in the Samnite", "He was born on 8 December 65 BC[nb 4] in the Samnite", MaintenanceType.CommonArea, MaintenanceStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor2, floor2);
        mockMvc.perform(MockMvcRequestBuilders.post("/maintenance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveMaintenanceExistingNotFound() throws Exception{
        Maintenance nonExisting =   new Maintenance(5L, "c variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed", "Arun", " born on 8 Dece", MaintenanceType.CommonArea, MaintenanceStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1);
        mockMvc.perform(MockMvcRequestBuilders.post("/maintenance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifySaveMaintenancePersistenceIDNotSet() throws Exception{
        Maintenance idNotSet =   new Maintenance(0L, "ponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success", "Ruwan", "8 December 65 BC[nb 4] in the S", MaintenanceType.CommonArea, MaintenanceStatus.Ongoing, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), contractor1, floor1);;
        given(maintenanceService.saveMaintenance(any())).willReturn(idNotSet);
        mockMvc.perform(MockMvcRequestBuilders.post("/maintenance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(idNotSet)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void verifySaveMaintenanceDataException() throws Exception{
        Maintenance exceptMaintenance = new Maintenance();
        BeanUtils.copyProperties(maintenances.get(2), exceptMaintenance);

        given(maintenanceService.saveMaintenance(any())).willThrow(new DataAccessException("Unable to save") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/maintenance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptMaintenance)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void verifySaveMaintenanceArgumentException() throws Exception{
        Maintenance exceptMaintenance = new Maintenance();
        BeanUtils.copyProperties(maintenances.get(2), exceptMaintenance);

        given(maintenanceService.saveMaintenance(any())).willThrow(new IllegalArgumentException("Invalid argument") {
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/maintenance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(exceptMaintenance)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        given(maintenanceService.deleteByID(1L)).willReturn(ServiceStatus.SUCCESS);
        mockMvc.perform(MockMvcRequestBuilders.delete("/maintenance/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        given(maintenanceService.getByID(22L)).willReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/maintenance/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/maintenance/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

    @Test
    public void verifyDeleteByIDDBError() throws Exception{
        given(maintenanceService.deleteByID(111L)).willThrow( new EmptyResultDataAccessException("Cannot access record 111", 1));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/maintenance/{id}", 111L).accept(MediaType.APPLICATION_JSON));

        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
    }

     @Test
    public void findAllByDescriptionTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDescription(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/description/{description}" , users.get(0).getDescription())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByDescriptionInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDescription(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/description/{description}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByDescriptionNoMatchTest() throws Exception{
        given(userService.findAllByDescription(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/description/{description}"  , users.get(0).getDescription())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByBlockNameTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByBlockName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/blockName/{blockName}" , users.get(0).getBlockName())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByBlockNameInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByBlockName(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/blockName/{blockName}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByBlockNameNoMatchTest() throws Exception{
        given(userService.findAllByBlockName(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/blockName/{blockName}"  , users.get(0).getBlockName())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByDoneByTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDoneBy(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/doneBy/{doneBy}" , users.get(0).getDoneBy())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByDoneByInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByDoneBy(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/doneBy/{doneBy}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByDoneByNoMatchTest() throws Exception{
        given(userService.findAllByDoneBy(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/doneBy/{doneBy}"  , users.get(0).getDoneBy())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByMaintenanceTypeTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByMaintenanceType(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/maintenanceType/{maintenanceType}" , users.get(0).getMaintenanceType())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByMaintenanceTypeInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByMaintenanceType(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/maintenanceType/{maintenanceType}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByMaintenanceTypeNoMatchTest() throws Exception{
        given(userService.findAllByMaintenanceType(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/maintenanceType/{maintenanceType}"  , users.get(0).getMaintenanceType())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByStatusTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByStatus(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/status/{status}" , users.get(0).getStatus())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByStatusInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByStatus(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/status/{status}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByStatusNoMatchTest() throws Exception{
        given(userService.findAllByStatus(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/status/{status}"  , users.get(0).getStatus())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	
    @Test
    public void findAllByContractor_IdTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByContractor_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/contractor/{contractor}" , users.get(0).getContractor())
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(1)));


    }

    @Test
    public void findAllByContractor_IdInvalidParamTest() throws Exception{
        ArrayList<User> matchedUsers = new ArrayList<>(1);
        matchedUsers.add(this.users.get(0));
        given(userService.findAllByContractor_Id(any())).willReturn(matchedUsers);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/contractor/{contractor}", null)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()) ;

    }

    @Test
    public void findAllByContractor_IdNoMatchTest() throws Exception{
        given(userService.findAllByContractor_Id(any())).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/maintenance/contractor/{contractor}"  , users.get(0).getContractor())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()) ;

    }
	



    private MaintenanceDTO getDTO(Maintenance maintenance){
        return modelMapper.map(maintenance, MaintenanceDTO.class);
    }

    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
