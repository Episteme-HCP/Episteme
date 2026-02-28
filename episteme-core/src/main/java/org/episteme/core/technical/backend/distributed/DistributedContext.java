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

package org.episteme.core.technical.backend.distributed;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.nio.DoubleBuffer;

/**
 * Defines the contract for distributed computing contexts.
 * <p>
 * Implementations can range from local thread pools to cluster-based systems
 * like Spark, Hazelcast, or GridGain.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface DistributedContext {

    /**
     * Priority levels for distributed tasks.
     */
    enum Priority {
        LOW, NORMAL, HIGH, CRITICAL
    }

    /**
     * Submits a task for execution with NORMAL priority.
     * 
     * @param <T>  Result type
     * @param task Task to execute
     * @return Future representing the result
     */
    <T extends Serializable> Future<T> submit(Callable<T> task);

    /**
     * Submits a task for execution with a specific priority.
     * 
     * @param <T>      Result type
     * @param task     Task to execute
     * @param priority Priority level
     * @return Future representing the result
     */
    default <T extends Serializable> Future<T> submit(Callable<T> task, Priority priority) {
        return submit(task);
    }

    /**
     * Submits a collection of tasks for execution.
     * 
     * @param <T>   Result type
     * @param tasks List of tasks
     * @return List of Futures
     */
    <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks);

    /**
     * Returns the number of available processing nodes or cores.
     */
    int getParallelism();

    /**
     * Shuts down the context.
     */
    void shutdown();

    /**
     * Initiates a one-sided data transfer to a remote node (RDMA Put).
     *
     * @param source     Data to send
     * @param targetRank Target node ID
     * @param offset     Remote offset
     */
    default void put(DoubleBuffer source, int targetRank, long offset) {
        throw new UnsupportedOperationException("RDMA Put not supported by this context");
    }

    /**
     * Initiates a one-sided data transfer from a remote node (RDMA Get).
     *
     * @param target     Buffer to receive data
     * @param sourceRank Source node ID
     * @param offset     Remote offset
     */
    default void get(DoubleBuffer target, int sourceRank, long offset) {
        throw new UnsupportedOperationException("RDMA Get not supported by this context");
    }

    /**
     * Synchronizes RDMA operations.
     */
    default void fence() {
        // No-op by default
    }

    /**
     * Broadcasts data from a root node to all other nodes.
     *
     * @param buffer The buffer containing data to broadcast. On root, this contains the source data.
     *               On other nodes, this buffer will be filled with the received data.
     * @param root   The rank of the root node broadcasting the data.
     */
    default void broadcast(DoubleBuffer buffer, int root) {
        throw new UnsupportedOperationException("Broadcast not supported by this context");
    }

    /**
     * Gathers data from all tasks and distributes the combined data to all tasks.
     *
     * @param sendBuffer The buffer containing the data to be sent by this task.
     * @param recvBuffer The buffer where the gathered data from all tasks will be stored.
     */
    default void allGather(DoubleBuffer sendBuffer, DoubleBuffer recvBuffer) {
        throw new UnsupportedOperationException("AllGather not supported by this context");
    }

    /**
     * Synchronizes all processes.
     * A process waits at this call until all processes have reached this barrier.
     */
    default void barrier() {
        // No-op by default for single-threaded/local contexts
    }
}
