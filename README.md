# Hypersistence Optimizer

Imagine having a tool that can automatically detect if you are using Java Persistence and Hibernate properly.

No more performance issues, no more silly mistakes that can cost you a lot of time and money.

[Hypersistence Optimizer](https://vladmihalcea.com/hypersistence-optimizer/) is that tool.

## Detecting performance issues before they hit production systems

So, assuming you have an entity like the following one:

```java
@Entity(name = "PostComment")
@Table(name = "post_comment")
public class PostComment {

    @Id
    private Long id;

    @ManyToOne
    private Post post;

    private String review;
    
}
```

Hypersistence Optimizer will log the following error message when scanning this entity:

```bash
ERROR [main]: Hypersistence Optimizer - CRITICAL - EagerFetchingEvent - The [post] attribute in the [io.hypersistence.optimizer.config.mapping.association.fetching.eager.EagerFetchingManyToOneTest$PostComment] entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. 

For more info about this event, check out this User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
````

## Issue management

If you'd like to create a new issue, be it a feature request or simply reporting a bug, then you can use [this GitHub repository issue list](https://github.com/vladmihalcea/hypersistence-optimizer/issues).

For bugs, please provide a replicating test case. You can use the `EagerFetchingManyToOneTest` as a template to create your new test case scenario.

When you are done, please send your test case as a Pull Request, and I'll take care of it.

And, thank you for using this tool and for wanting to make it even more awesome.

## Full vs. trial version

By default, this repository is configured for the full version. 

If you want to use it with the trial version, you should use the `trial` branch instead:

```bash
> git checkout trial
```

## Module descriptions

There are multiple modules in this repository:

- `hypersistence-optimizer-test-case`
- `hypersistence-optimizer-config-example`
- `hypersistence-optimizer-spring-boot-example`
- `hypersistence-optimizer-spring-jpa-example`
- `hypersistence-optimizer-spring-jpa-hibernate4-example`
- `hypersistence-optimizer-spring-jpa-hibernate3-example`
- `hypersistence-optimizer-spring-hibernate-example`
- `hypersistence-optimizer-spring-hibernate4-example`
- `hypersistence-optimizer-spring-hibernate3-example`
- `hypersistence-optimizer-glassfish-hibernate-example`
- `hypersistence-optimizer-glassfish-hibernate4-example`

> You should check out the entire repository since the child modules use the versions defined in the parent module `pom.xml`.

### `hypersistence-optimizer-test-case`

This module provides a test case template you could use to replicate a certain Hypersistence Optimizer issue.

### `hypersistence-optimizer-config-example`

This module shows various examples of how you can configure Hypersistence Optimizer.

### `hypersistence-optimizer-spring-boot-example`

This module shows how you can use Hypersistence Optimizer with Spring Boot.

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
