package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.hibernate.event.configuration.query.QueryPlanCacheDisabledEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.statistics.StatisticsGenerationEvent;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;

import java.util.Properties;

/**
 * Hypersistence Optimizer checks the Hibernate 6 and 7 settings that affect the performance of the application:
 * <ul>
 *     <li>the {@code hibernate.query.plan_cache_enabled} setting disables the query plan cache</li>
 *     <li>the {@code hibernate.generate_statistics} setting collects statistics for every Session</li>
 * </ul>
 */
public class HibernateConfigurationChecksTest extends AbstractHypersistenceOptimizerTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
        };
    }

    @Override
    protected void additionalProperties(Properties properties) {
        properties.put("hibernate.query.plan_cache_enabled", Boolean.FALSE.toString());
    }

    @Override
    protected void verify() {
        assertEventTriggered(1, QueryPlanCacheDisabledEvent.class);
        assertEventTriggered(1, StatisticsGenerationEvent.class);
    }
}
