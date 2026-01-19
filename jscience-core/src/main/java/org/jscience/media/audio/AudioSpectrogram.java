package org.jscience.media.audio;

import org.jscience.mathematics.analysis.transform.SignalFFT;
import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

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
     * Uses the optimized SignalFFT from jscience-core.
     */
    public static double[] calculateSpectrum(double[] buffer, WindowFunction window) {
        int n = buffer.length;
        // SignalFFT requires power of 2
        if ((n & (n - 1)) != 0) {
            int powerOfTwo = 1 << (31 - Integer.numberOfLeadingZeros(n));
            double[] truncated = new double[powerOfTwo];
            System.arraycopy(buffer, 0, truncated, 0, powerOfTwo);
            buffer = truncated;
            n = powerOfTwo;
        }
        
        double[] real = buffer.clone();
        applyWindow(real, window);
        
        // Convert to Real[] for SignalFFT if needed, but we can use double[] if 
        // SignalFFT had a primitive version. Looking at SignalFFT, it uses Real[].
        // Let's check for a primitive version or use a helper.
        Real[] rReal = new Real[n];
        Real[] rImag = new Real[n];
        for (int i = 0; i < n; i++) {
            rReal[i] = Real.of(real[i]);
            rImag[i] = Real.ZERO;
        }

        SignalFFT.fft(rReal, rImag);
        
        double[] magnitude = new double[n / 2];
        for (int i = 0; i < n / 2; i++) {
            magnitude[i] = Math.sqrt(Math.pow(rReal[i].doubleValue(), 2) + 
                                     Math.pow(rImag[i].doubleValue(), 2));
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
