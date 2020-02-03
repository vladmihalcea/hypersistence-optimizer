package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.util.AbstractTest;
import org.junit.Test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static org.junit.Assert.assertTrue;

/**
 * @author Vlad Mihalcea
 */
public class FailFastOnPerformanceIssuesTest extends AbstractTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
            Post.class,
            PostComment.class
        };
    }

    @Test(expected = AssertionError.class)
    public void testNoPerformanceIssues() {
        HypersistenceOptimizer hypersistenceOptimizer = new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory())
        );

        assertTrue(hypersistenceOptimizer.getEvents().isEmpty());
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