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

package org.jscience.economics;

import java.util.Random;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulates macroeconomic factors over time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
        Real gdpGrowth = Real.of(0.02 + (random.nextGaussian() * 0.015));
        Real inflationChange = Real.of(random.nextGaussian() * 0.5);
        Real unemploymentChange = gdpGrowth.negate().multiply(Real.of(50))
                .add(Real.of(random.nextGaussian() * 0.2)); // Okun's Law approx

        // Update GDP
        if (economy.getGdp() != null) {
            Real currentGdp = economy.getGdp();
            Real newGdp = currentGdp.multiply(Real.ONE.add(gdpGrowth));
            economy.setGdp(newGdp);
        }

        // Update Rates
        Real currentInflation = economy.getInflationRate() != null ? economy.getInflationRate() : Real.ZERO;
        Real currentUnemployment = economy.getUnemploymentRate() != null ? economy.getUnemploymentRate() : Real.ZERO;

        Real newInflation = currentInflation.add(inflationChange);
        if (newInflation.compareTo(Real.ZERO) < 0)
            newInflation = Real.ZERO;

        Real newUnemployment = currentUnemployment.add(unemploymentChange);
        if (newUnemployment.compareTo(Real.ZERO) < 0)
            newUnemployment = Real.ZERO;

        economy.setInflationRate(newInflation);
        economy.setUnemploymentRate(newUnemployment);
    }

    /**
     * Predicts GDP for next N years based on constant growth.
     */
    public Real predictGDP(int years, Real assumedGrowthRate) {
        if (economy.getGdp() == null)
            return Real.ZERO;
        return economy.getGdp().multiply(Real.ONE.add(assumedGrowthRate).pow(Real.of(years)));
    }
}


