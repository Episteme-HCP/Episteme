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
    public static void fft(org.jscience.mathematics.numbers.real.Real[] real,
            org.jscience.mathematics.numbers.real.Real[] imag) {
        int n = real.length;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Array length must be power of 2");
        }

        // Bit reversal permutation
        int bits = Integer.numberOfTrailingZeros(n);
        for (int i = 0; i < n; i++) {
            int j = Integer.reverse(i) >>> (32 - bits);
            if (i < j) {
                org.jscience.mathematics.numbers.real.Real temp = real[i];
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
            // double angle = -2 * Math.PI / size;
            org.jscience.mathematics.numbers.real.Real angle = org.jscience.mathematics.numbers.real.Real.PI
                    .multiply(org.jscience.mathematics.numbers.real.Real.of(-2))
                    .divide(org.jscience.mathematics.numbers.real.Real.of(size));

            for (int i = 0; i < n; i += size) {
                for (int j = 0; j < halfSize; j++) {
                    // double wReal = Math.cos(angle * j);
                    // double wImag = Math.sin(angle * j);
                    org.jscience.mathematics.numbers.real.Real theta = angle
                            .multiply(org.jscience.mathematics.numbers.real.Real.of(j));
                    org.jscience.mathematics.numbers.real.Real wReal = theta.cos();
                    org.jscience.mathematics.numbers.real.Real wImag = theta.sin();

                    int evenIdx = i + j;
                    int oddIdx = i + j + halfSize;

                    // Complex multiplication: w * x[oddIdx]
                    // double tReal = wReal * real[oddIdx] - wImag * imag[oddIdx];
                    // double tImag = wReal * imag[oddIdx] + wImag * real[oddIdx];
                    org.jscience.mathematics.numbers.real.Real tReal = wReal.multiply(real[oddIdx])
                            .subtract(wImag.multiply(imag[oddIdx]));
                    org.jscience.mathematics.numbers.real.Real tImag = wReal.multiply(imag[oddIdx])
                            .add(wImag.multiply(real[oddIdx]));

                    // Butterfly
                    real[oddIdx] = real[evenIdx].subtract(tReal);
                    imag[oddIdx] = imag[evenIdx].subtract(tImag);
                    real[evenIdx] = real[evenIdx].add(tReal);
                    imag[evenIdx] = imag[evenIdx].add(tImag);
                }
            }
        }
    }

    /**
     * Compute inverse FFT.
     */
    public static void ifft(org.jscience.mathematics.numbers.real.Real[] real,
            org.jscience.mathematics.numbers.real.Real[] imag) {
        int n = real.length;

        // Conjugate
        for (int i = 0; i < n; i++) {
            imag[i] = imag[i].negate();
        }

        // Forward FFT
        fft(real, imag);

        // Conjugate and scale
        org.jscience.mathematics.numbers.real.Real nReal = org.jscience.mathematics.numbers.real.Real.of(n);
        for (int i = 0; i < n; i++) {
            real[i] = real[i].divide(nReal);
            imag[i] = imag[i].negate().divide(nReal);
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
