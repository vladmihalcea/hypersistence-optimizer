package io.hypersistence.optimizer.forum;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.hibernate.decorator.HypersistenceHibernatePersistenceProvider;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class HypersistenceConfiguration {

    @Bean
    public HypersistenceOptimizer hypersistenceOptimizer(
            EntityManagerFactory entityManagerFactory) {
        return new HypersistenceOptimizer(
            new JpaConfig(
                entityManagerFactory
            )
        );
    }

}
