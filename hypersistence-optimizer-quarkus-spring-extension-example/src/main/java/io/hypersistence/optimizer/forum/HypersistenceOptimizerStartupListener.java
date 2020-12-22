package io.hypersistence.optimizer.forum;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.quarkus.runtime.StartupEvent;
import org.springframework.stereotype.Component;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * With this startup listener we are making sure that the {@link HypersistenceOptimizer} is being created and injected, and when it is being initiated the scan will be invoked.
 */
@Component
public class HypersistenceOptimizerStartupListener {

    @Inject
    HypersistenceOptimizer hypersistenceOptimizer;

    public void onStartup(@Observes StartupEvent startupEvent) {

    }

}
