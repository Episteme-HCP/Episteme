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

/**
 * JScience Native module for high-performance computing via Project Panama.
 * <p>
 * This module provides zero-copy integration with native libraries:
 * <ul>
 *   <li>BLAS/LAPACK for linear algebra</li>
 *   <li>HDF5 for scientific data I/O</li>
 *   <li>FFTW for signal processing</li>
 * </ul>
 * <p>
 * Requires Java 22+ with {@code --enable-preview} for stable FFM API.
 *
 * @since 1.1
 */
module org.jscience.native {
    requires org.jscience.core;
    
    exports org.jscience.native.memory;
    exports org.jscience.native.matrix;
    exports org.jscience.native.blas;
    exports org.jscience.native.hdf5;
}
