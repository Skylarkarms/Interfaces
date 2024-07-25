package interfaces;

import java.util.function.BiFunction;

@FunctionalInterface
public interface BinaryFunction<T, U> extends BiFunction<T, T, U> {

    @FunctionalInterface
    interface ToBoolean<T> {
        boolean asBoolean(T t1, T t2);
    }
}
