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

package org.jscience.economics.loaders;

import org.jscience.economics.Money;
import org.jscience.history.TimePoint;
import org.jscience.io.AbstractLoader;
import org.jscience.io.MiniCatalog;
import org.jscience.mathematics.numbers.real.Real;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

/**
 * Loads Financial Market Data (OHLCV).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FinancialMarketLoader extends AbstractLoader<List<FinancialMarketLoader.Candle>> {

    private String currencyCode = "USD";

    public FinancialMarketLoader() {
    }

    public FinancialMarketLoader(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String getResourcePath() {
        return "/data/markets/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Candle>> getResourceType() {
        return (Class<List<FinancialMarketLoader.Candle>>) (Class<?>) List.class;
    }

    @Override
    protected List<Candle> loadFromSource(String id) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(id)) {
            if (is == null) {
                try (InputStream fileIs = new java.io.FileInputStream(id)) {
                    return loadCSV(fileIs, currencyCode);
                }
            }
            return loadCSV(is, currencyCode);
        }
    }

    @Override
    protected MiniCatalog<List<Candle>> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<List<Candle>> getAll() {
                return List.of(List.of());
            }

            @Override
            public Optional<List<Candle>> findByName(String name) {
                return Optional.of(List.of());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    public static class Candle {
        public TimePoint time;
        public Money open;
        public Money high;
        public Money low;
        public Money close;
        public Real volume;

        public Candle(TimePoint time, Money open, Money high, Money low, Money close, Real volume) {
            this.time = time;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }
    }

    public static List<Candle> loadCSV(InputStream is, String currencyCode) throws IOException {
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
                    LocalDate date = LocalDate.parse(parts[0]);
                    TimePoint time = TimePoint.of(date);

                    Money open = new Money(Real.of(Double.parseDouble(parts[1])), currencyCode);
                    Money high = new Money(Real.of(Double.parseDouble(parts[2])), currencyCode);
                    Money low = new Money(Real.of(Double.parseDouble(parts[3])), currencyCode);
                    Money close = new Money(Real.of(Double.parseDouble(parts[4])), currencyCode);
                    Real vol = Real.of(Double.parseDouble(parts[5]));

                    candles.add(new Candle(time, open, high, low, close, vol));
                }
            }
        }
        return candles;
    }
}
