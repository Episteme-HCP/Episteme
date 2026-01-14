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
 * Provider interface for creating distributed task instances.
 * 
 * <p>
 * TaskProvider enables the Primitive/Real abstraction. Each task type can have
 * two implementations:
 * <ul>
 * <li><b>Primitive</b>: Uses raw double/float primitives. Fastest execution,
 * but limited precision and no automatic GPU acceleration.</li>
 * <li><b>Real</b>: Uses the {@link org.jscience.mathematics.numbers.real.Real}
 * API.
 * Configurable precision (Float/Double/BigDecimal via MathContext) and
 * automatic GPU offloading for supported operations.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Example implementation:
 * 
 * <pre>
 * public class MandelbrotTaskProvider implements TaskProvider&lt;int[], double[]&gt; {
 *     &#64;Override
 *     public DistributedTask&lt;int[], double[]&gt; createTask() {
 *         return createTask(PrecisionMode.REAL); // Default to Real
 *     }
 * 
 *     &#64;Override
 *     public DistributedTask&lt;int[], double[]&gt; createTask(PrecisionMode mode) {
 *         return mode == PrecisionMode.PRIMITIVE
 *                 ? new PrimitiveMandelbrotTask()
 *                 : new RealMandelbrotTask();
 *     }
 * }
 * </pre>
 * </p>
 *
 * @param <I> Input type
 * @param <O> Output type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TaskProvider<I extends Serializable, O extends Serializable> {

    /**
     * Creates a task instance with default precision mode (typically REAL).
     *
     * @return a new task instance
     */
    DistributedTask<I, O> createTask();

    /**
     * Creates a task instance with the specified precision mode.
     *
     * @param mode the precision mode (PRIMITIVE or REAL)
     * @return a new task instance
     */
    DistributedTask<I, O> createTask(TaskRegistry.PrecisionMode mode);

    /**
     * Returns the task type identifier.
     *
     * @return the task type (e.g., "MANDELBROT")
     */
    String getTaskType();

    /**
     * Indicates whether GPU acceleration is supported.
     * <p>
     * GPU support is typically available only for REAL mode tasks.
     * </p>
     *
     * @return true if GPU is supported
     */
    default boolean supportsGPU() {
        return true; // Real-based tasks support GPU by default
    }

    /**
     * Returns a description of this task provider.
     *
     * @return human-readable description
     */
    default String getDescription() {
        return getTaskType() + " computation provider";
    }
}
