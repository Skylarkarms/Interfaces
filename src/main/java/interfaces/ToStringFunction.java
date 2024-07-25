package interfaces;


import utils.Functions;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@FunctionalInterface
public interface ToStringFunction<T> extends Function<T, String> {

    String[] template = new String[0];

    static boolean isEmpty(String[] strings) {
        return strings == template;
    }

    /**
     * A {@link StackTraceElement} array formatter for display as {@link String}
     * Uses {@link Arrays#toString(String, Object[], String, String, int, int)} and references the next variables:
     * <ul>
     *     <li>
     *         {@link #prefix}
     *     </li>
     *     <li>
     *         {@link #join}
     *     </li>
     *     <li>
     *         {@link #suffix}
     *     </li>
     * </ul>
     * These variables can be changed at any given time.
     * */
    @FunctionalInterface
    interface StackPrinter {

        String toString(StackTraceElement[] es, Arrays.ViewRange range);

        default String toStringAll(StackTraceElement[] es) {
            return toString(es, Arrays.ViewRange.all);
        }

        String prefix = "\n >> Provenance = [\n >> at: ", join = "\n >> at: ", suffix = "\n] <<";
        StackPrinter PROV = (es, viewRange) -> viewRange.apply(
                es, (stackTraceElements, s, e) -> Arrays.toString(prefix, es, join, suffix, s, e)
        );
    }

    final class Collections {
        public static <X> String[] toStringArray(
                Collection<X> source,
                ToStringFunction<X> map
        ) {
            int length;
            if (source == null || (length = source.size()) == 0) return template.clone();
            String[] res = new String[length];
            Iterator<X> xIterator = source.iterator();
            for (int i = 0; i < length; i++) {
                res[i] = map.apply(xIterator.next());
            }
            return res;
        }

        public static <X> String[] toStringArray(
                Collection<X> source
        ) throws NullPointerException {
            int length;
            if (source == null || (length = source.size()) == 0) return template.clone();
            String[] res = new String[length];
            Iterator<X> xIterator = source.iterator();
            for (int i = 0; i < length; i++) {
                res[i] = xIterator.next().toString();
            }
            return res;
        }
    }

    final class Arrays {

        public static <E> String[] toStringArray(
                E[] from, ToStringFunction<E> map
        ) {
            int length;
            if (from == null || (length = from.length) == 0) return template.clone();
            final String[] res = new String[length];
            for (int i = 0; i < length; i++) {
                res[i] = map.apply(from[i]);
            }
            return res;
        }

        public static <E> String[] toStringArray(
                E[] from
        ) throws NullPointerException {
            int length;
            if (from == null || (length = from.length) == 0) return template.clone();
            final String[] res = new String[length];
            for (int i = 0; i < length; i++) {
                res[i] = from[i].toString();
            }
            return res;
        }

        public static final String
                nullS = "null";

        static final String
                left = "["
                , left_2 = "=["
                , right = "]"
                , comma = ", "
                , empty = left.concat(right)
                ;

        /**
         * @param to = non-inclusive
         * */
        static<E> String toString(E[] es, int from, int to) {
            int length;
            if (es == null || (length = es.length) == 0) return empty;
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            StringBuilder sb = new StringBuilder(left);
            E next;
            sb.append(
                    (next = es[finalFrom++]) == null ? nullS : next.toString()
            );
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(comma)
                        .append((next = es[finalFrom]) == null ? nullS : next.toString());
            }
            return sb.append(right).toString();
        }

        static<E> String toString(E[] es, ToStringFunction<E> map) {
            int length;
            if (es == null || (length = es.length) == 0) return empty;
            StringBuilder sb = new StringBuilder(left);
            int i = 0;
            sb.append(
                    map.apply(es[i++])
            );
            for (; i < length; i++) {
                sb.append(comma)
                        .append(
                                map.apply(es[i])
                        );
            }
            return sb.append(right).toString();
        }
        /**
         * Parameter class that will define the sub-view of the E[] array to be transformed into a String.
         * <ul>
         *     <li>
         *         {@link #all}
         *     </li>
         *     <li>
         *         {@link #first}
         *     </li>
         *     <li>
         *         {@link #last}
         *     </li>
         *     <li>
         *         {@link #limit(int, Limit)}
         *     </li>
         *     <li>
         *         {@link #single(int)}
         *     </li>
         *     <li>
         *         {@link #window(int, int)}
         *     </li>
         * </ul>
         * */
        public static final class ViewRange {
            final RangeResolve resolve;

            @FunctionalInterface
            interface RangeResolve {
                int start = 0, end = 1;
                int[] template = new int[]{0, 0};
                int[] apply(int length);
            }
            /**
             * Defines the 'side' by which the offset begins counting.
             * */
            public enum Limit {
                /**
                 * Defines order first-to-last
                 * */
                toFirst,
                /**
                 * Defines order last-to-first
                 * */
                toLast
            }

