package com.kasunm.aggala.test.service;

import lk.empire.ams.model.entity.*;
import lk.empire.ams.model.enums.*;
import lk.empire.ams.repo.*;
import lk.empire.ams.service.MainFeatureService;
import lk.empire.ams.service.FeatureService;
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
 * <p>Title         : FeatureService unit tests
 * <p>Project       : Ams : Apartment management system for empire apartments
 * <p>Description   : Service unit test class for Feature. A Feature of an apartment
 *
 * @author Kasun Madurasinghe
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FeatureServiceTest {

    @MockBean
    private FeatureRepository repository;


    @Autowired
    private FeatureService featureService;

    private List<Feature> features = new ArrayList<>();

     

    @TestConfiguration
    static class FeatureServiceImplTestContextConfiguration {
        @Bean
        public FeatureService employeeService() {
            return new MainFeatureService();
        }
    }

    @Before
    public void setUp(){
         

		features.add( new Feature(1L, "Tenuki", "ute in the border region between Apulia and Lucania (Basilicata)", " the Samnite south of Italy.[5] His home town, V")); //ID 1
        features.add( new Feature(2L, "Thamasha", "orn on 8 December 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a ", "ber 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perhaps")); //ID 2
		features.add( new Feature(3L, "Kusal", "ber 65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and", "] in the Samnite south of Italy.[5] His home town, Venusia, lay ")); //ID 3

        given(repository.findAll()).willReturn(features);
        given(repository.findById(1L)).willReturn(Optional.of(features.get(0)));
        given(repository.findById(null)).willThrow(new IllegalArgumentException("Expects valid ID"));
        given(repository.findById(111L)).willReturn(Optional.empty());


    }

    @Test
    public void verifyGetFeaturesSuccess(){
        List<Feature> features = featureService.getFeatures();
        Assert.assertNotNull(features);
        Assert.assertTrue("Expect 3 features in result", features.size() == 3);
        Assert.assertTrue("Expect same test data reference to be returned", features == this.features);
    }



    @Test
    public void verifyGetByIDNotFound(){
        Optional<Feature> feature = null;
        feature = featureService.getByID(111L);
        Assert.assertTrue("No match found", !feature.isPresent());
    }

    @Test
    public void verifyGetByIDNull(){
        Optional<Feature> feature = null;
        try {
            feature = featureService.getByID(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("For ID Zero throws IllegalArgumentException", feature == null);
        }
    }

    @Test
    public void verifyGetByIDSuccess(){
        Optional<Feature> feature = featureService.getByID(1L);
        Assert.assertNotNull(features);
        Assert.assertTrue("Expect a features in result", feature.isPresent());
        Assert.assertTrue("Expect same test data reference to be returned", features.get(0) == feature.get());
    }

    @Test
    public void verifySaveFeatureSuccess() throws Exception{
        Feature newFeature =   new Feature(1L, "Tenuki", "ute in the border region between Apulia and Lucania (Basilicata)", " the Samnite south of Italy.[5] His home town, V");
        Feature savedFeature4 = new Feature();
        BeanUtils.copyProperties(newFeature, savedFeature4);
        savedFeature4.setId(5L);
        given(repository.save(newFeature)).willReturn(savedFeature4);
        given(repository.saveAndFlush(newFeature)).willReturn(savedFeature4);

        Feature feature = featureService.saveFeature(newFeature);
        Assert.assertNotNull(feature);
        Assert.assertTrue("Expect valid ID in returned feature", feature.getId() != null && feature.getId() > 0);
    }

    @Test
    public void verifySaveFeatureNull() throws Exception{
        Feature feature = null;
        try {
            feature = featureService.saveFeature(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expect valid ID in returned feature", !StringUtils.isEmpty(e.getMessage()) && feature == null && e.getMessage().contains("parameter expected"));
        }
    }

    @Test
    public void verifySaveFeatureRepoIDNotReturned() throws Exception{
        Feature newFeature =  new Feature(0L, "Sulaiman", "taly.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perh", "65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were ");
        given(repository.save(newFeature)).willReturn(newFeature);
        given(repository.saveAndFlush(newFeature)).willReturn(newFeature);

        Feature feature = featureService.saveFeature(newFeature);
        Assert.assertNotNull(feature);
        Assert.assertTrue("Valid ID not returned ${classDisplayName}", feature.getId() == null || feature.getId() == 0);
    }

    @Test
    public void verifySaveFeatureRepoException() throws Exception{
        Feature newFeature =  new Feature(0L, "Sulaiman", "taly.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were spoken in the area and this perh", "65 BC[nb 4] in the Samnite south of Italy.[5] His home town, Venusia, lay on a trade route in the border region between Apulia and Lucania (Basilicata). Various Italic dialects were ");
        given(repository.save(newFeature)).willThrow(new PersistenceException("error saving ${classDisplayName}"));
        given(repository.saveAndFlush(newFeature)).willThrow(new PersistenceException("error saving ${classDisplayName}"));

        Feature feature = null;
        try {
            feature = featureService.saveFeature(newFeature);
        } catch (RuntimeException e) {
            Assert.assertTrue("DB Exception", e != null && feature == null);
        }

    }

    @Test
    public void verifyDeleteFeatureSuccess() throws Exception{
        ServiceStatus status = featureService.deleteByID(2L);
        Assert.assertNotNull(status);
        Assert.assertTrue("Expect delete feature operation successful", status == ServiceStatus.SUCCESS);
    }

    @Test
    public void verifyDeleteFeatureIDException() throws Exception{
        doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(1L);
        ServiceStatus status = null;
        try {
            status = featureService.deleteByID(1L);
        } catch (RuntimeException e) {
            Assert.assertTrue("Expect entity not found for deletion", status == null);
        }
    }

  	
	@Test
    public void findAllByNameTest() throws Exception{
        ArrayList<Feature> matchedFeatures = new ArrayList<>(1);
        matchedFeatures.add(this.features.get(0));
        given(repository.findAllByName(any())).willReturn(matchedFeatures);
        List <Feature> resultFeatures = featureService.findAllByName(users.get(0).getName());
        Assert.assertNotNull(resultFeatures);
        Assert.assertTrue(resultFeatures.size() > 0);
        Assert.assertTrue(resultFeatures.get(0).getId() == this.features.get(0).getId());

    }

    @Test
    public void findAllByNameInvalidParamTest() throws Exception{
        ArrayList<Feature> matchedFeatures = new ArrayList<>(1);
        matchedFeatures.add(this.features.get(0));
        given(repository.findAllByName(any())).willReturn(matchedFeatures);
        List <Feature> resultFeatures = null;
        try {
            resultFeatures = featureService.findAllByName(null);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Invalid parameters passed", e != null && resultFeatures == null);
        }
    }




}
