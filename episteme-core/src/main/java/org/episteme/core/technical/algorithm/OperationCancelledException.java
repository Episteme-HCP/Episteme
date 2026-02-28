/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.technical.algorithm;

/**
 * Thrown when a mathematical operation is cancelled by the user or system via {@link OperationContext} 
 * or {@link org.episteme.core.ComputeContext}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class OperationCancelledException extends RuntimeException {
    
    public OperationCancelledException() {
        super("Operation cancelled");
    }

    public OperationCancelledException(String message) {
        super(message);
    }
}
