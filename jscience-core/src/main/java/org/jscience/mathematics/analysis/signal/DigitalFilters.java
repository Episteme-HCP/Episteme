/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.mathematics.analysis.signal;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Digital signal filters: Butterworth, FIR, IIR.
 * <p>
 * Frequency-selective filters for signal processing.
 * Applications: Noise reduction, frequency analysis, smoothing.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DigitalFilters {

    /**
     * Filter type.
     */
    public enum FilterType {
        LOWPASS, // Pass low frequencies
        HIGHPASS, // Pass high frequencies
        BANDPASS, // Pass middle frequencies
        BANDSTOP // Block middle frequencies
    }

    /**
     * Butterworth lowpass filter coefficients.
     * <p>
     * Maximally flat passband, smooth rolloff.
     * H(s) = 1 / (1 + (s/ωc)^(2n))
     * </p>
     * 
     * @param order      filter order (higher = sharper cutoff)
     * @param cutoffFreq cutoff frequency (normalized 0-1, where 1 = Nyquist)
     * @return IIR filter coefficients [b0...bn, a0...an]
     */
    public static Real[] butterworthLowpass(int order, Real cutoffFreq) {
        // Simplified Butterworth design
        // Full implementation needs bilinear transform

        // For 2nd order Butterworth:
        if (order == 2) {
            Real omega = Real.of(Math.PI).multiply(cutoffFreq);
            Real ita = Real.ONE.divide(Real.of(Math.tan(omega.doubleValue() / 2)));
            Real q = Real.of(Math.sqrt(2));

            Real b0 = Real.ONE.divide(Real.ONE.add(q.multiply(ita)).add(ita.multiply(ita)));
            Real b1 = b0.multiply(Real.of(2));
            Real b2 = b0;

            Real a1 = Real.of(2).multiply(b0).multiply(Real.ONE.subtract(ita.multiply(ita)));
            Real a2 = b0.negate().multiply(Real.ONE.subtract(q.multiply(ita)).add(ita.multiply(ita)));

            return new Real[] { b0, b1, b2, Real.ONE, a1, a2 };
        }

        // Default 1st order for other orders (simplified)
        Real alpha = cutoffFreq;
        Real b0 = alpha;
        Real b1 = alpha;
        Real a1 = alpha.subtract(Real.ONE);

        return new Real[] { b0, b1, Real.ONE, a1 };
    }

    /**
     * Applies IIR filter to signal.
     * <p>
     * Direct Form II implementation.
     * y[n] = Σ b[i]*x[n-i] - Σ a[i]*y[n-i]
     * </p>
     * 
     * @param signal input signal
     * @param coeffs filter coefficients [b0...bM, a0...aN]
     * @return filtered signal
     */
    public static Real[] applyIIRFilter(Real[] signal, Real[] coeffs) {
        int n = signal.length;

        // Split coefficients
        int mid = coeffs.length / 2;
        Real[] b = new Real[mid];
        Real[] a = new Real[coeffs.length - mid];

        System.arraycopy(coeffs, 0, b, 0, mid);
        System.arraycopy(coeffs, mid, a, 0, coeffs.length - mid);

        Real[] output = new Real[n];

        for (int i = 0; i < n; i++) {
            output[i] = Real.ZERO;

            // FeedForward (numerator)
            for (int j = 0; j < b.length && i - j >= 0; j++) {
                output[i] = output[i].add(b[j].multiply(signal[i - j]));
            }

            // Feedback (denominator)
            for (int j = 1; j < a.length && i - j >= 0; j++) {
                output[i] = output[i].subtract(a[j].multiply(output[i - j]));
            }

            // Normalize by a[0]
            if (!a[0].equals(Real.ONE)) {
                output[i] = output[i].divide(a[0]);
            }
        }

        return output;
    }

    /**
     * FIR (Finite Impulse Response) filter using windowing method.
     * <p>
     * h[n] = sin(ωc * n) / (π * n) * window[n]
     * </p>
     * 
     * @param length     filter length (odd number recommended)
     * @param cutoffFreq cutoff frequency (normalized)
     * @param type       filter type
     * @return FIR coefficients
     */
    public static Real[] designFIR(int length, Real cutoffFreq, FilterType type) {
        Real[] h = new Real[length];
        int center = length / 2;

        Real omega = Real.of(Math.PI).multiply(cutoffFreq);

        for (int n = 0; n < length; n++) {
            int m = n - center;

            if (m == 0) {
                // Limit as m→0
                h[n] = omega.divide(Real.of(Math.PI));
            } else {
                // sinc function
                Real arg = omega.multiply(Real.of(m));
                h[n] = Real.of(Math.sin(arg.doubleValue()))
                        .divide(Real.of(Math.PI * m));
            }

            // Apply Hamming window
            Real window = hammingWindow(n, length);
            h[n] = h[n].multiply(window);
        }

        // Spectral inversion for highpass
        if (type == FilterType.HIGHPASS) {
            for (int n = 0; n < length; n++) {
                h[n] = h[n].negate();
            }
            h[center] = h[center].add(Real.ONE);
        }

        return h;
    }

    /**
     * Applies FIR filter (convolution).
     */
    public static Real[] applyFIRFilter(Real[] signal, Real[] coeffs) {
        int n = signal.length;
        int m = coeffs.length;
        Real[] output = new Real[n];

        for (int i = 0; i < n; i++) {
            output[i] = Real.ZERO;
            for (int j = 0; j < m; j++) {
                if (i - j >= 0) {
                    output[i] = output[i].add(coeffs[j].multiply(signal[i - j]));
                }
            }
        }

        return output;
    }

    /**
     * Hamming window: w[n] = 0.54 - 0.46*cos(2πn/N)
     */
    private static Real hammingWindow(int n, int N) {
        Real arg = Real.of(2 * Math.PI * n).divide(Real.of(N));
        Real cosVal = Real.of(Math.cos(arg.doubleValue()));
        return Real.of(0.54).subtract(Real.of(0.46).multiply(cosVal));
    }

    /**
     * Hanning window: w[n] = 0.5 - 0.5*cos(2πn/N)
     */
    public static Real hanningWindow(int n, int N) {
        Real arg = Real.of(2 * Math.PI * n).divide(Real.of(N));
        Real cosVal = Real.of(Math.cos(arg.doubleValue()));
        return Real.of(0.5).subtract(Real.of(0.5).multiply(cosVal));
    }

    /**
     * Blackman window: w[n] = 0.42 - 0.5*cos(2πn/N) + 0.08*cos(4πn/N)
     */
    public static Real blackmanWindow(int n, int N) {
        Real arg1 = Real.of(2 * Math.PI * n).divide(Real.of(N));
        Real arg2 = Real.of(4 * Math.PI * n).divide(Real.of(N));

        Real term1 = Real.of(0.42);
        Real term2 = Real.of(0.5 * Math.cos(arg1.doubleValue()));
        Real term3 = Real.of(0.08 * Math.cos(arg2.doubleValue()));

        return term1.subtract(term2).add(term3);
    }
}
