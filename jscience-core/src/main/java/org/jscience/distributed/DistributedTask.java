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

package org.jscience.distributed;

import java.io.Serializable;

/**
 * Core interface for distributed scientific computation tasks.
 * 
 * <p>
 * Tasks implementing this interface can be dynamically dispatched by the
 * JScience distributed computing infrastructure. The server can send task
 * implementations to workers on-demand, eliminating the need for hardcoded
 * task handlers.
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * public class MandelbrotTask implements DistributedTask&lt;MandelbrotInput, double[]&gt; {
 *     &#64;Override
 *     public String getTaskType() {
 *         return "MANDELBROT";
 *     }
 * 
 *     &#64;Override
 *     public double[] execute(MandelbrotInput input) {
 *         // Compute Mandelbrot slice
 *         return result;
 *     }
 * }
 * </pre>
 * </p>
 *
 * @param <I> Input type (must be Serializable for network transfer)
 * @param <O> Output type (must be Serializable for network transfer)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface DistributedTask<I extends Serializable, O extends Serializable> extends Serializable {

    /**
     * Returns the unique task type identifier.
     * <p>
     * This identifier is used by the task registry to locate and instantiate
     * the appropriate task implementation. Examples: "MANDELBROT",
     * "PROTEIN_FOLDING",
     * "N_BODY", "CLIMATE_MODEL".
     * </p>
     *
     * @return the task type identifier (non-null, uppercase with underscores)
     */
    String getTaskType();

    /**
     * Executes the computation for the given input chunk.
     * <p>
     * This method contains the core scientific algorithm. It receives a chunk
     * of input data and returns the computed result. The infrastructure handles
     * serialization, network transfer, and result aggregation.
     * </p>
     *
     * @param input the input data for this computation chunk
     * @return the computed result
     * @throws ComputationException if the computation fails
     */
    O execute(I input) throws ComputationException;

    /**
     * Returns the expected input type class.
     * <p>
     * Used for deserialization and type checking during task dispatch.
     * </p>
     *
     * @return the Class object representing the input type
     */
    Class<I> getInputType();

    /**
     * Returns the expected output type class.
     * <p>
     * Used for serialization and type checking during result collection.
     * </p>
     *
     * @return the Class object representing the output type
     */
    Class<O> getOutputType();

    /**
     * Returns a human-readable description of this task.
     * <p>
     * Displayed in monitoring UIs (GridMonitor, DataLake browser).
     * </p>
     *
     * @return a brief description of what this task computes
     */
    default String getDescription() {
        return getTaskType() + " computation task";
    }

    /**
     * Returns the estimated computational complexity.
     * <p>
     * Used by the scheduler to balance load across workers.
     * Higher values indicate more intensive computation.
     * </p>
     *
     * @return complexity score (1-100, default 50)
     */
    default int getComplexity() {
        return 50;
    }

    /**
     * Indicates whether this task can utilize GPU acceleration.
     * <p>
     * If true, the worker may route this task to a GPU-enabled backend
     * if available.
     * </p>
     *
     * @return true if GPU execution is supported
     */
    default boolean supportsGPU() {
        return false;
    }
}
