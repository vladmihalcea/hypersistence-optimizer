package io.hypersistence.optimizer.forum.pool;

/**
 * HikariCP is the default Spring Boot connection pool, and its default {@code maxLifetime} is 30 minutes.
 */
public class HikariCPConnectionPoolTest extends AbstractMySQLConnectionPoolTest {

    @Override
    protected String connectionPool() {
        return "HikariCP";
    }
}
