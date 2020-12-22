package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.forum.dao.PostRepository;
import io.hypersistence.optimizer.forum.dao.TagRepository;
import io.hypersistence.optimizer.forum.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    @Transactional
    public Post newPost(String title, List<String> tags) {
        Post post = new Post();
        post.setTitle(title);
        post.getTags().addAll(tagRepository.findByNameIn(tags));
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public List<Post> findAllByTitle(String title) {
        return postRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public List<Post> findAll(int maxResults) {
        return postRepository.findAll(maxResults);
    }
}

