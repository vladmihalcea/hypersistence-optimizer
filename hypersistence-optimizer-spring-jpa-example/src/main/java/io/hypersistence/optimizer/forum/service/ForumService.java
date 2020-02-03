package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.forum.domain.Post;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Service
public interface ForumService {

    Post newPost(String title, String... tags);

    List<Post> findAllByTitle(String title);

    Post findById(Long id);

    List<Post> findAll(int maxResults);
}
