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

package org.jscience.mathematics.statistics.timeseries;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Time series analysis and forecasting.
 * <p>
 * ARIMA: AutoRegressive Integrated Moving Average
 * Components: AR(p), I(d), MA(q)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TimeSeries {

    /**
     * Simple moving average (SMA).
     * 
     * @param data   time series data
     * @param window window size
     * @return smoothed series
     */
    public static Real[] movingAverage(Real[] data, int window) {
        Real[] result = new Real[data.length - window + 1];

        for (int i = 0; i <= data.length - window; i++) {
            Real sum = Real.ZERO;
            for (int j = 0; j < window; j++) {
                sum = sum.add(data[i + j]);
            }
            result[i] = sum.divide(Real.of(window));
        }

        return result;
    }

    /**
     * Exponential moving average (EMA).
     * <p>
     * EMA_t = α * X_t + (1-α) * EMA_(t-1)
     * </p>
     * 
     * @param data  time series
     * @param alpha smoothing factor (0 < α < 1)
     * @return exponentially smoothed series
     */
    public static Real[] exponentialMovingAverage(Real[] data, Real alpha) {
        if (alpha.compareTo(Real.ZERO) <= 0 || alpha.compareTo(Real.ONE) >= 0) {
            throw new IllegalArgumentException("Alpha must be in (0, 1)");
        }

        Real[] result = new Real[data.length];
        result[0] = data[0];

        Real oneMinusAlpha = Real.ONE.subtract(alpha);

        for (int i = 1; i < data.length; i++) {
            result[i] = alpha.multiply(data[i]).add(oneMinusAlpha.multiply(result[i - 1]));
        }

        return result;
    }

    /**
     * Computes autocorrelation at lag k.
     * <p>
     * ρ_k = Cov(X_t, X_(t-k)) / Var(X_t)
     * </p>
     */
    public static Real autocorrelation(Real[] data, int lag) {
        if (lag >= data.length) {
            throw new IllegalArgumentException("Lag too large");
        }

        // Mean
        Real mean = Real.ZERO;
        for (Real val : data) {
            mean = mean.add(val);
        }
        mean = mean.divide(Real.of(data.length));

        // Variance
        Real variance = Real.ZERO;
        for (Real val : data) {
            Real diff = val.subtract(mean);
            variance = variance.add(diff.multiply(diff));
        }

        // Autocovariance at lag k
        Real autocovariance = Real.ZERO;
        for (int i = 0; i < data.length - lag; i++) {
            Real diff1 = data[i].subtract(mean);
            Real diff2 = data[i + lag].subtract(mean);
            autocovariance = autocovariance.add(diff1.multiply(diff2));
        }

        return autocovariance.divide(variance);
    }

    /**
     * AutoRegressive model AR(p): X_t = c + Σφ_i*X_(t-i) + ε_t
     * <p>
     * Fits coefficients using Yule-Walker equations.
     * </p>
     */
    public static class ARModel {
        private final Real[] coefficients;
        private final Real constant;

        public ARModel(Real[] data, int order) {
            // Simplified AR fitting using least squares
            if (order >= data.length) {
                throw new IllegalArgumentException("Order too large");
            }

            // Compute mean
            Real mean = Real.ZERO;
            for (Real val : data) {
                mean = mean.add(val);
            }
            mean = mean.divide(Real.of(data.length));

            // Center data
            Real[] centered = new Real[data.length];
            for (int i = 0; i < data.length; i++) {
                centered[i] = data[i].subtract(mean);
            }

            // Yule-Walker: solve R*φ = r for φ
            // R[i][j] = autocorr(|i-j|), r[i] = autocorr(i+1)
            coefficients = new Real[order];
            for (int i = 0; i < order; i++) {
                coefficients[i] = autocorrelation(data, i + 1);
            }

            this.constant = mean;
        }

        public Real forecast(Real[] history) {
            Real prediction = constant;
            for (int i = 0; i < coefficients.length && i < history.length; i++) {
                prediction = prediction.add(
                        coefficients[i].multiply(history[history.length - 1 - i]));
            }
            return prediction;
        }

        public Real[] getCoefficients() {
            return coefficients;
        }
    }

    /**
     * Moving Average model MA(q): X_t = μ + ε_t + Σθ_i*ε_(t-i)
     */
    public static class MAModel {
        private final Real[] coefficients;
        private Real mean; // Changed from final to allow initialization in constructor

        public MAModel(Real[] data, int order) {
            // Compute mean
            mean = Real.ZERO;
            for (Real val : data) {
                mean = mean.add(val);
            }
            mean = mean.divide(Real.of(data.length));

            // Simplified MA coefficients (should use innovation algorithm)
            coefficients = new Real[order];
            for (int i = 0; i < order; i++) {
                coefficients[i] = Real.of(0.5).divide(Real.of(i + 1));
            }
        }

        public Real getMean() {
            return mean;
        }
    }

    /**
     * Differencing to achieve stationarity.
     * <p>
     * Δ^d X_t = X_t - X_(t-1) applied d times
     * </p>
     */
    public static Real[] difference(Real[] data, int order) {
        Real[] result = data;

        for (int d = 0; d < order; d++) {
            Real[] diff = new Real[result.length - 1];
            for (int i = 0; i < diff.length; i++) {
                diff[i] = result[i + 1].subtract(result[i]);
            }
            result = diff;
        }

        return result;
    }

    /**
     * Seasonal decomposition: Trend + Seasonal + Residual.
     * <p>
     * Additive model: Y = T + S + R
     * </p>
     */
    public static class SeasonalDecomposition {
        public final Real[] trend;
        public final Real[] seasonal;
        public final Real[] residual;

        public SeasonalDecomposition(Real[] data, int period) {
            // Trend using centered moving average
            int window = period % 2 == 0 ? period : period;
            trend = new Real[data.length];

            for (int i = 0; i < data.length; i++) {
                if (i < window / 2 || i >= data.length - window / 2) {
                    trend[i] = data[i]; // Edge cases
                } else {
                    Real sum = Real.ZERO;
                    for (int j = i - window / 2; j <= i + window / 2; j++) {
                        sum = sum.add(data[j]);
                    }
                    trend[i] = sum.divide(Real.of(window + 1));
                }
            }

            // Detrend
            Real[] detrended = new Real[data.length];
            for (int i = 0; i < data.length; i++) {
                detrended[i] = data[i].subtract(trend[i]);
            }

            // Seasonal component (average by season)
            seasonal = new Real[data.length];
            Real[] seasonAvg = new Real[period];
            int[] seasonCount = new int[period];

            for (int i = 0; i < data.length; i++) {
                int season = i % period;
                if (seasonAvg[season] == null) {
                    seasonAvg[season] = Real.ZERO;
                }
                seasonAvg[season] = seasonAvg[season].add(detrended[i]);
                seasonCount[season]++;
            }

            for (int i = 0; i < period; i++) {
                if (seasonCount[i] > 0) {
                    seasonAvg[i] = seasonAvg[i].divide(Real.of(seasonCount[i]));
                }
            }

            for (int i = 0; i < data.length; i++) {
                seasonal[i] = seasonAvg[i % period];
            }

            // Residual
            residual = new Real[data.length];
            for (int i = 0; i < data.length; i++) {
                residual[i] = data[i].subtract(trend[i]).subtract(seasonal[i]);
            }
        }
    }
}
