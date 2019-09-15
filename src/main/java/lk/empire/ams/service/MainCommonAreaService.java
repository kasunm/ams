package lk.empire.ams.service;


import lk.empire.ams.model.entity.CommonArea;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.CommonAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.Assert;



/**
 * <p>Title         : CommonAreaService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@Service
public class MainCommonAreaService implements CommonAreaService{
    @Autowired
    CommonAreaRepository commonAreaRepository;

    /**
     * Get all commonAreas
     * @return List<CommonArea>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public List<CommonArea> getCommonAreas(){
        return commonAreaRepository.findAll();
    }

    /**
     * Get a specific commonArea by id
     * @param id Long
     * @return Optional<CommonArea>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<CommonArea> getByID(Long id){
         return commonAreaRepository.findById(id);
    }

    /**
     * Save commonArea and set id to passed commonArea
     * @param commonArea CommonArea
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public CommonArea saveCommonArea(CommonArea commonArea){
        Assert.notNull(commonArea, "Common Area parameter expected");
        Assert.isTrue(commonArea.isValid(), "Valid Common Area is expected");
        CommonArea savedCommonArea = commonAreaRepository.saveAndFlush(commonArea);
        if(savedCommonArea != null && savedCommonArea.getId() != null && savedCommonArea.getId() > 0) {
            commonArea.setId(savedCommonArea.getId());
        }
        return commonArea;
    }

    /**
     * Delete a commonArea by ID
     * @param commonAreaID long
     * @return ServiceStatus
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 500, readOnly = false)
    public ServiceStatus deleteByID(long commonAreaID){
        commonAreaRepository.deleteById(commonAreaID);
         return ServiceStatus.SUCCESS;
    }

      /** ------------- Search methods ------------- */

     	/**
     * Find  commonAreas by type
     * @param type String     
     * @return List<CommonArea>
     */
    public List<CommonArea> findAllByType(String type){
		Assert.notNull(type, "Expects a valid type");

        return commonAreaRepository.findAllByType(type);
    }
	/**
     * Find  commonAreas by availability
     * @param availability Availability     
     * @return List<CommonArea>
     */
    public List<CommonArea> findAllByAvailability(Availability availability){
		Assert.notNull(availability, "Expects a valid availability");

        return commonAreaRepository.findAllByAvailability(availability);
    }
	/**
     * Find  commonAreas by floor
     * @param floor Long     
     * @return List<CommonArea>
     */
    public List<CommonArea> findAllByFloor_Name(Long floor){
		Assert.notNull(floor, "Expects a valid floor");
		Assert.isTrue(floor > 0, "Expects a valid floor > 0");

        return commonAreaRepository.findAllByFloor_Name(floor);
    }



    @PostConstruct
    public void initDB(){

    }
}
