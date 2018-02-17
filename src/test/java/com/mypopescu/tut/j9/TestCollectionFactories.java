package com.mypopescu.tut.j9;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alex Popescu
 * @since 1.0, 2/13/18 11:35 PM
 */
public class TestCollectionFactories {
    @Test
    void listOf() {
        List<String> l = List.of("1", "2", "3");

        assertThrows(UnsupportedOperationException.class,
                     () -> l.add("4"));
        assertThrows(UnsupportedOperationException.class,
                     () -> l.set(0, "4"));
        assertThrows(UnsupportedOperationException.class,
                     () -> l.remove(2));
    }

    @Test
    void setOf() {
        IllegalArgumentException iaex = assertThrows(IllegalArgumentException.class,
                                                     () -> Set.of("1", "1"));
        assertTrue(iaex.getMessage().contains("duplicate element"));

        Set<String> l = Set.of("1", "2", "3");

        assertThrows(UnsupportedOperationException.class,
                     () -> l.add("4"));
        assertThrows(UnsupportedOperationException.class,
                     () -> l.remove("2"));
    }

    @ParameterizedTest
    @MethodSource("createSampleMap")
    void mapOf(Map<String, String> m) {
        assertThrows(UnsupportedOperationException.class,
                     () -> m.put("Alex", "P"));
        assertThrows(UnsupportedOperationException.class,
                     () -> m.put("New", "New"));
        assertThrows(UnsupportedOperationException.class,
                     () -> m.putIfAbsent("New", "New"));
        // is this slightly unexpected?
        // not really as the behavior needs to be
        assertThrows(UnsupportedOperationException.class,
                     () -> m.putIfAbsent("Alex", "P"));
    }

    static Stream<Arguments> createSampleMap() {
        return Stream.of(
                Arguments.of(Map.of("Alex", "Alex", "Ana", "Ana")),
                Arguments.of(Map.ofEntries(entry("Alex", "Alex"),
                                           entry("Ana", "Ana"))));
    }
}