            public static ViewRange all = new ViewRange(
                    l -> {
                        int[] copy = RangeResolve.template.clone();
                        copy[RangeResolve.start] = 0;
                        copy[RangeResolve.end] = l;
                        return copy;
                    }
            );
            public static ViewRange first = new ViewRange(
                    l -> {
                        int[] copy = RangeResolve.template.clone();
                        copy[RangeResolve.start] = 0;
                        copy[RangeResolve.end] = 1;
                        return copy;
                    }
            );
            public static ViewRange last = new ViewRange(
                    l -> {
                        int[] copy = RangeResolve.template.clone();
                        copy[RangeResolve.start] = l - 1;
                        copy[RangeResolve.end] = l;
                        return copy;
                    }
            );

            private ViewRange(RangeResolve resolve) {
                this.resolve = resolve;
            }

            /**
             * Defines the {@link Limit} of the stack to print.
             * If the offset surpasses the max length of the stack, the length will be used instead,
             * returning the stack at index 0, or the last index depending on the direction chosen.
             * */
            public static ViewRange limit(int limit, Limit direction) {
                return switch (direction) {
                    case toLast -> new ViewRange(
                            l -> {
                                int[] copy = RangeResolve.template.clone();
                                copy[RangeResolve.start] = (l - 1) - limit;
                                copy[RangeResolve.end] = l;
                                return copy;
                            }
                    );
                    case toFirst -> new ViewRange(
                            l -> {
                                int[] copy = RangeResolve.template.clone();
                                copy[RangeResolve.start] = 0;
                                copy[RangeResolve.end] = limit + 1;
                                return copy;
                            }
                    );
                };
            }

            /**
             * Will create a sub-view of the array that will span between the index
             * defined at 'start' and the one defined at 'end', both inclusive.
             * 'start' will truncate at 0, and 'end' at length - 1;
             * */
            public static ViewRange window(int start, int end) {
                return new ViewRange(
                        l -> {
                            int[] copy = RangeResolve.template.clone();
                            copy[RangeResolve.start] = start;
                            copy[RangeResolve.end] = end + 1;
                            return copy;
                        }
                );
            }

            /**
             * Will create view comprised of a single index cell, or:
             * index 0, if the index is too small.
             * last index if the value surpassed the length of the array.
             * */
            public static ViewRange single(int index) {
                return new ViewRange(
                        l -> {
                            int[] copy = RangeResolve.template.clone();
                            copy[RangeResolve.start] = index;
                            copy[RangeResolve.end] = index + 1;
                            return copy;
                        }
                );
            }

            interface StringResolve<E> {
                String toS(E[] es, int s, int e);
                StringResolve<?> defaultToString = (StringResolve<Object>) Arrays::toString;
                StringResolve<String> identity = Arrays::toString;
                @SuppressWarnings("unchecked")
                static<E> StringResolve<E> getDefault() {
                    return (StringResolve<E>) defaultToString;
                }
            }

            private<E> String apply(E[] es, StringResolve<E> r) {
                int[] res = resolve.apply(es.length);
                return r.toS(es, res[RangeResolve.start], res[RangeResolve.end]);
            }
        }

        static<E> String toString(E[] es, ViewRange range) {
            return range.apply(es, ViewRange.StringResolve.getDefault());
        }

        public static String toString(String[] strings, int from, int to) {
            return toString(
                    strings, ToStringFunction.identity,
                    from, to
            );
        }

        public static String toString(String[] strings, String join) {
            return toString(
                    strings, join, 0, strings.length
            );
        }

