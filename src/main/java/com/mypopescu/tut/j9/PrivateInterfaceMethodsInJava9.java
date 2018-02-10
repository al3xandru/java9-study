package com.mypopescu.tut.j9;


import java.util.stream.IntStream;

/**
 * @author Alex Popescu
 * @since 1.0, 2/10/18 1:37 PM
 */
public interface PrivateInterfaceMethodsInJava9 {
    default boolean evenSum(int... numbers) {
        return sum(numbers) % 2 == 0;
    }

    default boolean oddSum(int... numbers) {
        return sum(numbers) % 2 != 0;
    }

    private int sum(int... numbers) {
        return IntStream.of(numbers).sum();
    }
}
