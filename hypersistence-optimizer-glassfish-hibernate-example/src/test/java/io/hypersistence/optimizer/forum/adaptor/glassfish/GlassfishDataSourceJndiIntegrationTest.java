package io.hypersistence.optimizer.forum.adaptor.glassfish;

import io.hypersistence.optimizer.forum.adaptor.glassfish.datasource.DefaultDataSourceConfiguration;
import io.hypersistence.optimizer.forum.domain.Post;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

public class GlassfishDataSourceJndiIntegrationTest extends AbstractGlassfishDataSourceJndiIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(Post.class.getPackage())
            .addClass(DefaultDataSourceConfiguration.class)
            .addAsManifestResource("MANIFEST/test-persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
