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

package org.jscience.mathematics.analysis.transform;

import org.jscience.technical.backend.algorithms.FFTProvider;
import org.jscience.technical.backend.algorithms.MulticoreFFTProvider;

/**
 * Fast Fourier Transform (FFT) implementation.
 * <p>
 * Cooley-Tukey radix-2 DIT algorithm.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FFT {

    /**
     * Compute forward FFT of complex data.
     * 
     * @param real Real part (modified in place)
     * @param imag Imaginary part (modified in place)
     */
    private static FFTProvider provider = new MulticoreFFTProvider();

    public static void setProvider(FFTProvider p) {
        provider = p;
    }

    /**
     * Compute forward FFT of complex data.
     * 
     * @param real Real part (modified in place)
     * @param imag Imaginary part (modified in place)
     */
    public static void fft(org.jscience.mathematics.numbers.real.Real[] real,
            org.jscience.mathematics.numbers.real.Real[] imag) {
        if (provider != null) {
            org.jscience.mathematics.numbers.real.Real[][] res = provider.transform(real, imag);
            // API expects in-place modification, so copy back.
            System.arraycopy(res[0], 0, real, 0, real.length);
            System.arraycopy(res[1], 0, imag, 0, imag.length);
        } else {
            new MulticoreFFTProvider().transform(real, imag);
        }
    }

    /**
     * Compute inverse FFT.
     */
    public static void ifft(org.jscience.mathematics.numbers.real.Real[] real,
            org.jscience.mathematics.numbers.real.Real[] imag) {
        if (provider != null) {
            org.jscience.mathematics.numbers.real.Real[][] res = provider.inverseTransform(real, imag);
            System.arraycopy(res[0], 0, real, 0, real.length);
            System.arraycopy(res[1], 0, imag, 0, imag.length);
        }
    }

    /**
     * FFT of real-valued data.
     * 
     * @param data Real input (must be power of 2 length)
     * @return [real_part, imag_part]
     */
    public static org.jscience.mathematics.numbers.real.Real[][] fftReal(
            org.jscience.mathematics.numbers.real.Real[] data) {
        int n = data.length;
        org.jscience.mathematics.numbers.real.Real[] real = data.clone();
        org.jscience.mathematics.numbers.real.Real[] imag = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;
        for (int i = 0; i < n; i++)
            imag[i] = zero;

        fft(real, imag);
        return new org.jscience.mathematics.numbers.real.Real[][] { real, imag };
    }

    /**
     * Compute magnitude spectrum.
     */
    public static org.jscience.mathematics.numbers.real.Real[] magnitude(
            org.jscience.mathematics.numbers.real.Real[] real, org.jscience.mathematics.numbers.real.Real[] imag) {
        int n = real.length;
        org.jscience.mathematics.numbers.real.Real[] mag = new org.jscience.mathematics.numbers.real.Real[n];
        for (int i = 0; i < n; i++) {
            // mag[i] = Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
            mag[i] = real[i].multiply(real[i]).add(imag[i].multiply(imag[i])).sqrt();
        }
        return mag;
    }

    /**
     * Compute phase spectrum.
     */
    public static org.jscience.mathematics.numbers.real.Real[] phase(org.jscience.mathematics.numbers.real.Real[] real,
            org.jscience.mathematics.numbers.real.Real[] imag) {
        int n = real.length;
        org.jscience.mathematics.numbers.real.Real[] ph = new org.jscience.mathematics.numbers.real.Real[n];
        for (int i = 0; i < n; i++) {
            // ph[i] = Math.atan2(imag[i], real[i]);
            ph[i] = imag[i].atan2(real[i]);
        }
        return ph;
    }

    /**
     * Power spectral density.
     */
    public static org.jscience.mathematics.numbers.real.Real[] powerSpectrum(
            org.jscience.mathematics.numbers.real.Real[] data) {
        org.jscience.mathematics.numbers.real.Real[][] result = fftReal(data);
        int n = data.length;
        org.jscience.mathematics.numbers.real.Real[] power = new org.jscience.mathematics.numbers.real.Real[n / 2 + 1];
        org.jscience.mathematics.numbers.real.Real nReal = org.jscience.mathematics.numbers.real.Real.of(n);

        for (int i = 0; i <= n / 2; i++) {
            org.jscience.mathematics.numbers.real.Real r = result[0][i];
            org.jscience.mathematics.numbers.real.Real im = result[1][i];
            // power[i] = (result[0][i] * result[0][i] + result[1][i] * result[1][i]) / n;
            power[i] = r.multiply(r).add(im.multiply(im)).divide(nReal);
        }
        return power;
    }

    /**
     * Compute convolution using FFT.
     */
    public static org.jscience.mathematics.numbers.real.Real[] convolve(org.jscience.mathematics.numbers.real.Real[] a,
            org.jscience.mathematics.numbers.real.Real[] b) {
        int n = 1;
        while (n < a.length + b.length - 1)
            n *= 2;

        org.jscience.mathematics.numbers.real.Real[] aReal = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real[] aImag = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real[] bReal = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real[] bImag = new org.jscience.mathematics.numbers.real.Real[n];

        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;
        for (int i = 0; i < n; i++) {
            aReal[i] = (i < a.length) ? a[i] : zero;
            aImag[i] = zero;
            bReal[i] = (i < b.length) ? b[i] : zero;
            bImag[i] = zero;
        }

        fft(aReal, aImag);
        fft(bReal, bImag);

        // Complex multiplication
        org.jscience.mathematics.numbers.real.Real[] cReal = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real[] cImag = new org.jscience.mathematics.numbers.real.Real[n];
        for (int i = 0; i < n; i++) {
            // cReal[i] = aReal[i] * bReal[i] - aImag[i] * bImag[i];
            cReal[i] = aReal[i].multiply(bReal[i]).subtract(aImag[i].multiply(bImag[i]));
            // cImag[i] = aReal[i] * bImag[i] + aImag[i] * bReal[i];
            cImag[i] = aReal[i].multiply(bImag[i]).add(aImag[i].multiply(bReal[i]));
        }

        ifft(cReal, cImag);

        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[a.length
                + b.length - 1];
        System.arraycopy(cReal, 0, result, 0, result.length);
        return result;
    }

    /**
     * Compute correlation using FFT.
     */
    public static org.jscience.mathematics.numbers.real.Real[] correlate(org.jscience.mathematics.numbers.real.Real[] a,
            org.jscience.mathematics.numbers.real.Real[] b) {
        // Reverse b for correlation
        org.jscience.mathematics.numbers.real.Real[] bRev = new org.jscience.mathematics.numbers.real.Real[b.length];
        for (int i = 0; i < b.length; i++) {
            bRev[i] = b[b.length - 1 - i];
        }
        return convolve(a, bRev);
    }
}
