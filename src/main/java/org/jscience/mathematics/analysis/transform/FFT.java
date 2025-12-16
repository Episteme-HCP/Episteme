package org.jscience.mathematics.analysis.transform;

/**
 * Fast Fourier Transform (FFT) implementation.
 * <p>
 * Cooley-Tukey radix-2 DIT algorithm.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class FFT {

    /**
     * Compute forward FFT of complex data.
     * 
     * @param real Real part (modified in place)
     * @param imag Imaginary part (modified in place)
     */
    public static void fft(double[] real, double[] imag) {
        int n = real.length;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Array length must be power of 2");
        }

        // Bit reversal permutation
        int bits = Integer.numberOfTrailingZeros(n);
        for (int i = 0; i < n; i++) {
            int j = Integer.reverse(i) >>> (32 - bits);
            if (i < j) {
                double temp = real[i];
                real[i] = real[j];
                real[j] = temp;
                temp = imag[i];
                imag[i] = imag[j];
                imag[j] = temp;
            }
        }

        // Cooley-Tukey iterative FFT
        for (int size = 2; size <= n; size *= 2) {
            int halfSize = size / 2;
            double angle = -2 * Math.PI / size;

            for (int i = 0; i < n; i += size) {
                for (int j = 0; j < halfSize; j++) {
                    double wReal = Math.cos(angle * j);
                    double wImag = Math.sin(angle * j);

                    int evenIdx = i + j;
                    int oddIdx = i + j + halfSize;

                    // Complex multiplication: w * x[oddIdx]
                    double tReal = wReal * real[oddIdx] - wImag * imag[oddIdx];
                    double tImag = wReal * imag[oddIdx] + wImag * real[oddIdx];

                    // Butterfly
                    real[oddIdx] = real[evenIdx] - tReal;
                    imag[oddIdx] = imag[evenIdx] - tImag;
                    real[evenIdx] += tReal;
                    imag[evenIdx] += tImag;
                }
            }
        }
    }

    /**
     * Compute inverse FFT.
     */
    public static void ifft(double[] real, double[] imag) {
        int n = real.length;

        // Conjugate
        for (int i = 0; i < n; i++) {
            imag[i] = -imag[i];
        }

        // Forward FFT
        fft(real, imag);

        // Conjugate and scale
        for (int i = 0; i < n; i++) {
            real[i] /= n;
            imag[i] = -imag[i] / n;
        }
    }

    /**
     * FFT of real-valued data.
     * 
     * @param data Real input (must be power of 2 length)
     * @return [real_part, imag_part]
     */
    public static double[][] fftReal(double[] data) {
        int n = data.length;
        double[] real = data.clone();
        double[] imag = new double[n];
        fft(real, imag);
        return new double[][] { real, imag };
    }

    /**
     * Compute magnitude spectrum.
     */
    public static double[] magnitude(double[] real, double[] imag) {
        int n = real.length;
        double[] mag = new double[n];
        for (int i = 0; i < n; i++) {
            mag[i] = Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
        }
        return mag;
    }

    /**
     * Compute phase spectrum.
     */
    public static double[] phase(double[] real, double[] imag) {
        int n = real.length;
        double[] ph = new double[n];
        for (int i = 0; i < n; i++) {
            ph[i] = Math.atan2(imag[i], real[i]);
        }
        return ph;
    }

    /**
     * Power spectral density.
     */
    public static double[] powerSpectrum(double[] data) {
        double[][] result = fftReal(data);
        int n = data.length;
        double[] power = new double[n / 2 + 1];
        for (int i = 0; i <= n / 2; i++) {
            power[i] = (result[0][i] * result[0][i] + result[1][i] * result[1][i]) / n;
        }
        return power;
    }

    /**
     * Compute convolution using FFT.
     */
    public static double[] convolve(double[] a, double[] b) {
        int n = 1;
        while (n < a.length + b.length - 1)
            n *= 2;

        double[] aReal = new double[n], aImag = new double[n];
        double[] bReal = new double[n], bImag = new double[n];

        System.arraycopy(a, 0, aReal, 0, a.length);
        System.arraycopy(b, 0, bReal, 0, b.length);

        fft(aReal, aImag);
        fft(bReal, bImag);

        // Complex multiplication
        double[] cReal = new double[n], cImag = new double[n];
        for (int i = 0; i < n; i++) {
            cReal[i] = aReal[i] * bReal[i] - aImag[i] * bImag[i];
            cImag[i] = aReal[i] * bImag[i] + aImag[i] * bReal[i];
        }

        ifft(cReal, cImag);

        double[] result = new double[a.length + b.length - 1];
        System.arraycopy(cReal, 0, result, 0, result.length);
        return result;
    }

    /**
     * Compute correlation using FFT.
     */
    public static double[] correlate(double[] a, double[] b) {
        // Reverse b for correlation
        double[] bRev = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            bRev[i] = b[b.length - 1 - i];
        }
        return convolve(a, bRev);
    }
}
