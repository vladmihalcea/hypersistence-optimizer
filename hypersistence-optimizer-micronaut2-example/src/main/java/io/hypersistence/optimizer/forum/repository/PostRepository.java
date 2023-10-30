package io.hypersistence.optimizer.forum.repository;

import io.hypersistence.optimizer.forum.domain.Post;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Repository
public abstract class PostRepository implements PageableRepository<Post, Long> {

    private final EntityManager entityManager;

    public PostRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public abstract List<Post> findByTitle(String title);

    public List<Post> findAll(int maxResults) {
        return entityManager.createQuery(
            "select p " +
            "from Post p ", Post.class)
            .setMaxResults(maxResults)
            .getResultList();
    }
}
