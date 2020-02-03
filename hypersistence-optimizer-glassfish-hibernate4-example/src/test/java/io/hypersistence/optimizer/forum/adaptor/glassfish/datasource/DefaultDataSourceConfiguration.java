package io.hypersistence.optimizer.forum.adaptor.glassfish.datasource;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;

/**
 * DataSourceConfiguration - Default DataSource configuration
 *
 * @author Vlad Mihalcea
 */

@DataSourceDefinition(
        name = "java:global/jdbc/default",
        className = "org.hsqldb.jdbc.JDBCDataSource",
        url = "jdbc:hsqldb:mem:test",
        user = "sa"
)
@Stateless
public class DefaultDataSourceConfiguration {
}