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

package org.jscience.technical.backend;

import java.util.concurrent.CompletableFuture;

/**
 * Execution context for running operations on a backend.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ExecutionContext extends AutoCloseable {

    /**
     * Executes an operation synchronously and returns the result.
     * 
     * @param <T>       the result type
     * @param operation the operation to execute
     * @return the operation result
     * @throws RuntimeException if execution fails
     */
    <T> T execute(Operation<T> operation);

    /**
     * Executes an operation asynchronously.
     * 
     * @param <T>       the result type
     * @param operation the operation to execute
     * @return a future representing the pending result
     */
    default <T> CompletableFuture<T> executeAsync(Operation<T> operation) {
        return CompletableFuture.supplyAsync(() -> execute(operation));
    }

    /**
     * Releases resources associated with this context.
     * <p>
     * This method is called automatically when used in try-with-resources.
     * </p>
     */
    @Override
    void close();
}

