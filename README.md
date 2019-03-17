#Hypersistence Optimizer

Imagine having a tool that can automatically detect if you are using Java Persistence and Hibernate properly.

No more performance issues, no more silly mistakes that can cost you a lot of time and money.

Now, you have this tool. It's called Hypersistence Optimizer.

##Introduction

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
ERROR [main]: Hypersistence Optimizer - CRITICAL - EagerFetchingEvent - 
The [post] attribute in the [io.hypersistence.optimizer.hibernate.mapping.association.fetching.eager.EagerFetchingManyToOneTest$PostComment] entity uses eager fetching. 

Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data.
````

##Test case module

If you want to play with it, you need to install the library which is available [here](https://vladmihalcea.com/hypersistence-optimizer/).

Afterwards, if you want to play with it, you can use the `hypersistence-optimizer-test-case` module and run the [`EagerFetchingManyToOneTest`](https://github.com/vladmihalcea/hypersistence-optimizer/blob/404c6841ad8e0cb4c031107ed0b4356321661034/hypersistence-optimizer-test-case/src/test/java/io/hypersistence/optimizer/hibernate/mapping/association/fetching/eager/EagerFetchingManyToOneTest.java) test class which uses the aformentioned `PostComment` entity mapping.

###Issue management

If you'd like to create a new issue, be it a feature request or simply reporting a bug, then you can use [this GitHub repository issue list](https://github.com/vladmihalcea/hypersistence-optimizer/issues).

For bugs, please provide a replicating test case. You can use the `EagerFetchingManyToOneTest` as a template to create your new test case scenario.

When you are done, please send your test case as a Pull Request, and I'll take care of it.

And, thank you for using this tool and for wanting to make it even more awesome.

##Spring example

If you are using Spring, you can try the JPA or Hibernate modules.

###Spring and JPA example

The `hypersistence-optimizer-spring-jpa-example` shows how you can integrate the Hypersistence Optimizer with a Spring JPA application.

````java
final ListEventHandler listEventListener = new ListEventHandler();

new HypersistenceOptimizer(
    new JpaConfig(
        entityManager.getEntityManagerFactory()
    )
    .setEventListener(new ChainEventHandler(
        Arrays.asList(
            listEventListener,
            LogEventHandler.INSTANCE
        )
    ))
).init();
````

###Spring and Hibernate example

The `hypersistence-optimizer-spring-hibernate-example` shows how you can integrate the Hypersistence Optimizer with a Spring Hibernate application.

````java
final ListEventHandler listEventListener = new ListEventHandler();

new HypersistenceOptimizer(
    new HibernateConfig(
        sessionFactory
    )
    .setEventListener(new ChainEventHandler(
        Arrays.asList(
            listEventListener,
            LogEventHandler.INSTANCE
        )
    ))
).init();
````

It's as simple as that! 

Enjoy running you data access layer at warp speed now.