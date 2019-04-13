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
import io.hypersistence.optimizer.core.event.ChainEventHandler;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.core.event.LogEventHandler;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.SkipAutoCommitCheckEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.ManyToManyListEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneParentSideEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneWithoutMapsIdEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ApplicationTest {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void testOptimizer() {
        final ListEventHandler listEventListener = new ListEventHandler();

        new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory)
            .setEventHandler(new ChainEventHandler(
                Arrays.asList(
                    listEventListener,
                    LogEventHandler.INSTANCE
                )
            ))
        ).init();

        List<Class<?extends Event>> eventClasses = listEventListener.getEvents()
                .stream()
                .map(Event::getClass)
                .distinct()
                .collect(Collectors.toList());

        assertEquals(5, eventClasses.size());
        eventClasses.contains(EagerFetchingEvent.class);
        eventClasses.contains(ManyToManyListEvent.class);
        eventClasses.contains(OneToOneParentSideEvent.class);
        eventClasses.contains(OneToOneWithoutMapsIdEvent.class);
        eventClasses.contains(SkipAutoCommitCheckEvent.class);
    }
}