/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.economics.growth.providers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.economics.growth.EconomyProvider;
import org.episteme.natural.economics.growth.EconomyParameters;

/**
 * Standard implementation of the Solow-Swan stochastic growth model.
 */
public class StandardEconomyProvider implements EconomyProvider {

    @Override
    public Real evolve(Real k, EconomyParameters params, double dt, double dW) {
        double kVal = k.doubleValue();
        double dk = (params.savingsRate() * Math.pow(kVal, params.capitalShare()) 
                   - (params.popGrowthRate() + params.techGrowthRate() + params.depreciationRate()) * kVal) * dt 
                   + params.volatility() * kVal * dW;
        return Real.of(Math.max(0.001, kVal + dk));
    }

    @Override
    public double evolve(double k, EconomyParameters params, double dt, double dW) {
        double dk = (params.savingsRate() * Math.pow(k, params.capitalShare()) 
                   - (params.popGrowthRate() + params.techGrowthRate() + params.depreciationRate()) * k) * dt 
                   + params.volatility() * k * dW;
        return Math.max(0.001, k + dk);
    }

    @Override
    public float evolve(float k, EconomyParameters params, float dt, float dW) {
        float dk = (float) ((params.savingsRate() * Math.pow(k, params.capitalShare()) 
                   - (params.popGrowthRate() + params.techGrowthRate() + params.depreciationRate()) * k) * dt 
                   + params.volatility() * k * dW);
        return Math.max(0.001f, k + dk);
    }

    @Override
    public String getName() { return "Standard Solow-Swan"; }
    @Override
    public String getAlgorithmType() { return "Economics"; }
}
