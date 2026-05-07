/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.util;

import java.lang.foreign.MemorySegment;

/**
 * Common interface for native memory segment pooling strategies.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MemorySegmentPool extends AutoCloseable {
    
    /**
     * Acquires a memory segment from the pool.
     * 
     * @param sizeBytes requested size in bytes
     * @return a memory segment of at least the requested size
     */
    MemorySegment acquire(long sizeBytes);
    
    /**
     * Returns a memory segment to the pool for reuse.
     * 
     * @param segment the segment to return
     */
    void release(MemorySegment segment);
    
    /**
     * Gets the number of segments currently in the pool.
     * 
     * @return pool size
     */
    int getPoolSize();
    
    @Override
    void close();
}
