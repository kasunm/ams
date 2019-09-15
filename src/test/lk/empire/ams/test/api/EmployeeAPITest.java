package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.EmployeeController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.EmployeeDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainEmployeeService;
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
 * <p>Title         : Employee API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Employee. An Employee of apartment
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, EmployeeController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainEmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private EmployeeRepository employeeRepository;
    //No war
    private List<Employee> employees = new ArrayList<>();

      


    private final ModelMapper modelMapper = new ModelMapper();

    public EmployeeAPITest(){


    }

    @Before
    public void initData(){
         
        employees.add( new Employee(0L, new ArrayList<AppEvent>()));
		employees.add( new Employee(0L, new ArrayList<AppEvent>()));
		employees.add( new Employee(0L, new ArrayList<AppEvent>()));
        try {
        employeeRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Employee savedEmployee = employeeRepository.save(employees.get(0));
        Assert.assertNotNull(savedEmployee);
        Assert.assertTrue(savedEmployee.getId() > 0);
        employees.get(0).setId(savedEmployee.getId());
        savedEmployee = employeeRepository.save(employees.get(1));
        Assert.assertNotNull(savedEmployee);
        Assert.assertTrue(savedEmployee.getId() > 0);
        employees.get(1).setId(savedEmployee.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetEmployeeByIDPresent() throws Exception{
        mockMvc.perform(get("/employees/{id}",employees.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(employees.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetEmployeeByIDNotPresent() throws Exception{
        mockMvc.perform(get("/employees/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetEmployeeByIDZero() throws Exception{
        mockMvc.perform(get("/employees/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllEmployees() throws Exception{
        mockMvc.perform(get("/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveEmployeeSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(employees.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveEmployeeExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(employees.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/employees/" + employees.get(1).getId()));
    }


    @Test
    public void verifySaveEmployeeFailValidation() throws Exception{
        Employee invalid =   new Employee(6L, new ArrayList<AppEvent>());
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveEmployeeExistingNotFound() throws Exception{
        Employee nonExisting =   new Employee(5L, new ArrayList<AppEvent>());
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", employees.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private EmployeeDTO getDTO(Employee employee){
        return modelMapper.map(employee, EmployeeDTO.class);
    }

}
