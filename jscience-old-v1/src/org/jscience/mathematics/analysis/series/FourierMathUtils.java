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

package org.jscience.mathematics.analysis.series;

import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.algebraic.numbers.Complex;

/**
 * The Fourier math library.
 * This class cannot be subclassed or instantiated because all methods are static.
 *
 * @author Mark Hale
 * @version 0.8
 * @planetmath FourierTransform
 */
public final class FourierMathUtils extends Object {
    private FourierMathUtils() {
    }

    /**
     * Fourier transform.
     *
     * @return an array containing positive frequencies in ascending order
     *         followed by negative frequencies in ascending order.
     * @author Don Cross
     */
    public static Complex[] transform(final Complex data[]) {
        final int N = data.length;
        if (!isPowerOf2(N))
            throw new IllegalArgumentException("The number of samples must be a power of 2.");

        final double arrayRe[] = new double[N];
        final double arrayIm[] = new double[N];

        final int numBits = numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
        for (int i = 0; i < N; i++) {
            final int j = reverseBits(i, numBits);
            arrayRe[j] = data[i].real();
            arrayIm[j] = data[i].imag();
        }
// FFT
        fft(arrayRe, arrayIm, MathConstants.TWO_PI);

        final Complex answer[] = new Complex[N];
        for (int i = 0; i < N; i++)
            answer[i] = new Complex(arrayRe[i], arrayIm[i]);
        return answer;
    }

    /**
     * Fourier transform.
     *
     * @return an array containing positive frequencies in ascending order
     *         followed by negative frequencies in ascending order.
     * @author Don Cross
     */
    public static Complex[] transform(final double dataReal[], final double dataImag[]) {
        final int N = dataReal.length;
        if (!isPowerOf2(N))
            throw new IllegalArgumentException("The number of samples must be a power of 2.");

        final double arrayRe[] = new double[N];
        final double arrayIm[] = new double[N];

        final int numBits = numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
        for (int i = 0; i < N; i++) {
            final int j = reverseBits(i, numBits);
            arrayRe[j] = dataReal[i];
            arrayIm[j] = dataImag[i];
        }
// FFT
        fft(arrayRe, arrayIm, MathConstants.TWO_PI);

        final Complex answer[] = new Complex[N];
        for (int i = 0; i < N; i++)
            answer[i] = new Complex(arrayRe[i], arrayIm[i]);
        return answer;
    }

    /**
     * Fourier transform.
     *
     * @return an array containing positive frequencies in ascending order
     *         followed by negative frequencies in ascending order.
     */
    public static Complex[] transform(final double data[]) {
        final int N = data.length;
        if (!isPowerOf2(N))
            throw new IllegalArgumentException("The number of samples must be a power of 2.");

        final double arrayRe[] = new double[N];
        final double arrayIm[] = new double[N];

        final int numBits = numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
        for (int i = 0; i < N; i++) {
            final int j = reverseBits(i, numBits);
            arrayRe[j] = data[i];
        }
// FFT
        fft(arrayRe, arrayIm, MathConstants.TWO_PI);

        final Complex answer[] = new Complex[N];
        for (int i = 0; i < N; i++)
            answer[i] = new Complex(arrayRe[i], arrayIm[i]);
        return answer;
    }

    /**
     * Inverse Fourier transform.
     *
     * @return an array containing the positive time part of the signal
     *         followed by the negative time part.
     * @author Don Cross
     */
    public static Complex[] inverseTransform(final Complex data[]) {
        final int N = data.length;
        if (!isPowerOf2(N))
            throw new IllegalArgumentException("Data length must be a power of 2.");

        final double arrayRe[] = new double[N];
        final double arrayIm[] = new double[N];

        final int numBits = numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
        for (int i = 0; i < N; i++) {
            final int j = reverseBits(i, numBits);
            arrayRe[j] = data[i].real();
            arrayIm[j] = data[i].imag();
        }
// inverse FFT
        fft(arrayRe, arrayIm, -MathConstants.TWO_PI);
// Normalize
        final Complex answer[] = new Complex[N];
        final double denom = N;
        for (int i = 0; i < N; i++)
            answer[i] = new Complex(arrayRe[i] / denom, arrayIm[i] / denom);
        return answer;
    }

