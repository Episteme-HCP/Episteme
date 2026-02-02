/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend;

/**
 * Types of hardware accelerators available for computation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public enum HardwareAccelerator {
    /** Standard CPU scalar instructions */
    CPU,
    /** SIMD vector instructions (AVX, NEON, Vector API) */
    SIMD,
    /** Graphics Processing Unit (CUDA, OpenCL) */
    GPU,
    /** Field Programmable Gate Array */
    FPGA,
    /** Quantum Processing Unit */
    QUANTUM
}
