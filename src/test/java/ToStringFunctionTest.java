import interfaces.ToStringFunction;

public class ToStringFunctionTest {
    static final long[] longs = new long[]{1, 3, 5, 7, 9};
    static final int[] ints = new int[]{5, 7, 9, 12, 15};
    static final Integer[] intOs = new Integer[]{5, 7, null, 12, null};
    public static void main(String[] args) {
        System.out.println(
                ToStringFunction.inspect(longs)
        );
        System.out.println(
                ToStringFunction.inspect(ints)
        );
        System.out.println(
                ToStringFunction.inspect(intOs)
        );
    }
}
