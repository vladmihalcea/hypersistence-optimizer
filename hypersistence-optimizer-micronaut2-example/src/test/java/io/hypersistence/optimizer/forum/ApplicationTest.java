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
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.forum.domain.Post;
import io.hypersistence.optimizer.forum.domain.Tag;
import io.hypersistence.optimizer.forum.service.ForumService;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.Connection9903031104Event;
import io.hypersistence.optimizer.hibernate.event.configuration.query.Query7786258879Event;
import io.hypersistence.optimizer.hibernate.event.configuration.query.Query2468924130Event;
import io.hypersistence.optimizer.hibernate.event.configuration.schema.SchemaGenerationEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.Association3890524098Event;
import io.hypersistence.optimizer.hibernate.event.mapping.association.Association934543432Event;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneWithoutMapsIdEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.query.QueryEvent;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(application = Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTest.class);

    @Inject
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Inject
    private EntityManager entityManager;

    @Inject
    private ForumService forumService;

    @BeforeEach
    public void init() {
        Tag hibernate = new Tag();
        hibernate.setName("hibernate");
        entityManager.persist(hibernate);

        Tag jpa = new Tag();
        jpa.setName("jpa");
        entityManager.persist(jpa);
    }

    @Test
    public void test() {
        assertEventTriggered(2, EagerFetchingEvent.class);
        assertEventTriggered(1, Association3890524098Event.class);
        assertEventTriggered(1, Association934543432Event.class);
        assertEventTriggered(1, OneToOneWithoutMapsIdEvent.class);
        assertEventTriggered(1, SchemaGenerationEvent.class);
        assertEventTriggered(1, Query2468924130Event.class);
        assertEventTriggered(1, Query7786258879Event.class);

        Post newPost = null;

        for (int i = 0; i < 10; i++) {
            newPost = forumService.newPost("High-Performance Java Persistence", Arrays.asList("hibernate", "jpa"));
            assertNotNull(newPost.getId());
        }

        List<Post> posts = forumService.findAllByTitle("High-Performance Java Persistence");
        assertEquals(10, posts.size());

        Post post = forumService.findById(newPost.getId());
        assertEquals("High-Performance Java Persistence", post.getTitle());

        assertEventTriggered(0, QueryEvent.class);
        assertEquals(5, forumService.findAll(5).size());
        assertEventTriggered(1, QueryEvent.class);
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
