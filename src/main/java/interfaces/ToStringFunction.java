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
     *         {@link Params#prefix}
     *     </li>
     *     <li>
     *         {@link Params#join}
     *     </li>
     *     <li>
     *         {@link Params#suffix}
     *     </li>
     * </ul>
     * These variables can be changed at any given time.
     * */
    @FunctionalInterface
    interface StackPrinter {

        /**
         * @param es the {@link StackTraceElement} array to parsed on to a {@link String}.
         * @param range defines the portion/range of the array to be computed.
         * @see Arrays.ViewRange
         * */
        String toString(StackTraceElement[] es, Arrays.ViewRange range);

        /**
         * @param es the {@link StackTraceElement} array to parsed on to a {@link String}.
         * @implNote will use {@link Arrays.ViewRange#all} by default.
         * */
        default String toStringAll(StackTraceElement[] es) {
            return toString(es, Arrays.ViewRange.all);
        }

        final class Params {
            private Params() {
            }

            public String prefix = "\n >> Provenance = [\n >> at: ",
                    join = "\n >> at: ",
                    suffix = "\n] <<";
        }

        Params params = new Params();

        /**
         * Default {@link StackPrinter} formatter that applies {@link Arrays#toString(String, Object[], String, String, int, int)}
         * with the {@link Arrays.ViewRange} variation
         * Using the values:
         * <ul>
         *     <li>
         *         {@code prefix} = {@link Params#prefix}
         *     </li>
         *     <li>
         *         {@code join} = {@link Params#join}
         *     </li>
         *     <li>
         *         {@code suffix} = {@link Params#suffix}
         *     </li>
         * </ul>
         * */
        StackPrinter PROV = (es, viewRange) -> viewRange.apply(
                es, (stackTraceElements, s, e) -> {
                    Params p = params;
                    return Arrays.toString(p.prefix, es, p.join, p.suffix, s, e);
                }
        );
    }

    final class Collections {
        private Collections() {}


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
        private Arrays() {}

        /**
         * Maps an array to a {@link String} array.
         * @param source the source array.
         * @param map the function to be applied to each element.
         * */
        public static <E> String[] toStringArray(
                E[] source, ToStringFunction<E> map
        ) {
            int length;
            if (source == null || (length = source.length) == 0) return template.clone();
            final String[] res = new String[length];
            for (int i = 0; i < length; i++) {
                res[i] = map.apply(source[i]);
            }
            return res;
        }

        /**
         * Maps an array to a {@link String[]} by applying {@link Object#toString()} on each element.
         * @param source the source array.
         * */
        public static <E> String[] toStringArray(
                E[] source
        ) throws NullPointerException {
            int length;
            if (source == null || (length = source.length) == 0) return template.clone();
            final String[] res = new String[length];
            for (int i = 0; i < length; i++) {
                res[i] = source[i].toString();
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
         * Formats a source array to a {@link String} following the convention specified at {@link java.util.Arrays#toString(Object[])}
         * @param source the array to be mapped.
         * @param from = the index to begin the formatting (inclusive)
         * @param to = the index to end (non-inclusive).
         * @apiNote Information about source's original length and previous indexes will be lost.
         * */
        public static<E> String toString(E[] source, int from, int to) {
            int length;
            if (source == null || (length = source.length) == 0) return empty;
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            StringBuilder sb = new StringBuilder(left);
            E next;
            sb.append(
                    (next = source[finalFrom++]) == null ? nullS : next.toString()
            );
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(comma)
                        .append((next = source[finalFrom]) == null ? nullS : next.toString());
            }
            return sb.append(right).toString();
        }

        /**
         * Formats a source array to a {@link String} by applying the specified function on each element and following the convention defined at {@link java.util.Arrays#toString(Object[])}
         * @param source the source array to be mapped.
         * @param map the {@link ToStringFunction} to be applied to each element {@link E}
         * */
        static<E> String toString(E[] source, ToStringFunction<E> map) {
            int length;
            if (source == null || (length = source.length) == 0) return empty;
            StringBuilder sb = new StringBuilder(left);
            int i = 0;
            sb.append(
                    map.apply(source[i++])
            );
            for (; i < length; i++) {
                sb.append(comma)
                        .append(
                                map.apply(source[i])
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

            /**
             * The view will comprise the totality of the length of the source array.
             * */
            public static ViewRange all = new ViewRange(
                    l -> {
                        int[] copy = RangeResolve.template.clone();
                        copy[RangeResolve.start] = 0;
                        copy[RangeResolve.end] = l;
                        return copy;
                    }
            );
            /**
             * The view will return just the first element of the source array.
             * */
            public static ViewRange first = new ViewRange(
                    l -> {
                        int[] copy = RangeResolve.template.clone();
                        copy[RangeResolve.start] = 0;
                        copy[RangeResolve.end] = 1;
                        return copy;
                    }
            );
            /**
             * The view will return the last element of the source array.
             * */
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

        /**
         * Default implementation of {@link #toString(Object[], int, int)} where both parameters '{@code from}' and '{@code to}' have been replaced by {@link ViewRange}
         * @see ViewRange
         * */
        public static<E> String toString(E[] es, ViewRange range) {
            return range.apply(es, ViewRange.StringResolve.getDefault());
        }

        public static String toString(String[] strings, int from, int to) {
            return toString(
                    strings, ToStringFunction.identity,
                    from, to
            );
        }

        /**
         * Default implementation of {@link #toString(Object[], String, int, int)}
         * <p> where:
         * <ul>
         *     <li>
         *         {@code from} = 0
         *     </li>
         *     <li>
         *         {@code to} = length
         *     </li>
         * </ul>
         *
         * */
        public static String toString(String[] strings, String join) {
            return toString(
                    strings, join, 0, strings.length
            );
        }

        /**
         * Formats a source array to a {@link String} by applying a {@link ToStringFunction} on each element and
         * following the convention specified at {@link java.util.Arrays#toString(Object[])}
         * @param source the array to be mapped.
         * @param map the mapping function
         * @param from the index to begin the formatting (inclusive)
         * @param to the index to end (non-inclusive).
         * @apiNote Information about source's original length and previous indexes will be lost.
         * */
        public static<E> String toString(
                E[] source
                , ToStringFunction<E> map
                , int from
                , int to
        ) {
            int length;
            if (source == null || (length = source.length) == 0) return empty;
            StringBuilder sb = new StringBuilder(left);
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            sb.append(
                    map.apply(source[finalFrom++])
            );
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(comma)
                        .append(
                                map.apply(source[finalFrom])
                        );
            }
            return sb.append(right).toString();
        }
        /**
         * @param to = non-inclusive
         * @param prefix will be appended BEFORE the first {@link #left}
         * */
        public static<E> String toString(String prefix, E[] es, String join, ToStringFunction<E> map, int from, int to) {
            return toString(prefix.concat(left), es, join, right, map, from, to);
        }

        /**
         * Formats a source array to a {@link String} by applying a {@link ToStringFunction} on each element and
         * following the convention:
         * <p> 'prefix' + map.apply(firstElement) + 'join' + map.apply(secondElement) + 'join' + ... + map.apply(finalElement) + 'suffix';
         * @param source the array to be mapped.
         * @param map the mapping function
         * @param from the index to begin the formatting (inclusive)
         * @param to the index to end (non-inclusive).
         * @apiNote Information about source's original length and previous indexes will be lost.
         * */
        public static<E> String toString(String prefix, E[] source, String join, String suffix, ToStringFunction<E> map, int from, int to) {
            int length;
            if (source == null || (length = source.length) == 0) return empty;
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            StringBuilder sb = new StringBuilder(prefix);
            sb.append(map.apply(source[finalFrom++]));
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(join)
                        .append(map.apply(source[finalFrom]));
            }
            return sb.append(suffix).toString();
        }

        /**
         * Formats a source array to a {@link String} by applying an inlined {@link String#valueOf(Object)}
         * following the convention:
         * <p> 'prefix' + map.apply(firstElement) + 'join' + map.apply(secondElement) + 'join' + ... + map.apply(finalElement) + 'suffix';
         * @param source the array to be mapped.
         * @param from the index to begin the formatting (inclusive)
         * @param to the index to end (non-inclusive).
         * @apiNote Information about source's original length and previous indexes will be lost.
         * */
        public static<E> String toString(String prefix, E[] source, String join, String suffix, int from, int to) {
            int length;
            if (source == null || (length = source.length) == 0) return empty;
            int finalTo = Math.min(to, length);
            int finalFrom = Math.min(Math.max(from, 0), length - 1);
            StringBuilder sb = new StringBuilder(prefix);
            E next;
            sb.append(
                    (next = source[finalFrom++]) == null ? nullS : next.toString()
            );
            for (; finalFrom < finalTo; finalFrom++) {
                sb.append(join)
                        .append((next = source[finalFrom]) == null ? nullS : next.toString());
            }
            return sb.append(suffix).toString();
        }

        /**
         * Default implementation of {@link #toString(String, Object[], String, String, int, int)}
         * <p> where:
         * <ul>
         *     <li>
         *         {@code prefix} = {@link #left_2}
         *     </li>
         *     <li>
         *         {@code suffix} = {@link #right}
         *     </li>
         * </ul>
         * */
        static<E> String toString(String prefix, E[] source, String join, int from, int to) {
            return toString(
                    prefix.concat(left_2), source, join, right, from, to
            );
        }

        /**
         * Default implementation of {@link #toString(String, Object[], String, String, int, int)} that accepts a {@link ViewRange} as parameter,
         * replacing the parameters '{@code from}' and '{@code to}'.
         * @see ViewRange
         * */
        static<E> String toString(E[] source, String join, ViewRange range) {
            return range.apply(source,
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
         * Default implementation of {@link #toString(String, Object[], String, String, int, int)}
         * <p> Where:
         * <ul>
         *     <li>
         *         {@code prefix} = {@link #left}
         *     </li>
         *     <li>
         *         {@code suffix} = {@link #right}
         *     </li>
         * </ul>
         * */
        static<E> String toString(E[] source, String join, int from, int to) {
            return toString(left, source, join, right, from, to);
        }
    }

    /**
     * Instance for lambda reference of {@link String#valueOf(Object)}
     * */
    ToStringFunction<?> valueOf = String::valueOf;
    /**
     * Instance for lambda function of {@code s -> s;}
     * This reference shares the same instance as {@link Functions#myIdentity()}
     * */
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

    /**
     * @return true if this == {@link #identity}
     * */
    default boolean isIdentity() {
        return this == identity;
    }

    /**
     * @return reified reference of {@link #valueOf}
     * */
    @SuppressWarnings("unchecked")
    static<T> ToStringFunction<T> valueOf() {
        return (ToStringFunction<T>) valueOf;
    }

    /**
     * @return true if this == {@link #valueOf}
     * */
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

    /**
     * Returns a {@link String} with specific details about the array's:
     * <ul>
     *     <li>
     *         length
     *     </li>
     *     <li>
     *         {@link Class}
     *     </li>
     *     <li>
     *         Element index
     *     </li>
     * </ul>
     * */
    static<E> String inspect(E[] array) {
        int length;
        if (array == null || (length = array.length) == 0) {
            return array == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(array.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(array[i]);
        }
        return builder.append("\n    }").toString();
    }
    /**
     * Similar behavior to {@link #inspect(Object[])} that applyies a {@link ToStringFunction} to each of the elements of the array.
     * @param array the array to be inspected.
     * @param map the function to be applied to each element.
     * */
    static<E> String inspect(E[] array, ToStringFunction<E> map) {
        int length;
        if (array == null || (length = array.length) == 0) {
            return array == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(array.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(map.apply(array[i]));
        }
        return builder.append("\n    }").toString();
    }
    static<T> String inspect(double[] doubles) {
        return inspect(doubles, FromDouble.valueOf);
    }
    static<T> String inspect(double[] doubles, FromDouble toString) {
        int length;
        if (doubles == null || (length = doubles.length) == 0) {
            return doubles == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(doubles.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(toString.asString(doubles[i]));
        }
        return builder.append("\n    }").toString();
    }
    static<T> String inspect(long[] longs) {
        return inspect(longs, FromLong.valueOf);
    }
    static<T> String inspect(int[] ints) {
        return inspect(ints, FromInt.valueOf);
    }
    static<T> String inspect(long[] longs, FromLong toString) {
        int length;
        if (longs == null || (length = longs.length) == 0) {
            return longs == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(longs.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(toString.asString(longs[i]));
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

    static<T> String inspect(int[] ints, FromInt toString) {
        int length;
        if (ints == null || (length = ints.length) == 0) {
            return ints == null ? "Array is null" : "Array is empty";
        }
        StringBuilder builder = getBuilder(ints.getClass(), length);
        for (int i = 0; i < length; i++) {
            builder.append("\n [").append(i).append("] >> ").append(toString.asString(ints[i]));
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