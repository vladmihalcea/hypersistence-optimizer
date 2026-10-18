package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.jdbc.event.caching.JdbcStatementCacheSizeEvent;
import io.hypersistence.optimizer.jdbc.event.session.MySQLUseLocalSessionStateEvent;
import io.hypersistence.optimizer.jdbc.event.timeout.JdbcSocketTimeoutEvent;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;
import io.hypersistence.optimizer.util.providers.Database;

/**
 * Hypersistence Optimizer reads the JDBC Driver settings from the database connection, no matter if they were set
 * via the JDBC URL, the {@code DataSource} setters, or the connection pool settings.
 * <p>
 * The MySQL JDBC Driver used by this test has the statement cache disabled, and it uses the default
 * {@code useLocalSessionState=false} setting, which adds extra database roundtrips. It also has no {@code socketTimeout},
 * so a thread waiting for a query result stays blocked if the TCP connection is silently dropped.
 * <p>
 * To run this test, you need a MySQL database, like the one provided by the {@code docker-compose.yml} file
 * in the root folder of this repository.
 */
public class MySQLJdbcDriverChecksTest extends AbstractHypersistenceOptimizerTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
        };
    }

    @Override
    protected Database database() {
        return Database.MYSQL;
    }

    @Override
    protected void verify() {
        for (Event event : hypersistenceOptimizer.getEvents()) {
            LOGGER.info("{} - {}", event.getClass().getSimpleName(), event.getDescription());
        }
        assertEventTriggered(1, JdbcStatementCacheSizeEvent.class);
        assertEventTriggered(1, MySQLUseLocalSessionStateEvent.class);
        assertEventTriggered(1, JdbcSocketTimeoutEvent.class);
    }
}
