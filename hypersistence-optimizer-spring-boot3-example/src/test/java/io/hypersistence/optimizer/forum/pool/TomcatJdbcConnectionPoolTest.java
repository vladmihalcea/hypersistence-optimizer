package io.hypersistence.optimizer.forum.pool;

import org.springframework.test.context.TestPropertySource;

/**
 * The Tomcat JDBC connection pool, selected via the {@code spring.datasource.type} property, doesn't validate the idle connections by default.
 */
@TestPropertySource(properties = "spring.datasource.type=org.apache.tomcat.jdbc.pool.DataSource")
public class TomcatJdbcConnectionPoolTest extends AbstractMySQLConnectionPoolTest {

    @Override
    protected String connectionPool() {
        return "Tomcat JDBC";
    }
}
