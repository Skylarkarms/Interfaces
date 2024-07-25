package utils;


import interfaces.ToStringFunction;

import java.util.Objects;
import java.util.function.*;

public final class Functions {

    private static final String CLEARED_STRING = ": [EMPTY FUNCTION]";

    /**@return A function that will return a captured {@link Function#apply(Object)} with a {@link RuntimeException} with a customizable message*/
    public static<T, S> Function<T, S> exceptionalFunction(Supplier<String> messageSupplier, Function<T, S> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception | Error e) {
                throw new RuntimeException(messageSupplier.get(), e);
            }
        };
    }

    private static final UnaryOperator<?> IDENTITY = UnaryOperator.identity();
    private static final Predicate<?> predicateAlwaysFalse = new Predicate<>() {
        @Override
        public boolean test(Object o) {
            return false;
        }

        @Override
        public String toString() {
            return super.toString() + CLEARED_STRING;
        }
    };
    private static final Predicate<?> predicateAlwaysTrue = new Predicate<>() {
        @Override
        public boolean test(Object o) {
            return true;
        }

        @Override
        public String toString() {
            return super.toString()
                    + "\n Predicate always true \n"
                    + CLEARED_STRING;
        }
    };

    private static final Predicate<Object[]> hasNulls = objects -> {
        for (int i = objects.length - 1; i >= 0; i--) {
            if (objects[i] == null) return true;
        }
        return false;
    };

    private static final Predicate<Object> nonNull = Objects::nonNull;

    @SuppressWarnings("unchecked")
    public static<T> Predicate<T> nonNull() {
        return (Predicate<T>) nonNull;
    }

    public static BooleanSupplier defaultTrue = () -> true;
    public static BooleanSupplier defaultFalse = () -> false;

    /**
     * @return null if not default,
     * <p> {@code true} if {@link #defaultTrue}
     * <p> {@code false} if {@link #defaultFalse}
     * */
    public static Boolean isDefault(BooleanSupplier that) {
        boolean notFalse;
        if ((notFalse = that != defaultFalse) && that != defaultTrue) {
            return null;
        } else return notFalse;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> alwaysFalse() {
        return (Predicate<T>) predicateAlwaysFalse;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> alwaysTrue() {
        return (Predicate<T>) predicateAlwaysTrue;
    }

    public static boolean isAlwaysTrue(Predicate<?> that) {
        return that == predicateAlwaysTrue;
    }

    /**
     * {@link Predicate} instance designed to check for null items in an array.
     * @return {@code true} if an array has any null items.
     * */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T[]> nullChecker() {
        return (Predicate<T[]>) (Predicate<?>) hasNulls;
    }

    /**
     * The operator is free to override it's equals method.
     * */
    public static boolean isIdentity(Function<?, ?> operator) {
        return operator.equals(IDENTITY);
    }

    /**
     * Returns an {@link Function#identity()} function instance.
     * Using this instance will allow the inference of whether the Function is 'identity' or not via:
     * {@link #isIdentity(Function)}
     * */
    @SuppressWarnings("unchecked")
    public static<T> UnaryOperator<T> myIdentity() {
        return (UnaryOperator<T>) IDENTITY;
    }

    private static final Runnable emptyRunnable = new Runnable() {
        @Override
        public void run() {

        }

        @Override
        public String toString() {
            return super.toString() + CLEARED_STRING;
        }
    };

    public static Runnable emptyRunnable() {
        return emptyRunnable;
    }
    public static boolean isEmpty(Runnable runnable) {
        return runnable == emptyRunnable;
    }

    public static <T> boolean isAlwaysFalse(Predicate<T> excludeOut) {
        return excludeOut == predicateAlwaysFalse;
    }

    @FunctionalInterface
    public interface ToBoolean<T> {
        boolean asBoolean(T t);

        default Function<T, Boolean> asFun() {
            return new Function<>() {
                @Override
                public Boolean apply(T t) {
                    return asBoolean(t);
                }

                @Override
                public boolean equals(Object obj) {
                    return this == obj
                            || ToBoolean.this == obj;
                }
            };
        }
    }

    public static Runnable exceptional(Runnable action) {
        StackTraceElement[] es = Thread.currentThread().getStackTrace();
        return () -> {
            try {
                action.run();
            } catch (Exception | Error e) {
                throw new IllegalStateException(
                        ToStringFunction.StackPrinter.PROV.toStringAll(es)
                        , e);
            }
        };
    }

    private static final Supplier<?> NULL_SUPP = new Supplier<>() {
        @Override
        public Object get() {
            return null;
        }

        @Override
        public String toString() {
            return super.toString() + CLEARED_STRING;
        }

    };

    @SuppressWarnings("unchecked")
    public static<T> Supplier<T> nullSupplier() {
        return (Supplier<T>) NULL_SUPP;
    }

    public static<T> Supplier<T> illegalSupplier(String errorMessage) {
        return () -> {
            throw new IllegalStateException(errorMessage);
        };
    }

    public static boolean isNull(Supplier<?> that) {
        return that == NULL_SUPP;
    }
}