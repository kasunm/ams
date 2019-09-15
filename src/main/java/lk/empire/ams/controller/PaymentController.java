package lk.empire.ams.controller;

import lk.empire.ams.model.dto.PaymentDTO;
import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.service.*;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import javax.persistence.PersistenceException;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.*;
import java.util.stream.Collectors;
/**
 * <p>Title         : PaymentController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Payment. A Payment for of an apartment or related activity
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/payments", value = "PaymentController", description = "All services related to PaymentController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	private final UnitService unitService;
	private final ClientService clientService;


    public PaymentController(PaymentService paymentService , UnitService unitService, ClientService clientService){
        this.paymentService = paymentService;
		this.unitService = unitService;
		this.clientService = clientService;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all payments
     * @return ResponseEntity<List<PaymentDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getPayments(){
        logger.debug("Request to get all Payments");
        List<PaymentDTO> payments = paymentService.getPayments().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(payments == null || payments.size() < 1) throw new ResourceNotFoundException("Unable to find any Payments");
        return new ResponseEntity(payments, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific payment by id
     * @param id Long
     * @return ResponseEntity<PaymentDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<PaymentDTO> getPayments(@PathVariable Long id) {
        logger.debug("Request to get a Payment by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Payment> payment = paymentService.getByID(id);
        if(payment != null && payment.isPresent()) return new ResponseEntity(convertToDTO(payment.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Payment with id " + id);
    }


    /**
     * Persist payment. if id > 0 is present expects valid payment object already present, and update it by
     * replacing values. Otherwise simply creates a new payment and id is returned
     * @param payment PaymentDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<PaymentDTO> savePayment(@RequestBody @Valid PaymentDTO payment) throws Exception{
        logger.debug("Request to save Payment");
        Payment existingPayment = new Payment();
        if(payment.getId() != null && payment.getId() > 0) {
            //Updating existing payment - Check item with matching ID present
            Optional<Payment> savedPayment = paymentService.getByID(payment.getId());
            if(savedPayment != null && savedPayment.isPresent()) existingPayment = savedPayment.get();
            else throw new ResourceNotFoundException("In order to update Payment " + payment.getId() + ", existing Payment must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Payment savePayment = copyToPayment(payment, existingPayment);
        Payment savedPayment = paymentService.savePayment(savePayment);
        if(savedPayment.getId() != null && savedPayment.getId() > 0){
            logger.info("Saved Payment with id " + savePayment.getId());
            PaymentDTO savedPaymentDTo = convertToDTO(savedPayment);
            return  ResponseEntity.created (new URI("/payments/" + savedPayment.getId())).body(savedPaymentDTo);
        }
        else{
            throw new PersistenceException("Payment not persisted: " + new Gson().toJson(savedPayment));
        }
    }

   /**
     * Delete a payment by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Payment with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Payment> payment = paymentService.getByID(id);
        if(payment == null || !payment.isPresent()) throw new ResourceNotFoundException("In order to delete  Payment " + id + ", existing  Payment must be available with same ID");
        paymentService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  payments by client
     * @param client Long     
     * @return List<Payment>
     */
    @GetMapping(path = "client/{client}")
    public ResponseEntity<List<Payment>> findPaymentsByClient_Id(@PathVariable Long client){
        logger.debug("findPaymentsByClient_Id(" + client + ")");
		Assert.notNull(client, "Expects a valid client");
		Assert.isTrue(client > 0, "Expects a valid client > 0");

        List<Payment> payments =  paymentService.findPaymentsByClient_Id(client);
        if(payments == null || payments.size() < 1) throw new ResourceNotFoundException("Unable to find any payments matching criteria");
        return new ResponseEntity(getDTOs(payments), HttpStatus.ACCEPTED);
    }
	/**
     * Find  payments by status
     * @param status PaymentStatus     
     * @return List<Payment>
     */
    @GetMapping(path = "status/{status}")
    public ResponseEntity<List<Payment>> findAllByStatus(@PathVariable PaymentStatus status){
        logger.debug("findAllByStatus(" + status + ")");
		Assert.notNull(status, "Expects a valid status");

        List<Payment> payments =  paymentService.findAllByStatus(status);
        if(payments == null || payments.size() < 1) throw new ResourceNotFoundException("Unable to find any payments matching criteria");
        return new ResponseEntity(getDTOs(payments), HttpStatus.ACCEPTED);
    }
	/**
     * Find  payments by dueDate
     * @param dueDate LocalDate     
     * @return List<Payment>
     */
    @GetMapping(path = "dueDate/{dueDate}")
    public ResponseEntity<List<Payment>> findAllByDueDate(@PathVariable LocalDate dueDate){
        logger.debug("findAllByDueDate(" + dueDate + ")");
		Assert.notNull(dueDate, "Expects a valid dueDate");

        List<Payment> payments =  paymentService.findAllByDueDate(dueDate);
        if(payments == null || payments.size() < 1) throw new ResourceNotFoundException("Unable to find any payments matching criteria");
        return new ResponseEntity(getDTOs(payments), HttpStatus.ACCEPTED);
    }
	/**
     * Find  payments by unit
     * @param unit Long     
     * @return List<Payment>
     */
    @GetMapping(path = "unit/{unit}")
    public ResponseEntity<List<Payment>> findAllByUnit_Id(@PathVariable Long unit){
        logger.debug("findAllByUnit_Id(" + unit + ")");
		Assert.notNull(unit, "Expects a valid unit");
		Assert.isTrue(unit > 0, "Expects a valid unit > 0");

        List<Payment> payments =  paymentService.findAllByUnit_Id(unit);
        if(payments == null || payments.size() < 1) throw new ResourceNotFoundException("Unable to find any payments matching criteria");
        return new ResponseEntity(getDTOs(payments), HttpStatus.ACCEPTED);
    }
	/**
     * Find  payments by status,client
     * @param status PaymentStatus
     * @param client Long     
     * @return List<Payment>
     */
    @GetMapping(path = "client-pending/{client}")
    public ResponseEntity<List<Payment>> findAllPendingAndClient_Id( @PathVariable  Long client){
        logger.debug("findAllByStatusAndClient_Id(" + client + ")");
		Assert.notNull(client, "Expects a valid client");
		Assert.isTrue(client > 0, "Expects a valid client > 0");

        List<Payment> payments =  paymentService.findAllByStatusAndClient_Id(PaymentStatus.Pending,client);
        if(payments == null || payments.size() < 1) throw new ResourceNotFoundException("Unable to find any payments matching criteria");
        return new ResponseEntity(getDTOs(payments), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private PaymentDTO convertToDTO(Payment payment){
        return modelMapper.map(payment, PaymentDTO.class);
    }

     private List<PaymentDTO> getDTOs(List<Payment> payments){
           if(payments == null) return null;
           List<PaymentDTO> dtoList = new ArrayList<PaymentDTO>(payments.size());
           for(Payment payment: payments){
               PaymentDTO dto = convertToDTO(payment);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Payment copyToPayment(PaymentDTO paymentDTO, Payment payment){
		if(paymentDTO.getUnitId() != null && paymentDTO.getUnitId() > 0){
            payment.setUnit( unitService.getByID(paymentDTO.getUnitId()).get());
        }
		if(paymentDTO.getClientId() != null && paymentDTO.getClientId() > 0){
            payment.setClient( clientService.getByID(paymentDTO.getClientId()).get());
        }

         modelMapper.map(paymentDTO, payment);
          return payment;
    }

}
