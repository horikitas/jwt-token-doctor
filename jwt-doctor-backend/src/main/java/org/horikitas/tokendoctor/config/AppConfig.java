package org.horikitas.tokendoctor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.horikitas.tokendoctor.service.JwtDiagnosisService;
import org.horikitas.tokendoctor.service.rules.JwtRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public JwtDiagnosisService jwtDiagnosisService(
            ObjectMapper objectMapper,
            List<JwtRule> jwtRules
    ) {
        return new JwtDiagnosisService(objectMapper, jwtRules);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}