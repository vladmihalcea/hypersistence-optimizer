package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.mapping.EntityMappingEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.identifier.Identifier192869363Event;
import io.hypersistence.optimizer.util.AbstractTest;
import io.hypersistence.optimizer.util.providers.Database;
import org.junit.Test;

import jakarta.persistence.*;

import static org.junit.Assert.fail;

/**
 * @author Vlad Mihalcea
 */
public class EventFilterTest extends AbstractTest {

    private HypersistenceOptimizer hypersistenceOptimizer;

    @Override
    protected Database database() {
        return Database.MYSQL;
    }

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
            Post.class
        };
    }

    @Override
    protected void afterInit() {
        hypersistenceOptimizer = new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory())
                .setEventFilter(event -> !(event instanceof Identifier192869363Event))
        );
    }

    @Test
    public void test() {
        assertNoEventTriggered(EntityMappingEvent.class);
    }

    protected void assertNoEventTriggered(Class<? extends Event> baseClass) {
        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (baseClass.isAssignableFrom(event.getClass())) {
                fail("The " + event + " was unexpectedly triggered!");
            }
        }
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;
    }
}