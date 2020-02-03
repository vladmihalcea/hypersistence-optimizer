package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.jdbc.event.monitoring.JdbcMonitoring7934948874Event;
import io.hypersistence.optimizer.jdbc.event.timeout.JdbcTimeout3359973659Event;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;
import io.hypersistence.optimizer.util.providers.Database;

/**
 * Hypersistence Optimizer checks the PostgreSQL JDBC Driver reliability and observability settings:
 * <ul>
 *     <li>without a {@code socketTimeout}, a thread waiting for a query result stays blocked forever
 *     if the TCP connection is silently dropped,</li>
 *     <li>with the default {@code ApplicationName}, the {@code pg_stat_activity} view cannot tell
 *     which application executes a given statement.</li>
 * </ul>
 * <p>
 * To run this test, you need a PostgreSQL database, like the one provided by the {@code docker-compose.yml} file
 * in the root folder of this repository.
 */
public class PostgreSQLJdbcDriverChecksTest extends AbstractHypersistenceOptimizerTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
        };
    }

    @Override
    protected Database database() {
        return Database.POSTGRESQL;
    }

    @Override
    protected void verify() {
        for (Event event : hypersistenceOptimizer.getEvents()) {
        }
        assertEventTriggered(1, JdbcMonitoring7934948874Event.class);
        assertEventTriggered(1, JdbcTimeout3359973659Event.class);
    }
}
