package io.hypersistence.optimizer.util;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.Config;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.core.exception.DefaultExceptionHandler;
import io.hypersistence.optimizer.util.providers.DataSourceProvider;
import io.hypersistence.optimizer.util.providers.Database;
import io.hypersistence.optimizer.util.transaction.HibernateTransactionConsumer;
import io.hypersistence.optimizer.util.transaction.HibernateTransactionFunction;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertSame;

public abstract class AbstractTest {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SessionFactory sf;

    private List<Exception> exceptions = new ArrayList<Exception>();

    private ListEventHandler listEventHandler = new ListEventHandler();

    @Before
    public void init() {
        sf = newSessionFactory();
        afterInit();
    }

    protected void afterInit() {

    }

    @After
    public void destroy() {
            sf.close();
    }

    public SessionFactory sessionFactory() {
        return sf;
    }

    protected abstract Class<?>[] entities();

    protected List<String> entityClassNames() {
        List<String> classNames = new ArrayList<String>();
        for (Class entityClass : entities()) {
            classNames.add(entityClass.getName());
        }
        return classNames;
    }

    protected String[] packages() {
        return null;
    }

    protected String[] resources() {
        return null;
    }

    protected Interceptor interceptor() {
        return null;
    }

    protected SessionFactory newSessionFactory() {
        Properties properties = properties();
        AnnotationConfiguration configuration = new AnnotationConfiguration().addProperties(properties);
        for (Class<?> entityClass : entities()) {
            configuration.addAnnotatedClass(entityClass);
        }
        String[] resources = resources();
        if (resources != null) {
            for (String resource : resources) {
                configuration.addResource(resource);
            }
        }

        return configuration.buildSessionFactory();
    }

    protected Properties properties() {
        Properties properties = new Properties();
        DataSourceProvider dataSourceProvider = database().dataSourceProvider();
        properties.put("hibernate.dialect", dataSourceProvider.hibernateDialect());
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put(Environment.URL, dataSourceProvider.url());
        properties.put(Environment.USER, dataSourceProvider.username());
        properties.put(Environment.PASS, dataSourceProvider.password());
        properties.put("hibernate.generate_statistics", Boolean.TRUE.toString());
        properties.put("hibernate.connection.provider_disables_autocommit", Boolean.TRUE.toString());
        additionalProperties(properties);
        return properties;
    }

    protected void additionalProperties(Properties properties) {

    }

    protected DataSourceProxyType dataSourceProxyType() {
        return DataSourceProxyType.DATA_SOURCE_PROXY;
    }

    protected DataSource newDataSource() {
        DataSource dataSource =
            proxyDataSource()
                ? dataSourceProxyType().dataSource(dataSourceProvider().dataSource())
                : dataSourceProvider().dataSource();
        return dataSource;
    }

    protected boolean proxyDataSource() {
        return true;
    }

    protected DataSourceProvider dataSourceProvider() {
        return database().dataSourceProvider();
    }

    protected Database database() {
        return Database.HSQLDB;
    }

    protected HypersistenceOptimizer hypersistenceOptimizer() {
        Config config = new HibernateConfig(sessionFactory())
            .addEventHandler(listEventHandler)
            .setExceptionHandler(e -> {
                DefaultExceptionHandler.INSTANCE.handle(e);
                exceptions.add(e);
            });

        return new HypersistenceOptimizer(config);
    }

    public ListEventHandler listEventHandler() {
        return listEventHandler;
    }

    public List<Exception> exceptions() {
        return exceptions;
    }

    protected <T> T doInHibernate(HibernateTransactionFunction<T> callable) {
        T result = null;
        Session session = null;
        Transaction txn = null;
        try {
            session = sessionFactory().openSession();
            callable.beforeTransactionCompletion();
            txn = session.beginTransaction();

            result = callable.apply(session);
            if (txn.isActive()) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null && txn.isActive()) {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
            throw new RuntimeException(t);
        } finally {
            callable.afterTransactionCompletion();
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    protected void doInHibernate(HibernateTransactionConsumer callable) {
        Session session = null;
        Transaction txn = null;
        try {
            session = sessionFactory().openSession();
            callable.beforeTransactionCompletion();
            txn = session.beginTransaction();

            callable.accept(session);
            if (txn.isActive()) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null && txn.isActive()) {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
            throw new RuntimeException(t);
        } finally {
            callable.afterTransactionCompletion();
            if (session != null) {
                session.close();
            }
        }
    }

    protected void assertNoEventTriggered() {
        int count = 0;

        for (Event event : listEventHandler.getEvents()) {
            count++;
        }

        assertSame(0, count);
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

    protected <T extends Event> T getTriggeredEvent(Class<T> eventClass) {
        for (Event event : listEventHandler.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                return (T) event;
            }
        }
        return null;
    }

    protected <T extends Event> List<T> getTriggeredEvents(Class<T> eventClass) {
        List<T> matchingEvents = new ArrayList<T>();

        for (Event event : listEventHandler.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                matchingEvents.add((T) event);
            }
        }
        return matchingEvents;
    }
}
