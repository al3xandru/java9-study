package com.mypopescu.tut.j9;


import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alex Popescu
 * @since 1.0, 2/16/18 12:32 AM
 */
public class TestVersionApi {
    @Test
    void runtimeVersion() {
        Runtime.Version v = Runtime.version();
        String runtimeVersion = String.format("%d.%d.%d", v.major(), v.minor(), v.security());

        assertEquals(System.getProperty("java.version"), runtimeVersion);
        v.build().ifPresent(bv -> assertNotEquals(0, bv.intValue()));
    }
}
