/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification of ResourceTracker deterministic cleanup.
 */
public class ResourceTrackerTest {

    @Test
    public void testDeterministicCleanup() {
        AtomicInteger closedCount = new AtomicInteger(0);
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            tracker.track(closedCount, c -> c.incrementAndGet());
            tracker.track(closedCount, c -> c.incrementAndGet());
            assertEquals(0, closedCount.get(), "Should not be closed yet");
        }
        
        assertEquals(2, closedCount.get(), "All tracked resources should be released on close()");
    }

    @Test
    public void testExceptionSafety() {
        AtomicInteger closedCount = new AtomicInteger(0);
        
        try {
            try (ResourceTracker tracker = new ResourceTracker()) {
                tracker.track(closedCount, c -> c.incrementAndGet());
                throw new RuntimeException("Test Error");
            }
        } catch (RuntimeException e) {
            // Expected
        }
        
        assertEquals(1, closedCount.get(), "Resources should be released even if an exception occurs");
    }

    @Test
    public void testNestedTrackers() {
        AtomicInteger outerCount = new AtomicInteger(0);
        AtomicInteger innerCount = new AtomicInteger(0);

        try (ResourceTracker outer = new ResourceTracker()) {
            outer.track(outerCount, c -> c.incrementAndGet());
            
            try (ResourceTracker inner = new ResourceTracker()) {
                inner.track(innerCount, c -> c.incrementAndGet());
                assertEquals(0, innerCount.get());
            }
            
            assertEquals(1, innerCount.get(), "Inner should be closed");
            assertEquals(0, outerCount.get(), "Outer should still be open");
        }
        
        assertEquals(1, outerCount.get(), "Outer should be closed");
    }
}
