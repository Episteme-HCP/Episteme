/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.social;

import org.jscience.economics.analysis.TechnicalIndicators;
import org.jscience.economics.loaders.FinancialMarketLoader;
import org.jscience.economics.loaders.FinancialMarketLoader.Candle;
import org.jscience.economics.Money;
import org.jscience.history.TimePoint;
import org.jscience.mathematics.numbers.real.Real;

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
        LOW("🟢 LOW RISK"),
        MODERATE("🟡 MODERATE RISK"),
        HIGH("🟠 HIGH RISK"),
        EXTREME("🔴 EXTREME RISK - CRASH WARNING");

        private final String label;

        RiskLevel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║     JScience Market Crash Predictor          ║");
        System.out.println("║     Technical Analysis & Risk Assessment     ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();

        // Generate sample market data (simulating historical prices)
        List<Candle> marketData = generateSampleData();
        
        System.out.println("📊 Analyzing " + marketData.size() + " trading days of data...");
        System.out.println();

        // Calculate Technical Indicators
        analyzeMarket(marketData);
    }

    private static void analyzeMarket(List<Candle> candles) {
        // Current price
        double currentPrice = candles.get(candles.size() - 1).close.getAmount().doubleValue();
        System.out.printf("Current Price: $%.2f%n", currentPrice);
        System.out.println();

        // SMA Analysis
        System.out.println("═══ MOVING AVERAGE ANALYSIS ═══");
        Real sma20 = TechnicalIndicators.sma(candles, 20);
        Real sma50 = TechnicalIndicators.sma(candles, 50);
        Real sma200 = TechnicalIndicators.sma(candles, 200);

        if (sma20 != null) System.out.printf("  20-day SMA:  $%.2f%n", sma20.doubleValue());
        if (sma50 != null) System.out.printf("  50-day SMA:  $%.2f%n", sma50.doubleValue());
        if (sma200 != null) System.out.printf("  200-day SMA: $%.2f%n", sma200.doubleValue());

        boolean belowSMA200 = TechnicalIndicators.isBelowSMA(candles, 200, 0.05);
        if (belowSMA200) {
            System.out.println("  ⚠️  WARNING: Price >5% below 200-day SMA (bearish)");
        }
        System.out.println();

        // RSI Analysis
        System.out.println("═══ RSI ANALYSIS ═══");
        Real rsi = TechnicalIndicators.rsi(candles, 14);
        if (rsi != null) {
            double rsiVal = rsi.doubleValue();
            System.out.printf("  14-day RSI: %.1f%n", rsiVal);
            if (rsiVal < 30) {
                System.out.println("  📉 OVERSOLD condition detected");
            } else if (rsiVal > 70) {
                System.out.println("  📈 OVERBOUGHT condition detected");
            } else {
                System.out.println("  ✓ RSI in neutral zone");
            }
        }
        System.out.println();

        // Volatility Analysis
        System.out.println("═══ VOLATILITY ANALYSIS ═══");
        Real vol20 = TechnicalIndicators.volatility(candles, 20);
        Real vol50 = TechnicalIndicators.volatility(candles, 50);
        if (vol20 != null && vol50 != null) {
            double shortVol = vol20.doubleValue() * 100;
            double longVol = vol50.doubleValue() * 100;
            System.out.printf("  20-day Volatility: %.2f%%%n", shortVol);
            System.out.printf("  50-day Volatility: %.2f%%%n", longVol);
            
            if (shortVol > longVol * 2) {
                System.out.println("  ⚠️  VOLATILITY SPIKE: Short-term vol >2x long-term");
            }
        }
        System.out.println();

        // Bollinger Bands
        System.out.println("═══ BOLLINGER BANDS (20, 2σ) ═══");
        Real[] bb = TechnicalIndicators.bollingerBands(candles, 20, 2.0);
        if (bb != null) {
            System.out.printf("  Lower Band:  $%.2f%n", bb[0].doubleValue());
            System.out.printf("  Middle Band: $%.2f%n", bb[1].doubleValue());
            System.out.printf("  Upper Band:  $%.2f%n", bb[2].doubleValue());
            
            if (currentPrice < bb[0].doubleValue()) {
                System.out.println("  ⚠️  Price BELOW lower Bollinger Band");
            }
        }
        System.out.println();

        // Overall Risk Assessment
        RiskLevel risk = calculateRiskLevel(candles, rsi, vol20, vol50, belowSMA200, bb, currentPrice);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           RISK ASSESSMENT                    ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.printf("║  %s%n", risk);
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    private static RiskLevel calculateRiskLevel(List<Candle> candles, Real rsi, 
            Real vol20, Real vol50, boolean belowSMA200, Real[] bb, double currentPrice) {
        
        int riskScore = 0;

        // Below 200-day SMA
        if (belowSMA200) riskScore += 2;

        // RSI extreme
        if (rsi != null) {
            double rsiVal = rsi.doubleValue();
            if (rsiVal < 25 || rsiVal > 75) riskScore += 2;
            else if (rsiVal < 30 || rsiVal > 70) riskScore += 1;
        }

        // Volatility spike
        if (vol20 != null && vol50 != null) {
            if (vol20.doubleValue() > vol50.doubleValue() * 2.5) riskScore += 3;
            else if (vol20.doubleValue() > vol50.doubleValue() * 2) riskScore += 2;
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

        if (riskScore >= 7) return RiskLevel.EXTREME;
        if (riskScore >= 5) return RiskLevel.HIGH;
        if (riskScore >= 3) return RiskLevel.MODERATE;
        return RiskLevel.LOW;
    }

    /**
     * Generates sample market data simulating a volatile market with potential crash.
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
                new Money(Real.of(open), "USD"),
                new Money(Real.of(high), "USD"),
                new Money(Real.of(low), "USD"),
                new Money(Real.of(close), "USD"),
                Real.of(volume)
            );
            candles.add(candle);

            price = close;
            date = date.plusDays(1);
            // Skip weekends
            if (date.getDayOfWeek().getValue() == 6) date = date.plusDays(2);
        }

        return candles;
    }
}
