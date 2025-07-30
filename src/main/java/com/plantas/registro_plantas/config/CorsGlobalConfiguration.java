package com.plantas.registro_plantas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsGlobalConfiguration {

    @Bean
    public WebFluxConfigurer corsConfigurer(){
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(org.springframework.web.reactive.config.CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }            
            
        };
    }
    
}
