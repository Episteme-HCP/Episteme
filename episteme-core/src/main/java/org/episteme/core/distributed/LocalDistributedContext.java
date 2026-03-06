/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.distributed;

import org.episteme.core.technical.backend.distributed.DistributedContext;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.nio.DoubleBuffer;

/**
 * Local implementation of DistributedContext using ForkJoinPool.
 * <p>
 * This serves as the default "distributed" context, running tasks in parallel
 * on the local machine.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LocalDistributedContext implements DistributedContext {

    private static final org.episteme.core.util.Logger LOG = org.episteme.core.util.Logger
            .getLogger(LocalDistributedContext.class);

    private final ForkJoinPool pool;

    public LocalDistributedContext() {
        this.pool = ForkJoinPool.commonPool();
    }

    public LocalDistributedContext(int parallelism) {
        this.pool = (parallelism == ForkJoinPool.getCommonPoolParallelism()) ? 
                    ForkJoinPool.commonPool() : new ForkJoinPool(parallelism);
        LOG.debug(() -> String.format("LocalDistributedContext initialized with parallelism=%d (CommonPool: %b)", 
                  pool.getParallelism(), pool == ForkJoinPool.commonPool()));
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task) {
        // Capture current MathContext
        org.episteme.core.mathematics.context.MathContext capturedContext = org.episteme.core.mathematics.context.MathContext
                .getCurrent();

        // Wrap task to propagate context
        Callable<T> wrappedTask = () -> {
            org.episteme.core.mathematics.context.MathContext oldContext = org.episteme.core.mathematics.context.MathContext
                    .getCurrent();
            try {
                org.episteme.core.mathematics.context.MathContext.setCurrent(capturedContext);
                LOG.debug(() -> "Executing task with context: " + capturedContext);
                return task.call();
            } finally {
                org.episteme.core.mathematics.context.MathContext.setCurrent(oldContext);
            }
        };

        return pool.submit(wrappedTask);
    }

    @Override
    public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
        // Capture current MathContext
        org.episteme.core.mathematics.context.MathContext capturedContext = org.episteme.core.mathematics.context.MathContext
                .getCurrent();

        // Wrap all tasks to propagate context
        List<Callable<T>> wrappedTasks = tasks.stream()
                .map(task -> (Callable<T>) () -> {
                    org.episteme.core.mathematics.context.MathContext oldContext = org.episteme.core.mathematics.context.MathContext
                            .getCurrent();
                    try {
                        org.episteme.core.mathematics.context.MathContext.setCurrent(capturedContext);
                        return task.call();
                    } finally {
                        org.episteme.core.mathematics.context.MathContext.setCurrent(oldContext);
                    }
                })
                .collect(Collectors.toList());

        try {
            return pool.invokeAll(wrappedTasks).stream()
                    .map(f -> (Future<T>) f)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during parallel task execution", e);
        }
    }

    @Override
    public int getParallelism() {
        return pool.getParallelism();
    }

    @Override
    public void shutdown() {
        if (pool != ForkJoinPool.commonPool()) {
            pool.shutdown();
        }
    }

    private final DoubleBuffer localMemory = DoubleBuffer.allocate(1000000);

    @Override
    public void put(DoubleBuffer source, int targetRank, long offset) {
        int pos = source.position();
        localMemory.put((int) offset, source, pos, source.remaining());
    }

    @Override
    public void get(DoubleBuffer target, int sourceRank, long offset) {
        int limit = target.remaining();
        for (int i = 0; i < limit; i++) {
            target.put(localMemory.get((int) offset + i));
        }
    }

    @Override
    public void fence() {
        // No-op for local memory
    }

    @Override
    public void broadcast(DoubleBuffer buffer, int root) {
        // In a local context (single node), broadcast is a no-op as the data is already present.
    }

    @Override
    public void allGather(DoubleBuffer sendBuffer, DoubleBuffer recvBuffer) {
        // In a local context, AllGather effectively copies sendBuffer to recvBuffer
        if (recvBuffer.remaining() < sendBuffer.remaining()) {
             throw new IllegalArgumentException("Receive buffer too small for local AllGather");
        }
        
        // Save position/limit
        int sendPos = sendBuffer.position();
        // int recvPos = recvBuffer.position(); // Unused
        
        // Copy data
        recvBuffer.put(sendBuffer);
        
        // Restore positions (optional, but good practice for "simulating" independent buffers)
        // However, for typical AllGather usage, recvBuffer is expected to be filled.
        // We leave recvBuffer at its new position.
        sendBuffer.position(sendPos);
    }

    @Override
    public void barrier() {
        // No-op for single threaded/local context
    }
}


