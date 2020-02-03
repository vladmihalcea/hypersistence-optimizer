package io.hypersistence.optimizer.forum;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
public class HypersistenceConfiguration {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public HypersistenceOptimizer hypersistenceOptimizer() {
        return new HypersistenceOptimizer(
            new JpaConfig(
                entityManagerFactory
            )
        );
    }
}
