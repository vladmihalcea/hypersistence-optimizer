package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.forum.dao.PostRepository;
import io.hypersistence.optimizer.forum.dao.TagRepository;
import io.hypersistence.optimizer.forum.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
public class ForumServiceImpl implements ForumService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    @Transactional
    public Post newPost(String title, String... tags) {
        Post post = new Post();
        post.setTitle(title);
        post.getTags().addAll(tagRepository.findByName(tags));
        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAllByTitle(String title) {
        return postRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}

