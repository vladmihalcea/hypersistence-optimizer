package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.EventFilter;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.SkipAutoCommitCheckEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.dialect.DialectVersionEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.identifier.PostInsertGeneratorEvent;
import io.hypersistence.optimizer.util.AbstractTest;
import io.hypersistence.optimizer.util.providers.Database;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Vlad Mihalcea
 */
public class EventFilterTest extends AbstractTest {

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

    @Test
    public void test() {

        ListEventHandler listEventHandler = new ListEventHandler();

        new HypersistenceOptimizer(
                new JpaConfig(entityManagerFactory())
                .setEventHandler(listEventHandler)
                .setEventFilter(new EventFilter() {
                    @Override
                    public boolean accept(Event event) {
                        if(event instanceof PostInsertGeneratorEvent) {
                            return false;
                        }
                        return true;
                    }
                })
        ).init();

        List<Event> events = listEventHandler.getEvents();
        assertTrue(events.isEmpty());
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