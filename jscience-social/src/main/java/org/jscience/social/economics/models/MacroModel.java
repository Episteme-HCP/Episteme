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

package org.jscience.social.economics.models;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.UniversalDataModel;
import org.jscience.core.measure.Quantity;
import org.jscience.social.economics.Economy;

/**
 * Simulates macroeconomic factors over time.
 * <p>
 * <b>Reference:</b><br>
 * Zeigler, B. P., Praehofer, H., & Kim, T. G. (2000). <i>Theory of Modeling and Simulation</i>. Academic Press.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MacroModel implements UniversalDataModel {

    private final String name;
    private final Economy economy;
    private final Random random;

    public MacroModel(String name, Economy economy) {
        this.name = name;
        this.economy = economy;
        this.random = new Random();
    }

    /**
     * Simulates one year of economic activity.
     */
    public void simulateYear() {
        Real gdpGrowth = Real.of(0.02 + (random.nextGaussian() * 0.015));
        Real inflationChange = Real.of(random.nextGaussian() * 0.5);
        Real unemploymentChange = gdpGrowth.negate().multiply(Real.of(50))
                .add(Real.of(random.nextGaussian() * 0.2));

        if (economy.getGDP() != null) {
            org.jscience.social.economics.money.Money currentGDP = economy.getGDP();
            org.jscience.social.economics.money.Money newGDP = currentGDP.multiply(Real.ONE.add(gdpGrowth));
            economy.setGDP(newGDP);
        }

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
    public org.jscience.social.economics.money.Money predictGDP(int years, Real assumedGrowthRate) {
        if (economy.getGDP() == null)
            return org.jscience.social.economics.money.Money.usd(0.0);
        return economy.getGDP().multiply(Real.ONE.add(assumedGrowthRate).pow(Real.of(years)));
    }

    @Override
    public String getModelType() {
        return "MACROECONOMIC_SIMULATION";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>(economy.getMetadata());
        meta.put("model_name", name);
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        return economy.getQuantities();
    }
}





