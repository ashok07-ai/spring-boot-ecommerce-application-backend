package com.ecommerce.project.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application-wide beans.
 * This class provides centralized bean definitions to be used across the application.
 */
@Configuration
public class AppConfig {

    /**
     * Defines a bean for the ModelMapper instance.
     * ModelMapper is a library used for object mapping, allowing easy transformation
     * between DTOs (Data Transfer Objects) and entity objects.
     *
     * @return A new instance of ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
