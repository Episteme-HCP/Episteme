/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.analysis.transform;

import org.jscience.core.mathematics.analysis.fft.FFTProvider;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.jscience.core.mathematics.analysis.fft.providers.MulticoreFFTProvider;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Signal Processing FFT (Fast Fourier Transform).
 * <p>
 * Optimized for signal processing, operating on primitive arrays or Real arrays.
 * Uses AlgorithmManager for hardware acceleration.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SignalFFT {

    private static FFTProvider getProvider() {
        FFTProvider provider = AlgorithmManager.getProvider(FFTProvider.class);
        return provider != null ? provider : new MulticoreFFTProvider();
    }

    /**
     * Compute forward FFT of complex data.
     * 
     * @param real Real part (modified in place)
     * @param imag Imaginary part (modified in place)
     */
    public static void fft(Real[] real, Real[] imag) {
        FFTProvider provider = getProvider();
        Real[][] res = provider.transform(real, imag);
        System.arraycopy(res[0], 0, real, 0, real.length);
        System.arraycopy(res[1], 0, imag, 0, imag.length);
    }

    /**
     * Compute inverse FFT.
     */
    public static void ifft(Real[] real, Real[] imag) {
        FFTProvider provider = getProvider();
        Real[][] res = provider.inverseTransform(real, imag);
        System.arraycopy(res[0], 0, real, 0, real.length);
        System.arraycopy(res[1], 0, imag, 0, imag.length);
    }

    /** Primitive versions */
    public static void fft(double[] real, double[] imag) {
        FFTProvider provider = getProvider();
        double[][] res = provider.transform(real, imag);
        System.arraycopy(res[0], 0, real, 0, real.length);
        System.arraycopy(res[1], 0, imag, 0, imag.length);
    }

    public static void ifft(double[] real, double[] imag) {
        FFTProvider provider = getProvider();
        double[][] res = provider.inverseTransform(real, imag);
        System.arraycopy(res[0], 0, real, 0, real.length);
        System.arraycopy(res[1], 0, imag, 0, imag.length);
    }

    /** 2D Transforms */
    public static void fft2D(double[][] real, double[][] imag) {
        FFTProvider provider = getProvider();
        double[][][] res = provider.transform2D(real, imag);
        for (int i = 0; i < real.length; i++) {
            System.arraycopy(res[0][i], 0, real[i], 0, real[i].length);
            System.arraycopy(res[1][i], 0, imag[i], 0, imag[i].length);
        }
    }

    /**
     * FFT of real-valued data.
     * 
     * @param data Real input (must be power of 2 length)
     * @return [real_part, imag_part]
     */
    public static Real[][] fftReal(Real[] data) {
        int n = data.length;
        Real[] real = data.clone();
        Real[] imag = new Real[n];
        Real zero = Real.ZERO;
        for (int i = 0; i < n; i++) imag[i] = zero;
        fft(real, imag);
        return new Real[][] { real, imag };
    }

    /**
     * Compute magnitude spectrum.
     */
    public static Real[] magnitude(Real[] real, Real[] imag) {
        int n = real.length;
        Real[] mag = new Real[n];
        for (int i = 0; i < n; i++) {
            mag[i] = real[i].multiply(real[i]).add(imag[i].multiply(imag[i])).sqrt();
        }
        return mag;
    }

    /**
     * Compute phase spectrum.
     */
    public static Real[] phase(Real[] real, Real[] imag) {
        int n = real.length;
        Real[] ph = new Real[n];
        for (int i = 0; i < n; i++) {
            ph[i] = imag[i].atan2(real[i]);
        }
        return ph;
    }

    /**
     * Power spectral density.
     */
    public static Real[] powerSpectrum(Real[] data) {
        Real[][] result = fftReal(data);
        int n = data.length;
        Real[] power = new Real[n / 2 + 1];
        Real nReal = Real.of(n);

        for (int i = 0; i <= n / 2; i++) {
            Real r = result[0][i];
            Real im = result[1][i];
            power[i] = r.multiply(r).add(im.multiply(im)).divide(nReal);
        }
        return power;
    }

    /**
     * Compute convolution using FFT.
     */
    public static Real[] convolve(Real[] a, Real[] b) {
        int n = 1;
        while (n < a.length + b.length - 1) n *= 2;

        Real[] aReal = new Real[n];
        Real[] aImag = new Real[n];
        Real[] bReal = new Real[n];
        Real[] bImag = new Real[n];

        Real zero = Real.ZERO;
        for (int i = 0; i < n; i++) {
            aReal[i] = (i < a.length) ? a[i] : zero;
            aImag[i] = zero;
            bReal[i] = (i < b.length) ? b[i] : zero;
            bImag[i] = zero;
        }

        fft(aReal, aImag);
        fft(bReal, bImag);

        Real[] cReal = new Real[n];
        Real[] cImag = new Real[n];
        for (int i = 0; i < n; i++) {
            cReal[i] = aReal[i].multiply(bReal[i]).subtract(aImag[i].multiply(bImag[i]));
            cImag[i] = aReal[i].multiply(bImag[i]).add(aImag[i].multiply(bReal[i]));
        }

        ifft(cReal, cImag);

        Real[] result = new Real[a.length + b.length - 1];
        System.arraycopy(cReal, 0, result, 0, result.length);
        return result;
    }

    /**
     * Compute correlation using FFT.
     */
    public static Real[] correlate(Real[] a, Real[] b) {
        Real[] bRev = new Real[b.length];
        for (int i = 0; i < b.length; i++) {
            bRev[i] = b[b.length - 1 - i];
        }
        return convolve(a, bRev);
    }
}
