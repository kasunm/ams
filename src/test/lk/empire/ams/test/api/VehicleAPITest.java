package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.VehicleController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.VehicleDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainVehicleService;
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
 * <p>Title         : Vehicle API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Vehicle. A vehicle
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, VehicleController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VehicleAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainVehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private VehicleRepository vehicleRepository;
    //No war
    private List<Vehicle> vehicles = new ArrayList<>();

      	@Autowired
	UnitRepository unitRepository; 	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
	


    private final ModelMapper modelMapper = new ModelMapper();

    public VehicleAPITest(){


    }

    @Before
    public void initData(){
        unit0 =   new Unit(0L, "Aloka", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(0L, "Ruwan", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	Unit savedunit1 = unitRepository.save(unit1);
	unit1 = savedunit1;
unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	 
        vehicles.add( new Vehicle(0L, "d2160351335250", unit1, new HasSet <VehicleParking>(), "8 Dec", "He was born on 8 December 65 BC[nb 4] in the Samn"));
		vehicles.add( new Vehicle(0L, "w85", unit1, new HasSet <VehicleParking>(), "8 December 65", "as born on 8 December 65 BC[nb 4]"));
		vehicles.add( new Vehicle(0L, "v808877", unit1, new HasSet <VehicleParking>(), "rn on 8 Decembe", "rn on 8 December 65 "));
        try {
        vehicleRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Vehicle savedVehicle = vehicleRepository.save(vehicles.get(0));
        Assert.assertNotNull(savedVehicle);
        Assert.assertTrue(savedVehicle.getId() > 0);
        vehicles.get(0).setId(savedVehicle.getId());
        savedVehicle = vehicleRepository.save(vehicles.get(1));
        Assert.assertNotNull(savedVehicle);
        Assert.assertTrue(savedVehicle.getId() > 0);
        vehicles.get(1).setId(savedVehicle.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetVehicleByIDPresent() throws Exception{
        mockMvc.perform(get("/vehicles/{id}",vehicles.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(vehicles.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetVehicleByIDNotPresent() throws Exception{
        mockMvc.perform(get("/vehicles/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetVehicleByIDZero() throws Exception{
        mockMvc.perform(get("/vehicles/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllVehicles() throws Exception{
        mockMvc.perform(get("/vehicles").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySaveVehicleSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(vehicles.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySaveVehicleExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(vehicles.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/vehicles/" + vehicles.get(1).getId()));
    }


    @Test
    public void verifySaveVehicleFailValidation() throws Exception{
        Vehicle invalid =   new Vehicle(6L, "H", unit2, new HasSet <VehicleParking>(), "He was born on 8 December 65 BC[", "He was born on 8 December 65 BC[nb 4] in the Samnite");
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySaveVehicleExistingNotFound() throws Exception{
        Vehicle nonExisting =   new Vehicle(5L, "w780537587772265", unit1, new HasSet <VehicleParking>(), "He was born on 8 December 65", "on 8 December 65 BC[nb ");
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicles/{id}", vehicles.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicles/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicles/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VehicleDTO getDTO(Vehicle vehicle){
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

}
