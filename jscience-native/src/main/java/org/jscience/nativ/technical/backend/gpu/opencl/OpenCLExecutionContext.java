/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;
import org.jocl.*;

/**
 * Execution context for OpenCL operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
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
        return operation.compute(this);
    }

    @Override
    public void close() {
        // Resource management should be handled by the backend
    }
}
