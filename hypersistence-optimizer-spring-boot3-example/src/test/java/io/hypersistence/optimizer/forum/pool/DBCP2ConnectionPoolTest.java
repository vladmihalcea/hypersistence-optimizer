package io.hypersistence.optimizer.forum.pool;

import org.springframework.test.context.TestPropertySource;

/**
 * The Apache Commons DBCP2 connection pool, selected via the {@code spring.datasource.type} property, has no idle connection evictor by default.
 */
@TestPropertySource(properties = "spring.datasource.type=org.apache.commons.dbcp2.BasicDataSource")
public class DBCP2ConnectionPoolTest extends AbstractMySQLConnectionPoolTest {

    @Override
    protected String connectionPool() {
        return "DBCP2";
    }
}
