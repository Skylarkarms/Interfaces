package utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Consumers {
    /**
     * Can be directly referenced compared with the '{@code consumer}' parameter via {@link Object#equals(Object)}.
     * */
    public static<S, T> Consumer<S> map(Function<S, T> map, Consumer<? super T> consumer) {
        return new Consumer<>() {
            @Override
            public void accept(S s) {
                consumer.accept(map.apply(s));
            }

            @Override
            public boolean equals(Object obj) {
                return
                        this == obj
                                || Objects.equals(obj, consumer);
            }
        };
    }

    /**
     * Will accept the value if the {@code expect} {@link Predicate} test has been passed successfully ({@code true})
     * */
    public static<T> Consumer<T> test(Predicate<T> expect, Consumer<? super T> consumer) {
        return new Consumer<>() {
            @Override
            public void accept(T s) {
                if (expect.test(s)) consumer.accept(s);
            }

            @Override
            public boolean equals(Object obj) {
                return
                        this == obj
                                || Objects.equals(obj, consumer);
            }
        };
    }
    private static final Consumer<?> EMPTY = new Consumer<>() {
        @Override
        public void accept(Object o) {

        }

        @Override
        public String toString() {
            return "[EMPTY_CONSUMER]@".concat(Integer.toString(hashCode()));
        }
    };

    @SuppressWarnings("unchecked")
    public static<E> Consumer<E> getEmpty() {
        return (Consumer<E>) EMPTY;
    }

    public static boolean isEmpty(Consumer<?> aConsumer) {
        return aConsumer == EMPTY;
    }
}