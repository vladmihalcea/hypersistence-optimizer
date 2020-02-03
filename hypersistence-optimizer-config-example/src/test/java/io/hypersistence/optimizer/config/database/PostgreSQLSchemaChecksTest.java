package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.hibernate.event.mapping.association.Association7107405069Event;
import io.hypersistence.optimizer.hibernate.event.mapping.identifier.Identifier6127655941Event;

/**
 * The database scanner reads the database catalog of the tables mapped by the entities, and it detects the table
 * without a Primary Key and the Foreign Key column without an index.
 */
public class PostgreSQLSchemaChecksTest extends AbstractPostgreSQLSchemaTest {

    @Override
    protected void verify() {
        assertEventTriggered(1, Identifier6127655941Event.class);
        assertEventTriggered(1, Association7107405069Event.class);
    }
}
