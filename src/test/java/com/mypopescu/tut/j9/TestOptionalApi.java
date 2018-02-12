package com.mypopescu.tut.j9;


import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Code snippets from or inspired by Nicolai Parlog's
 * https://blog.codefx.org/java/java-9-optional/
 *
 * @author Alex Popescu
 * @since 1.0, 2/11/18 1:53 PM
 */
public class TestOptionalApi {
    @ParameterizedTest
    @MethodSource("optionalsParameterFactory")
    void stream(Optional<String> o, int expectedCount) {
        assertEquals(expectedCount,
                     o.stream().count());
    }

    private static Stream<Arguments> optionalsParameterFactory() {
        return Stream.of(
                Arguments.of(Optional.of("notempty"), 1),
                Arguments.of(Optional.of(""), 1),
                Arguments.of(Optional.empty(), 0)
        );
    }

    @Test
    void or() {
        Optional<String> o = Optional.of("optvalue")
                                     .or(() -> Optional.of("orvalue"));
        assertEquals("optvalue", o.get());

        o = Optional.<String>empty()
                    .or(() -> Optional.of("orvalue"));

        assertEquals("orvalue", o.get());
    }

    /**
     * Extends {@link java.util.Optional#ifPresent(Consumer)} allowing
     * the execution of a <code>Runnable</code> when the value is not present.
     */
    @Test
    void ifPresentOrElse() {
        assertAll("ifPresentOrElse",
                  () -> Optional.of("optvalue").ifPresentOrElse(v -> assertEquals("optvalue", v), () -> fail("runnable called")),
                  () -> assertThrows(AssertionFailedError.class,
                                     () -> Optional.empty().ifPresentOrElse(v -> fail("consumer called"), () -> assertTrue(false)))
        );
    }
}
