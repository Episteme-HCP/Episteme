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

package org.jscience.social.economics.analysis;

import org.jscience.social.economics.loaders.FinancialMarketReader.Candle;
import org.jscience.core.mathematics.numbers.real.Real;

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

        Real sum = Real.ZERO;
        int start = candles.size() - period;
        for (int i = start; i < candles.size(); i++) {
            sum = sum.add(candles.get(i).close.getAmount());
        }
        return sum.divide(Real.of(period));
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
        Real[] returns = new Real[period];
        int start = candles.size() - period - 1;
        for (int i = 0; i < period; i++) {
            Real prev = candles.get(start + i).close.getAmount();
            Real curr = candles.get(start + i + 1).close.getAmount();
            returns[i] = curr.subtract(prev).divide(prev);
        }

        // Mean of returns
        Real mean = Real.ZERO;
        for (Real r : returns) {
            mean = mean.add(r);
        }
        mean = mean.divide(Real.of(period));

        // Standard deviation
        Real sumSq = Real.ZERO;
        for (Real r : returns) {
            Real diff = r.subtract(mean);
            sumSq = sumSq.add(diff.multiply(diff));
        }
        return sumSq.divide(Real.of(period)).sqrt();
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

        Real gains = Real.ZERO;
        Real losses = Real.ZERO;
        int start = candles.size() - period - 1;

        for (int i = 0; i < period; i++) {
            Real prev = candles.get(start + i).close.getAmount();
            Real curr = candles.get(start + i + 1).close.getAmount();
            Real change = curr.subtract(prev);
            if (change.compareTo(Real.ZERO) > 0) {
                gains = gains.add(change);
            } else {
                losses = losses.subtract(change); // Make positive
            }
        }

        if (losses.isZero()) {
            return Real.of(100.0); // All gains, max RSI
        }

        Real avgGain = gains.divide(Real.of(period));
        Real avgLoss = losses.divide(Real.of(period));
        Real rs = avgGain.divide(avgLoss);
        // rsi = 100.0 - (100.0 / (1.0 + rs))
        return Real.of(100).subtract(Real.of(100).divide(Real.ONE.add(rs)));
    }

    /**
     * Calculates Bollinger Bands (upper, middle, lower).
     *
     * @param candles       Price data
     * @param period        SMA period (typically 20)
     * @param stdMultiplier Standard deviation multiplier (typically 2)
     * @return Array of [lower, middle, upper] bands, or null if insufficient data
     */
    public static Real[] bollingerBands(List<Candle> candles, int period, Real stdMultiplier) {
        Real middle = sma(candles, period);
        if (middle == null) {
            return null;
        }

        // Calculate standard deviation of closing prices
        Real sum = Real.ZERO;
        Real sumSq = Real.ZERO;
        int start = candles.size() - period;
        for (int i = start; i < candles.size(); i++) {
            Real price = candles.get(i).close.getAmount();
            sum = sum.add(price);
            sumSq = sumSq.add(price.multiply(price));
        }
        Real periodReal = Real.of(period);
        Real mean = sum.divide(periodReal);
        // variance = (sumSq / period) - (mean * mean)
        Real variance = sumSq.divide(periodReal).subtract(mean.multiply(mean));
        Real stdDev = variance.sqrt();

        Real offset = stdMultiplier.multiply(stdDev);
        return new Real[] {
                middle.subtract(offset), // Lower band
                middle, // Middle band (SMA)
                middle.add(offset) // Upper band
        };
    }

    public static Real[] bollingerBands(List<Candle> candles, int period, double stdMultiplier) {
        return bollingerBands(candles, period, Real.of(stdMultiplier));
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
        Real first = candles.get(0).close.getAmount();
        Real last = candles.get(candles.size() - 1).close.getAmount();
        // (last - first) / first * 100.0
        return last.subtract(first).divide(first).multiply(Real.of(100));
    }

    /**
     * Detects if current price is significantly below SMA (bearish signal).
     *
     * @param candles   Price data
     * @param smaPeriod SMA period
     * @param threshold Percentage below SMA to trigger (e.g., 0.05 = 5%)
     * @return true if price is significantly below SMA
     */
    public static boolean isBelowSMA(List<Candle> candles, int smaPeriod, Real threshold) {
        Real smaVal = sma(candles, smaPeriod);
        if (smaVal == null || candles.isEmpty()) {
            return false;
        }
        Real currentPrice = candles.get(candles.size() - 1).close.getAmount();
        // currentPrice < smaPrice * (1 - threshold)
        return currentPrice.compareTo(smaVal.multiply(Real.ONE.subtract(threshold))) < 0;
    }

    public static boolean isBelowSMA(List<Candle> candles, int smaPeriod, double threshold) {
        return isBelowSMA(candles, smaPeriod, Real.of(threshold));
    }
}



