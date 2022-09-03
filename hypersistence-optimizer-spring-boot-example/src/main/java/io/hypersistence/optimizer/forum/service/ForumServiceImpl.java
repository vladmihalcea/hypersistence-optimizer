package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.forum.domain.Post;
import io.hypersistence.optimizer.forum.domain.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Service
public class ForumServiceImpl implements ForumService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Post newPost(String title, List<String> tags) {
        Post post = new Post();
        post.setTitle(title);
        post.getTags().addAll(
            entityManager.createQuery(
                "select t " +
                "from Tag t " +
                "where t.name in :tags", Tag.class)
            .setParameter("tags", tags)
            .getResultList()
        );
        entityManager.persist(post);
        return post;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAllByTitle(String title) {
        return entityManager.createQuery(
            "select p " +
            "from Post p " +
            "where p.title = :title", Post.class)
        .setParameter("title", title)
        .getResultList();
    }

    @Override
    @Transactional
    public Post findById(Long id) {
        return entityManager.find(Post.class, id);
    }

    @Override
    public List<Post> findAll(int maxResults) {
        return entityManager.createQuery(
            "select p " +
            "from Post p ", Post.class)
        .setMaxResults(maxResults)
        .getResultList();
    }
}

