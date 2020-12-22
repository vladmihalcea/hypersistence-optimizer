package io.hypersistence.optimizer.forum;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * With this startup listener we are making sure that the {@link HypersistenceOptimizer} is being created and injected, and when it is being initiated the scan will be invoked.
 */
@ApplicationScoped
public class HypersistenceOptimizerStartupListener {

    @Inject
    EntityManager entityManager;

    public void onStartup(@Observes StartupEvent startupEvent) {
        new HypersistenceOptimizer(new JpaConfig(entityManager.getEntityManagerFactory()));
    }

}
