package org.jscience.engineering.signals;

/**
 * Basic signal processing utilities.
 */
public class SignalProcessing {

    private SignalProcessing() {
    }

    /**
     * Discrete Fourier Transform (naive O(n²) implementation).
     * For production use, replace with FFT.
     * 
     * @param signal Real-valued signal
     * @return Complex array [real[], imag[]]
     */
    public static double[][] dft(double[] signal) {
        int N = signal.length;
        double[] real = new double[N];
        double[] imag = new double[N];

        for (int k = 0; k < N; k++) {
            for (int n = 0; n < N; n++) {
                double angle = 2 * Math.PI * k * n / N;
                real[k] += signal[n] * Math.cos(angle);
                imag[k] -= signal[n] * Math.sin(angle);
            }
        }
        return new double[][] { real, imag };
    }

    /**
     * Magnitude spectrum from DFT output.
     */
    public static double[] magnitudeSpectrum(double[] real, double[] imag) {
        double[] magnitude = new double[real.length];
        for (int i = 0; i < real.length; i++) {
            magnitude[i] = Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
        }
        return magnitude;
    }

    /**
     * Moving average filter.
     */
    public static double[] movingAverage(double[] signal, int windowSize) {
        double[] result = new double[signal.length];
        for (int i = 0; i < signal.length; i++) {
            double sum = 0;
            int count = 0;
            for (int j = Math.max(0, i - windowSize / 2); j <= Math.min(signal.length - 1, i + windowSize / 2); j++) {
                sum += signal[j];
                count++;
            }
            result[i] = sum / count;
        }
        return result;
    }

    /**
     * Convolution of two signals.
     */
    public static double[] convolve(double[] signal, double[] kernel) {
        int resultLength = signal.length + kernel.length - 1;
        double[] result = new double[resultLength];

        for (int i = 0; i < signal.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                result[i + j] += signal[i] * kernel[j];
            }
        }
        return result;
    }

    /**
     * Simple low-pass filter response check.
     * Returns true if frequency is below cutoff.
     */
    public static boolean lowPassFilter(double frequency, double cutoff) {
        return frequency <= cutoff;
    }

    /**
     * Root Mean Square (RMS) of signal.
     */
    public static double rms(double[] signal) {
        double sumSq = 0;
        for (double v : signal) {
            sumSq += v * v;
        }
        return Math.sqrt(sumSq / signal.length);
    }

    /**
     * Peak-to-peak amplitude.
     */
    public static double peakToPeak(double[] signal) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double v : signal) {
            if (v < min)
                min = v;
            if (v > max)
                max = v;
        }
        return max - min;
    }
}
