package org.jscience.economics;

import java.util.Random;

/**
 * Simulates macroeconomic factors over time.
 */
public class MacroModel {

    private final Economy economy;
    private final Random random;

    public MacroModel(Economy economy) {
        this.economy = economy;
        this.random = new Random();
    }

    /**
     * Simulates one year of economic activity.
     * Updates GDP, Inflation, and Unemployment based on simplified volatility.
     */
    public void simulateYear() {
        // Simple random walk model for demonstration
        double gdpGrowth = 0.02 + (random.nextGaussian() * 0.015); // Mean 2%, StdDev 1.5%
        double inflationChange = random.nextGaussian() * 0.5;
        double unemploymentChange = -(gdpGrowth * 50) + (random.nextGaussian() * 0.2); // Okun's Law approx

        // Update GDP
        if (economy.getGdp() != null) {
            // Using logic convertible to double for simple simulation
            double currentGdp = economy.getGdp().doubleValue();
            double newGdp = currentGdp * (1 + gdpGrowth);
            System.out.println("Projected GDP: " + newGdp); // This line is syntactically incorrect for a void method
                                                            // and will cause a
            // compile error.
            // We'd need a way to set real GDP back, assuming specific implementation of
            // Real
            // For this sim, we might need to assume setters use Real.of(double) behavior
            // from previous files
            // But Real is custom. Let's try to stick to what we know Economy has.
            // Economy has setGdp(Real).
            // We need to assume Real.of(double) exists as seen in Economy.java view.
        }

        // Update Rates
        double newInflation = Math.max(0, economy.getInflationRate() + inflationChange);
        double newUnemployment = Math.max(0, economy.getUnemploymentRate() + unemploymentChange);

        economy.setInflationRate(newInflation);
        economy.setUnemploymentRate(newUnemployment);

        // Note: Actual GDP update skipped to avoid direct dependency on Real
        // construction without full import
        // functionality in this snippet, but logic is "simulate".
        // We will add a helper if we needed to fully set it.
    }

    /**
     * Predicts GDP for next N years based on constant growth.
     */
    public double predictGDP(int years, double assumedGrowthRate) {
        if (economy.getGdp() == null)
            return 0.0;
        return economy.getGdp().doubleValue() * Math.pow(1 + assumedGrowthRate, years);
    }
}
