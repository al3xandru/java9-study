package com.mypopescu.tut.j9;


import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Based on Arnaud Roger's <a href="https://www.sitepoint.com/deep-dive-into-java-9s-stack-walking-api/">https://www.sitepoint.com/deep-dive-into-java-9s-stack-walking-api/</a>
 *
 * <blockquote>There are situations when you need to know who called your method.</blockquote>
 *
 * The section explaining why the {@link StackWalker#walk(Function)} takes a function is particularly
 * interesting:
 *
 * <blockquote>
 *     <p>
 *         [...] it is easiest to imagine it as a stable data structure that the JVM only mutates at the top,
 *         either adding or removing individual frames when methods are entered or exited.
 *         This is not entirely true, though. Instead, you should think of the stack as something that
 *         the VM can restructure anytime (including in the middle of your code being executed) to improve
 *         performance.
 *     </p>
 *     <p>
 *         So for the walker to see a consistent stack, the API needs to make sure that the stack is stable
 *         while it is building the frames. It can only do that if is in control of the call stack,
 *         which means your processing of the stream must happen within the call to the API.
 *         That’s why the stream can not be returned but must be traversed inside the call to walk.
 *     </p>
 * </blockquote>
 *
 * @author Alex Popescu
 * @since 1.0, 2/15/18 11:05 PM
 */
public class TestStackWalker {
    @Test()
    void preJava9StackTrace() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        StackTraceElement top = stackTrace[0];

        assertEquals("preJava9StackTrace", top.getMethodName());
        assertEquals(TestStackWalker.class.getName(), top.getClassName());
        // even if you can get the class and method names, you can't access the class
    }

    @Test
    void testGetInstance() {
        StackWalker s1 = StackWalker.getInstance();
        StackWalker s2 = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
        StackWalker s3 = StackWalker.getInstance(
                Set.of(RETAIN_CLASS_REFERENCE,
                       StackWalker.Option.SHOW_HIDDEN_FRAMES));
        long c1 = s1.walk(Stream::count);
        assertNotEquals(0, c1);

        long c2 = s2.walk(Stream::count);
        assertEquals(c1, c2);

        long c3 = s3.walk(Stream::count);
        assertNotEquals(c2, c3);
    }

    /**
     * When using {@link java.lang.StackTraceElement} you don't have access to the
     * class as seen in {@link #preJava9StackTrace()}.
     * When using {@link StackWalker#walk(Function)} you have access to all these details.
     *
     * There's an even more direct way to access the caller class:
     * {@link StackWalker#getCallerClass()}.
     */
    @Test
    void accessCallerClass() {
        StackWalker sw = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
        List<Class<?>> classes = sw.walk(stackFrameStream ->
            stackFrameStream.skip(1)
                            .limit(1)
                            .map(StackWalker.StackFrame::getDeclaringClass)
                            .collect(Collectors.toList()));
        Class<?> caller = classes.get(0);
        assertEquals(ReflectionUtils.class, caller);

        // simpler
        Class<?> callerClass = StackWalker.getInstance(RETAIN_CLASS_REFERENCE)
                .getCallerClass();

        assertEquals(caller, callerClass);
    }

    @Test
    void getDeclaringClassRequiresRetainClassReference() {
        StackWalker sw = StackWalker.getInstance();
        assertThrows(UnsupportedOperationException.class,
            () -> sw.forEach(frame -> System.out.println(frame.getDeclaringClass())));

        StackWalker sw2 = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
        try {
            sw2.forEach(StackWalker.StackFrame::getDeclaringClass);
        }
        catch(UnsupportedOperationException uoex) {
            fail("getDeclaringClass should work when RETAIN_CLASS_REFERENCE");
        }
    }

}
