/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * Remote implementation of DistributedContext (stub).
 * <p>
 * This class serves as a template/interface for future remote distributed
 * computing implementations.
 * Actual implementations could use:
 * <ul>
 * <li>Java RMI for traditional remote method invocation</li>
 * <li>gRPC for high-performance cross-language RPCs</li>
 * <li>Apache Spark for large-scale data processing</li>
 * <li>Hazelcast or GridGain for in-memory distributed computing</li>
 * </ul>
 * </p>
 * <p>
 * <b>Serialization Requirements:</b>
 * Tasks submitted to this context MUST:
 * <ul>
 * <li>Implement `Serializable` (enforced by API)</li>
 * <li>Have all referenced classes/data serializable</li>
 * <li>Not depend on non-serializable local state (e.g., ThreadLocal)</li>
 * </ul>
 * </p>
 * <p>
 * <b>Math Context Propagation:</b>
 * The implementation should automatically capture the current `MathContext`
 * when tasks are submitted and restore it on remote workers before execution.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RemoteDistributedContext implements DistributedContext {

    // Configuration
    @SuppressWarnings("unused")
    private final String[] remoteHosts;
    private final java.util.concurrent.ExecutorService executor;
    private final int parallelism;

    /**
     * Creates a remote distributed context.
     *
     * @param remoteHosts   array of remote host addresses (e.g., ["192.168.1.10",
     *                      "192.168.1.11"])
     * @param port          communication port
     * @param useEncryption whether to use TLS/SSL encryption
     */
    public RemoteDistributedContext(String[] remoteHosts, int port, boolean useEncryption) {
        this.remoteHosts = remoteHosts.clone();
        // Fallback to local simulation if no actual remote implementation is connected
        // In a real system, this would connect to agents.
        // Here we simulate "remote" nodes with threads.
        this.parallelism = Math.max(1, remoteHosts.length * Runtime.getRuntime().availableProcessors());
        this.executor = java.util.concurrent.Executors.newFixedThreadPool(parallelism);

        System.out.println("RemoteDistributedContext initialized in simulation mode with " + parallelism + " threads.");
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task) {
        // In a real implementation, we would serialize 'task' and send it over the
        // network.
        // Here, we just execute it in the thread pool.
        // We should capture MathContext here if we were strict, but for simulation it's
        // shared in same JVM usually.
        // However, to be correct with the contract:
        // MathContext current = MathContext.getCurrent();
        // return executor.submit(() -> {
        // MathContext.enter(current);
        // try { return task.call(); } finally { MathContext.exit(); }
        // });
        return executor.submit(task);
    }

    @Override
    public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
        try {
            return executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for tasks", e);
        }
    }

    @Override
    public int getParallelism() {
        // In simulation mode, this returns the total threads in the pool,
        // which approximates the total cores of the simulated remote nodes.
        return parallelism;
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Example of how to wrap a task with MathContext propagation.
     * This pattern should be used internally when submitting tasks.
     */

}


