package io.hypersistence.optimizer.forum.pool;

import org.springframework.test.context.TestPropertySource;

/**
 * The Oracle Universal Connection Pool, selected via the {@code spring.datasource.type} property, works with any JDBC Driver,
 * but it doesn't validate the idle connections.
 */
@TestPropertySource(properties = "spring.datasource.type=oracle.ucp.jdbc.PoolDataSourceImpl")
public class OracleUcpConnectionPoolTest extends AbstractMySQLConnectionPoolTest {

    @Override
    protected String connectionPool() {
        return "UCP";
    }
}
