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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.economics.loaders.FinancialMarketLoader;
import org.jscience.economics.Money;
import org.jscience.economics.Currency;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.history.TimePoint;

import java.util.ArrayList;
import java.util.List;

public class TechnicalIndicatorsTest {

    private List<FinancialMarketLoader.Candle> createCandles(double... prices) {
        List<FinancialMarketLoader.Candle> candles = new ArrayList<>();
        Currency usd = Currency.USD;
        for (double p : prices) {
            Money open = new Money(Real.of(p), usd);
            Money close = new Money(Real.of(p), usd);
            Money high = new Money(Real.of(p), usd);
            Money low = new Money(Real.of(p), usd);
            Real vol = Real.of(100.0); // Volume as Real

            candles.add(new FinancialMarketLoader.Candle(TimePoint.now(), open, high, low, close, vol));
        }
        return candles;
    }

    @Test
    public void testSMA() {
        List<FinancialMarketLoader.Candle> candles = createCandles(10, 20, 30, 40, 50);
        Real sma = TechnicalIndicators.sma(candles, 3);
        assertNotNull(sma);
        // Average of last 3: 30, 40, 50 -> 40
        assertEquals(40.0, sma.doubleValue(), 0.001);

        Real smaEmpty = TechnicalIndicators.sma(new ArrayList<>(), 3);
        assertNull(smaEmpty);
    }

    @Test
    public void testVolatility() {
        // Constant price -> 0 volatility
        List<FinancialMarketLoader.Candle> candles = createCandles(10, 10, 10, 10, 10);
        Real vol = TechnicalIndicators.volatility(candles, 4);
        assertNotNull(vol);
        assertEquals(0.0, vol.doubleValue(), 0.001);

        // Volatile
        List<FinancialMarketLoader.Candle> volatileCandles = createCandles(10, 20, 10, 20);
        Real vol2 = TechnicalIndicators.volatility(volatileCandles, 3);
        assertNotNull(vol2);
        assertTrue(vol2.doubleValue() > 0);
    }

    @Test
    public void testRSI() {
        // Always up -> RSI 100
        List<FinancialMarketLoader.Candle> bull = createCandles(10, 20, 30, 40, 50, 60, 70, 80);
        Real rsi = TechnicalIndicators.rsi(bull, 5);
        assertNotNull(rsi);
        assertEquals(100.0, rsi.doubleValue(), 0.001);

        // Mixed
        List<FinancialMarketLoader.Candle> mixed = createCandles(10, 20, 15, 25, 20, 30);
        Real rsiMixed = TechnicalIndicators.rsi(mixed, 4);
        assertNotNull(rsiMixed);
        assertTrue(rsiMixed.doubleValue() > 0 && rsiMixed.doubleValue() < 100);
    }

    @Test
    public void testBollingerBands() {
        List<FinancialMarketLoader.Candle> candles = createCandles(10, 12, 10, 12, 10, 12, 10, 12);
        Real[] bands = TechnicalIndicators.bollingerBands(candles, 4, 2.0);
        assertNotNull(bands);
        assertEquals(3, bands.length);

        double lower = bands[0].doubleValue();
        double middle = bands[1].doubleValue();
        double upper = bands[2].doubleValue();

        assertTrue(lower <= middle);
        assertTrue(middle <= upper);
    }

    @Test
    public void testPercentChange() {
        List<FinancialMarketLoader.Candle> candles = createCandles(100, 105, 110, 120);
        Real change = TechnicalIndicators.percentChange(candles);
        assertNotNull(change);
        // (120 - 100) / 100 * 100 = 20%
        assertEquals(20.0, change.doubleValue(), 0.001);
    }

    @Test
    public void testIsBelowSMA() {
        List<FinancialMarketLoader.Candle> candles = createCandles(100, 100, 100, 50); // Crash at end
        // SMA(3) of 100, 100, 50 -> 83.33
        // Current 50.
        // Threshold 10% (.10). SMA * 0.9 = 75.
        // 50 < 75 -> True
        assertTrue(TechnicalIndicators.isBelowSMA(candles, 3, 0.10));

        List<FinancialMarketLoader.Candle> bull = createCandles(10, 20, 30, 40);
        assertFalse(TechnicalIndicators.isBelowSMA(bull, 3, 0.10));
    }
}
