package com.gocaspi.taskfly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
/**
 * Class for MongoValidationConfig
 */
@Configuration
public class MongoValidationConfig {
    /**
     *
     * @param factory LocalValidatorFactoryBean
     * @return ValidatingMongoEventListener
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }
}
