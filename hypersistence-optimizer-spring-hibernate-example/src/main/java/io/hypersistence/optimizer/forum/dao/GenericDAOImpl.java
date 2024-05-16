package io.hypersistence.optimizer.forum.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Repository
@Transactional
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    @Autowired
    private SessionFactory sessionFactory;

    private final Class<T> entityClass;

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public T findById(ID id) {
        return getSession().get(entityClass, id);
    }

    @Override
    public T persist(T entity) {
        getSession().persist(entity);
        return entity;
    }

    /**
     * Find a number of entities given by the {@code maxResults} argument.
     * This method intentionnaly avoids using ORDER BY so that
     * Hypersistence Optimizer can detect the issue.
     *
     * @param maxResults the maximum number of entities to fetch
     * @return entities being fetched
     */
    @Override
    public List<T> findAll(int maxResults) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        criteria.from(entityClass);

        return getSession()
            .createQuery(criteria)
            .setMaxResults(maxResults)
            .getResultList();
    }
}
