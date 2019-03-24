package io.hypersistence.optimizer.config.mapping.association.fetching.eager;

import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Vlad Mihalcea
 */
public class EagerFetchingManyToOneTest extends AbstractHypersistenceOptimizerTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
                Post.class,
                PostComment.class
        };
    }

    @Override
    protected void verify() {
        List<Event> events = listEventHandler().getEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof EagerFetchingEvent);
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