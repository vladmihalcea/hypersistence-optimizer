package io.hypersistence.optimizer.forum.pool;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.Connection9729576883Event;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.Connection9071557016Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The {@code hibernate.connection.provider_disables_autocommit} property tells Hibernate that the pool disables
 * the auto-commit mode, but the default HikariCP {@code autoCommit} setting is {@code true}, so the transactions would no longer be atomic.
 * Hypersistence Optimizer detects the mismatch, and triggers the {@link Connection9729576883Event}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
    "spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true"
})
public class HikariCPAutoCommitMismatchTest {

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Test
    public void test() {
        assertEquals(1, count(Connection9729576883Event.class));
        assertEquals(0, count(Connection9071557016Event.class));
    }

    private int count(Class<? extends Event> eventClass) {
        int count = 0;
        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (eventClass.isInstance(event)) {
                count++;
            }
        }
        return count;
    }
}
