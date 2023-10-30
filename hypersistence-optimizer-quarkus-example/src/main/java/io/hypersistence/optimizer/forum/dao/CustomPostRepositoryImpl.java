package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Post;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@ApplicationScoped
public class CustomPostRepositoryImpl implements CustomPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Post> findAll(int maxResults) {
        return entityManager.createQuery(
            "select p " +
            "from Post p ", Post.class)
        .setMaxResults(maxResults)
        .getResultList();
    }
}
