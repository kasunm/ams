package lk.empire.ams.service;


import lk.empire.ams.model.entity.CommonArea;
import lk.empire.ams.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.*;
import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.*;


/**
 * <p>Title         : CommonAreaService
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service class for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */

public interface CommonAreaService {

    /**
     * Get all commonAreas
     * @return List<CommonArea>
     */
    List<CommonArea> getCommonAreas();

    /**
     * Get a specific commonArea by id
     * @param id Long
     * @return Optional<CommonArea>
     */
    Optional<CommonArea> getByID(Long id);

    /**
     * Save commonArea and set id to passed commonArea
     * @param commonArea CommonArea
     * @return ServiceStatus
     */
    CommonArea saveCommonArea(CommonArea commonArea);

    /**
     * Delete a commonArea by ID
     * @param commonAreaID long
     * @return ServiceStatus
     */
    ServiceStatus deleteByID(long commonAreaID);



	/**
     * Find  commonAreas by type
     * @param type String     
     * @return List<CommonArea>
     */
    public List<CommonArea> findAllByType(String type);

	/**
     * Find  commonAreas by availability
     * @param availability Availability     
     * @return List<CommonArea>
     */
    public List<CommonArea> findAllByAvailability(Availability availability);

	/**
     * Find  commonAreas by floor
     * @param floor Long     
     * @return List<CommonArea>
     */
    public List<CommonArea> findAllByFloor_Name(Long floor);




}
