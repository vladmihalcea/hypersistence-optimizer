package models;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.util.ReflectionUtils;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PostRepositoryImpl implements PostRepository {

    private final JPAApi jpaApi;

    private final DatabaseExecutionContext executionContext;

    private final HypersistenceOptimizer hypersistenceOptimizer;

    @Inject
    public PostRepositoryImpl(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;

        Map<String, EntityManagerFactory> emfs = ReflectionUtils.getFieldValue(jpaApi, "emfs");

        this.hypersistenceOptimizer = new HypersistenceOptimizer(
            new JpaConfig(emfs.get("default"))
        );
    }

    @Override
    public CompletionStage<Post> save(Post post) {
        return supplyAsync(
            () -> wrap(
                em -> persist(em, post)
            ),
            executionContext
        );
    }

    @Override
    public CompletionStage<Stream<Post>> findAll() {
        return supplyAsync(() -> wrap(this::stream), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Post persist(EntityManager em, Post post) {
        em.persist(post);
        return post;
    }

    private Stream<Post> stream(EntityManager em) {
        return em.createQuery(
            "select p from Post p", Post.class
        ).getResultList().stream();
    }
}
