package io.hypersistence.optimizer.util;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.Config;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.hypersistence.optimizer.core.event.ChainEventHandler;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.core.event.LogEventHandler;
import io.hypersistence.optimizer.core.exception.DefaultExceptionHandler;
import io.hypersistence.optimizer.core.exception.ExceptionHandler;
import io.hypersistence.optimizer.util.providers.DataSourceProvider;
import io.hypersistence.optimizer.util.providers.Database;
import io.hypersistence.optimizer.util.transaction.HibernateTransactionConsumer;
import io.hypersistence.optimizer.util.transaction.HibernateTransactionFunction;
import io.hypersistence.optimizer.util.transaction.JPATransactionFunction;
import io.hypersistence.optimizer.util.transaction.JPATransactionVoidFunction;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.util.*;

import static org.junit.Assert.assertSame;

public abstract class AbstractTest {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private EntityManagerFactory emf;

    private SessionFactory sf;

    private List<Exception> exceptions = new ArrayList<Exception>();

    private ListEventHandler listEventHandler = new ListEventHandler();

    @Before
    public void init() {
        if (nativeHibernateSessionFactoryBootstrap()) {
            sf = newSessionFactory();
        } else {
            emf = newEntityManagerFactory();
        }
        afterInit();
    }

    protected void afterInit() {

    }

    @After
    public void destroy() {
        if (nativeHibernateSessionFactoryBootstrap()) {
            sf.close();
        } else {
            emf.close();
        }
    }

    public EntityManagerFactory entityManagerFactory() {
        return emf;
    }

    public SessionFactory sessionFactory() {
        return nativeHibernateSessionFactoryBootstrap() ? sf : entityManagerFactory().unwrap(SessionFactory.class);
    }

    protected boolean nativeHibernateSessionFactoryBootstrap() {
        return false;
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

    private SessionFactory newSessionFactory() {
        final BootstrapServiceRegistryBuilder bsrb = new BootstrapServiceRegistryBuilder()
                .enableAutoClose();

        final BootstrapServiceRegistry bsr = bsrb.build();

        final StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder(bsr)
                .applySettings(properties())
                .build();

        final MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        for (Class annotatedClass : entities()) {
            metadataSources.addAnnotatedClass(annotatedClass);
        }

        String[] packages = packages();
        if (packages != null) {
            for (String annotatedPackage : packages) {
                metadataSources.addPackage(annotatedPackage);
            }
        }

        String[] resources = resources();
        if (resources != null) {
            for (String resource : resources) {
                metadataSources.addResource(resource);
            }
        }

        final MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();
        metadataBuilder.enableNewIdentifierGeneratorSupport(true);

        MetadataImplementor metadata = (MetadataImplementor) metadataBuilder.build();

        final SessionFactoryBuilder sfb = metadata.getSessionFactoryBuilder();
        Interceptor interceptor = interceptor();
        if (interceptor != null) {
            sfb.applyInterceptor(interceptor);
        }

        return sfb.build();
    }

    protected EntityManagerFactory newEntityManagerFactory() {
        PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo(getClass().getSimpleName());
        Map<String, Object> configuration = new HashMap<String, Object>();
        configuration.put(AvailableSettings.INTERCEPTOR, interceptor());

        EntityManagerFactoryBuilderImpl entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(
                new PersistenceUnitInfoDescriptor(persistenceUnitInfo), configuration
        );
        return entityManagerFactoryBuilder.build();
    }

    protected PersistenceUnitInfoImpl persistenceUnitInfo(String name) {
        PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl(
                name, entityClassNames(), properties()
        );
        String[] resources = resources();
        if (resources != null) {
            persistenceUnitInfo.getMappingFileNames().addAll(Arrays.asList(resources));
        }
        return persistenceUnitInfo;
    }

    protected Properties properties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dataSourceProvider().hibernateDialect());
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        DataSource dataSource = newDataSource();
        if (dataSource != null) {
            properties.put("hibernate.connection.datasource", dataSource);
        }
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
        Config config = new HibernateConfig(sessionFactory());
        config.setEventHandler(new ChainEventHandler(
                Arrays.asList(
                        listEventHandler,
                        LogEventHandler.INSTANCE
                )
        ));
        config.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handle(Exception e) {
                DefaultExceptionHandler.INSTANCE.handle(e);
                exceptions.add(e);
            }
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
            if (txn.getStatus() == TransactionStatus.ACTIVE) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null && txn.getStatus() == TransactionStatus.ACTIVE) {
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
            if (txn.getStatus() == TransactionStatus.ACTIVE) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null && txn.getStatus() == TransactionStatus.ACTIVE) {
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

    protected <T> T doInJPA(JPATransactionFunction<T> function) {
        T result = null;
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = entityManagerFactory().createEntityManager();
            function.beforeTransactionCompletion();
            txn = entityManager.getTransaction();
            txn.begin();
            result = function.apply(entityManager);
            if (!txn.getRollbackOnly()) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null) {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
            throw new RuntimeException(t);
        } finally {
            function.afterTransactionCompletion();
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return result;
    }

    protected void doInJPA(JPATransactionVoidFunction function) {
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = entityManagerFactory().createEntityManager();
            function.beforeTransactionCompletion();
            txn = entityManager.getTransaction();
            txn.begin();
            function.accept(entityManager);
            if (!txn.getRollbackOnly()) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null) {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    LOGGER.error("Rollback failure", e);
                }
            }
            throw new RuntimeException(t);
        } finally {
            function.afterTransactionCompletion();
            if (entityManager != null) {
                entityManager.close();
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
