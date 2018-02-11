package com.mypopescu.tut.j9;


import java.util.stream.IntStream;

/**
 * This is how I'd implement a private interface method prior to Java 9.
 * It's based on nest classes: https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
 * @author Alex Popescu
 * @since 1.0, 2/10/18 2:12 AM
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
