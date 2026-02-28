/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.backend;

/**
 * Modes of execution for computational tasks.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public enum ExecutionMode {
    /** Single-threaded execution */
    SEQUENTIAL,
    /** Multi-threaded execution on a single node */
    MULTICORE,
    /** Parallel execution across multiple nodes */
    DISTRIBUTED
}
