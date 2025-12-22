/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.economics.loaders;

import org.jscience.economics.Money;
import org.jscience.history.TimePoint;

import org.jscience.mathematics.numbers.real.Real;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Loads Financial Market Data (OHLCV).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FinancialMarketLoader {

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

    public List<Candle> loadCSV(InputStream is, String currencyCode) throws IOException {
        List<Candle> candles = new ArrayList<>();
        if (is == null)
            return candles;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    // Date,Open,High,Low,Close,Volume
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
