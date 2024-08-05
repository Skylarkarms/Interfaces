import interfaces.ToStringFunction;

import java.time.Duration;
import java.util.concurrent.locks.LockSupport;

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
        StackTraceElement[] es = Thread.currentThread().getStackTrace();
        String s = ToStringFunction.StackPrinter.PROV.toStringAll(es);
        System.out.println(s);
        LockSupport.parkNanos(Duration.ofSeconds(4).toNanos());
        ToStringFunction.StackPrinter.Params p = ToStringFunction.StackPrinter.params;
        p.prefix = "<< BEGINNING \n <-> ";
        p.join = "\n <-> ";
        p.suffix = "\n END >>";
        String s2 = ToStringFunction.StackPrinter.PROV.toStringAll(es);
        System.out.println(s2);
    }
}
