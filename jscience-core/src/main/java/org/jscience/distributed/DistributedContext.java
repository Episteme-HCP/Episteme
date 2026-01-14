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

package org.jscience.distributed;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

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
}

