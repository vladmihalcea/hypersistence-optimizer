package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.forum.domain.Post;
import io.hypersistence.optimizer.forum.domain.Tag;
import io.hypersistence.optimizer.forum.service.ForumService;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.Connection9071557016Event;
import io.hypersistence.optimizer.hibernate.event.configuration.query.Query7786258879Event;
import io.hypersistence.optimizer.hibernate.event.configuration.query.Query2468924130Event;
import io.hypersistence.optimizer.hibernate.event.configuration.schema.SchemaGenerationEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.Association3890524098Event;
import io.hypersistence.optimizer.hibernate.event.mapping.association.Association934543432Event;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneWithoutMapsIdEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.query.QueryEvent;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Vlad Mihalcea
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringHibernateXmlTest {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ForumService forumService;

    private HypersistenceOptimizer hypersistenceOptimizer;

    @Before
    public void init() {
        hypersistenceOptimizer = new HypersistenceOptimizer(
            new HibernateConfig(
                sessionFactory
            )
        );

        try {
            transactionTemplate.execute((TransactionCallback<Void>) transactionStatus -> {
                Tag hibernate = new Tag();
                hibernate.setName("hibernate");
                sessionFactory.getCurrentSession().persist(hibernate);

                Tag jpa = new Tag();
                jpa.setName("jpa");
                sessionFactory.getCurrentSession().persist(jpa);
                return null;
            });
        } catch (TransactionException e) {
            LOGGER.error("Failure", e);
        }
    }

    @Test
    public void test() {
        assertEventTriggered(2, EagerFetchingEvent.class);
        assertEventTriggered(1, Association3890524098Event.class);
        assertEventTriggered(1, Association934543432Event.class);
        assertEventTriggered(1, OneToOneWithoutMapsIdEvent.class);
        assertEventTriggered(1, Connection9071557016Event.class);
        assertEventTriggered(1, SchemaGenerationEvent.class);
        assertEventTriggered(1, Query2468924130Event.class);
        assertEventTriggered(1, Query7786258879Event.class);

        Post newPost = forumService.newPost("High-Performance Java Persistence", "hibernate", "jpa");
        assertNotNull(newPost.getId());

        List<Post> posts = forumService.findAllByTitle("High-Performance Java Persistence");
        assertEquals(1, posts.size());

        Post post = forumService.findById(newPost.getId());
        assertEquals("High-Performance Java Persistence", post.getTitle());

        assertEventTriggered(0, QueryEvent.class);
        assertEquals(1, forumService.findAll(5).size());
        assertEventTriggered(1, QueryEvent.class);
    }

    protected void assertEventTriggered(int expectedCount, Class<? extends Event> eventClass) {
        int count = 0;

        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                count++;
            }
        }

        assertSame(expectedCount, count);
    }
}
