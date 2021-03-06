package com.acme.caas.cucumber;

import com.acme.caas.ApiApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ApiApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
