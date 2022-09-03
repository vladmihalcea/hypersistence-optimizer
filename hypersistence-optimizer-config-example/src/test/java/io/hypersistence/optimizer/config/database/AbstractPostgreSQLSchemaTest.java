package io.hypersistence.optimizer.config.database;

import io.hypersistence.optimizer.util.AbstractHypersistenceOptimizerTest;
import io.hypersistence.optimizer.util.providers.Database;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The {@link AbstractPostgreSQLSchemaTest} creates a database schema that doesn't match the best practices
 * for the entity mappings:
 * <ul>
 *     <li>the {@code schema_post_comment} table has no Primary Key</li>
 *     <li>the {@code schema_post_comment.post_id} Foreign Key column is not indexed</li>
 * </ul>
 * <p>
 * To run this test, you need a PostgreSQL database, like the one provided by the {@code docker-compose.yml} file
 * in the root folder of this repository.
 */
public abstract class AbstractPostgreSQLSchemaTest extends AbstractHypersistenceOptimizerTest {

    @BeforeClass
    public static void createSchema() throws SQLException {
        executeStatements(
            "drop table if exists schema_post_comment",
            "drop table if exists schema_post",
            "create table schema_post (id bigint not null, title varchar(255), primary key (id))",
            "create table schema_post_comment (id bigint not null, review varchar(255), post_id bigint)"
        );
    }

    @AfterClass
    public static void dropSchema() throws SQLException {
        executeStatements(
            "drop table if exists schema_post_comment",
            "drop table if exists schema_post"
        );
    }

    private static void executeStatements(String... sqls) throws SQLException {
        try (Connection connection = Database.POSTGRESQL.dataSourceProvider().dataSource().getConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : sqls) {
                statement.executeUpdate(sql);
            }
        }
    }

    @Override
    public Class<?>[] entities() {
        return new Class<?>[]{
            Post.class,
            PostComment.class
        };
    }

    @Override
    protected Database database() {
        return Database.POSTGRESQL;
    }

    @Override
    protected void additionalProperties(Properties properties) {
        //The schema is created by the test, not by Hibernate
        properties.put("hibernate.hbm2ddl.auto", "none");
    }

    @Entity(name = "Post")
    @Table(name = "schema_post")
    public static class Post {

        @Id
        private Long id;

        private String title;
    }

    @Entity(name = "PostComment")
    @Table(name = "schema_post_comment")
    public static class PostComment {

        @Id
        private Long id;

        private String review;

        @ManyToOne(fetch = FetchType.LAZY)
        private Post post;
    }
}
