package com.mypopescu.tut.j9;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alex Popescu
 * @since 1.0, 2/11/18 12:55 AM
 */
public class TestTryWithFinalResource {
    @Test
    @Tag("java9")
    public void tryWithFinalResource() throws Exception {
        final TestAutoCloseable ac = new TestAutoCloseable();
        try(ac) {
            ac.op();
        }
        assertTrue(ac.done);
        assertTrue(ac.closed);
    }

    @Test
    @Tag("java8")
    public void tryWithResource() throws Exception {
        try(TestAutoCloseable ac = new TestAutoCloseable()) {
            ac.op();
        }
        // now I can't do the assertions
        /*
        assertTrue(ac.done);
        assertTrue(ac.closed);
        */
    }

    private static class TestAutoCloseable implements AutoCloseable {
        boolean closed = false;
        boolean done = false;

        public void op() {
            done = true;
        }

        @Override
        public void close() {
            closed = true;
        }
    }
}
