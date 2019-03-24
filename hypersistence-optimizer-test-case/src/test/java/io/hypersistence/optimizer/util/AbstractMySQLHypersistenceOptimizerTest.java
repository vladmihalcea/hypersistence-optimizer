package io.hypersistence.optimizer.util;

import io.hypersistence.optimizer.util.providers.DataSourceProvider;
import io.hypersistence.optimizer.util.providers.MySQLDataSourceProvider;

/**
 * @author Vlad Mihalcea
 */
public abstract class AbstractMySQLHypersistenceOptimizerTest extends AbstractHypersistenceOptimizerTest {

    @Override
    protected DataSourceProvider dataSourceProvider() {
        return new MySQLDataSourceProvider();
    }
}
