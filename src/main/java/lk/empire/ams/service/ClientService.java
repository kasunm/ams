package lk.empire.ams.service;


import lk.empire.ams.model.entity.Client;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : ClientService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Client. A Client of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface ClientService {

    /**
     * Get all clients
     * @return List<Client>
     */
    List<Client> getClients();

    /**
     * Get a specific client by id
     * @param id Long
     * @return Optional<Client>
     */
    Optional<Client> getByID(Long id);

    /**
     * Save client and set id to passed client
     * @param client Client
     * @return ServiceStatus
     */
    Client saveClient(Client client);

    /**
     * Delete a client by ID
     * @param clientID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long clientID);



	/**
     * Find  clients by firstName
     * @param firstName String     
     * @return List<Client>
     */
    public List<Client> findAllByFirstName(String firstName);

	/**
     * Find  clients by email
     * @param email String     
     * @return List<Client>
     */
    public List<Client> findAllByEmail(String email);

	/**
     * Find  clients by nic
     * @param nic String     
     * @return List<Client>
     */
    public List<Client> findAllByNic(String nic);




}
