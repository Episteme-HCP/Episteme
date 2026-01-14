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

package org.jscience.technical.backend.cpu;

import org.jscience.technical.backend.ComputeBackend;
import org.jscience.technical.backend.ExecutionContext;

/**
 * CPU-based compute backend (default fallback).
 * <p>
 * This backend uses pure Java implementations and is always available.
 * It supports parallel operations via Java parallel streams.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CPUBackend implements ComputeBackend {

    @Override
    public String getId() {
        return "cpu";
    }

    @Override
    public String getName() {
        return "CPU";
    }

    @Override
    public String getDescription() {
        return "Standard CPU processing using multi-core Java implementations.";
    }

    @Override
    public boolean isAvailable() {
        return true; // CPU backend is always available
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public boolean supportsParallelOps() {
        return Runtime.getRuntime().availableProcessors() > 1;
    }

    @Override
    public boolean supportsFloatingPoint() {
        return true;
    }

    @Override
    public boolean supportsComplexNumbers() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0; // Lowest priority (fallback)
    }
}
