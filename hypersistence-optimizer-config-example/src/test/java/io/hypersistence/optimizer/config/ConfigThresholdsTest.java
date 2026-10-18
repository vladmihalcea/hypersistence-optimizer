package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.ConfigThresholds;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.hibernate.event.query.QueryResultListSizeEvent;
import io.hypersistence.optimizer.hibernate.event.session.SessionTimeoutEvent;
import io.hypersistence.optimizer.util.AbstractTest;
import org.junit.Test;

import jakarta.persistence.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * The thresholds the checks compare the application settings against can be customized
 * using the {@link ConfigThresholds} builder.
 *
 * @author Vlad Mihalcea
 */
public class ConfigThresholdsTest extends AbstractTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
            Post.class
        };
    }

    private ListEventHandler listEventHandler = new ListEventHandler();

    @Override
    protected void afterInit() {
        new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory())
                .addEventHandler(listEventHandler)
                .setThresholds(
                    ConfigThresholds.builder()
                        .setSessionTimeoutMillis(25)
                        .setQueryMaxResultSize(2)
                        .build()
                )
        );
    }

    @Test
    public void test() {
        doInJPA(entityManager -> {
            for (long id = 1; id <= 3; id++) {
                Post post = new Post();
                post.setId(id);
                post.setTitle(String.format("High-Performance Java Persistence, part %d", id));
                entityManager.persist(post);
            }
        });

        listEventHandler.getEvents().clear();

        doInJPA(entityManager -> {
            assertEquals(
                3,
                entityManager.createQuery("select p from Post p", Post.class)
                    .getResultList()
                    .size()
            );
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        });

        assertEventTriggered(1, QueryResultListSizeEvent.class);
        assertEventTriggered(1, SessionTimeoutEvent.class);
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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}