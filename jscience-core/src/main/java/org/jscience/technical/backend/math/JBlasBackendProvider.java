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

package org.jscience.technical.backend.math;

import org.jscience.technical.backend.BackendProvider;
import org.jscience.technical.backend.BackendDiscovery;

/**
 * BackendProvider for JBlas.
 */
public class JBlasBackendProvider implements BackendProvider {
    @Override
    public String getType() {
        return BackendDiscovery.TYPE_MATH;
    }

    @Override
    public String getId() {
        return "jblas";
    }

    @Override
    public String getName() {
        return "JBlas";
    }

    @Override
    public String getDescription() {
        return "Linear algebra for Java based on BLAS/LAPACK.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jblas.NativeBlas");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 90;
    }

    @Override
    public Object createBackend() {
        if (isAvailable()) {
            return new org.jscience.mathematics.linearalgebra.backends.JBlasLinearAlgebraProvider<>();
        }
        return null;
    }
}

