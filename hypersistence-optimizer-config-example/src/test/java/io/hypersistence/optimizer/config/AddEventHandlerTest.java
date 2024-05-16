package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.ChainEventHandler;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.util.AbstractTest;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * @author Vlad Mihalcea
 */
public class AddEventHandlerTest extends AbstractTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
            Post.class,
            PostComment.class
        };
    }

    private final List<String> tipsUrls = new ArrayList<>();

    @Override
    protected void afterInit() {
        new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory())
                .addEventHandler(
                    event -> {
                        tipsUrls.add(event.getInfoUrl());
                    }
                )
        );
    }

    @Test
    public void test() {
        assertEventTriggered(1, EagerFetchingEvent.class);

        assertFalse(tipsUrls.isEmpty());
    }

    protected void assertEventTriggered(int expectedCount, Class<? extends Event> eventClass) {
        int count = 0;

        for (Event event : hypersistenceOptimizer().getEvents()) {
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