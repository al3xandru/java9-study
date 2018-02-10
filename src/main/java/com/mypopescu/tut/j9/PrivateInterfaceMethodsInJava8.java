package com.mypopescu.tut.j9;


import java.util.stream.IntStream;

/**
 * @author Alex Popescu
 * @since 1.0, 2/10/18 2:12 AM
 * https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
 */
public interface PrivateInterfaceMethodsInJava8 {
    default boolean evenSum(int... numbers) {
        return DefaultImpl.sum(numbers) % 2 == 0;
    }

    static class DefaultImpl {
        private static int sum(int... numbers) {
            return IntStream.of(numbers).sum();
        }
    }
}
