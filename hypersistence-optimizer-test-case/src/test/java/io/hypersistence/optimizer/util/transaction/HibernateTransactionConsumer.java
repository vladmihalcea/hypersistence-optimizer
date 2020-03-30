package io.hypersistence.optimizer.util.transaction;

import org.hibernate.Session;

/**
 * @author Vlad Mihalcea
 */
public interface HibernateTransactionConsumer {
    void accept(Session t);

    default void beforeTransactionCompletion() {

    }

    default void afterTransactionCompletion() {

    }
}
