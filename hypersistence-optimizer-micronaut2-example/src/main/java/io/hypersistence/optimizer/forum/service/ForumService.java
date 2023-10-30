package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.forum.domain.Post;
import io.hypersistence.optimizer.forum.repository.PostRepository;
import io.hypersistence.optimizer.forum.repository.TagRepository;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Singleton
public class ForumService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForumService.class);

    @Inject
    private PostRepository postRepository;

    @Inject
    private TagRepository tagRepository;

    @Inject
    private HypersistenceOptimizer hypersistenceOptimizer;

    @EventListener
    void onStartup(ServerStartupEvent event) {
        LOGGER.info("Found {} events", hypersistenceOptimizer.getEvents().size());
    }

    @Transactional
    public Post newPost(String title, List<String> tags) {
        Post post = new Post();
        post.setTitle(title);
        post.getTags().addAll(tagRepository.findByNameIn(tags));
        return postRepository.save(post);
    }

    @Transactional
    public List<Post> findAllByTitle(String title) {
        return postRepository.findByTitle(title);
    }

    @Transactional
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> findAll(int maxResults) {
        return postRepository.findAll(maxResults);
    }
}

