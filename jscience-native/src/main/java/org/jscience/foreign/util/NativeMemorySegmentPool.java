/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.foreign.util;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * A pool for native memory segments to reduce allocation overhead in tight loops.
 * <p>
 * This class is not thread-safe.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeMemorySegmentPool implements AutoCloseable {

    private final Arena arena;
    private final Map<Long, Deque<MemorySegment>> pools;

    /**
     * Creates a new pool using a shared arena.
     *
     * @param arena the arena to allocate from
     */
    public NativeMemorySegmentPool(Arena arena) {
        this.arena = arena;
        this.pools = new HashMap<>();
    }

    /**
     * Borrow a segment of at least the requested size.
     *
     * @param sizeBytes requested size
     * @return a memory segment
     */
    public MemorySegment borrow(long sizeBytes) {
        // Find pool for this size or slightly larger (simplification for now: exact size)
        Deque<MemorySegment> pool = pools.computeIfAbsent(sizeBytes, k -> new ArrayDeque<>());
        if (pool.isEmpty()) {
            return arena.allocate(sizeBytes);
        }
        return pool.pop();
    }

    /**
     * Return a segment to the pool.
     *
     * @param segment the segment to return
     */
    public void release(MemorySegment segment) {
        long size = segment.byteSize();
        Deque<MemorySegment> pool = pools.computeIfAbsent(size, k -> new ArrayDeque<>());
        pool.push(segment);
    }

    @Override
    public void close() {
        pools.clear();
        // Note: we don't close the arena if it was shared. 
        // If we want to own it, we should have a different constructor.
    }
}
