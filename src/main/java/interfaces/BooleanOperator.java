package interfaces;

/**
 * A <a href="package-summary.html">functional interface</a> that will accept and return a '{@code boolean}' primitive.
 * <p> If no return value is desired used the {@link Consumer} variation instead.
 * */
@FunctionalInterface
public interface BooleanOperator {

    /**
     * Applies this function to the given boolean primitive.
     *
     * @param aBoolean the function argument
     * @return the function result
     */
    boolean apply(boolean aBoolean);

    /**
     * Represents an operation that accepts a single boolean primitive argument and returns no
     * result. Unlike most other functional interfaces, {@code Consumer} is expected
     * to operate via side-effects.
     *
     * <p>This is a <a href="package-summary.html">functional interface</a>
     * whose functional method is {@link #accept(boolean)}.
     */
    @FunctionalInterface
    interface Consumer extends BooleanOperator {
        /**
         * Performs this operation on the given boolean argument.
         *
         * @param aBoolean the boolean input argument
         */
        void accept(boolean aBoolean);

        @Override
        default boolean apply(boolean aBoolean) {
            accept(aBoolean);
            return aBoolean;
        }

        /**
         * Default lambda implementation of {@link Consumer} where no action will take place upon consumption.
         * */
        Consumer IDENTITY = new Consumer() {
            @Override
            public void accept(boolean aBoolean) {}

            @Override
            public boolean isIdentity() {
                return true;
            }

            @Override
            public String toString() {
                return "[IDENTITY_CONSUMER]@".concat(Integer.toString(hashCode()));
            }
        };
    }

    /**
     * Default lambda implementation of {@link BooleanOperator} where {@link #apply(boolean)} will always return the same value applied;
     * */
    BooleanOperator IDENTITY = new BooleanOperator() {
        @Override
        public boolean apply(boolean aBoolean) {
            return aBoolean;
        }

        @Override
        public boolean isIdentity() {
            return true;
        }

        @Override
        public String toString() {
            return "[IDENTITY_OPERATOR]@".concat(Integer.toString(hashCode()));
        }
    };

    /**
     * @return true if this {@link BooleanOperator} is instance of {@link BooleanOperator#IDENTITY}
     * */
    default boolean isIdentity() {
        return false;
    }
}