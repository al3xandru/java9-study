package com.mypopescu.tut.j9;


import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
    public void takeWhile() {
        assertEquals(3,
            IntStream.of(1, 2, 3, 4, 5)
                    .takeWhile(i -> i < 4)
                    .count());
    }

    @Test
    public void dropWhile() {
        assertEquals(2,
                     IntStream.of(1, 2, 3, 4, 5)
                              .dropWhile(i -> i < 4)
                              .count());
    }

    @Test
    public void iterate() {
        assertEquals(2046,
                     IntStream.iterate(2, i -> 2 * i)
                              .limit(10)
                              .sum());
    }

    @Test
    @Disabled
    public void newIterate() {
        assertEquals(2045,
                     IntStream.iterate(2, i -> i <= 10, i -> 2 * 1)
                              .sum());
    }
}
