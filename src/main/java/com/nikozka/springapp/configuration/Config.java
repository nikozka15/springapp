package com.nikozka.springapp.configuration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Validator innValidator() {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
