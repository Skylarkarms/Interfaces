package interfaces;

/**
 * A functional interface that will accept and return a '{@code boolean}' primitive.
 * <p> If no return value is desired used the {@link Consumer} variation instead.
 * */
@FunctionalInterface
public interface BooleanOperator {
    boolean apply(boolean aBoolean);

    @FunctionalInterface
    interface Consumer extends BooleanOperator {
        void accept(boolean aBoolean);

        @Override
        default boolean apply(boolean aBoolean) {
            accept(aBoolean);
            return aBoolean;
        }

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

    BooleanOperator IDENTITY = new BooleanOperator() {
        @Override
        public boolean apply(boolean aBoolean) {
            return false;
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

    default boolean isIdentity() {
        return false;
    }
}