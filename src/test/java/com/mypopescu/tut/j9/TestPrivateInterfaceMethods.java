package com.mypopescu.tut.j9;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Private interface methods in Java 9:
 * https://blog.codefx.org/java/java-9-tutorial/#Private-Interface-Methods
 *
 * @author Alex Popescu
 * @since 1.0, 2/10/18 1:38 PM
 */
public class TestPrivateInterfaceMethods {
    @Test
    public void testJava9() {
        PrivateInterfaceMethodsInJava9 p = new PrivateInterfaceMethodsInJava9() { };
        assumeTrue(System.getProperty("java.version").startsWith("9."),
                String.format("Test runs only in Java 9+: %s", System.getProperty("java.version")));
        assertTrue(p.evenSum(1, 1, 1, 1));
    }

    @Test
    public void testJava8() {
        PrivateInterfaceMethodsInJava8 p = new PrivateInterfaceMethodsInJava8() {};
        assertTrue(p.evenSum(2, 2, 2));
    }
}
