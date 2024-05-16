package io.hypersistence.optimizer.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.Config;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.hibernate.decorator.HypersistenceHibernatePersistenceProvider;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.session.SessionTimeoutEvent;
import io.hypersistence.optimizer.util.AbstractTest;
import io.hypersistence.optimizer.util.transaction.JPATransactionVoidFunction;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.junit.Test;

import javax.persistence.*;

import java.util.Collections;

import static org.junit.Assert.assertSame;

/**
 * @author Vlad Mihalcea
 */
public class RuntimeConfigurationPropertiesTest extends AbstractTest {

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
        };
    }

    private ListEventHandler listEventHandler = new ListEventHandler();

    @Override
    protected SessionFactory newSessionFactory() {
        return HypersistenceHibernatePersistenceProvider.decorate(
            super.newSessionFactory()
        );
    }

    @Override
    protected void afterInit() {
        new HypersistenceOptimizer(
            new HibernateConfig(sessionFactory())
                .addEventHandler(listEventHandler)
                .setProperties(
                    Collections.singletonMap(
                        Config.Property.Session.TIMEOUT_MILLIS,
                        25
                    )
                )
        );
    }

    @Test
    public void test() {
        doInHibernate(session -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        });

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
}