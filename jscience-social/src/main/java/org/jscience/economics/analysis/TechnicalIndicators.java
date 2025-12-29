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

package org.jscience.economics.analysis;

import org.jscience.economics.loaders.FinancialMarketLoader.Candle;
import org.jscience.mathematics.numbers.real.Real;

import java.util.List;

/**
 * Technical Analysis Indicators for financial markets.
 * Provides calculations for common market indicators used in algorithmic
 * trading
 * and market analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class TechnicalIndicators {

    private TechnicalIndicators() {
        // Utility class
    }

    /**
     * Calculates the Simple Moving Average (SMA) of closing prices.
     *
     * @param candles Price data
     * @param period  Number of periods for the average
     * @return SMA value, or null if insufficient data
     */
    public static Real sma(List<Candle> candles, int period) {
        if (candles == null || candles.size() < period || period <= 0) {
            return null;
        }

        double sum = 0.0;
        int start = candles.size() - period;
        for (int i = start; i < candles.size(); i++) {
            sum += candles.get(i).close.getAmount().doubleValue();
        }
        return Real.of(sum / period);
    }

    /**
     * Calculates the volatility (standard deviation of returns) over a period.
     *
     * @param candles Price data
     * @param period  Number of periods
     * @return Volatility as standard deviation, or null if insufficient data
     */
    public static Real volatility(List<Candle> candles, int period) {
        if (candles == null || candles.size() < period + 1 || period <= 0) {
            return null;
        }

        // Calculate returns
        double[] returns = new double[period];
        int start = candles.size() - period - 1;
        for (int i = 0; i < period; i++) {
            double prev = candles.get(start + i).close.getAmount().doubleValue();
            double curr = candles.get(start + i + 1).close.getAmount().doubleValue();
            returns[i] = (curr - prev) / prev;
        }

        // Mean of returns
        double mean = 0.0;
        for (double r : returns) {
            mean += r;
        }
        mean /= period;

        // Standard deviation
        double sumSq = 0.0;
        for (double r : returns) {
            sumSq += (r - mean) * (r - mean);
        }
        return Real.of(Math.sqrt(sumSq / period));
    }

    /**
     * Calculates the Relative Strength Index (RSI).
     * RSI < 30 indicates oversold, RSI > 70 indicates overbought.
     *
     * @param candles Price data
     * @param period  Number of periods (typically 14)
     * @return RSI value (0-100), or null if insufficient data
     */
    public static Real rsi(List<Candle> candles, int period) {
        if (candles == null || candles.size() < period + 1 || period <= 0) {
            return null;
        }

        double gains = 0.0;
        double losses = 0.0;
        int start = candles.size() - period - 1;

        for (int i = 0; i < period; i++) {
            double prev = candles.get(start + i).close.getAmount().doubleValue();
            double curr = candles.get(start + i + 1).close.getAmount().doubleValue();
            double change = curr - prev;
            if (change > 0) {
                gains += change;
            } else {
                losses -= change; // Make positive
            }
        }

        if (losses == 0) {
            return Real.of(100.0); // All gains, max RSI
        }

        double avgGain = gains / period;
        double avgLoss = losses / period;
        double rs = avgGain / avgLoss;
        double rsi = 100.0 - (100.0 / (1.0 + rs));

        return Real.of(rsi);
    }

    /**
     * Calculates Bollinger Bands (upper, middle, lower).
     *
     * @param candles       Price data
     * @param period        SMA period (typically 20)
     * @param stdMultiplier Standard deviation multiplier (typically 2)
     * @return Array of [lower, middle, upper] bands, or null if insufficient data
     */
    public static Real[] bollingerBands(List<Candle> candles, int period, double stdMultiplier) {
        Real middle = sma(candles, period);
        if (middle == null) {
            return null;
        }

        // Calculate standard deviation of closing prices
        double sum = 0.0;
        double sumSq = 0.0;
        int start = candles.size() - period;
        for (int i = start; i < candles.size(); i++) {
            double price = candles.get(i).close.getAmount().doubleValue();
            sum += price;
            sumSq += price * price;
        }
        double mean = sum / period;
        double variance = (sumSq / period) - (mean * mean);
        double stdDev = Math.sqrt(variance);

        double middleVal = middle.doubleValue();
        return new Real[] {
                Real.of(middleVal - stdMultiplier * stdDev), // Lower band
                middle, // Middle band (SMA)
                Real.of(middleVal + stdMultiplier * stdDev) // Upper band
        };
    }

    /**
     * Calculates the percentage change from the first to the last candle.
     *
     * @param candles Price data
     * @return Percentage change, or null if insufficient data
     */
    public static Real percentChange(List<Candle> candles) {
        if (candles == null || candles.size() < 2) {
            return null;
        }
        double first = candles.get(0).close.getAmount().doubleValue();
        double last = candles.get(candles.size() - 1).close.getAmount().doubleValue();
        return Real.of((last - first) / first * 100.0);
    }

    /**
     * Detects if current price is significantly below SMA (bearish signal).
     *
     * @param candles   Price data
     * @param smaPeriod SMA period
     * @param threshold Percentage below SMA to trigger (e.g., 0.05 = 5%)
     * @return true if price is significantly below SMA
     */
    public static boolean isBelowSMA(List<Candle> candles, int smaPeriod, double threshold) {
        Real smaVal = sma(candles, smaPeriod);
        if (smaVal == null || candles.isEmpty()) {
            return false;
        }
        double currentPrice = candles.get(candles.size() - 1).close.getAmount().doubleValue();
        double smaPrice = smaVal.doubleValue();
        return currentPrice < smaPrice * (1 - threshold);
    }
}
