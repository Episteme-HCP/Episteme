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
package org.jscience.mathematics.backend;

import org.jscience.mathematics.analysis.Function;

/**
 * Interface for computational backends (CPU, GPU, FPGA).
 * <p>
 * Allows functions to offload heavy computations to specialized hardware.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ComputeBackend {

    /**
     * Returns the name of this backend (e.g., "CPU", "OpenCL GPU").
     * 
     * @return backend name
     */
    String getName();

    /**
     * Checks if this backend is available on the current system.
     * 
     * @return true if available
     */
    boolean isAvailable();

    /**
     * Batch evaluates a function on an array of doubles.
     * <p>
     * This is the primary method for acceleration.
     * </p>
     * 
     * @param f      the function to evaluate
     * @param inputs array of input values
     * @return array of results
     */
    double[] evaluate(Function<Double, Double> f, double[] inputs);

    /**
     * Batch evaluates a function on an array of inputs (generic).
     * 
     * @param <D>    input type
     * @param <C>    output type
     * @param f      the function
     * @param inputs array of inputs
     * @return array of results
     */
    // Note: Generic arrays are tricky in Java, keeping it simple for now with
    // double[]
    // Future versions can add generic support via Lists or specific buffers
}
