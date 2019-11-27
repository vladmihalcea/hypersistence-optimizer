package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.Config;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.ChainEventHandler;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.core.event.LogEventHandler;
import io.hypersistence.optimizer.core.exception.DefaultExceptionHandler;
import io.hypersistence.optimizer.core.exception.ExceptionHandler;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;
import io.hypersistence.optimizer.util.AbstractTest;
import org.junit.Test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Vlad Mihalcea
 */
public class ListEventHandlerTest extends AbstractTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
                Post.class,
                PostComment.class
        };
    }

    private ListEventHandler listEventHandler = new ListEventHandler();

    @Override
    protected void afterInit() {
        new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory())
                .addEventHandler(listEventHandler)
        ).init();
    }

    @Test
    public void test() {
        assertEventTriggered(1, EagerFetchingEvent.class);
    }

    protected void assertEventTriggered(int expectedCount, Class<? extends Event> eventClass) {
        int count = 0;

        for (Event event : listEventHandler.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                count++;
            }
        }

        assertSame(expectedCount, count);
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post {

        @Id
        private Long id;

        private String title;
    }

    @Entity(name = "PostComment")
    @Table(name = "post_comment")
    public static class PostComment {

        @Id
        private Long id;

        @ManyToOne
        private Post post;

        private String review;

    }
}