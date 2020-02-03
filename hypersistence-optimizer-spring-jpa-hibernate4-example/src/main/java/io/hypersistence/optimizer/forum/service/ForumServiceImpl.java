package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.forum.dao.PostDAO;
import io.hypersistence.optimizer.forum.dao.TagDAO;
import io.hypersistence.optimizer.forum.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private TagDAO tagDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Post newPost(String title, String... tags) {
        Post post = new Post();
        post.setTitle(title);
        post.getTags().addAll(tagDAO.findByName(tags));
        return postDAO.persist(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAllByTitle(String title) {
        return postDAO.findByTitle(title);
    }

    @Override
    @Transactional
    public Post findById(Long id) {
        return postDAO.findById(id);
    }

    @Override
    public List<Post> findAll(int maxResults) {
        return postDAO.findAll(maxResults);
    }
}

