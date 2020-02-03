package io.hypersistence.optimizer.forum.dao;

import io.hypersistence.optimizer.forum.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vlad Mihalcea
 */
@Repository
public class PostDAOImpl extends GenericDAOImpl<Post, Long> implements PostDAO {

    protected PostDAOImpl() {
        super(Post.class);
    }

    @Override
    public List<Post> findByTitle(String title) {
        return getSession().createQuery(
            "select p " +
            "from Post p " +
            "where p.title = :title")
        .setParameter("title", title)
        .list();
    }
}
