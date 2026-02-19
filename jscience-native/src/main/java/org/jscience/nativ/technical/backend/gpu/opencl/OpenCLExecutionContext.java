/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.backend.gpu.opencl;

import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;

/**
 * OpenCL Execution Context.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenCLExecutionContext implements ExecutionContext {

    private final cl_context context;
    private final cl_command_queue commandQueue;

    public OpenCLExecutionContext(cl_context context, cl_command_queue commandQueue) {
        this.context = context;
        this.commandQueue = commandQueue;
    }

    public cl_context getContext() {
        return context;
    }

    public cl_command_queue getCommandQueue() {
        return commandQueue;
    }

    @Override
    public <T> T execute(Operation<T> operation) {
        // Direct execution - allows the operation to define its own logic
        // using the provided context (even if limited)
        return operation.compute(this);
    }

    @Override
    public void close() {
        // Cleanup if needed (context/queue are shared usually, but per-context
        // resources should be freed)
    }
}
