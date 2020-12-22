package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.forum.dao.PostRepository;
import io.hypersistence.optimizer.forum.dao.TagRepository;
import io.hypersistence.optimizer.forum.domain.Post;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@ApplicationScoped
public class ForumServiceImpl implements ForumService {

    @Inject
    PostRepository postRepository;

    @Inject
    TagRepository tagRepository;

    @Override
    @Transactional
    public Post newPost(String title, List<String> tags) {
        Post post = new Post();
        post.setTitle(title);
        post.getTags().addAll(tagRepository.findByNameIn(tags));
        postRepository.persist(post);
        return post;
    }

    @Override
    @Transactional
    public List<Post> findAllByTitle(String title) {
        return postRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public Post findById(Long id) {
        return postRepository.findByIdOptional(id).orElse(null);
    }

    @Override
    public List<Post> findAll(int maxResults) {
        return postRepository.findAll().page(0, maxResults).list();
    }
}

