package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.hibernate.event.mapping.association.UnindexedForeignKeyEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.identifier.MissingPrimaryKeyEvent;

/**
 * The database scanner reads the database catalog of the tables mapped by the entities, and it detects the table
 * without a Primary Key and the Foreign Key column without an index.
 */
public class PostgreSQLSchemaChecksTest extends AbstractPostgreSQLSchemaTest {

    @Override
    protected void verify() {
        assertEventTriggered(1, MissingPrimaryKeyEvent.class);
        assertEventTriggered(1, UnindexedForeignKeyEvent.class);
        LOGGER.info(getTriggeredEvent(MissingPrimaryKeyEvent.class).getDescription());
        LOGGER.info(getTriggeredEvent(UnindexedForeignKeyEvent.class).getDescription());
    }
}
