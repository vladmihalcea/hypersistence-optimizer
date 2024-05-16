package io.hypersistence.optimizer.util.transaction;

import jakarta.persistence.EntityManager;

/**
 * @author Vlad Mihalcea
 */
public interface JPATransactionVoidFunction {

    void accept(EntityManager t);

    default void beforeTransactionCompletion() {

    }

    default void afterTransactionCompletion() {

    }
}
