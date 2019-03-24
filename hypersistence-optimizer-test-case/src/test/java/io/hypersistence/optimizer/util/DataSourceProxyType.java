package io.hypersistence.optimizer.util;

import javax.sql.DataSource;

/**
 * @author Vlad Mihalcea
 */
public enum DataSourceProxyType {
    DATA_SOURCE_PROXY {
        @Override
        public DataSource dataSource(DataSource dataSource) {
            return dataSource;
        }
    };

    public abstract DataSource dataSource(DataSource dataSource);
}
