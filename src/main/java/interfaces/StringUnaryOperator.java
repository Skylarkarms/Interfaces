package interfaces;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface StringUnaryOperator extends UnaryOperator<String> {
    static String stripEdges(String stringArray) {
        return stringArray.substring(1, stringArray.length() - 1);
    }
}
