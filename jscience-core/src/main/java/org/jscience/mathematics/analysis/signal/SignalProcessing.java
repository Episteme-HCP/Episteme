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

package org.jscience.mathematics.analysis.signal;

import org.jscience.mathematics.analysis.transform.SignalFFT;

/**
 * Digital signal processing utilities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SignalProcessing {

    /**
     * Low-pass filter using FFT (brick-wall).
     * 
     * @param signal     Input signal
     * @param cutoff     Cutoff frequency (normalized 0-1)
     * @param sampleRate Sampling rate
     */
    public static org.jscience.mathematics.numbers.real.Real[] lowPassFilter(
            org.jscience.mathematics.numbers.real.Real[] signal, org.jscience.mathematics.numbers.real.Real cutoff,
            org.jscience.mathematics.numbers.real.Real sampleRate) {
        int n = nextPowerOf2(signal.length);
        org.jscience.mathematics.numbers.real.Real[] padded = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;

        // Fill padded with zero default first effectively (arrays null init, wait Real
        // array is nulls)
        for (int i = 0; i < n; i++)
            padded[i] = zero;
        System.arraycopy(signal, 0, padded, 0, signal.length);

        org.jscience.mathematics.numbers.real.Real[][] spectrum = SignalFFT.fftReal(padded);
        org.jscience.mathematics.numbers.real.Real[] real = spectrum[0];
        org.jscience.mathematics.numbers.real.Real[] imag = spectrum[1];

        // double nyquist = sampleRate / 2;
        org.jscience.mathematics.numbers.real.Real nyquist = sampleRate
                .divide(org.jscience.mathematics.numbers.real.Real.of(2));

        // int cutoffBin = (int) (cutoff / nyquist * (n / 2));
        double cVal = cutoff.divide(nyquist).multiply(org.jscience.mathematics.numbers.real.Real.of(n / 2))
                .doubleValue();
        int cutoffBin = (int) cVal;

        // Zero out frequencies above cutoff
        for (int i = cutoffBin + 1; i < n - cutoffBin; i++) {
            real[i] = zero;
            imag[i] = zero;
        }

        SignalFFT.ifft(real, imag);

        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[signal.length];
        System.arraycopy(real, 0, result, 0, signal.length);
        return result;
    }

    /**
     * High-pass filter using FFT.
     */
    public static org.jscience.mathematics.numbers.real.Real[] highPassFilter(
            org.jscience.mathematics.numbers.real.Real[] signal, org.jscience.mathematics.numbers.real.Real cutoff,
            org.jscience.mathematics.numbers.real.Real sampleRate) {
        int n = nextPowerOf2(signal.length);
        org.jscience.mathematics.numbers.real.Real[] padded = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;
        for (int i = 0; i < n; i++)
            padded[i] = zero;
        System.arraycopy(signal, 0, padded, 0, signal.length);

        org.jscience.mathematics.numbers.real.Real[][] spectrum = SignalFFT.fftReal(padded);
        org.jscience.mathematics.numbers.real.Real[] real = spectrum[0];
        org.jscience.mathematics.numbers.real.Real[] imag = spectrum[1];

        // double nyquist = sampleRate / 2;
        org.jscience.mathematics.numbers.real.Real nyquist = sampleRate
                .divide(org.jscience.mathematics.numbers.real.Real.of(2));
        double cVal = cutoff.divide(nyquist).multiply(org.jscience.mathematics.numbers.real.Real.of(n / 2))
                .doubleValue();
        int cutoffBin = (int) cVal;

        // Zero out frequencies below cutoff
        for (int i = 0; i <= cutoffBin; i++) {
            real[i] = zero;
            imag[i] = zero;
            if (i > 0 && n - i < n) {
                real[n - i] = zero;
                imag[n - i] = zero;
            }
        }

        SignalFFT.ifft(real, imag);

        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[signal.length];
        System.arraycopy(real, 0, result, 0, signal.length);
        return result;
    }

    /**
     * Moving average filter.
     */
    public static org.jscience.mathematics.numbers.real.Real[] movingAverage(
            org.jscience.mathematics.numbers.real.Real[] signal, int windowSize) {
        int n = signal.length;
        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real sum = org.jscience.mathematics.numbers.real.Real.ZERO;

        for (int i = 0; i < n; i++) {
            sum = sum.add(signal[i]);
            if (i >= windowSize) {
                sum = sum.subtract(signal[i - windowSize]);
            }
            int count = Math.min(i + 1, windowSize);
            result[i] = sum.divide(org.jscience.mathematics.numbers.real.Real.of(count));
        }
        return result;
    }

    /**
     * Compute RMS (Root Mean Square) value.
     */
    public static org.jscience.mathematics.numbers.real.Real rms(org.jscience.mathematics.numbers.real.Real[] signal) {
        org.jscience.mathematics.numbers.real.Real sum = org.jscience.mathematics.numbers.real.Real.ZERO;
        for (org.jscience.mathematics.numbers.real.Real v : signal) {
            sum = sum.add(v.multiply(v));
        }
        return sum.divide(org.jscience.mathematics.numbers.real.Real.of(signal.length)).sqrt();
    }

    /**
     * Peak detection.
     * 
     * @param signal    Input signal
     * @param threshold Minimum peak amplitude
     * @return Indices of peaks
     */
    public static int[] findPeaks(org.jscience.mathematics.numbers.real.Real[] signal,
            org.jscience.mathematics.numbers.real.Real threshold) {
        java.util.List<Integer> peaks = new java.util.ArrayList<>();
        for (int i = 1; i < signal.length - 1; i++) {
            // signal[i] > threshold && signal[i] > signal[i-1] && signal[i] > signal[i+1]
            // Real doesn't implement compareTo directly in generic sense usually, check
            // Comparable
            // But checking .doubleValue() is easiest for inequality if conversion is safe
            // Assuming Real implements Comparable<Real> or use compareTo

            // Actually Real implements FieldElement?
            // Let's use compareTo if available, otherwise subtract and check sign?
            // Real likely extends Number or Comparable.
            // Checking outline: Real extends Number, implements FieldElement<Real>,
            // Comparable<Real> usually.
            // If not, use doubleValue() for inequalities to be safe/quick.

            if (signal[i].doubleValue() > threshold.doubleValue()
                    && signal[i].doubleValue() > signal[i - 1].doubleValue()
                    && signal[i].doubleValue() > signal[i + 1].doubleValue()) {
                peaks.add(i);
            }
        }
        return peaks.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Zero-crossing rate.
     */
    public static org.jscience.mathematics.numbers.real.Real zeroCrossingRate(
            org.jscience.mathematics.numbers.real.Real[] signal) {
        int crossings = 0;
        for (int i = 1; i < signal.length; i++) {
            // if (signal[i] * signal[i - 1] < 0)
            if (signal[i].multiply(signal[i - 1]).doubleValue() < 0) {
                crossings++;
            }
        }
        return org.jscience.mathematics.numbers.real.Real.of(crossings)
                .divide(org.jscience.mathematics.numbers.real.Real.of(signal.length - 1));
    }

    /**
     * Downsample signal by factor.
     */
    public static org.jscience.mathematics.numbers.real.Real[] downsample(
            org.jscience.mathematics.numbers.real.Real[] signal, int factor) {
        int n = (signal.length + factor - 1) / factor;
        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[n];
        for (int i = 0; i < n; i++) {
            result[i] = signal[i * factor];
        }
        return result;
    }

    /**
     * Upsample signal by factor (zero-insertion).
     */
    public static org.jscience.mathematics.numbers.real.Real[] upsample(
            org.jscience.mathematics.numbers.real.Real[] signal, int factor) {
        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[signal.length
                * factor];
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;
        for (int i = 0; i < result.length; i++)
            result[i] = zero;

        for (int i = 0; i < signal.length; i++) {
            result[i * factor] = signal[i];
        }
        return result;
    }

    /**
     * Generate sine wave.
     */
    public static org.jscience.mathematics.numbers.real.Real[] sineWave(
            org.jscience.mathematics.numbers.real.Real frequency, org.jscience.mathematics.numbers.real.Real sampleRate,
            int samples) {
        org.jscience.mathematics.numbers.real.Real[] wave = new org.jscience.mathematics.numbers.real.Real[samples];
        org.jscience.mathematics.numbers.real.Real twoPi = org.jscience.mathematics.numbers.real.Real.PI
                .multiply(org.jscience.mathematics.numbers.real.Real.of(2));

        for (int i = 0; i < samples; i++) {
            // wave[i] = Math.sin(2 * Math.PI * frequency * i / sampleRate);
            org.jscience.mathematics.numbers.real.Real t = org.jscience.mathematics.numbers.real.Real.of(i)
                    .divide(sampleRate);
            wave[i] = twoPi.multiply(frequency).multiply(t).sin();
        }
        return wave;
    }

    /**
     * Generate white noise.
     */
    public static org.jscience.mathematics.numbers.real.Real[] whiteNoise(int samples) {
        org.jscience.mathematics.numbers.real.Real[] noise = new org.jscience.mathematics.numbers.real.Real[samples];
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < samples; i++) {
            noise[i] = org.jscience.mathematics.numbers.real.Real.of(rand.nextGaussian());
        }
        return noise;
    }

    /**
     * Add two signals.
     */
    public static org.jscience.mathematics.numbers.real.Real[] add(org.jscience.mathematics.numbers.real.Real[] a,
            org.jscience.mathematics.numbers.real.Real[] b) {
        int n = Math.max(a.length, b.length);
        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[n];
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;

        for (int i = 0; i < n; i++) {
            org.jscience.mathematics.numbers.real.Real va = i < a.length ? a[i] : zero;
            org.jscience.mathematics.numbers.real.Real vb = i < b.length ? b[i] : zero;
            result[i] = va.add(vb);
        }
        return result;
    }

    /**
     * Fast Fourier Transform of real-valued signal.
     */
    public static org.jscience.mathematics.numbers.real.Real[][] fft(
            org.jscience.mathematics.numbers.real.Real[] signal) {
        return SignalFFT.fftReal(signal);
    }

    /**
     * Inverse FFT.
     */
    public static org.jscience.mathematics.numbers.real.Real[] ifft(org.jscience.mathematics.numbers.real.Real[] real,
            org.jscience.mathematics.numbers.real.Real[] imag) {
        org.jscience.mathematics.numbers.real.Real[] r = real.clone();
        org.jscience.mathematics.numbers.real.Real[] i = imag.clone();
        SignalFFT.ifft(r, i);
        return r;
    }

    /**
     * Power spectrum from FFT result.
     */
    public static org.jscience.mathematics.numbers.real.Real[] powerSpectrum(
            org.jscience.mathematics.numbers.real.Real[] real, org.jscience.mathematics.numbers.real.Real[] imag) {
        org.jscience.mathematics.numbers.real.Real[] power = new org.jscience.mathematics.numbers.real.Real[real.length];
        for (int k = 0; k < real.length; k++) {
            power[k] = real[k].multiply(real[k]).add(imag[k].multiply(imag[k]));
        }
        return power;
    }

    /**
     * Power spectral density using FFT.
     */
    public static org.jscience.mathematics.numbers.real.Real[] powerSpectralDensity(
            org.jscience.mathematics.numbers.real.Real[] signal) {
        return SignalFFT.powerSpectrum(signal);
    }

    /**
     * Exponential moving average.
     */
    public static org.jscience.mathematics.numbers.real.Real[] exponentialMovingAverage(
            org.jscience.mathematics.numbers.real.Real[] signal, org.jscience.mathematics.numbers.real.Real alpha) {
        org.jscience.mathematics.numbers.real.Real[] filtered = new org.jscience.mathematics.numbers.real.Real[signal.length];
        filtered[0] = signal[0];
        org.jscience.mathematics.numbers.real.Real oneMinusAlpha = org.jscience.mathematics.numbers.real.Real.ONE
                .subtract(alpha);

        for (int i = 1; i < signal.length; i++) {
            filtered[i] = alpha.multiply(signal[i]).add(oneMinusAlpha.multiply(filtered[i - 1]));
        }
        return filtered;
    }

    /**
     * Convolution of two signals.
     */
    public static org.jscience.mathematics.numbers.real.Real[] convolve(
            org.jscience.mathematics.numbers.real.Real[] signal, org.jscience.mathematics.numbers.real.Real[] kernel) {
        int outLen = signal.length + kernel.length - 1;
        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[outLen];
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;
        for (int k = 0; k < outLen; k++)
            result[k] = zero;

        for (int i = 0; i < signal.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                result[i + j] = result[i + j].add(signal[i].multiply(kernel[j]));
            }
        }
        return result;
    }

    /**
     * Cross-correlation of two signals.
     */
    public static org.jscience.mathematics.numbers.real.Real[] crossCorrelation(
            org.jscience.mathematics.numbers.real.Real[] x, org.jscience.mathematics.numbers.real.Real[] y) {
        int N = x.length;
        int M = y.length;
        org.jscience.mathematics.numbers.real.Real[] result = new org.jscience.mathematics.numbers.real.Real[N + M - 1];
        org.jscience.mathematics.numbers.real.Real zero = org.jscience.mathematics.numbers.real.Real.ZERO;
        for (int k = 0; k < result.length; k++)
            result[k] = zero;

        for (int lag = -(M - 1); lag < N; lag++) {
            int idx = lag + M - 1;
            for (int i = 0; i < N; i++) {
                int j = i - lag;
                if (j >= 0 && j < M) {
                    result[idx] = result[idx].add(x[i].multiply(y[j]));
                }
            }
        }
        return result;
    }

    /**
     * Magnitude spectrum from DFT output.
     */
    public static org.jscience.mathematics.numbers.real.Real[] magnitudeSpectrum(
            org.jscience.mathematics.numbers.real.Real[] real, org.jscience.mathematics.numbers.real.Real[] imag) {
        org.jscience.mathematics.numbers.real.Real[] magnitude = new org.jscience.mathematics.numbers.real.Real[real.length];
        for (int i = 0; i < real.length; i++) {
            magnitude[i] = real[i].multiply(real[i]).add(imag[i].multiply(imag[i])).sqrt();
        }
        return magnitude;
    }

    /**
     * Peak-to-peak amplitude.
     */
    public static org.jscience.mathematics.numbers.real.Real peakToPeak(
            org.jscience.mathematics.numbers.real.Real[] signal) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (org.jscience.mathematics.numbers.real.Real vReal : signal) {
            double v = vReal.doubleValue();
            if (v < min)
                min = v;
            if (v > max)
                max = v;
        }
        return org.jscience.mathematics.numbers.real.Real.of(max - min);
    }

    /**
     * Adds white noise to a signal.
     */
    public static org.jscience.mathematics.numbers.real.Real[] addNoise(
            org.jscience.mathematics.numbers.real.Real[] signal, org.jscience.mathematics.numbers.real.Real amplitude) {
        java.util.Random rand = new java.util.Random();
        org.jscience.mathematics.numbers.real.Real[] noisy = new org.jscience.mathematics.numbers.real.Real[signal.length];
        for (int i = 0; i < signal.length; i++) {
            double r = 2 * rand.nextDouble() - 1;
            noisy[i] = signal[i].add(amplitude.multiply(org.jscience.mathematics.numbers.real.Real.of(r)));
        }
        return noisy;
    }

    private static int nextPowerOf2(int n) {
        int p = 1;
        while (p < n)
            p *= 2;
        return p;
    }
}


