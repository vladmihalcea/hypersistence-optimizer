package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.hibernate.event.query.MySQLResultSetStreamingEvent;
import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;
import io.hypersistence.optimizer.util.providers.Database;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unless the {@code useCursorFetch} property is enabled, the MySQL JDBC Driver loads the entire result set
 * into memory, even if the query is streamed with a fetch size. Hypersistence Optimizer detects it at runtime,
 * so you know that streaming doesn't reduce the memory footprint of your application.
 * <p>
 * To run this test, you need a MySQL database, like the one provided by the {@code docker-compose.yml} file
 * in the root folder of this repository.
 */
public class MySQLResultSetStreamingTest extends AbstractHypersistenceOptimizerTest {

    public static final int POST_COUNT = 10;

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
            Post.class
        };
    }

    @Override
    protected Database database() {
        return Database.MYSQL;
    }

    @Override
    protected void additionalProperties(Properties properties) {
        //The MySQL DataSource connections are in auto-commit mode, so Hibernate must disable it
        properties.put("hibernate.connection.provider_disables_autocommit", Boolean.FALSE.toString());
    }

    @Override
    protected void verify() {
        doInJPA(entityManager -> {
            for (long id = 1; id <= POST_COUNT; id++) {
                Post post = new Post();
                post.id = id;
                post.title = String.format("High-Performance Java Persistence, part %d", id);
                entityManager.persist(post);
            }
        });

        hypersistenceOptimizer.getEvents().clear();

        doInJPA(entityManager -> {
            try (Stream<Post> posts = entityManager
                .createQuery("select p from Post p", Post.class)
                .setHint("org.hibernate.fetchSize", 100)
                .getResultStream()) {
                assertEquals(POST_COUNT, posts.count());
            }
        });

        assertEventTriggered(1, MySQLResultSetStreamingEvent.class);
        MySQLResultSetStreamingEvent event = getTriggeredEvent(MySQLResultSetStreamingEvent.class);
        LOGGER.info(event.getDescription());
        assertTrue(event.getDescription().contains("[useCursorFetch]"));
    }

    @Entity(name = "Post")
    @Table(name = "streaming_post")
    public static class Post {

        @Id
        private Long id;

        private String title;
    }
}
