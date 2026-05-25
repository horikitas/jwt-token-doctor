package org.horikitas.tokendoctor.cucumber;

import org.horikitas.tokendoctor.JwtDoctorApplication;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@CucumberContextConfiguration
@SpringBootTest(classes = JwtDoctorApplication.class)
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}
