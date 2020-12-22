package io.hypersistence.optimizer.forum.service;

import io.hypersistence.optimizer.forum.domain.Post;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
public interface ForumService {

    Post newPost(String title, List<String> tags);

    List<Post> findAllByTitle(String title);

    Post findById(Long id);

    List<Post> findAll(int maxResults);
}
