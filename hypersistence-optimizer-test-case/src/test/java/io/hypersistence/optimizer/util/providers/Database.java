package io.hypersistence.optimizer.util.providers;

import io.hypersistence.optimizer.util.ReflectionUtils;

/**
 * @author Vlad Mihalcea
 */
public enum Database {
    HSQLDB(HSQLDBDataSourceProvider.class),
    MYSQL(MySQLDataSourceProvider.class);

    private Class<? extends DataSourceProvider> dataSourceProviderClass;

    Database(Class<? extends DataSourceProvider> dataSourceProviderClass) {
        this.dataSourceProviderClass = dataSourceProviderClass;
    }

    public DataSourceProvider dataSourceProvider() {
        return ReflectionUtils.newInstance(dataSourceProviderClass.getName());
    }
}
