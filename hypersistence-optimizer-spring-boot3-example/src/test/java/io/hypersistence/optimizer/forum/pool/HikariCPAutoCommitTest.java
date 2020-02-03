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
 * When HikariCP disables the auto-commit mode, and Hibernate is told so via the
 * {@code hibernate.connection.provider_disables_autocommit} property, Hibernate can acquire the database connection lazily,
 * so neither the {@link Connection9071557016Event} nor the {@link Connection9729576883Event} is triggered.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
    "spring.datasource.hikari.auto-commit=false",
    "spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true"
})
public class HikariCPAutoCommitTest {

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Test
    public void test() {
        assertEquals(0, count(Connection9071557016Event.class));
        assertEquals(0, count(Connection9729576883Event.class));
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
