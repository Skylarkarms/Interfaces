package interfaces;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Registration <a href="package-summary.html">functional interface</a> for reactive patterns that propagate actions.
 * */
@FunctionalInterface
public interface Register<V> {
    /**
     * Main method that accepts a {@link Consumer} observer.
     * @param valueConsumer the observer to be registered.
     * */
    void register(Consumer<? super V> valueConsumer);

    /**
     * Creates a Register that will test for no-null validation before dispatching the value to the newly register {@link Consumer}
     * @param valueSupplier the supplier of the given value.
     * @implNote try to instantiate the Supplier as a class field (making both Register and Supplier sibling fields) to prevent lambda recreation.
     * */
    static<V> Register<V> nullCheck(Supplier<V> valueSupplier) {
        return valueConsumer -> {
            V value;
            if ((value = valueSupplier.get()) != null) valueConsumer.accept(value);
        };
    }

    /**
     * An Illegal Register with custom lazy error message.
     * @param errorMessageSupplier lazily gets the error to be displayed
     * */
    static<V> Register<V> illegalState(StringSupplier errorMessageSupplier) {
        return valueConsumer -> {
            throw new IllegalStateException(errorMessageSupplier.get());
        };
    }
    /**
     * An Illegal Register with custom error message.
     * @param errorMessage the error to be displayed
     * */
    static<V> Register<V> illegalState(String errorMessage) {
        return valueConsumer -> {
            throw new IllegalStateException(errorMessage);
        };
    }
}
