package io.hypersistence.optimizer.forum.pool;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.jdbc.event.server.DatabaseServer566701416Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Hypersistence Optimizer reads the settings of the connection pool used by Spring Boot, and it checks whether
 * the pool retires or validates its idle connections before the MySQL server closes them.
 * <p>
 * Since the MySQL {@code wait_timeout} is lowered to 60 seconds for these connections, and none of the pools
 * is configured to check its idle connections that often, the {@link DatabaseServer566701416Event} is triggered
 * for every connection pool that Spring Boot supports.
 * <p>
 * To run these tests, you need a MySQL database, like the one provided by the {@code docker-compose.yml} file
 * in the root folder of this repository.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:mysql://localhost/high_performance_java_persistence?sessionVariables=wait_timeout=" +
        AbstractMySQLConnectionPoolTest.WAIT_TIMEOUT_SECONDS,
    "spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver",
    "spring.datasource.username=mysql",
    "spring.datasource.password=admin",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public abstract class AbstractMySQLConnectionPoolTest {

    public static final int WAIT_TIMEOUT_SECONDS = 60;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    /**
     * Get the name of the connection pool, as reported by the {@link DatabaseServer566701416Event}.
     *
     * @return the connection pool name
     */
    protected abstract String connectionPool();

    @Test
    public void test() {
        List<DatabaseServer566701416Event> events = new ArrayList<>();
        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (event instanceof DatabaseServer566701416Event) {
                events.add((DatabaseServer566701416Event) event);
            }
        }
        assertEquals(1, events.size());

        DatabaseServer566701416Event event = events.get(0);
    }
}
