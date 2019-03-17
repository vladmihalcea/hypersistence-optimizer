package io.hypersistence.optimizer.forum.dao;

import java.io.Serializable;

/**
 * @author Vlad Mihalcea
 */
public interface GenericDAO<T, ID extends Serializable> {

    T findById(ID id);

    T persist(T entity);
}
