package com.acme.caas.redis;

import com.acme.caas.config.WebConfigurer;
import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.service.RedisService;
import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import io.cucumber.java.bs.A;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import tech.jhipster.config.JHipsterProperties;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("dev")
public class RedisIntegrationTests {

    @Autowired
    private RedisService redisClient;

    @Autowired
    private JReJSON redis;

    @BeforeAll
    public static void setupAll() {

    }

    @BeforeEach
    public void setupEach() {
        redis.del("auto-generated-test");
    }

    @Test
    public void createDeleteTest() throws Exception {

        var template = getInitialTemplate();

        redisClient.createTemplate(template);

        Assert.assertNotNull(redis.get(template.getSettingsId()));

        redisClient.deleteTemplate(template);


        Assert.assertThrows(NullPointerException.class,() -> {
            redis.get(template.getSettingsId());
        });
    }

    @Test
    public void createUpdateTest() throws Exception {

        //Build a CaasTemplate to test with
        var template = getInitialTemplate();

        //You can't update a template that isn't already created
        Assert.assertThrows(Exception.class, () -> {
            redisClient.updateTemplate(template);
        });

        //add the entire template to redis
        redisClient.createTemplate(template);
        Assert.assertNotNull(redis.get(template.getSettingsId()));

        //Update the name of the template
        var newName = "Updated Name";
        redisClient.updateTemplateName(template.getSettingsId(),newName);
        Assert.assertEquals(newName, redis.get(template.getSettingsId(), String.class,new Path(".templateName")));

        //Update the template setting with key of 'setting1'
        var settingKey = "setting1";
        var newSettingValue = "newValue";
        redisClient.updateTemplateData(template.getSettingsId(),settingKey,newSettingValue);
        Assert.assertEquals(newSettingValue,redis.get(template.getSettingsId(),String.class, new Path(".templateSettings."+ settingKey)));

        //Delete the template setting with key 'setting1'
        redisClient.deleteTemplateData(template.getSettingsId(),settingKey);
        Assert.assertThrows(Exception.class, () -> {
            redis.get(template.getSettingsId(),String.class,new Path(".templateSettings."+settingKey));
        });

        //Get a CaasTemplate from redis with key 'auto-generated-test'
        var retrievedTemplate = redisClient.getTemplate(template.getSettingsId());
        var expectedTemplate = getInitialTemplate();
        expectedTemplate.setTemplateName(newName);
        expectedTemplate.setTemplateSettings(Map.of("setting2","value2"));
        Assert.assertNotNull(retrievedTemplate); //the retrieved template isn't null
        Assert.assertEquals(retrievedTemplate,expectedTemplate); //the retrieved template is what was expected

        //Get the setting value for the setting with key 'setting2'
        Assert.assertEquals("value2",redisClient.getTemplateSettings(template.getSettingsId(), "setting2"));

        //Set the entire template in Redis back to the original template
        redisClient.updateTemplate(template);
        Assert.assertEquals(template, redisClient.getTemplate(template.getSettingsId()));
        //no..... I'm not using redisClient to verify against itself...noooo. Move on.. nothing to see here

        //Delete the template
        redisClient.deleteTemplate(template.getSettingsId());
        Assert.assertThrows(Exception.class, () -> {
            redis.get(template.getSettingsId());
        });

    }

    public CaaSTemplate getInitialTemplate(){
        return CaaSTemplate.builder()
            .settingsId("auto-generated-test")
            .templateName("Testing Template")
            .templateSettings(Map.of("setting1", "value1","setting2","value2"))
            .build();
    }


}
