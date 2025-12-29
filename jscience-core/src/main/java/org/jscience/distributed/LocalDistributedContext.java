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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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

    private static final org.jscience.util.Logger LOG = org.jscience.util.Logger
            .getLogger(LocalDistributedContext.class);

    private final ForkJoinPool pool;

    public LocalDistributedContext() {
        this.pool = ForkJoinPool.commonPool();
        LOG.info(() -> String.format("LocalDistributedContext initialized with parallelism=%d", pool.getParallelism()));
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task) {
        // Capture current MathContext
        org.jscience.mathematics.context.MathContext capturedContext = org.jscience.mathematics.context.MathContext
                .getCurrent();

        // Wrap task to propagate context
        Callable<T> wrappedTask = () -> {
            org.jscience.mathematics.context.MathContext oldContext = org.jscience.mathematics.context.MathContext
                    .getCurrent();
            try {
                org.jscience.mathematics.context.MathContext.setCurrent(capturedContext);
                LOG.debug(() -> "Executing task with context: " + capturedContext);
                return task.call();
            } finally {
                org.jscience.mathematics.context.MathContext.setCurrent(oldContext);
            }
        };

        return pool.submit(wrappedTask);
    }

    @Override
    public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
        // Capture current MathContext
        org.jscience.mathematics.context.MathContext capturedContext = org.jscience.mathematics.context.MathContext
                .getCurrent();

        // Wrap all tasks to propagate context
        List<Callable<T>> wrappedTasks = tasks.stream()
                .map(task -> (Callable<T>) () -> {
                    org.jscience.mathematics.context.MathContext oldContext = org.jscience.mathematics.context.MathContext
                            .getCurrent();
                    try {
                        org.jscience.mathematics.context.MathContext.setCurrent(capturedContext);
                        return task.call();
                    } finally {
                        org.jscience.mathematics.context.MathContext.setCurrent(oldContext);
                    }
                })
                .collect(Collectors.toList());

        return pool.invokeAll(wrappedTasks).stream()
                .map(f -> (Future<T>) f)
                .collect(Collectors.toList());
    }

    @Override
    public int getParallelism() {
        return pool.getParallelism();
    }

    @Override
    public void shutdown() {
        // Common pool doesn't need explicit shutdown usually, but we can if needed
    }
}