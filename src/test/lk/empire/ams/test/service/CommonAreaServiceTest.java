package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainCommonAreaService;
import lk.empire.ams.service.CommonAreaService;
import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * <p>Title         : CommonAreaService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for CommonArea. A Common area of apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CommonAreaServiceTest {

    @MockBean
    private CommonAreaRepository repository;


    @Autowired
    private CommonAreaService commonAreaService;

    private List<CommonArea> commonAreas = new ArrayList<>();

     	private Floor floor0;
		private Floor floor1;
		private Floor floor2;
	

    @TestConfiguration
    static class CommonAreaServiceImplTestContextConfiguration {
        @Bean
        public CommonAreaService employeeService() {
            return new MainCommonAreaService();
        }
    }

    @Before
    public void setUp(){
        floor0 =   new Floor(0L, "e was born on", "Aloka", 438136, apartment1);
	floor1 =   new Floor(1L, " bo", "Nayani", 175239, apartment1);
	floor2 =   new Floor(2L, "He was born on 8 Decem", "He was born on 8 Decem", 180557, apartment2);
	 

		commonAreas.add( new CommonArea(1L, " town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references t", " 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this per", " December 65 BC[nb 4] i", Availability.Available, floor1)); //ID 1
        commonAreas.add( new CommonArea(2L, "and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly h", " December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in ", " born on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Ve", Availability.Available, floor1)); //ID 2
		commonAreas.add( new CommonArea(3L, "c dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus ", "e south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic di", "he Samnite south of Italy.[5", Availability.Available, floor1)); //ID 3

        given(repository.findAll()).willReturn(commonAreas);
        given(repository.findById(1L)).willReturn(Optional.of(commonAreas.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetCommonAreasSuccess(){
        List<CommonArea> commonAreas = commonAreaService.getCommonAreas();
        Assert.assertNotNull(commonAreas);
        Assert.assertTrue("Expect 3 commonAreas in result", commonAreas.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", commonAreas == this.commonAreas);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<CommonArea> commonArea = null;
        commonArea = commonAreaService.getByID(111L);
        Assert.assertTrue("No match found", !commonArea.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<CommonArea> commonArea = null;
        try {
            commonArea = commonAreaService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", commonArea == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<CommonArea> commonArea = commonAreaService.getByID(1L);
        Assert.assertNotNull(commonAreas);
        Assert.assertTrue("Expect a commonAreas in result", commonArea.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", commonAreas.get(0) == commonArea.get());
    }

    @Test
    public void verifySaveCommonAreaSuccess() throws Exception{
        CommonArea newCommonArea =   new CommonArea(1L, " town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps enriched his feeling for language. He could have been familiar with Greek words even as a young boy and later he poked fun at the jargon of mixed Greek and Oscan spoken in neighbouring Canusium.[6] One of the works he probably studied in school was the Odyssia of Livius Andronicus, taught by teachers like the 'Orbilius' mentioned in one of his poems.[7] Army veterans could have been settled there at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references t", " 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this per", " December 65 BC[nb 4] i", Availability.Available, floor1);
        CommonArea savedCommonArea4 = new CommonArea();
        BeanUtils.copyProperties(newCommonArea, savedCommonArea4);
        savedCommonArea4.setId(5L);
        given(repository.save(newCommonArea)).willReturn(savedCommonArea4);
        given(repository.saveAndFlush(newCommonArea)).willReturn(savedCommonArea4);

        CommonArea commonArea = commonAreaService.saveCommonArea(newCommonArea);
        Assert.assertNotNull(commonArea);
        Assert.assertTrue("Expect valid ID in returned commonArea", commonArea.getId() != null && commonArea.getId() > 0);
    }

    @Test
    public void verifySaveCommonAreaNull() throws Exception{
        CommonArea commonArea = null;
        try {
            commonArea = commonAreaService.saveCommonArea(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned commonArea", !StringUtils.isEmpty(e.getMessage()) && commonArea == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveCommonAreaRepoIDNotReturned() throws Exception{
        CommonArea newCommonArea =  new CommonArea(0L, "e at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholi", "mber 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, la", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home ", Availability.Available, floor1);
        given(repository.save(newCommonArea)).willReturn(newCommonArea);
        given(repository.saveAndFlush(newCommonArea)).willReturn(newCommonArea);

        CommonArea commonArea = commonAreaService.saveCommonArea(newCommonArea);
        Assert.assertNotNull(commonArea);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", commonArea.getId() == null || commonArea.getId() == 0);
    }

    @Test
    public void verifySaveCommonAreaRepoException() throws Exception{
        CommonArea newCommonArea =  new CommonArea(0L, "e at the expense of local families uprooted by Rome as punishment for their part in the Social War (9188 BC).[8] Such state-sponsored migration must have added still more linguistic variety to the area. According to a local tradition reported by Horace,[9] a colony of Romans or Latins had been installed in Venusia after the Samnites had been driven out early in the third century. In that case, young Horace could have felt himself to be a Roman[10][11] though there are also indications that he regarded himself as a Samnite or Sabellus by birth.[12][13] Italians in modern and ancient times have always been devoted to their home towns, even after success in the wider world, and Horace was no different. Images of his childhood setting and references to it are found throughout his poems.[14] Horace's father was probably a Venutian taken captive by Romans in the Social War, or possibly he was descended from a Sabine captured in the Samnite Wars. Either way, he was a slave for at least part of his life. He was evidently a man of strong abilities however and managed to gain his freedom and improve his social position. Thus Horace claimed to be the free-born son of a prosperous 'coactor'.[15] The term 'coactor' could denote various roles, such as tax collector, but its use by Horace[16] was explained by scholi", "mber 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, la", "December 65 BC[nb 4] in the Samnite south of Italy.[5] His home ", Availability.Available, floor1);
        given(repository.save(newCommonArea)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newCommonArea)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        CommonArea commonArea = null;
        try {
            commonArea = commonAreaService.saveCommonArea(newCommonArea);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && commonArea == null);
        }

    }

    @Test
    public void verifyDeleteCommonAreaSuccess() throws Exception{
        ServiceStatus status = commonAreaService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete commonArea operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteCommonAreaIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = commonAreaService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByTypeTest() throws Exception{
        ArrayList<CommonArea> matchedCommonAreas = new ArrayList<>(1);
        matchedCommonAreas.add(this.commonAreas.get(0));
        given(repository.findAllByType(any())).willReturn(matchedCommonAreas);
        List <CommonArea> resultCommonAreas = commonAreaService.findAllByType(users.get(0).getType());
        Assert.assertNotNull(resultCommonAreas);
        Assert.assertTrue(resultCommonAreas.size() > 0);
        Assert.assertTrue(resultCommonAreas.get(0).getId() == this.commonAreas.get(0).getId());

    }

    @Test
    public void findAllByTypeInvalidParamTest() throws Exception{
        ArrayList<CommonArea> matchedCommonAreas = new ArrayList<>(1);
        matchedCommonAreas.add(this.commonAreas.get(0));
        given(repository.findAllByType(any())).willReturn(matchedCommonAreas);
        List <CommonArea> resultCommonAreas = null;
        try {
            resultCommonAreas = commonAreaService.findAllByType(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultCommonAreas == null);
        }
    }
 	
	@Test
    public void findAllByAvailabilityTest() throws Exception{
        ArrayList<CommonArea> matchedCommonAreas = new ArrayList<>(1);
        matchedCommonAreas.add(this.commonAreas.get(0));
        given(repository.findAllByAvailability(any())).willReturn(matchedCommonAreas);
        List <CommonArea> resultCommonAreas = commonAreaService.findAllByAvailability(users.get(0).getAvailability());
        Assert.assertNotNull(resultCommonAreas);
        Assert.assertTrue(resultCommonAreas.size() > 0);
        Assert.assertTrue(resultCommonAreas.get(0).getId() == this.commonAreas.get(0).getId());

    }

    @Test
    public void findAllByAvailabilityInvalidParamTest() throws Exception{
        ArrayList<CommonArea> matchedCommonAreas = new ArrayList<>(1);
        matchedCommonAreas.add(this.commonAreas.get(0));
        given(repository.findAllByAvailability(any())).willReturn(matchedCommonAreas);
        List <CommonArea> resultCommonAreas = null;
        try {
            resultCommonAreas = commonAreaService.findAllByAvailability(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultCommonAreas == null);
        }
    }
 	
	@Test
    public void findAllByFloor_NameTest() throws Exception{
        ArrayList<CommonArea> matchedCommonAreas = new ArrayList<>(1);
        matchedCommonAreas.add(this.commonAreas.get(0));
        given(repository.findAllByFloor_Name(any())).willReturn(matchedCommonAreas);
        List <CommonArea> resultCommonAreas = commonAreaService.findAllByFloor_Name(users.get(0).getFloor());
        Assert.assertNotNull(resultCommonAreas);
        Assert.assertTrue(resultCommonAreas.size() > 0);
        Assert.assertTrue(resultCommonAreas.get(0).getId() == this.commonAreas.get(0).getId());

    }

    @Test
    public void findAllByFloor_NameInvalidParamTest() throws Exception{
        ArrayList<CommonArea> matchedCommonAreas = new ArrayList<>(1);
        matchedCommonAreas.add(this.commonAreas.get(0));
        given(repository.findAllByFloor_Name(any())).willReturn(matchedCommonAreas);
        List <CommonArea> resultCommonAreas = null;
        try {
            resultCommonAreas = commonAreaService.findAllByFloor_Name(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultCommonAreas == null);
        }
    }




}