    /**
     * Inverse Fourier transform.
     *
     * @return an array containing the positive time part of the signal
     *         followed by the negative time part.
     * @author Don Cross
     */
    public static Complex[] inverseTransform(final double dataReal[], final double dataImag[]) {
        final int N = dataReal.length;
        if (!isPowerOf2(N))
            throw new IllegalArgumentException("Data length must be a power of 2.");

        final double arrayRe[] = new double[N];
        final double arrayIm[] = new double[N];

        final int numBits = numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
        for (int i = 0; i < N; i++) {
            final int j = reverseBits(i, numBits);
            arrayRe[j] = dataReal[i];
            arrayIm[j] = dataImag[i];
        }
// inverse FFT
        fft(arrayRe, arrayIm, -MathConstants.TWO_PI);
// Normalize
        final Complex answer[] = new Complex[N];
        final double denom = N;
        for (int i = 0; i < N; i++)
            answer[i] = new Complex(arrayRe[i] / denom, arrayIm[i] / denom);
        return answer;
    }

    /**
     * Inverse Fourier transform.
     *
     * @return an array containing the positive time part of the signal
     *         followed by the negative time part.
     */
    public static Complex[] inverseTransform(final double data[]) {
        final int N = data.length;
        if (!isPowerOf2(N))
            throw new IllegalArgumentException("Data length must be a power of 2.");

        final double arrayRe[] = new double[N];
        final double arrayIm[] = new double[N];

        final int numBits = numberOfBitsNeeded(N);
// Simultaneous data copy and bit-reversal ordering into output
        for (int i = 0; i < N; i++) {
            final int j = reverseBits(i, numBits);
            arrayRe[j] = data[i];
        }
// inverse FFT
        fft(arrayRe, arrayIm, -MathConstants.TWO_PI);
// Normalize
        final Complex answer[] = new Complex[N];
        final double denom = N;
        for (int i = 0; i < N; i++)
            answer[i] = new Complex(arrayRe[i] / denom, arrayIm[i] / denom);
        return answer;
    }

    /**
     * Common FFT code.
     *
     * @param twoPi TWO_PI for transform, -TWO_PI for inverse transform.
     */
    private static void fft(double arrayRe[], double arrayIm[], final double twoPi) {
        final int N = arrayRe.length;
        int blockEnd = 1;
        for (int blockSize = 2; blockSize <= N; blockSize <<= 1) {
            final double deltaAngle = twoPi / blockSize;
            double alpha = Math.sin(0.5 * deltaAngle);
            alpha = 2.0 * alpha * alpha;
            final double beta = Math.sin(deltaAngle);
            for (int i = 0; i < N; i += blockSize) {
                double angleRe = 1.0;
                double angleIm = 0.0;
                for (int j = i, n = 0; n < blockEnd; j++, n++) {
                    final int k = j + blockEnd;
                    // tmp = angle*array[k]
                    final double tmpRe = angleRe * arrayRe[k] - angleIm * arrayIm[k];
                    final double tmpIm = angleRe * arrayIm[k] + angleIm * arrayRe[k];
                    arrayRe[k] = arrayRe[j] - tmpRe;
                    arrayIm[k] = arrayIm[j] - tmpIm;
                    arrayRe[j] += tmpRe;
                    arrayIm[j] += tmpIm;
                    // angle = angle - (a-bi)*angle
                    angleRe -= alpha * angleRe + beta * angleIm;
                    angleIm -= alpha * angleIm - beta * angleRe;
                }
            }
            blockEnd = blockSize;
        }
    }

    /**
     * Returns true if x is a power of 2.
     *
     * @author Don Cross
     */
    private static boolean isPowerOf2(final int x) {
        final int BITS_PER_WORD = 32;
        for (int i = 1, y = 2; i < BITS_PER_WORD; i++, y <<= 1) {
            if (x == y)
                return true;
        }
        return false;
    }

    /**
     * Number of bits needed.
     *
     * @author Don Cross
     */
    private static int numberOfBitsNeeded(final int pwrOf2) {
        if (pwrOf2 < 2)
            throw new IllegalArgumentException();
        for (int i = 0; ; i++) {
            if ((pwrOf2 & (1 << i)) > 0)
                return i;
        }
    }

    /**
     * Reverse bits.
     *
     * @author Don Cross
     */
    private static int reverseBits(int index, final int numBits) {
        int i, rev;
        for (i = rev = 0; i < numBits; i++) {
            rev = (rev << 1) | (index & 1);
            index >>= 1;
        }
        return rev;
    }

    /**
     * Sorts the output from the Fourier transfom methods into
     * ascending frequency/time order.
     */
    public static Complex[] sort(final Complex output[]) {
        final Complex ret[] = new Complex[output.length];
        final int Nby2 = output.length / 2;
        for (int i = 0; i < Nby2; i++) {
            ret[Nby2 + i] = output[i];
            ret[i] = output[Nby2 + i];
        }
        return ret;
    }
}

