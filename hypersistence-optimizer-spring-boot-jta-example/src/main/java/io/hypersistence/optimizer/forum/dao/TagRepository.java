package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findByNameIn(List<String> tags);
}
