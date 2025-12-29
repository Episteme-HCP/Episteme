/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.statistics.ml;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DataNormalization {

    private DataNormalization() {
    }

    /**
     * Min-Max normalization to [0, 1].
     */
    public static double[] minMaxNormalize(double[] data) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double v : data) {
            if (v < min)
                min = v;
            if (v > max)
                max = v;
        }
        double range = max - min;
        if (range == 0)
            range = 1;

        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (data[i] - min) / range;
        }
        return result;
    }

    /**
     * Z-score normalization (standardization).
     * z = (x - μ) / σ
     */
    public static double[] zScoreNormalize(double[] data) {
        double mean = mean(data);
        double std = standardDeviation(data, mean);
        if (std == 0)
            std = 1;

        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (data[i] - mean) / std;
        }
        return result;
    }

    /**
     * Normalize to range [a, b].
     */
    public static double[] normalizeToRange(double[] data, double a, double b) {
        double[] normalized = minMaxNormalize(data);
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = a + normalized[i] * (b - a);
        }
        return result;
    }

    /**
     * L2 normalization (unit vector).
     */
    public static double[] l2Normalize(double[] data) {
        double sumSq = 0;
        for (double v : data) {
            sumSq += v * v;
        }
        double norm = Math.sqrt(sumSq);
        if (norm == 0)
            norm = 1;

        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i] / norm;
        }
        return result;
    }

    private static double mean(double[] data) {
        double sum = 0;
        for (double v : data)
            sum += v;
        return sum / data.length;
    }

    private static double standardDeviation(double[] data, double mean) {
        double sumSq = 0;
        for (double v : data) {
            sumSq += (v - mean) * (v - mean);
        }
        return Math.sqrt(sumSq / data.length);
    }
}
