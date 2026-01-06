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

package org.jscience.apps.economics;

import org.jscience.economics.analysis.TechnicalIndicators;
import org.jscience.economics.loaders.FinancialMarketReader.Candle;
import org.jscience.economics.Money;
import org.jscience.history.TimePoint;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.i18n.I18nManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Killer App: Market Crash Predictor.
 * <p>
 * Analyzes historical market data using technical indicators to assess
 * crash risk and generate warnings. Uses SMA, RSI, and volatility analysis.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MarketCrashPredictor {

    public enum RiskLevel {
        LOW("market.risk.low"),
        MODERATE("market.risk.moderate"),
        HIGH("market.risk.high"),
        EXTREME("market.risk.extreme");

        private final String key;

        RiskLevel(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return I18nManager.getInstance().get(key);
        }
    }

    private static final I18nManager i18n = I18nManager.getInstance();

    public static void main(String[] args) {
        System.out.println(
                "╔═════════════════════════════════════════════╗");
        System.out.println("║     " + i18n.get("market.console.title") + "          ║");
        System.out.println("║     " + i18n.get("market.console.subtitle") + "     ║");
        System.out.println(
                "╚═════════════════════════════════════════════╝");
        System.out.println();

        // Generate sample market data (simulating historical prices)
        List<Candle> marketData = generateSampleData();

        System.out.println(
                "📊 " + java.text.MessageFormat.format(i18n.get("market.console.analyzing"), marketData.size()));
        System.out.println();

        // Calculate Technical Indicators
        analyzeMarket(marketData);
    }

    private static void analyzeMarket(List<Candle> candles) {
        // Current price
        double currentPrice = candles.get(candles.size() - 1).close.getAmount().doubleValue();
        System.out.println(java.text.MessageFormat.format(i18n.get("market.console.current_price"),
                String.format("%.2f", currentPrice)));
        System.out.println();

        // SMA Analysis
        System.out.println("═══ " + i18n.get("market.console.sma_analysis") + " ═══");
        Real sma20 = TechnicalIndicators.sma(candles, 20);
        Real sma50 = TechnicalIndicators.sma(candles, 50);
        Real sma200 = TechnicalIndicators.sma(candles, 200);

        if (sma20 != null)
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.sma_val"), 20,
                    String.format("%.2f", sma20.doubleValue())));
        if (sma50 != null)
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.sma_val"), 50,
                    String.format("%.2f", sma50.doubleValue())));
        if (sma200 != null)
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.sma_val"), 200,
                    String.format("%.2f", sma200.doubleValue())));

        boolean belowSMA200 = TechnicalIndicators.isBelowSMA(candles, 200, 0.05);
        if (belowSMA200) {
            System.out.println("  ⚠️   " + i18n.get("market.console.warning.sma200"));
        }
        System.out.println();

        // RSI Analysis
        System.out.println("═══ " + i18n.get("market.console.rsi_analysis") + " ═══");
        Real rsi = TechnicalIndicators.rsi(candles, 14);
        if (rsi != null) {
            double rsiVal = rsi.doubleValue();
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.rsi_val"),
                    String.format("%.1f", rsiVal)));
            if (rsiVal < 30) {
                System.out.println("  📉 " + i18n.get("market.console.oversold"));
            } else if (rsiVal > 70) {
                System.out.println("  📈 " + i18n.get("market.console.overbought"));
            } else {
                System.out.println("  ✅ " + i18n.get("market.console.rsi_neutral"));
            }
        }
        System.out.println();

        // Volatility Analysis
        System.out.println("═══ " + i18n.get("market.console.vol_analysis") + " ═══");
        Real vol20 = TechnicalIndicators.volatility(candles, 20);
        Real vol50 = TechnicalIndicators.volatility(candles, 50);
        if (vol20 != null && vol50 != null) {
            double shortVol = vol20.doubleValue() * 100;
            double longVol = vol50.doubleValue() * 100;
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.vol_val"), 20,
                    String.format("%.2f", shortVol)));
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.vol_val"), 50,
                    String.format("%.2f", longVol)));

            if (shortVol > longVol * 2) {
                System.out.println("  ⚠️   " + i18n.get("market.console.vol_spike"));
            }
        }
        System.out.println();

        // Bollinger Bands
        System.out.println("═══ " + i18n.get("market.console.bb_analysis") + " ═══");
        Real[] bb = TechnicalIndicators.bollingerBands(candles, 20, 2.0);
        if (bb != null) {
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.bb_lower"),
                    String.format("%.2f", bb[0].doubleValue())));
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.bb_mid"),
                    String.format("%.2f", bb[1].doubleValue())));
            System.out.println("  " + java.text.MessageFormat.format(i18n.get("market.console.bb_upper"),
                    String.format("%.2f", bb[2].doubleValue())));

            if (currentPrice < bb[0].doubleValue()) {
                System.out.println("  ⚠️   " + i18n.get("market.console.warning.bb"));
            }
        }
        System.out.println();

        // Overall Risk Assessment
        RiskLevel risk = calculateRiskLevel(candles, rsi, vol20, vol50, belowSMA200, bb, currentPrice);
        System.out.println(
                "╔═════════════════════════════════════════════╗");
        System.out.println("║           " + i18n.get("market.console.risk_assessment") + "                    ║");
        System.out.println(
                "╠═════════════════════════════════════════════╣");
        System.out.printf("║  %s%n", risk);
        System.out.println(
                "╚═════════════════════════════════════════════╝");
    }

    private static RiskLevel calculateRiskLevel(List<Candle> candles, Real rsi,
            Real vol20, Real vol50, boolean belowSMA200, Real[] bb, double currentPrice) {

        int riskScore = 0;

        // Below 200-day SMA
        if (belowSMA200)
            riskScore += 2;

        // RSI extreme
        if (rsi != null) {
            double rsiVal = rsi.doubleValue();
            if (rsiVal < 25 || rsiVal > 75)
                riskScore += 2;
            else if (rsiVal < 30 || rsiVal > 70)
                riskScore += 1;
        }

        // Volatility spike
        if (vol20 != null && vol50 != null) {
            if (vol20.doubleValue() > vol50.doubleValue() * 2.5)
                riskScore += 3;
            else if (vol20.doubleValue() > vol50.doubleValue() * 2)
                riskScore += 2;
        }

        // Below Bollinger lower band
        if (bb != null && currentPrice < bb[0].doubleValue()) {
            riskScore += 2;
        }

        // Recent price decline
        Real pctChange = TechnicalIndicators.percentChange(candles);
        if (pctChange != null && pctChange.doubleValue() < -15) {
            riskScore += 3;
        } else if (pctChange != null && pctChange.doubleValue() < -10) {
            riskScore += 2;
        }

        if (riskScore >= 7)
            return RiskLevel.EXTREME;
        if (riskScore >= 5)
            return RiskLevel.HIGH;
        if (riskScore >= 3)
            return RiskLevel.MODERATE;
        return RiskLevel.LOW;
    }

    /**
     * Generates sample market data simulating a volatile market with potential
     * crash.
     */
    private static List<Candle> generateSampleData() {
        List<Candle> candles = new ArrayList<>();

        double price = 100.0;
        LocalDate date = LocalDate.of(2024, 1, 1);

        for (int i = 0; i < 252; i++) { // ~1 year of trading days
            // Simulate price movement with occasional volatility spikes
            double volatility = 0.02; // Normal 2% daily volatility

            // Add crash scenario around day 200-220
            if (i >= 200 && i <= 220) {
                volatility = 0.05; // 5% volatility during crash
                price *= (1 - 0.02); // Downward bias during crash
            }

            double change = (Math.random() - 0.5) * 2 * volatility * price;
            double open = price;
            double close = price + change;
            double high = Math.max(open, close) * (1 + Math.random() * 0.01);
            double low = Math.min(open, close) * (1 - Math.random() * 0.01);
            double volume = 1000000 + Math.random() * 500000;

            // Add volume spike during crash
            if (i >= 200 && i <= 220) {
                volume *= 3;
            }

            TimePoint time = TimePoint.of(date);
            Candle candle = new Candle(
                    time,
                    Money.usd(open),
                    Money.usd(high),
                    Money.usd(low),
                    Money.usd(close),
                    Real.of(volume));
            candles.add(candle);

            price = close;
            date = date.plusDays(1);
            // Skip weekends
            if (date.getDayOfWeek().getValue() == 6)
                date = date.plusDays(2);
        }

        return candles;
    }
}
