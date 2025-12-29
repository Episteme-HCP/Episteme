/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.benchmark;

/**
 * Standard interface for all JScience benchmarks.
 * <p>
 * This interface allows benchmarks to be auto-discovered via ServiceLoader
 * and executed uniformly.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface RunnableBenchmark {

    /**
     * @return Unique human-readable name of the benchmark
     */
    String getName();

    /**
     * @return The domain of this benchmark (e.g. "Linear Algebra", "Physics")
     */
    String getDomain();

    /**
     * Setup method called once before the benchmark runs.
     * Use this to initialize data structures.
     */
    void setup();

    /**
     * The core action to measure.
     * This method will be called repeatedly in a loop.
     */
    void run();

    /**
     * Cleanup method called once after the benchmark completes.
     */
    void teardown();

    /**
     * @return Estimated number of iterations required for a stable result
     */
    default int getSuggestedIterations() {
        return 10;
    }
}
