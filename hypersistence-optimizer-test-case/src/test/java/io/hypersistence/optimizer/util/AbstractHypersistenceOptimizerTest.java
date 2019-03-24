package io.hypersistence.optimizer.util;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import org.junit.Test;

/**
 * @author Vlad Mihalcea
 */
public abstract class AbstractHypersistenceOptimizerTest extends AbstractTest {

    protected HypersistenceOptimizer hypersistenceOptimizer;

    @Test
    public void test() {
        hypersistenceOptimizer = hypersistenceOptimizer();
        hypersistenceOptimizer.init();

        verify();
    }

    protected abstract void verify();
}
