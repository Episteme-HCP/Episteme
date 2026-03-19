/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NativeSafe boundary protection.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NativeSafeTest {

    private static MethodHandle dummyHandle;

    @BeforeAll
    public static void setUp() throws Exception {
        // Create a dummy MethodHandle for testing
        // We use Math.max(long, long) as a safe proxy for a native handle (it takes long args)
        dummyHandle = MethodHandles.lookup().findStatic(Math.class, "max", 
                MethodType.methodType(long.class, long.class, long.class));
    }

    @Test
    public void testInvokeWithValidArgs() {
        Object result = NativeSafe.invoke(dummyHandle, 10L, 20L);
        assertEquals(20L, result);
    }

    @Test
    public void testInvokeWithNullHandle() {
        assertThrows(IllegalStateException.class, () -> {
            NativeSafe.invoke(null, 10L, 20L);
        });
    }

    @Test
    public void testInvokeWithMemorySegment() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segment = arena.allocate(1024);
            // NativeSafe.invoke should log a warning for NULL segments but still call if it's not actually NULL
            Object result = NativeSafe.invoke(dummyHandle, segment.address(), 0L);
            assertNotNull(result);
        }
    }

    @Test
    public void testInvokeWithNullMemorySegment() {
        // This should log a warning but still attempt the call
        Object result = NativeSafe.invoke(dummyHandle, MemorySegment.NULL.address(), 0L);
        assertNotNull(result);
    }
}
