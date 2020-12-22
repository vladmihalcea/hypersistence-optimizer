### Quarkus limitations with Hibernate and Hypersistence Optimier

- Not all Hibernate related properties can be set 
    - List of available options: https://quarkus.io/guides/hibernate-orm#hibernate-configuration-properties
    - Issue where the "problem" is being escalated/clarified: https://github.com/quarkusio/quarkus/issues/12724
- `META-INF/services` based configuration is not being applied to the application