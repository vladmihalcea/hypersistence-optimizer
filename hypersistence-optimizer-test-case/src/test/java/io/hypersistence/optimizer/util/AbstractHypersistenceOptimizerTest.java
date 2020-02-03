package io.hypersistence.optimizer.util;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * @author Vlad Mihalcea
 */
public abstract class AbstractHypersistenceOptimizerTest extends AbstractTest {

    protected HypersistenceOptimizer hypersistenceOptimizer;

    @Test
    public void test() {
        hypersistenceOptimizer = hypersistenceOptimizer();

        verify();
    }

    protected abstract void verify();

    protected void assertNoEventTriggered() {
        int count = 0;

        for (Event event : hypersistenceOptimizer.getEvents()) {
            count++;
        }

        assertSame(0, count);
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

    protected <T extends Event> T getTriggeredEvent(Class<T> eventClass) {
        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                return (T) event;
            }
        }
        return null;
    }

    protected <T extends Event> List<T> getTriggeredEvents(Class<T> eventClass) {
        List<T> matchingEvents = new ArrayList<T>();

        for (Event event : hypersistenceOptimizer.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                matchingEvents.add((T) event);
            }
        }
        return matchingEvents;
    }
}
