package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.hibernate.event.configuration.statistics.StatisticsGenerationEvent;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;

/**
 * Hypersistence Optimizer checks the Hibernate settings that affect the performance of the application,
 * like the {@code hibernate.generate_statistics} setting, which collects statistics for every Session.
 */
public class HibernateConfigurationChecksTest extends AbstractHypersistenceOptimizerTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
        };
    }

    @Override
    protected void verify() {
        assertEventTriggered(1, StatisticsGenerationEvent.class);
    }
}
