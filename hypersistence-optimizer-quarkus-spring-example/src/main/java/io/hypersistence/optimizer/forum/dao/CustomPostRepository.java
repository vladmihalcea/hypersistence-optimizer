package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
public interface CustomPostRepository {

    List<Post> findAll(int maxResults);
}
