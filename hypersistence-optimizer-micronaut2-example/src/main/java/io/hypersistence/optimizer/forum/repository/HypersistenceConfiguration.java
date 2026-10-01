package io.hypersistence.optimizer.forum.repository;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.Config;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.micronaut.context.annotation.Factory;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;

@Factory
public class HypersistenceConfiguration {

    @Singleton
    public HypersistenceOptimizer hypersistenceOptimizer(SessionFactory sessionFactory) {
        return new HypersistenceOptimizer(
            new HibernateConfig(
                sessionFactory
            )
            .setEventPersistence(
                new Config.EventPersistence()
                    .setEnabled(true)
                    .setApplicationName("micronaut2-example")
                    .setWebAppEnabled(true)
                    .setWebAppPort(0)
            )
        );
    }
}
