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

package org.jscience.economics.loaders;

import org.jscience.economics.Money;
import org.jscience.history.TimePoint;
import org.jscience.mathematics.numbers.real.Real;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads financial market data from various formats.
 */
public class FinancialMarketReader extends org.jscience.io.AbstractResourceReader<List<FinancialMarketReader.Candle>> {

    private static final FinancialMarketReader INSTANCE = new FinancialMarketReader();

    public static FinancialMarketReader getInstance() {
        return INSTANCE;
    }

    public static class Candle {
        public final TimePoint time;
        public final Money open;
        public final Money high;
        public final Money low;
        public final Money close;
        public final Real volume;

        public Candle(TimePoint time, Money open, Money high, Money low, Money close, Real volume) {
            this.time = time;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }

        @Override
        public String toString() {
            return String.format("%s [O:%s H:%s L:%s C:%s V:%s]", time, open, high, low, close, volume);
        }
    }

    @Override
    protected List<Candle> loadFromSource(String id) throws Exception {
        // Assume id is a classpath resource
        try (InputStream is = getClass().getResourceAsStream(id)) {
            if (is == null) {
                // Try context loader
                InputStream ctxIs = Thread.currentThread().getContextClassLoader().getResourceAsStream(id);
                if (ctxIs == null)
                     throw new java.io.IOException("Resource not found: " + id);
                return loadCSV(ctxIs, "USD"); // Default currency
            }
            return loadCSV(is, "USD"); // Default currency
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Candle>> getResourceType() {
        return (Class<List<Candle>>) (Object) List.class;
    }

    @Override
    public String getName() {
        return "loader.financial.name";
    }

    @Override
    public String getCategory() {
        return "Economics";
    }

    @Override
    public String getDescription() {
        return "Financial Market Data Reader.";
    }

    @Override
    public String getResourcePath() {
        return "financial_market_data.csv";
    }

    public static List<Candle> loadCSV(InputStream is, String currencyCode) {
        List<Candle> candles = new ArrayList<>();
        if (is == null)
            return candles;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    // Date, Open, High, Low, Close, Volume
                    try {
                        double openVal = Double.parseDouble(parts[1]);
                        double highVal = Double.parseDouble(parts[2]);
                        double lowVal = Double.parseDouble(parts[3]);
                        double closeVal = Double.parseDouble(parts[4]);
                        double volVal = Double.parseDouble(parts[5]);

                        candles.add(new Candle(
                                TimePoint.now(), // Placeholder time as parsing wasn't in original
                                new Money(Real.of(openVal), currencyCode),
                                new Money(Real.of(highVal), currencyCode),
                                new Money(Real.of(lowVal), currencyCode),
                                new Money(Real.of(closeVal), currencyCode),
                                Real.of(volVal)));
                    } catch (NumberFormatException e) {
                        // Skip malformed
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candles;
    }
}
