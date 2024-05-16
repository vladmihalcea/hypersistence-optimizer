package io.hypersistence.optimizer.util.transaction;

import jakarta.persistence.EntityManager;

/**
 * @author Vlad Mihalcea
 */
public interface JPATransactionFunction<T> {

    T apply(EntityManager entityManager);

    default void beforeTransactionCompletion() {

    }

    default void afterTransactionCompletion() {

    }
}
