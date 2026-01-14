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

package org.jscience.mathematics.wavelet;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The wavelet math library. This class cannot be subclassed or
 * instantiated because all methods are static.
 *
 * @author Daniel Lemire
 * @version 1.0
 */
public final class WaveletMathUtils extends Object {
/**
     * Creates a new WaveletMathUtils object.
     */
    private WaveletMathUtils() {
    }

    /**
     * Part of the Fast Wavelet Scheme. Downsampling of a set of data
     * points in base 2 with an arbitrary filter using zero-padding at the
     * boundaries in 1D.
     *
     * @param filter DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return an array half the length of the input data[] as long as
     *         data.length was even.
     */
    public static Complex[] downsample(Complex[] filter, Complex[] data) {
        int loc = filter.length;
        int demiloc = Math.round((loc / 2f) - 0.5f);
        int Nombre = data.length;
        int DemiNombre = Math.round((Nombre / 2f) - 0.5f);
        Complex[] answer = new Complex[DemiNombre];

        for (int i = 0; i < DemiNombre; i++) {
            answer[i] = Complex.ZERO;

            for (int j = Math.max(0, (2 * i) - demiloc + 1);
                    j < Math.min(Nombre, ((2 * i) + loc) - demiloc + 1); j++)
                answer[i] = answer[i].add(data[j].multiply(
                            filter[(j - (2 * i) + demiloc) - 1]));
        }

        return answer;
    }

    /**
     * Part of the Fast Wavelet Scheme. Downsampling of a set of data
     * points in base 2 with an arbitrary filter using zero-padding at the
     * boundaries in 1D.
     *
     * @param filter DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return an array half the length of the input data[] as long as
     *         data.length was even.
     */
    public static double[] downsample(double[] filter, double[] data) {
        int loc = filter.length;
        int demiloc = Math.round((loc / 2f) - 0.5f);
        int Nombre = data.length;
        int DemiNombre = Math.round((Nombre / 2f) - 0.5f);
        double[] answer = new double[DemiNombre];

        for (int i = 0; i < DemiNombre; i++) {
            answer[i] = 0.0;

            for (int j = Math.max(0, (2 * i) - demiloc + 1);
                    j < Math.min(Nombre, ((2 * i) + loc) - demiloc + 1); j++)
                answer[i] += (data[j] * filter[(j - (2 * i) + demiloc) - 1]);
        }

        return answer;
    }

    /**
     * Insertion of zeros between every other data point in 1D.
     *
     * @param data DOCUMENT ME!
     *
     * @return an array twice as long as the input data[].
     */
    public static Complex[] upsample(Complex[] data) {
        int Nombre = data.length;
        Complex[] answer = new Complex[2 * Nombre];

        for (int i = 0; i < Nombre; i++) {
            answer[2 * i] = data[i];
            answer[(2 * i) + 1] = Complex.ZERO;
        }

        return answer;
    }

    /**
     * Insertion of zeros between every other data point in 1D.
     *
     * @param data DOCUMENT ME!
     *
     * @return an array twice as long as the input data[].
     */
    public static double[] upsample(double[] data) {
        int Nombre = data.length;
        double[] answer = new double[2 * Nombre];

        for (int i = 0; i < Nombre; i++) {
            answer[2 * i] = data[i];
            answer[(2 * i) + 1] = 0.0;
        }

        return answer;
    }

    /**
     * Part of the Fast Wavelet Scheme. Upsampling of a set of data
     * points in base 2 with an arbitrary filter using zero-padding at the
     * boundaries in 1D.
     *
     * @param filter DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return an array twice as long as the input data[].
     */
    public static Complex[] upsample(Complex[] filter, Complex[] data) {
        int loc = filter.length;
        int demiloc = Math.round((loc / 2f) - 0.5f);
        int Nombre = data.length;
        Complex[] answer = new Complex[2 * Nombre];
        Complex[] tmp = new Complex[2 * Nombre];
        tmp = upsample(data);

        for (int i = 0; i < (2 * Nombre); i++) {
            answer[i] = Complex.ZERO;

            for (int j = Math.max(0, i - demiloc);
                    j < Math.min(2 * Nombre, (i + loc) - demiloc); j++)
                answer[i] = answer[i].add(tmp[j].multiply(
                            filter[(i + loc) - demiloc - j - 1]));
        }

        return answer;
    }

    /**
     * Part of the Fast Wavelet Scheme. Upsampling of a set of data
     * points in base 2 with an arbitrary filter using zero-padding at the
     * boundaries in 1D.
     *
     * @param filter DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return an array twice as long as the input data[].
     */
    public static double[] upsample(double[] filter, double[] data) {
        int loc = filter.length;
        int demiloc = Math.round((loc / 2f) - 0.5f);
        int Nombre = data.length;
        double[] answer = new double[2 * Nombre];
        double[] tmp = new double[2 * Nombre];
        tmp = upsample(data);

        for (int i = 0; i < (2 * Nombre); i++) {
            answer[i] = 0.0;

            for (int j = Math.max(0, i - demiloc);
                    j < Math.min(2 * Nombre, (i + loc) - demiloc); j++)
                answer[i] += (tmp[j] * filter[(i + loc) - demiloc - j - 1]);
        }

        return answer;
    }

    /**
     * Returns the highpass filter from the lowpass filter using
     * Cohen's formula.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[] lowToHigh(double[] v) {
        double[] ans = ArrayMathUtils.invert(v);
        int b = 1;

        for (int k = 0; k < ans.length; k++) {
            ans[k] = b * ans[k];
            b = -b;
        }

        return ans;
    }
}
