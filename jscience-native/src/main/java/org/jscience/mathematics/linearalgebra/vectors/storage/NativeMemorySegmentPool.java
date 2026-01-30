/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.vectors.storage;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * A pool of reusable native MemorySegments to avoid frequent allocations.
 * Uses a Shared Arena for the pool's lifetime.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeMemorySegmentPool implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(NativeMemorySegmentPool.class.getName());
    
    private final long segmentSize;
    private final Arena arena;
    private final ConcurrentLinkedQueue<MemorySegment> pool = new ConcurrentLinkedQueue<>();
    private final AtomicLong allocatedCount = new AtomicLong(0);
    private final int maxPoolSize;

    /**
     * Creates a pool for segments of a specific size.
     * @param segmentSize size in bytes
     * @param maxPoolSize maximum number of segments to keep in pool
     */
    public NativeMemorySegmentPool(long segmentSize, int maxPoolSize) {
        this.segmentSize = segmentSize;
        this.maxPoolSize = maxPoolSize;
        this.arena = Arena.ofShared();
    }

    /**
     * Acquires a segment from the pool or allocates a new one.
     */
    public MemorySegment acquire() {
        MemorySegment segment = pool.poll();
        if (segment == null) {
            segment = arena.allocate(segmentSize, ValueLayout.JAVA_DOUBLE.byteAlignment());
            allocatedCount.incrementAndGet();
            LOGGER.fine(() -> String.format("Allocated new native segment of %d bytes. Total: %d", 
                segmentSize, allocatedCount.get()));
        }
        return segment;
    }

    /**
     * Returns a segment to the pool for reuse.
     */
    public void release(MemorySegment segment) {
        if (segment.byteSize() != segmentSize) {
            throw new IllegalArgumentException("Segment size mismatch");
        }
        if (pool.size() < maxPoolSize) {
            pool.offer(segment);
        }
        // If pool is full, we don't need to do anything, 
        // the segment will be deallocated when the arena closes.
    }

    public long getAllocatedCount() {
        return allocatedCount.get();
    }

    public int getPoolSize() {
        return pool.size();
    }

    @Override
    public void close() {
        arena.close();
        pool.clear();
    }
}
