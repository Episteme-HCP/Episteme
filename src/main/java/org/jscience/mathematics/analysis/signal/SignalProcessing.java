package org.jscience.mathematics.analysis.signal;

import org.jscience.mathematics.analysis.transform.FFT;

/**
 * Digital signal processing utilities.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SignalProcessing {

    /**
     * Low-pass filter using FFT (brick-wall).
     * 
     * @param signal     Input signal
     * @param cutoff     Cutoff frequency (normalized 0-1)
     * @param sampleRate Sampling rate
     */
    public static double[] lowPassFilter(double[] signal, double cutoff, double sampleRate) {
        int n = nextPowerOf2(signal.length);
        double[] padded = new double[n];
        System.arraycopy(signal, 0, padded, 0, signal.length);

        double[][] spectrum = FFT.fftReal(padded);
        double[] real = spectrum[0];
        double[] imag = spectrum[1];

        double nyquist = sampleRate / 2;
        int cutoffBin = (int) (cutoff / nyquist * (n / 2));

        // Zero out frequencies above cutoff
        for (int i = cutoffBin + 1; i < n - cutoffBin; i++) {
            real[i] = 0;
            imag[i] = 0;
        }

        FFT.ifft(real, imag);

        double[] result = new double[signal.length];
        System.arraycopy(real, 0, result, 0, signal.length);
        return result;
    }

    /**
     * High-pass filter using FFT.
     */
    public static double[] highPassFilter(double[] signal, double cutoff, double sampleRate) {
        int n = nextPowerOf2(signal.length);
        double[] padded = new double[n];
        System.arraycopy(signal, 0, padded, 0, signal.length);

        double[][] spectrum = FFT.fftReal(padded);
        double[] real = spectrum[0];
        double[] imag = spectrum[1];

        double nyquist = sampleRate / 2;
        int cutoffBin = (int) (cutoff / nyquist * (n / 2));

        // Zero out frequencies below cutoff
        for (int i = 0; i <= cutoffBin; i++) {
            real[i] = 0;
            imag[i] = 0;
            if (i > 0 && n - i < n) {
                real[n - i] = 0;
                imag[n - i] = 0;
            }
        }

        FFT.ifft(real, imag);

        double[] result = new double[signal.length];
        System.arraycopy(real, 0, result, 0, signal.length);
        return result;
    }

    /**
     * Moving average filter.
     */
    public static double[] movingAverage(double[] signal, int windowSize) {
        int n = signal.length;
        double[] result = new double[n];
        double sum = 0;

        for (int i = 0; i < n; i++) {
            sum += signal[i];
            if (i >= windowSize) {
                sum -= signal[i - windowSize];
            }
            int count = Math.min(i + 1, windowSize);
            result[i] = sum / count;
        }
        return result;
    }

    /**
     * Compute RMS (Root Mean Square) value.
     */
    public static double rms(double[] signal) {
        double sum = 0;
        for (double v : signal) {
            sum += v * v;
        }
        return Math.sqrt(sum / signal.length);
    }

    /**
     * Peak detection.
     * 
     * @param signal    Input signal
     * @param threshold Minimum peak amplitude
     * @return Indices of peaks
     */
    public static int[] findPeaks(double[] signal, double threshold) {
        java.util.List<Integer> peaks = new java.util.ArrayList<>();
        for (int i = 1; i < signal.length - 1; i++) {
            if (signal[i] > threshold
                    && signal[i] > signal[i - 1]
                    && signal[i] > signal[i + 1]) {
                peaks.add(i);
            }
        }
        return peaks.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Zero-crossing rate.
     */
    public static double zeroCrossingRate(double[] signal) {
        int crossings = 0;
        for (int i = 1; i < signal.length; i++) {
            if (signal[i] * signal[i - 1] < 0) {
                crossings++;
            }
        }
        return (double) crossings / (signal.length - 1);
    }

    /**
     * Downsample signal by factor.
     */
    public static double[] downsample(double[] signal, int factor) {
        int n = (signal.length + factor - 1) / factor;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = signal[i * factor];
        }
        return result;
    }

    /**
     * Upsample signal by factor (zero-insertion).
     */
    public static double[] upsample(double[] signal, int factor) {
        double[] result = new double[signal.length * factor];
        for (int i = 0; i < signal.length; i++) {
            result[i * factor] = signal[i];
        }
        return result;
    }

    /**
     * Generate sine wave.
     */
    public static double[] sineWave(double frequency, double sampleRate, int samples) {
        double[] wave = new double[samples];
        for (int i = 0; i < samples; i++) {
            wave[i] = Math.sin(2 * Math.PI * frequency * i / sampleRate);
        }
        return wave;
    }

    /**
     * Generate white noise.
     */
    public static double[] whiteNoise(int samples) {
        double[] noise = new double[samples];
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < samples; i++) {
            noise[i] = rand.nextGaussian();
        }
        return noise;
    }

    /**
     * Add two signals.
     */
    public static double[] add(double[] a, double[] b) {
        int n = Math.max(a.length, b.length);
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            double va = i < a.length ? a[i] : 0;
            double vb = i < b.length ? b[i] : 0;
            result[i] = va + vb;
        }
        return result;
    }

    private static int nextPowerOf2(int n) {
        int p = 1;
        while (p < n)
            p *= 2;
        return p;
    }
}
