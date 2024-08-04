package interfaces;

import java.util.function.Consumer;

/**
 * Producer <a href="package-summary.html">functional interface</a> for reactive patterns.
 * Functional method: {@link #register(Consumer)}
 * */
@FunctionalInterface
public interface Producer<V> {
    /**
     * Main method that accepts a {@link Consumer} observer.
     * @param valueConsumer the observer to be registered.
     * */
    void register(Consumer<? super V> valueConsumer);

    /**
     * @implSpec The return value may be used as indication that the unregistering action
     * was successful or not.
     * */
    default boolean unregister(Consumer<? super V> valueConsumer) {
        throw new RuntimeException("Method not implemented");
    }

    /**
     * @implSpec The returning value may be that of the previous {@link Consumer} registered.
     * */
    default Consumer<? super V> unregister() {
        throw new RuntimeException("Method not implemented");
    }

    /**
     * An Illegal Register with custom lazy error message.
     * @param errorMessageSupplier lazily gets the error to be displayed
     * */
    static<V> Producer<V> illegalState(StringSupplier errorMessageSupplier) {
        return valueConsumer -> {
            throw new IllegalStateException(errorMessageSupplier.get());
        };
    }
    /**
     * An Illegal Register with custom error message.
     * @param errorMessage the error to be displayed
     * */
    static<V> Producer<V> illegalState(String errorMessage) {
        return valueConsumer -> {
            throw new IllegalStateException(errorMessage);
        };
    }
}
