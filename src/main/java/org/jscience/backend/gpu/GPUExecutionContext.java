/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.backend.gpu;

import org.jscience.backend.ExecutionContext;
import org.jscience.backend.Operation;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;

/**
 * GPU Execution Context.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class GPUExecutionContext implements ExecutionContext {

    private final cl_context context;
    private final cl_command_queue commandQueue;

    public GPUExecutionContext(cl_context context, cl_command_queue commandQueue) {
        this.context = context;
        this.commandQueue = commandQueue;
    }

    @Override
    public <T> T execute(Operation<T> operation) {
        // TODO: Implement GPU execution logic (kernel compilation, data transfer, etc.)
        // For now, fallback to CPU or throw
        throw new UnsupportedOperationException("GPU execution not yet implemented for generic operations");
    }

    @Override
    public void close() {
        // Cleanup if needed (context/queue are shared usually, but per-context
        // resources should be freed)
    }
}
