package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.ClientController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.ClientDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainClientService;
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
 * <p>Title         : Client API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Client. A Client of apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, ClientController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private ClientRepository clientRepository;
    //No war
    private List<Client> clients = new ArrayList<>();

      


    private final ModelMapper modelMapper = new ModelMapper();

    public ClientAPITest(){


    }

    @Before
    public void initData(){
         
        clients.add( new Client(0L, "Tenuki", "border region betw", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>()));
		clients.add( new Client(0L, "Supun", "] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in th", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>()));
		clients.add( new Client(0L, "Nayani", "n on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). V", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>()));
        try {
        clientRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Client savedClient = clientRepository.save(clients.get(0));
        Assert.assertNotNull(savedClient);
        Assert.assertTrue(savedClient.getId() > 0);
        clients.get(0).setId(savedClient.getId());
        savedClient = clientRepository.save(clients.get(1));
        Assert.assertNotNull(savedClient);
        Assert.assertTrue(savedClient.getId() > 0);
        clients.get(1).setId(savedClient.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetClientByIDPresent() throws Exception{
        mockMvc.perform(get("/clients/{id}",clients.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(clients.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetClientByIDNotPresent() throws Exception{
        mockMvc.perform(get("/clients/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetClientByIDZero() throws Exception{
        mockMvc.perform(get("/clients/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllClients() throws Exception{
        mockMvc.perform(get("/clients").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveClientSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(clients.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveClientExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(clients.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/clients/" + clients.get(1).getId()));
    }


    @Test
    public void verifySaveClientFailValidation() throws Exception{
        Client invalid =   new Client(6L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveClientExistingNotFound() throws Exception{
        Client nonExisting =   new Client(5L, "Thamasha", "outh of Italy.[5] His home town, Venusi", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
        mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", clients.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ClientDTO getDTO(Client client){
        return modelMapper.map(client, ClientDTO.class);
    }

}
