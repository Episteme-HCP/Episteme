/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.vectors.storage;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A pool of reusable native MemorySegments to avoid frequent allocations.
 * Uses a Shared Arena for the pool's lifetime.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class FixedSizeNativeMemorySegmentPool implements org.episteme.nativ.util.MemorySegmentPool {

    private static final Logger logger = LoggerFactory.getLogger(FixedSizeNativeMemorySegmentPool.class);
    
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
    public FixedSizeNativeMemorySegmentPool(long segmentSize, int maxPoolSize) {
        this.segmentSize = segmentSize;
        this.maxPoolSize = maxPoolSize;
        this.arena = Arena.ofShared();
    }

    /**
     * Acquires a segment from the pool or allocates a new one.
     * Ignores the sizeBytes parameter and always returns segments of the configured size.
     */
    @Override
    public MemorySegment acquire(long sizeBytes) {
        if (sizeBytes != segmentSize) {
            logger.warn("Requested size {} does not match pool size {}. Returning pool-sized segment.",
                sizeBytes, segmentSize);
        }
        return acquire();
    }

    /**
     * Acquires a segment from the pool or allocates a new one.
     */
    public MemorySegment acquire() {
        MemorySegment segment = pool.poll();
        if (segment == null) {
            segment = arena.allocate(segmentSize, ValueLayout.JAVA_DOUBLE.byteAlignment());
            allocatedCount.incrementAndGet();
            logger.debug("Allocated new native segment of {} bytes. Total: {}", 
                segmentSize, allocatedCount.get());
        }
        return segment;
    }

    /**
     * Returns a segment to the pool for reuse.
     */
    @Override
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

    @Override
    public int getPoolSize() {
        return pool.size();
    }

    @Override
    public void close() {
        arena.close();
        pool.clear();
    }
}





