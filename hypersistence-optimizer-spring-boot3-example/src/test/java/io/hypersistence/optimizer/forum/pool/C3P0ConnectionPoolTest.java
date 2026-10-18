package io.hypersistence.optimizer.forum.pool;

import org.springframework.test.context.TestPropertySource;

/**
 * The c3p0 connection pool, selected via the {@code spring.datasource.type} property, neither tests nor closes the idle connections by default.
 */
@TestPropertySource(properties = "spring.datasource.type=com.mchange.v2.c3p0.ComboPooledDataSource")
public class C3P0ConnectionPoolTest extends AbstractMySQLConnectionPoolTest {

    @Override
    protected String connectionPool() {
        return "c3p0";
    }
}
