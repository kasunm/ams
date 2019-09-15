package lk.empire.ams.controller;

import lk.empire.ams.model.dto.ClientDTO;
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
 * <p>Title         : ClientController
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Controller class for Client. A Client of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Api(basePath = "/clients", value = "ClientController", description = "All services related to ClientController", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

 

    public ClientController(ClientService clientService  ){
        this.clientService = clientService;
 
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /** ------------- Main service method ------------- */

    /**
     * get all clients
     * @return ResponseEntity<List<ClientDTO>>
     */
    @GetMapping(path = "")
    public ResponseEntity<?> getClients(){
        logger.debug("Request to get all Clients");
        List<ClientDTO> clients = clientService.getClients().stream().map(this::convertToDTO).collect(Collectors.toList());
        if(clients == null || clients.size() < 1) throw new ResourceNotFoundException("Unable to find any Clients");
        return new ResponseEntity(clients, HttpStatus.ACCEPTED);
    }

    /**
     * Get a specific client by id
     * @param id Long
     * @return ResponseEntity<ClientDTO>
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<ClientDTO> getClients(@PathVariable Long id) {
        logger.debug("Request to get a Client by id");
        if(id == null || id <= 0) throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Client> client = clientService.getByID(id);
        if(client != null && client.isPresent()) return new ResponseEntity(convertToDTO(client.get()) , HttpStatus.ACCEPTED);
        throw new ResourceNotFoundException("Unable to find any Client with id " + id);
    }


    /**
     * Persist client. if id > 0 is present expects valid client object already present, and update it by
     * replacing values. Otherwise simply creates a new client and id is returned
     * @param client ClientDTO
     * @return ResponseEntity<Long>
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<ClientDTO> saveClient(@RequestBody @Valid ClientDTO client) throws Exception{
        logger.debug("Request to save Client");
        Client existingClient = new Client();
        if(client.getId() != null && client.getId() > 0) {
            //Updating existing client - Check item with matching ID present
            Optional<Client> savedClient = clientService.getByID(client.getId());
            if(savedClient != null && savedClient.isPresent()) existingClient = savedClient.get();
            else throw new ResourceNotFoundException("In order to update Client " + client.getId() + ", existing Client must be available with same ID");
        }

        //In case not all persistent attributes not present in update DTO
        Client saveClient = copyToClient(client, existingClient);
        Client savedClient = clientService.saveClient(saveClient);
        if(savedClient.getId() != null && savedClient.getId() > 0){
            logger.info("Saved Client with id " + saveClient.getId());
            ClientDTO savedClientDTo = convertToDTO(savedClient);
            return  ResponseEntity.created (new URI("/clients/" + savedClient.getId())).body(savedClientDTo);
        }
        else{
            throw new PersistenceException("Client not persisted: " + new Gson().toJson(savedClient));
        }
    }

   /**
     * Delete a client by id
     * @param id Long
     * @return ResponseEntity<Long>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        logger.debug("Request to delete Client with id " + id);
        if(id == null || id == 0)  throw new IllegalArgumentException("Expects a valid id value > 0");
        Optional<Client> client = clientService.getByID(id);
        if(client == null || !client.isPresent()) throw new ResourceNotFoundException("In order to delete  Client " + id + ", existing  Client must be available with same ID");
        clientService.deleteByID(id);
        return new ResponseEntity<Long>(id,new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    /** ------------- Search methods ------------- */

 	/**
     * Find  clients by firstName
     * @param firstName String     
     * @return List<Client>
     */
    @GetMapping(path = "firstName/{firstName}")
    public ResponseEntity<List<Client>> findAllByFirstName(@PathVariable String firstName){
        logger.debug("findAllByFirstName(" + firstName + ")");
		Assert.notNull(firstName, "Expects a valid firstName");

        List<Client> clients =  clientService.findAllByFirstName(firstName);
        if(clients == null || clients.size() < 1) throw new ResourceNotFoundException("Unable to find any clients matching criteria");
        return new ResponseEntity(getDTOs(clients), HttpStatus.ACCEPTED);
    }
	/**
     * Find  clients by email
     * @param email String     
     * @return List<Client>
     */
    @GetMapping(path = "email/{email}")
    public ResponseEntity<List<Client>> findAllByEmail(@PathVariable String email){
        logger.debug("findAllByEmail(" + email + ")");
		Assert.notNull(email, "Expects a valid email");

        List<Client> clients =  clientService.findAllByEmail(email);
        if(clients == null || clients.size() < 1) throw new ResourceNotFoundException("Unable to find any clients matching criteria");
        return new ResponseEntity(getDTOs(clients), HttpStatus.ACCEPTED);
    }
	/**
     * Find  clients by nic
     * @param nic String     
     * @return List<Client>
     */
    @GetMapping(path = "nic/{nic}")
    public ResponseEntity<List<Client>> findAllByNic(@PathVariable String nic){
        logger.debug("findAllByNic(" + nic + ")");
		Assert.notNull(nic, "Expects a valid nic");

        List<Client> clients =  clientService.findAllByNic(nic);
        if(clients == null || clients.size() < 1) throw new ResourceNotFoundException("Unable to find any clients matching criteria");
        return new ResponseEntity(getDTOs(clients), HttpStatus.ACCEPTED);
    }


    /** ------------- Private supportive methods ------------- */

    private ClientDTO convertToDTO(Client client){
        return modelMapper.map(client, ClientDTO.class);
    }

     private List<ClientDTO> getDTOs(List<Client> clients){
           if(clients == null) return null;
           List<ClientDTO> dtoList = new ArrayList<ClientDTO>(clients.size());
           for(Client client: clients){
               ClientDTO dto = convertToDTO(client);
               dtoList.add(dto);
           }
           return dtoList;
        }

    private Client copyToClient(ClientDTO clientDTO, Client client){
 
         modelMapper.map(clientDTO, client);
          return client;
    }

}
