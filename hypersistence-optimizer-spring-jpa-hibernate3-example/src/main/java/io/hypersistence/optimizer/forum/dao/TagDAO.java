package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Tag;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
public interface TagDAO extends GenericDAO<Tag, Long> {

    List<Tag> findByName(String... tags);
}
