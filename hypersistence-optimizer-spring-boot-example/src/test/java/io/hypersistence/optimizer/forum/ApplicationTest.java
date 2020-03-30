/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hypersistence.optimizer.forum;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.forum.domain.Post;
import io.hypersistence.optimizer.forum.domain.Tag;
import io.hypersistence.optimizer.forum.service.ForumService;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.SkipAutoCommitCheckEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.query.QueryInClauseParameterPaddingEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.query.QueryPaginationCollectionFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.schema.SchemaGenerationEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.ManyToManyListEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneParentSideEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneWithoutMapsIdEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.query.PaginationWithoutOrderByEvent;
import io.hypersistence.optimizer.hibernate.event.session.SessionTimeoutEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ApplicationTest {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ForumService forumService;

    protected final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread bob = new Thread(r);
        bob.setName("Bob");
        return bob;
    });

    @Before
    public void init() {
        try {
            transactionTemplate.execute((TransactionCallback<Void>) transactionStatus -> {
                Tag hibernate = new Tag();
                hibernate.setName("hibernate");
                entityManager.persist(hibernate);

                Tag jpa = new Tag();
                jpa.setName("jpa");
                entityManager.persist(jpa);
                return null;
            });
        } catch (TransactionException e) {
            LOGGER.error("Failure", e);
        }
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        assertEventTriggered(2, EagerFetchingEvent.class);
        assertEventTriggered(1, ManyToManyListEvent.class);
        assertEventTriggered(1, OneToOneParentSideEvent.class);
        assertEventTriggered(1, OneToOneWithoutMapsIdEvent.class);
        assertEventTriggered(1, SkipAutoCommitCheckEvent.class);
        assertEventTriggered(1, SchemaGenerationEvent.class);
        assertEventTriggered(1, QueryPaginationCollectionFetchingEvent.class);
        assertEventTriggered(1, QueryInClauseParameterPaddingEvent.class);

        Post newPost = null;

        for (int i = 0; i < 10; i++) {
            newPost = forumService.newPost("High-Performance Java Persistence", Arrays.asList("hibernate", "jpa"));
            assertNotNull(newPost.getId());
        }

        List<Post> posts = forumService.findAllByTitle("High-Performance Java Persistence");
        assertEquals(10, posts.size());

        Post post = forumService.findById(newPost.getId());
        assertEquals("High-Performance Java Persistence", post.getTitle());

        assertEventTriggered(0, PaginationWithoutOrderByEvent.class);
        assertEquals(5, forumService.findAll(5).size());
        assertEventTriggered(1, PaginationWithoutOrderByEvent.class);

        hypersistenceOptimizer.getEvents().clear();

        executorService.submit(() -> transactionTemplate.execute(
            transactionStatus -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
                return null;
            }
        )).get();

        assertEventTriggered(1, SessionTimeoutEvent.class);
    }

    protected void assertEventTriggered(int expectedCount, Class<? extends Event> eventClass) {
        int count = 0;

        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                count++;
            }
        }

        assertSame(expectedCount, count);
    }
}