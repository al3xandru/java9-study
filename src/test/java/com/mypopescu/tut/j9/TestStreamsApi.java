package com.mypopescu.tut.j9;


import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Based on Nicolai Parlog's blog post:
 * https://blog.codefx.org/java/java-9-stream/
 *
 * @author Alex Popescu
 * @since 1.0, 2/11/18 1:52 AM
 */
public class TestStreamsApi {
    @Test
    void takeWhile() {
        assertEquals(3,
                     IntStream.of(1, 2, 3, 4, 5)
                              .takeWhile(i -> i < 4)
                              .count());
    }

    @Test
    void dropWhile() {
        assertEquals(2,
                     IntStream.of(1, 2, 3, 4, 5)
                              .dropWhile(i -> i < 4)
                              .count());
    }

    /**
     * There was no way to interrupt {@link java.util.stream.Stream#iterate}
     * except using {@link java.util.stream.Stream#limit}
     */
    @Test
    void iterate() {
        assertEquals(2046,
                     IntStream.iterate(2, i -> 2 * i)
                              .limit(10)
                              .sum());
    }

    /**
     * In Java 9, there's a new overloaded {@link java.util.stream.Stream#iterate}
     * which takes a {@link java.util.function.Predicate}.  The <code>Predicate</code>
     * is expressed in terms of the <code>seed</code> (the first parameter) and not
     * as a counter.
     */
    @Test
    void newIterate() {
        assertEquals(2046,
                     IntStream.iterate(2, i -> i <= Math.pow(2, 10), i -> i * 2).sum());
    }

    @ParameterizedTest
    @MethodSource("createNullableArguments")
    void ofNullable(String param, int expected) {
        assertEquals(expected,
                     Stream.ofNullable(param).count());
    }

    private static Stream<Arguments> createNullableArguments() {
        return Stream.of(
                Arguments.of("nonempty", 1),
                Arguments.of("", 1),
                Arguments.of(null, 0)
        );
    }
}
