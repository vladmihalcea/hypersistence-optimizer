package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Tag;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@ApplicationScoped
public class TagRepository implements PanacheRepositoryBase<Tag, Long> {

    public List<Tag> findByNameIn(List<String> tags) {
        return find("FROM Tag t WHERE t.name in (?1)", tags).list();
    }
}
