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

package org.jscience.core.technical.backend.algorithms;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for Fast Fourier Transform providers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FFTProvider {

    /**
     * Computes the forward FFT of complex data.
     * 
     * @param real Real components
     * @param imag Imaginary components
     * @return Transformed data [real, imag]
     */
    Real[][] transform(Real[] real, Real[] imag);

    /**
     * Computes the inverse FFT of complex data.
     * 
     * @param real Real components of the frequency domain
     * @param imag Imaginary components of the frequency domain
     * @return Transformed data [real, imag] in time domain
     */
    Real[][] inverseTransform(Real[] real, Real[] imag);

    /**
     * Computes forward FFT using double primitives.
     */
    double[][] transform(double[] real, double[] imag);

    /**
     * Computes inverse FFT using double primitives.
     */
    double[][] inverseTransform(double[] real, double[] imag);

    /**
     * Computes 2D forward FFT.
     */
    default double[][][] transform2D(double[][] real, double[][] imag) {
        // Default implementation: Successive 1D rows/cols
        int rows = real.length;
        int cols = real[0].length;
        double[][] resReal = new double[rows][cols];
        double[][] resImag = new double[rows][cols];
        
        // Transform rows
        for (int i = 0; i < rows; i++) {
            double[][] res = transform(real[i], imag[i]);
            resReal[i] = res[0];
            resImag[i] = res[1];
        }
        
        // Transform columns
        for (int j = 0; j < cols; j++) {
            double[] colReal = new double[rows];
            double[] colImag = new double[rows];
            for (int i = 0; i < rows; i++) {
                colReal[i] = resReal[i][j];
                colImag[i] = resImag[i][j];
            }
            double[][] res = transform(colReal, colImag);
            for (int i = 0; i < rows; i++) {
                resReal[i][j] = res[0][i];
                resImag[i][j] = res[1][i];
            }
        }
        return new double[][][] { resReal, resImag };
    }

    /**
     * Checks if this provider is available on the current system.
     */
    default boolean isAvailable() { return true; }

    /**
     * Returns the priority of this provider (higher = preferred).
     */
    default int getPriority() { return 0; }

    /**
     * Returns the name of this provider.
     * 
     * @return provider name
     */
    String getName();
}

