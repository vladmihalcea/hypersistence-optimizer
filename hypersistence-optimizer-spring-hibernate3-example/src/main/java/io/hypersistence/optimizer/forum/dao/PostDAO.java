package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Post;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
public interface PostDAO extends GenericDAO<Post, Long> {

    List<Post> findByTitle(String title);
}
