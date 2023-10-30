package io.hypersistence.optimizer.forum.repository;

import io.hypersistence.optimizer.forum.domain.Tag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findByNameIn(List<String> tags);
}