        /**
         * @param to = non-inclusive
         * */
        static<E> String toString(
                E[] es
                , ToStringFunction<E> map
                , int from
                , int to
        ) {
            int length;
            if (es == null || (length = es.length) == 0) return empty;
            StringBuilder sb = new StringBuilder(left);
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            sb.append(
                    map.apply(es[finalFrom++])
            );
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(comma)
                        .append(
                                map.apply(es[finalFrom])
                        );
            }
            return sb.append(right).toString();
        }
        /**
         * @param to = non-inclusive
         * @param prefix will be appended BEFORE the first {@link #left}
         * */
        static<E> String toString(String prefix, E[] es, String join, ToStringFunction<E> map, int from, int to) {
            return toString(prefix.concat(left), es, join, right, map, from, to);
        }

        static<E> String toString(String prefix, E[] es, String join, String suffix, ToStringFunction<E> map, int from, int to) {
            int length;
            if (es == null || (length = es.length) == 0) return empty;
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            StringBuilder sb = new StringBuilder(prefix);
            sb.append(map.apply(es[finalFrom++]));
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(join)
                        .append(map.apply(es[finalFrom]));
            }
            return sb.append(suffix).toString();
        }

        static<E> String toString(String prefix, E[] es, String join, String suffix, int from, int to) {
            int length;
            if (es == null || (length = es.length) == 0) return empty;
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            StringBuilder sb = new StringBuilder(prefix);
            E next;
            sb.append(
                    (next = es[finalFrom++]) == null ? nullS : next.toString()
            );
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(join)
                        .append((next = es[finalFrom]) == null ? nullS : next.toString());
            }
            return sb.append(suffix).toString();
        }

        static<E> String toString(String prefix, E[] es, String join, int from, int to) {
            return toString(
                    prefix.concat(left_2), es, join, right, from, to
            );
        }

        static<E> String toString(E[] es, String join, ViewRange range) {
            return range.apply(es,
                    (es1, s, e) -> toString(es1, join, s, e)
            );
        }

        /**
         * Will defualt to {@link #comma} as default join separator.
         * */
        static<E> String toString(String prefix, E[] es, int from, int to) {
            return toString(prefix.concat(left_2), es, comma, right, from, to);
        }
        /**
         * Will default to {@link #left} as 'prefix' and {@link #right} as 'suffix'
         * */
        static<E> String toString(E[] es, String join, int from, int to) {
            return toString(left, es, join, right, from, to);
        }
    }

    ToStringFunction<?> valueOf = String::valueOf;
    ToStringFunction<String> identity = new ToStringFunction<>() {
        final UnaryOperator<String> id = Functions.myIdentity();
        @Override
        public String apply(String s) {
            return id.apply(s);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            return Objects.equals(id, o);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    };

    default boolean isIdentity() {
        return this == identity;
    }

    @SuppressWarnings("unchecked")
    static<T> ToStringFunction<T> valueOf() {
        return (ToStringFunction<T>) valueOf;
    }

    default boolean isValueOf() {
        return this == valueOf;
    }

    @FunctionalInterface
    interface FromInt {
        String asString(int integer);

        default ToStringFunction<Integer> as() {
            return this::asString;
        }

        FromInt valueOf = String::valueOf;
    }

    @FunctionalInterface
    interface FromDouble {
        String asString(double aDouble);
        default ToStringFunction<Double> as() {
            return this::asString;
        }

        FromDouble valueOf = String::valueOf;
    }
    @FunctionalInterface
    interface FromLong {
        String asString(long aLong);
        default ToStringFunction<Long> as() {
            return this::asString;
        }

        FromLong valueOf = String::valueOf;
    }

    static<E> String inspect(E[] es) {
        int length;
        if (es == null || (length = es.length) == 0) {
            return es == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(es.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(es[i]);
        }
        return builder.append("\n    }").toString();
    }

    static<E> String inspect(E[] es, ToStringFunction<E> map) {
        int length;
        if (es == null || (length = es.length) == 0) {
            return es == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(es.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(map.apply(es[i]));
        }
        return builder.append("\n    }").toString();
    }
    static<T> String inspect(double[] ts) {
        return inspect(ts, FromDouble.valueOf);
    }
    static<T> String inspect(double[] ts, FromDouble toString) {
        int length;
        if (ts == null || (length = ts.length) == 0) {
            return ts == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(ts.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(toString.asString(ts[i]));
        }
        return builder.append("\n    }").toString();
    }
    static<T> String inspect(long[] ts) {
        return inspect(ts, FromLong.valueOf);
    }
    static<T> String inspect(int[] ts) {
        return inspect(ts, FromInt.valueOf);
    }
    static<T> String inspect(long[] ts, FromLong toString) {
        int length;
        if (ts == null || (length = ts.length) == 0) {
            return ts == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(ts.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(toString.asString(ts[i]));
        }
        return builder.append("\n    }").toString();
    }

    private static StringBuilder getBuilder(Class<?> aClass, int length) {
        return getStringBuilder(aClass.getComponentType().toString(), length);
    }

    private static StringBuilder getStringBuilder(String componentType, int length) {
        return new StringBuilder(
                "Reading Array..." +
                        "\n >> Type: " + componentType +
                        "\n >> Length: " + length +
                        "\n >> Contents: {"
        );
    }

    static<T> String inspect(int[] ts, FromInt toString) {
        int length;
        if (ts == null || (length = ts.length) == 0) {
            return ts == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(ts.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(toString.asString(ts[i]));
        }
        return builder.append("\n    }").toString();
    }

    static String inspect(double[][] ts) {
        int length;
        if (ts == null || (length = ts.length) == 0) {
            return ts == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getStringBuilder("double[][]", length);
            for (int i = 0; i < length; i++) {
                double[] aDs = ts[i];
                builder.append("\n <*> row: [").append(i).append("]");
                for (int j = 0; j < aDs.length; j++) {
                    builder.append("\n    [").append(j).append("] >> ").append(aDs[j]);
                }
            }
        return builder.append("\n    }").toString();
    }

    static<E> String inspect(E[][] ts) {
        int length;
        if (ts == null || (length = ts.length) == 0) {
            return ts == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(ts.getClass(), length);
            for (int i = 0; i < length; i++) {
                E[] aDs = ts[i];
                builder.append("\n <*> row: [").append(i).append("]");
                for (int j = 0; j < aDs.length; j++) {
                    builder.append("\n    [").append(j).append("] >> ").append(aDs[j]);
                }
            }
        return builder.append("\n    }").toString();
    }

    /**Used for SQL statements*/
    static<T> String toQueryString(long[] longs) {
        assert longs != null;
        String query = "(";
        int length = longs.length, last = length - 1;
        for (int i = 0; i < last; i++) {
            query = query.concat(String.valueOf(longs[i]).concat(", "));
        }
        return query.concat(String.valueOf(longs[last]).concat(")"));
    }
}