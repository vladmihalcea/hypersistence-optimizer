package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.jdbc.event.server.PostgreSQLAutoExplainEvent;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;
import io.hypersistence.optimizer.util.providers.Database;

/**
 * The PostgreSQL {@code auto_explain} module logs the actual execution plan of the slow queries, which you might not
 * be able to reproduce with {@code EXPLAIN ANALYZE} afterward. Hypersistence Optimizer detects that the module is not loaded,
 * and tells you how to enable it:
 * <pre>
 * session_preload_libraries = 'auto_explain'
 * auto_explain.log_min_duration = '100ms'
 * auto_explain.log_analyze = on
 * </pre>
 * <p>
 * To run this test, you need a PostgreSQL database, like the one provided by the {@code docker-compose.yml} file
 * in the root folder of this repository.
 */
public class PostgreSQLAutoExplainTest extends AbstractHypersistenceOptimizerTest {

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
        assertEventTriggered(1, PostgreSQLAutoExplainEvent.class);
        LOGGER.info(getTriggeredEvent(PostgreSQLAutoExplainEvent.class).getDescription());
    }
}
