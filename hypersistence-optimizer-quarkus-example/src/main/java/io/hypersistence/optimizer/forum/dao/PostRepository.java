package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, Long> {

    public List<Post> findByTitle(String title) {
        return find("FROM Post p WHERE p.title = ?1", title).list();
    }
}
