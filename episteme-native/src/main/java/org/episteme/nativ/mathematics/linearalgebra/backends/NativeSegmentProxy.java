/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.Arena;

/**
 * Interface for Matrix/Vector implementations that are backed by a persistent native MemorySegment.
 * Allows zero-copy data transfer between the JVM and Native Backends (FFM/Panama).
 */
public interface NativeSegmentProxy {
    
    /**
     * Returns the underlying native memory segment.
     */
    MemorySegment segment();

    /**
     * Returns the Arena managing the lifecycle of the segment.
     */
    Arena arena();

    /**
     * Checks if the segment is still alive and safe to access.
     */
    default boolean isAlive() {
        return arena().scope().isAlive();
    }
}
