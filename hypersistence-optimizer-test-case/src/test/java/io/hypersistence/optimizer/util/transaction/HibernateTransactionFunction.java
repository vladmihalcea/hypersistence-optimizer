package io.hypersistence.optimizer.util.transaction;

import org.hibernate.Session;

/**
 * @author Vlad Mihalcea
 */
public interface HibernateTransactionFunction<T> {

    T apply(Session t);

    default void beforeTransactionCompletion() {

    }

    default void afterTransactionCompletion() {

    }
}
