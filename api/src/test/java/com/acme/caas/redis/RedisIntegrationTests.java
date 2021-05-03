package com.acme.caas.redis;

import com.acme.caas.config.WebConfigurer;
import com.acme.caas.service.RedisService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockServletContext;
import tech.jhipster.config.JHipsterProperties;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RedisIntegrationTests {

//    @Autowired
//    private RedisService redisClient;
//
//    @BeforeAll
//    public void setupAll() {
//
//    }
//
//    @BeforeEach
//    public void setupEach() {
//
//    }
//
//    @Test
//    public void fullCircleTest(){
//
//    }
}
