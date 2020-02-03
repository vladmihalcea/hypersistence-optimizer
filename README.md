# Hypersistence Optimizer

[Hypersistence Optimizer](https://vladmihalcea.com/hypersistence-optimizer/) is a dynamic analyzing tool that can scan your JPA and Hibernate application and provide you tips about the changes you need to make to entity mappings, configurations, queries, and Persistence Context actions to speed up your data access layer.

Once you downloaded the [Full or Trial version](https://vladmihalcea.com/hypersistence-optimizer/), you need to follow a series of steps in order to install Hypersistence Optimizer.

## Unzipping the package

The first thing you need to do is to unzip the package you have just downloaded.

```bash
> unzip hypersistence-optimizer-2.15.0-trial-pack.zip
```

After unzipping the project package, you will get the following file structure:

```bash
creating: hypersistence-optimizer-2.15.0-trial/
   creating: hypersistence-optimizer-2.15.0-trial/lib/
  inflating: hypersistence-optimizer-2.15.0-trial/lib/hypersistence-optimizer-2.15.0-trial.jar
  inflating: hypersistence-optimizer-2.15.0-trial/lib/hypersistence-optimizer-2.15.0-trial-javax.jar
  inflating: hypersistence-optimizer-2.15.0-trial/lib/hypersistence-optimizer-2.15.0-trial-jakarta.jar
  inflating: hypersistence-optimizer-2.15.0-trial/lib/hypersistence-optimizer-2.15.0-trial-jre6.jar
  inflating: hypersistence-optimizer-2.15.0-trial/changelog.txt
  inflating: hypersistence-optimizer-2.15.0-trial/LICENSE.txt
  inflating: hypersistence-optimizer-2.15.0-trial/archiva-deploy.bat
  inflating: hypersistence-optimizer-2.15.0-trial/archiva-deploy.sh
  inflating: hypersistence-optimizer-2.15.0-trial/maven-install.bat
  inflating: hypersistence-optimizer-2.15.0-trial/maven-install.sh
  inflating: hypersistence-optimizer-2.15.0-trial/README.txt
```

The package contains the following resources:

* the `lib` folder contains the main `jar` file, as well as the JavaDoc and the Java sources
* the `docs` folder contains the Installation and User Guides
* the `changelog` file contains the release notes for all product versions
* the `LICENSE` file contains the project license info
* the `maven-install` scripts allow you to install the Java artifacts in your local Maven repository
* the `README` file contains a short description of the project

## Installation Guide

In order to install Hypersistence Optimizer, you need to read the Installation Guide, which is available [online](https://vladmihalcea.com/hypersistence-optimizer/docs/installation-guide/) if you want.

## User Guide

After you are done with the Installation Guide, you should read the User Guide too, as it shows how you can configure
Hypersistence Optimizer so that you can get the most out of it.

You can find the User Guide [online](https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/).

## GitHub repository

This repository contains examples for Spring Boot, Spring Framework, Java EE that can help you set up the tool.

### Full vs. Trial version

By default, this repository is configured for the Full version. 

If you want to use it with the Trial version, you should use the `trial` branch instead:

```bash
> git checkout trial
```

> You should check out the entire repository since the child modules use the versions defined in the parent module `pom.xml`.

### Module descriptions

There are multiple modules in this repository:

- `hypersistence-optimizer-test-case`
- `hypersistence-optimizer-config-example`
- `hypersistence-optimizer-spring-hibernate-example`
- `hypersistence-optimizer-spring-hibernate4-example`
- `hypersistence-optimizer-spring-hibernate3-example`
- `hypersistence-optimizer-spring-jpa-example`
- `hypersistence-optimizer-spring-jpa-hibernate4-example`
- `hypersistence-optimizer-spring-jpa-hibernate3-example`
- `hypersistence-optimizer-spring-boot-example`
- `hypersistence-optimizer-spring-boot-jta-example`
- `hypersistence-optimizer-spring-boot3-example`
- `hypersistence-optimizer-spring-boot3-hibernate7-example`
- `hypersistence-optimizer-glassfish-hibernate-example`
- `hypersistence-optimizer-glassfish-hibernate4-example`
- `hypersistence-optimizer-quarkus-spring-example`
- `hypersistence-optimizer-quarkus-example`
- `hypersistence-optimizer-micronaut-example`
- `hypersistence-optimizer-micronaut2-example`

### `hypersistence-optimizer-test-case`

This module provides a test case template you could use to replicate a certain Hypersistence Optimizer issue.

### `hypersistence-optimizer-config-example`

This module shows various examples of how you can configure Hypersistence Optimizer.

It also shows the checks added in Hypersistence Optimizer 2.15:

- `AsyncMetadataScannersTest` runs the mapping and configuration scanners in the background, so they don't delay the application bootstrap.
- `HibernateConfigurationChecksTest` detects the Hibernate 6 and 7 settings that affect performance, like a disabled query plan cache.
- `MySQLJdbcDriverChecksTest` detects the MySQL JDBC Driver settings that add extra database roundtrips, and the missing socket timeout.
- `PostgreSQLJdbcDriverChecksTest` detects the missing socket timeout and the default application name of the PostgreSQL JDBC Driver.
- `MySQLResultSetStreamingTest` detects a streamed query whose result set the MySQL JDBC Driver loads entirely into memory.
- `PostgreSQLAutoExplainTest` detects that the PostgreSQL `auto_explain` module, which logs the actual execution plans of the slow queries, is not enabled.
- `PostgreSQLSchemaChecksTest` reads the database catalog, and detects a table without a Primary Key and an unindexed Foreign Key column.
- `DatabaseScannerDisabledTest` shows how to disable the database scanner.

### `hypersistence-optimizer-spring-boot-example`

This module shows how you can use Hypersistence Optimizer with Spring Boot.

### `hypersistence-optimizer-spring-boot3-example`

This module shows how you can use Hypersistence Optimizer with Spring Boot 3.

The tests in the `io.hypersistence.optimizer.forum.pool` package run the application on MySQL with every connection pool
that Spring Boot supports (HikariCP, Tomcat JDBC, DBCP2, Oracle UCP, and c3p0), and check that Hypersistence Optimizer
detects the pool settings that let the MySQL server close the idle connections before the pool retires them.

### `hypersistence-optimizer-spring-jpa-example`

This module shows how you can use Hypersistence Optimizer with Spring and the JPA `LocalEntityManagerFactoryBean`.

### `hypersistence-optimizer-spring-jpa-hibernate4-example`

This module shows how you can use Hypersistence Optimizer with Spring, the JPA `LocalEntityManagerFactoryBean`, and Hibernate 4.

### `hypersistence-optimizer-spring-jpa-hibernate3-example`

This module shows how you can use Hypersistence Optimizer with Spring, the JPA `LocalEntityManagerFactoryBean`, and Hibernate 3.

### `hypersistence-optimizer-spring-hibernate-example`

This module shows how you can use Hypersistence Optimizer with Spring and the Hibernate `LocalSessionFactoryBean`.

### `hypersistence-optimizer-spring-hibernate4-example`

This module shows how you can use Hypersistence Optimizer with Spring and the Hibernate 4 `LocalSessionFactoryBean`.

### `hypersistence-optimizer-spring-hibernate3-example`

This module shows how you can use Hypersistence Optimizer with Spring and the Hibernate 3 `LocalSessionFactoryBean`.

### `hypersistence-optimizer-glassfish-hibernate-example`

This module shows how you can use Hypersistence Optimizer with Java EE and GlassFish.

### `hypersistence-optimizer-glassfish-hibernate4-example`

This module shows how you can use Hypersistence Optimizer with Java EE, GlassFish, and Hibernate 4.

### `hypersistence-optimizer-quarkus-spring-extension-example`

This module shows how you can use Hypersistence Optimizer with Quarkus's Spring based extensions.

### `hypersistence-optimizer-quarkus-example`

This module shows how you can use Hypersistence Optimizer with Quarkus (Hibernate Panache ORM).

The tests in the `io.hypersistence.optimizer.forum.pool` package run the application on MySQL, and check that Hypersistence Optimizer
detects the Agroal connection pool settings.

## Setup instructions

All modules work with Java 8 or newer versions, and the project is set up using Maven.

While most tests use the in-memory HSQLDB, some tests use MySQL or PostgreSQL, and, for this reason, you need to configure
the database connection properties in the `MySQLDataSourceProvider` and `PostgreSQLDataSourceProvider` classes.

By default, the project expects that you are using the following MySQL database connection properties:

* Database: high_performance_java_persistence
* Port: 3306
* UserName: mysql
* Password: admin
 
For PostgreSQL, the project expects the `high_performance_java_persistence` database on port 5432, with the `postgres` user and the `admin` password.

You can also use **docker-compose** to run the MySQL and PostgreSQL servers using the command: 

    docker-compose up
    
To run the tests, you can use:

    mvn clean test

## Issue management

If you'd like to create a new issue, be it a feature request or simply reporting a bug, then you can use [this GitHub repository issue list](https://github.com/vladmihalcea/hypersistence-optimizer/issues).

For bugs, it would be awesome if you provided a replicating test case as well. You can use the `EagerFetchingManyToOneTest` as a template to create your new test case scenario.

When you are done, please send your test case as a Pull Request, and I'll take care of it.

Thank you for using Hypersistence Optimizer and stay tuned for more!
