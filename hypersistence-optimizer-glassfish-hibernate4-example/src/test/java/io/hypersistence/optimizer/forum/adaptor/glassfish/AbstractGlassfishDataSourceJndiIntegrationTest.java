package io.hypersistence.optimizer.forum.adaptor.glassfish;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.forum.domain.Post;
import io.hypersistence.optimizer.hibernate.event.configuration.schema.SchemaGenerationEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.ManyToManyListEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneParentSideEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneWithoutMapsIdEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.query.PaginationWithoutOrderByEvent;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(Arquillian.class)
public abstract class AbstractGlassfishDataSourceJndiIntegrationTest {

    private HypersistenceOptimizer hypersistenceOptimizer;

    @Inject
    private UserTransaction userTransaction;

    @Before
    public void init() throws Exception {
        hypersistenceOptimizer = new HypersistenceOptimizer(
            new JpaConfig(
                getEntityManagerFactory()
            )
        );
    }

    @Test
    public void test() throws Exception {
        assertEventTriggered(2, EagerFetchingEvent.class);
        assertEventTriggered(1, ManyToManyListEvent.class);
        assertEventTriggered(1, OneToOneParentSideEvent.class);
        assertEventTriggered(1, OneToOneWithoutMapsIdEvent.class);
        assertEventTriggered(1, SchemaGenerationEvent.class);

        doInTransaction(() -> {
            Post post = new Post();
            post.setTitle("High-Performance Java Persistence");
            getEntityManager().persist(post);
        });

        doInTransaction(() -> {
            assertEventTriggered(0, PaginationWithoutOrderByEvent.class);

            List<Post> posts = getEntityManager().createQuery(
                "select p " +
                "from Post p ", Post.class)
            .setMaxResults(5)
            .getResultList();

            assertEquals(1, posts.size());

            assertEventTriggered(1, PaginationWithoutOrderByEvent.class);
        });
    }

    private void doInTransaction(VoidCallable callable) {
        try {
            userTransaction.begin();
            getEntityManager().joinTransaction();
            callable.call();
            userTransaction.commit();
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (SystemException e1) {
                throw new IllegalStateException(e);
            }
            throw new IllegalStateException(e);
        }
    }

    public interface VoidCallable {
        void call();
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

    protected abstract EntityManager getEntityManager();

    protected abstract EntityManagerFactory getEntityManagerFactory();
}
