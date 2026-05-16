/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.technical.backend.cpu;

import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;

/**
 * Marker interface for CPU-based compute backends.
 * <p>
 * All CPU backends (dense linear algebra, sparse, SIMD-enhanced, library-based
 * such as Colt, EJML, JBlas, Commons Math) must implement this interface.
 * The concrete default implementation is {@link DefaultCPUBackend}.
 * </p>
 * <p>
 * Note: {@link org.episteme.core.technical.backend.simd.SIMDBackend} extends
 * {@code CPUBackend} for backends that additionally use SIMD vector intrinsics
 * (JDK Vector API or Panama FFM).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface CPUBackend extends ComputeBackend {

    @Override
    default String getType() {
        return "cpu";
    }

    @Override
    default HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    default boolean supportsParallelOps() {
        return Runtime.getRuntime().availableProcessors() > 1;
    }

    @Override
    default boolean supportsFloatingPoint() {
        return true;
    }

    @Override
    default boolean supportsComplexNumbers() {
        return true;
    }
}
