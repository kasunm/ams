package lk.empire.ams.service;


import lk.empire.ams.model.entity.Client;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : ClientService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Client. A Client of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainClientService implements ClientService{
    @Autowired
    ClientRepository clientRepository;

    /**
     * Get all clients
     * @return List<Client>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getClients(){
        return clientRepository.findAll();
    }

    /**
     * Get a specific client by id
     * @param id Long
     * @return Optional<Client>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Client> getByID(Long id){
         return clientRepository.findById(id);
    }

    /**
     * Save client and set id to passed client
     * @param client Client
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Client saveClient(Client client){
        Assert.notNull(client, "Client parameter expected");
        Assert.isTrue(client.isValid(), "Valid Client is expected");
        Client savedClient = clientRepository.saveAndFlush(client);
        if(savedClient != null && savedClient.getId() != null && savedClient.getId() > 0) {
            client.setId(savedClient.getId());
        }
        return client;
    }

    /**
     * Delete a client by ID
     * @param clientID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long clientID){
        clientRepository.deleteById(clientID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  clients by firstName
     * @param firstName String     
     * @return List<Client>
     */
    public List<Client> findAllByFirstName(String firstName){
		Assert.notNull(firstName, "Expects a valid firstName");

        return clientRepository.findAllByFirstName(firstName);
    }
	/**
     * Find  clients by email
     * @param email String     
     * @return List<Client>
     */
    public List<Client> findAllByEmail(String email){
		Assert.notNull(email, "Expects a valid email");

        return clientRepository.findAllByEmail(email);
    }
	/**
     * Find  clients by nic
     * @param nic String     
     * @return List<Client>
     */
    public List<Client> findAllByNic(String nic){
		Assert.notNull(nic, "Expects a valid nic");

        return clientRepository.findAllByNic(nic);
    }



    @PostConstruct
    public void initDB(){

    }
}
