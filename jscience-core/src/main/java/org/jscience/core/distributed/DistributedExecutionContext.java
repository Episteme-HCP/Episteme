/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.distributed;

import org.jscience.core.technical.backend.distributed.DistributedContext;

import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Execution context adapter that bridges {@link DistributedContext} with 
 * the {@link ExecutionContext} interface.
 * <p>
 * This allows distributed contexts (MPI, Spark, local) to be used within the
 * unified backend execution model.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class DistributedExecutionContext implements ExecutionContext {

    private final DistributedContext delegate;

    /**
     * Creates a new distributed execution context wrapping the given delegate.
     *
     * @param delegate the underlying distributed context
     */
    public DistributedExecutionContext(DistributedContext delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Distributed context cannot be null");
        }
        this.delegate = delegate;
    }

    @Override
    public <T> T execute(Operation<T> operation) {
        // Execute the operation using the distributed context's submit mechanism
        try {
            // Wrap the operation as a Callable for the distributed context
            @SuppressWarnings("unchecked")
            Future<T> future = (Future<T>) delegate.submit((Callable<Serializable>) () -> {
                T result = operation.compute(this);
                if (result instanceof Serializable) {
                    return (Serializable) result;
                }
                throw new UnsupportedOperationException(
                        "Operation result must be Serializable for distributed execution");
            });
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Distributed execution failed", e);
        }
    }

    @Override
    public <T> CompletableFuture<T> executeAsync(Operation<T> operation) {
        return CompletableFuture.supplyAsync(() -> execute(operation));
    }

    /**
     * Returns the underlying distributed context.
     *
     * @return the delegate context
     */
    public DistributedContext getDistributedContext() {
        return delegate;
    }

    /**
     * Returns the parallelism level of the underlying context.
     *
     * @return available processing nodes or cores
     */
    public int getParallelism() {
        return delegate.getParallelism();
    }

    @Override
    public void close() {
        delegate.shutdown();
    }
}
