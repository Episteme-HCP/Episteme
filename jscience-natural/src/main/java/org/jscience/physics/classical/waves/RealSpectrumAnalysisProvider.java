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

package org.jscience.physics.classical.waves;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.analysis.transform.SignalFFT;

/**
 * Real-based implementation of spectrum analysis using JScience Real types.
 */
public class RealSpectrumAnalysisProvider implements SpectrumAnalysisProvider {

    @Override
    public double[] computeSpectrum(double[] samples, int bands, double sensitivity) {
        int n = samples.length;
        Real[] realArr = new Real[n];
        for (int i = 0; i < n; i++) realArr[i] = Real.of(samples[i]);
        
        Real[][] result = SignalFFT.fftReal(realArr);
        Real[] transformedReal = result[0];
        Real[] transformedImag = result[1];
        
        double[] spectrum = new double[bands];
        Real nReal = Real.of(n);
        Real sensitivityReal = Real.of(sensitivity * 20.0);
        
        for (int i = 0; i < bands; i++) {
            Real r = transformedReal[i];
            Real im = transformedImag[i];
            // Mag = sqrt(r^2 + im^2) / n
            Real mag = r.multiply(r).add(im.multiply(im)).sqrt().divide(nReal);
            double val = mag.multiply(sensitivityReal).doubleValue();
            spectrum[i] = Math.max(0, Math.min(1.0, val));
        }
        return spectrum;
    }
}
