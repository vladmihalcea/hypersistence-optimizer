package io.hypersistence.optimizer.util.transaction;

import java.util.concurrent.Callable;

/**
 * @author Vlad Mihalcea
 */
public interface VoidCallable extends Callable<Void> {

    void execute();

    default Void call() throws Exception {
        execute();
        return null;
    }
}
