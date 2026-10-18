package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.HibernateConfig;
import io.hypersistence.optimizer.hibernate.event.mapping.association.UnindexedForeignKeyEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.identifier.MissingPrimaryKeyEvent;

/**
 * If the database user cannot read the database catalog, or if you don't want Hypersistence Optimizer to run
 * any query against your database, you can disable the database scanner, and the schema checks are skipped.
 */
public class DatabaseScannerDisabledTest extends AbstractPostgreSQLSchemaTest {

    @Override
    protected HypersistenceOptimizer hypersistenceOptimizer() {
        return new HypersistenceOptimizer(
            new HibernateConfig(sessionFactory())
                .setDatabaseScannerEnabled(false)
        );
    }

    @Override
    protected void verify() {
        assertEventTriggered(0, MissingPrimaryKeyEvent.class);
        assertEventTriggered(0, UnindexedForeignKeyEvent.class);
    }
}
