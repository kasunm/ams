package lk.empire.ams.service;


import lk.empire.ams.model.entity.Apartment;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : ApartmentService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for Apartment. An apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainApartmentService implements ApartmentService{
    @Autowired
    ApartmentRepository apartmentRepository;

    /**
     * Get all apartments
     * @return List<Apartment>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Apartment> getApartments(){
        return apartmentRepository.findAll();
    }

    /**
     * Get a specific apartment by id
     * @param id Long
     * @return Optional<Apartment>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Apartment> getByID(Long id){
         return apartmentRepository.findById(id);
    }

    /**
     * Save apartment and set id to passed apartment
     * @param apartment Apartment
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public Apartment saveApartment(Apartment apartment){
        Assert.notNull(apartment, "Apartment parameter expected");
        Assert.isTrue(apartment.isValid(), "Valid Apartment is expected");
        Apartment savedApartment = apartmentRepository.saveAndFlush(apartment);
        if(savedApartment != null && savedApartment.getId() != null && savedApartment.getId() > 0) {
            apartment.setId(savedApartment.getId());
        }
        return apartment;
    }

    /**
     * Delete a apartment by ID
     * @param apartmentID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long apartmentID){
        apartmentRepository.deleteById(apartmentID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  apartments by name
     * @param name String     
     * @return List<Apartment>
     */
    public List<Apartment> findAllByName(String name){
		Assert.notNull(name, "Expects a valid name");

        return apartmentRepository.findAllByName(name);
    }



    @PostConstruct
    public void initDB(){

    }
}
