package lk.empire.ams.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.empire.ams.MainApplication;
import lk.empire.ams.controller.PaymentController;
import lk.empire.ams.error.CustomizedResponseEntityExceptionHandler;
import lk.empire.ams.model.dto.PaymentDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainPaymentService;
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
 * <p>Title         : Payment API integration Test
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Integration test class for Payment. A Payment for of an apartment or related activity
 * This will test basic REST API calls with test profile
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MainApplication.class, CustomizedResponseEntityExceptionHandler.class, PaymentController.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainPaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;
    //No bar
    @Autowired
    private PaymentRepository paymentRepository;
    //No war
    private List<Payment> payments = new ArrayList<>();

      	@Autowired
	UnitRepository unitRepository; 	private Unit unit0;
		private Unit unit1;
		private Unit unit2;
		@Autowired
	ClientRepository clientRepository; 	private Client client0;
		private Client client1;
		private Client client2;
	


    private final ModelMapper modelMapper = new ModelMapper();

    public PaymentAPITest(){


    }

    @Before
    public void initData(){
        unit0 =   new Unit(0L, "Tenuki", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	unit1 =   new Unit(0L, "Kusal", owner1, renter1, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor1, Availability.Available);
	Unit savedunit1 = unitRepository.save(unit1);
	unit1 = savedunit1;
unit2 =   new Unit(2L, "H", owner2, renter2, new ArrayList<ParkingSlot>(), new ArrayList<Vehicle>(), floor2, Availability.Available);
	client0 =   new Client(0L, "Nuwan", "gion between Apulia and Lucania (Basilicata). Various Italic", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	client1 =   new Client(0L, "Tenuki", " 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	Client savedclient1 = clientRepository.save(client1);
	client1 = savedclient1;
client2 =   new Client(2L, "H", "He was born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects", new HasSet <Unit>(), new HasSet <Unit>(), new HasSet <Payment>(), new HasSet <Notification>(), new ArrayList<AppEvent>());
	 
        payments.add( new Payment(0L, PaymentMethod.BankTransfer, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 766458, 176799, 102277.97602660464, unit1, client1));
		payments.add( new Payment(0L, PaymentMethod.Cheque, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 796516, 934168, 528123.9102237523, unit1, client1));
		payments.add( new Payment(0L, PaymentMethod.BankTransfer, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 783708, 991145, 363306.5945699274, unit1, client1));
        try {
        paymentRepository.deleteAll();
        } catch (Throwable e) {
            //Ignore any constraint violations, delete rest
        }
        Payment savedPayment = paymentRepository.save(payments.get(0));
        Assert.assertNotNull(savedPayment);
        Assert.assertTrue(savedPayment.getId() > 0);
        payments.get(0).setId(savedPayment.getId());
        savedPayment = paymentRepository.save(payments.get(1));
        Assert.assertNotNull(savedPayment);
        Assert.assertTrue(savedPayment.getId() > 0);
        payments.get(1).setId(savedPayment.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

    }

    @Test
    public void verifyGetPaymentByIDPresent() throws Exception{
        mockMvc.perform(get("/payments/{id}",payments.get(0).getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()).
                andDo(print()).
        andExpect(jsonPath("$.id").value(payments.get(0).getId())); //Match attribute ID
    }

    @Test
    public void verifyGetPaymentByIDNotPresent() throws Exception{
        mockMvc.perform(get("/payments/{id}",111).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); //Not found ID

    }


    @Test
    public void verifyGetPaymentByIDZero() throws Exception{
        mockMvc.perform(get("/payments/{id}",0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //Bad request

    }

    @Test
    public void verifyGetAllPayments() throws Exception{
        mockMvc.perform(get("/payments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print()).andExpect(jsonPath("$", IsCollectionWithSize.hasSize(greaterThan(1))));

    }

    @Test
    public void verifySavePaymentSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(payments.get(2))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void verifySavePaymentExistingSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(payments.get(1))))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/payments/" + payments.get(1).getId()));
    }


    @Test
    public void verifySavePaymentFailValidation() throws Exception{
        Payment invalid =   new Payment(6L, PaymentMethod.Cheque, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 996434, 219727, 470128.9517043, unit2, client2);
        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(invalid)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifySavePaymentExistingNotFound() throws Exception{
        Payment nonExisting =   new Payment(5L, PaymentMethod.Cash, PaymentStatus.Pending, LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), LocalDate.of(2000 + (int)(Math.random() * 19), 1 +  (int)(Math.random() * 11), 1 + (int)(Math.random() * 27)), 432667, 651314, 984784.0949062571, unit1, client1);
        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getDTO(nonExisting)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void verifyDeleteByIDSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/payments/{id}", payments.get(0).getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andDo(print());
    }


    @Test
    public void verifyDeleteByIDNonExisting() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/payments/{id}", 111L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) ;
    }


    @Test
    public void verifyDeleteByIDZero() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/payments/{id}", 0L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }


    public  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PaymentDTO getDTO(Payment payment){
        return modelMapper.map(payment, PaymentDTO.class);
    }

}
