package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.util.AbstractTest;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * By default, the mapping and the configuration scanners run when the {@link HypersistenceOptimizer} is instantiated,
 * so the application bootstrap waits for them.
 * <p>
 * With the {@code setAsyncMetadataScannersEnabled(true)} option, they run in background threads, so they don't
 * delay the application bootstrap, and you can wait for them using the
 * {@link HypersistenceOptimizer#awaitMetadataScanners(long, TimeUnit)} method.
 */
public class AsyncMetadataScannersTest extends AbstractTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
            Post.class,
            PostComment.class
        };
    }

    @Test
    public void test() throws InterruptedException {
        HypersistenceOptimizer hypersistenceOptimizer = new HypersistenceOptimizer(
            new HibernateConfig(sessionFactory())
                .setAsyncMetadataScannersEnabled(true)
        );

        assertTrue(hypersistenceOptimizer.awaitMetadataScanners(30, TimeUnit.SECONDS));

        int count = 0;
        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (event instanceof EagerFetchingEvent) {
                count++;
            }
        }
        assertSame(1, count);
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

        private String review;

        @ManyToOne
        private Post post;
    }
}
