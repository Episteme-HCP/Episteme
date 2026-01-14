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

package org.jscience.mathematics.linearalgebra.tensors.backends;

/**
 * ND4J CUDA (GPU) Tensor Provider.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ND4JCUDATensorProvider extends ND4JBaseTensorProvider {

    @Override
    protected boolean checkAvailability() {
        if (!checkCommonClasses()) return false;
        try {
            // Check specifically for CUDA backend availability
            Class.forName("org.nd4j.linalg.jcublas.JCublasBackend");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public String getId() {
        return "nd4j-cuda";
    }

    @Override
    public String getName() {
        return "ND4J CUDA (GPU)";
    }

    @Override
    public String getDescription() {
        return "ND4J Tensor Provider using CUDA GPU Backend";
    }

    @Override
    public int getPriority() {
        // Higher priority than Native if available
        return isAvailable() ? 100 : 0;
    }

    @Override
    public boolean supportsGPU() {
        return true;
    }
}
