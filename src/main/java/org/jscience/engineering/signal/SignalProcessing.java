package org.jscience.engineering.signal;

/**
 * Digital signal processing utilities.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SignalProcessing {

    /**
     * Discrete Fourier Transform (DFT).
     * X[k] = Σ x[n] * exp(-j*2π*k*n/N)
     * 
     * @param signal Real-valued input signal
     * @return Complex DFT result [real[], imag[]]
     */
    public static double[][] dft(double[] signal) {
        int N = signal.length;
        double[] real = new double[N];
        double[] imag = new double[N];

        for (int k = 0; k < N; k++) {
            for (int n = 0; n < N; n++) {
                double angle = -2 * Math.PI * k * n / N;
                real[k] += signal[n] * Math.cos(angle);
                imag[k] += signal[n] * Math.sin(angle);
            }
        }

        return new double[][] { real, imag };
    }

    /**
     * Inverse DFT.
     */
    public static double[] inverseDft(double[] real, double[] imag) {
        int N = real.length;
        double[] signal = new double[N];

        for (int n = 0; n < N; n++) {
            for (int k = 0; k < N; k++) {
                double angle = 2 * Math.PI * k * n / N;
                signal[n] += real[k] * Math.cos(angle) - imag[k] * Math.sin(angle);
            }
            signal[n] /= N;
        }

        return signal;
    }

    /**
     * Power spectrum from DFT result.
     * |X[k]|² = real[k]² + imag[k]²
     */
    public static double[] powerSpectrum(double[] real, double[] imag) {
        double[] power = new double[real.length];
        for (int i = 0; i < real.length; i++) {
            power[i] = real[i] * real[i] + imag[i] * imag[i];
        }
        return power;
    }

    /**
     * Moving average filter.
     */
    public static double[] movingAverage(double[] signal, int windowSize) {
        double[] filtered = new double[signal.length];
        double sum = 0;

        for (int i = 0; i < signal.length; i++) {
            sum += signal[i];
            if (i >= windowSize) {
                sum -= signal[i - windowSize];
            }
            int count = Math.min(i + 1, windowSize);
            filtered[i] = sum / count;
        }

        return filtered;
    }

    /**
     * Exponential moving average.
     * y[n] = α * x[n] + (1-α) * y[n-1]
     */
    public static double[] exponentialMovingAverage(double[] signal, double alpha) {
        double[] filtered = new double[signal.length];
        filtered[0] = signal[0];

        for (int i = 1; i < signal.length; i++) {
            filtered[i] = alpha * signal[i] + (1 - alpha) * filtered[i - 1];
        }

        return filtered;
    }

    /**
     * Convolution of two signals.
     */
    public static double[] convolve(double[] signal, double[] kernel) {
        int outLen = signal.length + kernel.length - 1;
        double[] result = new double[outLen];

        for (int i = 0; i < signal.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                result[i + j] += signal[i] * kernel[j];
            }
        }

        return result;
    }

    /**
     * Cross-correlation of two signals.
     */
    public static double[] crossCorrelation(double[] x, double[] y) {
        int N = x.length;
        int M = y.length;
        double[] result = new double[N + M - 1];

        for (int lag = -(M - 1); lag < N; lag++) {
            int idx = lag + M - 1;
            for (int i = 0; i < N; i++) {
                int j = i - lag;
                if (j >= 0 && j < M) {
                    result[idx] += x[i] * y[j];
                }
            }
        }

        return result;
    }

    /**
     * Zero-crossing rate (useful for audio analysis).
     */
    public static double zeroCrossingRate(double[] signal) {
        int crossings = 0;
        for (int i = 1; i < signal.length; i++) {
            if ((signal[i] >= 0 && signal[i - 1] < 0) ||
                    (signal[i] < 0 && signal[i - 1] >= 0)) {
                crossings++;
            }
        }
        return (double) crossings / signal.length;
    }

    /**
     * RMS (Root Mean Square) of signal.
     */
    public static double rms(double[] signal) {
        double sumSquares = 0;
        for (double s : signal) {
            sumSquares += s * s;
        }
        return Math.sqrt(sumSquares / signal.length);
    }

    /**
     * Peak detection with threshold.
     * Returns indices of peaks.
     */
    public static int[] findPeaks(double[] signal, double threshold) {
        java.util.List<Integer> peaks = new java.util.ArrayList<>();
        for (int i = 1; i < signal.length - 1; i++) {
            if (signal[i] > signal[i - 1] &&
                    signal[i] > signal[i + 1] &&
                    signal[i] > threshold) {
                peaks.add(i);
            }
        }
        return peaks.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Generates a sine wave.
     */
    public static double[] generateSineWave(double frequency, double sampleRate, int numSamples) {
        double[] wave = new double[numSamples];
        for (int i = 0; i < numSamples; i++) {
            wave[i] = Math.sin(2 * Math.PI * frequency * i / sampleRate);
        }
        return wave;
    }

    /**
     * Adds white noise to a signal.
     */
    public static double[] addNoise(double[] signal, double amplitude) {
        java.util.Random rand = new java.util.Random();
        double[] noisy = new double[signal.length];
        for (int i = 0; i < signal.length; i++) {
            noisy[i] = signal[i] + amplitude * (2 * rand.nextDouble() - 1);
        }
        return noisy;
    }
}
