package io.hypersistence.optimizer.forum.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Repository
@Transactional
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityClass;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<T> findAll(int maxResults) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        criteria.from(entityClass);

        return entityManager
            .createQuery(criteria)
            .setMaxResults(maxResults)
            .getResultList();
    }
}