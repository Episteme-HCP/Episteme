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

package org.episteme.core.media.audio;

import java.util.*;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmManager;

/**
 * Convenience wrapper for frequency analysis using SignalFFT.
 */
public final class AudioSpectrogram {

    private AudioSpectrogram() {}

    public enum WindowFunction {
        RECTANGULAR, HANNING, HAMMING, BLACKMAN
    }

    /**
     * Calculates the magnitude spectrum of a buffer.
     * Uses the best available {@link org.episteme.core.mathematics.analysis.fft.FFTProvider}.
     */
    public static double[] calculateSpectrum(double[] buffer, WindowFunction window) {
        AudioSpectrogramProvider provider = AlgorithmManager.getProvider(AudioSpectrogramProvider.class);
        if (provider != null) {
            return provider.calculateSpectrum(buffer, window);
        }
        
        // Inline fallback if no provider found (should not happen if Default is registered)
        int n = buffer.length;
        if ((n & (n - 1)) != 0) {
            int powerOfTwo = 1 << (31 - Integer.numberOfLeadingZeros(n));
            double[] truncated = new double[powerOfTwo];
            System.arraycopy(buffer, 0, truncated, 0, powerOfTwo);
            buffer = truncated;
            n = powerOfTwo;
        }
        
        double[] real = buffer.clone();
        applyWindow(real, window);
        double[] imag = new double[n];

        // FFT lookup
        org.episteme.core.mathematics.analysis.fft.FFTProvider fft = ServiceLoader.load(org.episteme.core.mathematics.analysis.fft.FFTProvider.class)
                .findFirst().orElse(new org.episteme.core.mathematics.analysis.fft.providers.MulticoreFFTProvider());
        
        double[][] result = fft.transform(real, imag);
        double[] magnitude = new double[n / 2];
        for (int i = 0; i < n / 2; i++) {
            magnitude[i] = Math.sqrt(result[0][i] * result[0][i] + result[1][i] * result[1][i]);
        }
        return magnitude;
    }

    private static void applyWindow(double[] data, WindowFunction type) {
        int n = data.length;
        for (int i = 0; i < n; i++) {
            double factor = switch (type) {
                case RECTANGULAR -> 1.0;
                case HANNING -> 0.5 * (1 - Math.cos(2 * Math.PI * i / (n - 1)));
                case HAMMING -> 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (n - 1));
                case BLACKMAN -> 0.42 - 0.5 * Math.cos(2 * Math.PI * i / (n - 1)) + 0.08 * Math.cos(4 * Math.PI * i / (n - 1));
            };
            data[i] *= factor;
        }
    }

    /**
     * Computes a full spectrogram (list of spectrums) for a long buffer.
     */
    public static List<double[]> computeSpectrogram(double[] audioData, int windowSize, int overlap, WindowFunction window) {
        AudioSpectrogramProvider provider = AlgorithmManager.getProvider(AudioSpectrogramProvider.class);
        if (provider != null) {
            return provider.computeSpectrogram(audioData, windowSize, overlap, window);
        }

        List<double[]> spectrogram = new ArrayList<>();
        int step = windowSize - overlap;
        for (int i = 0; i < audioData.length - windowSize; i += step) {
            double[] chunk = new double[windowSize];
            System.arraycopy(audioData, i, chunk, 0, windowSize);
            spectrogram.add(calculateSpectrum(chunk, window));
        }
        return spectrogram;
    }

    /**
     * Converts bin index to frequency.
     */
    public static Real binToFrequency(int binIndex, int fftSize, double sampleRate) {
        return Real.of(binIndex * sampleRate / fftSize);
    }
}

