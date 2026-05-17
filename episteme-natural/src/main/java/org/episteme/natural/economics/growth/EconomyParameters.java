/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.economics.growth;

import org.episteme.core.distributed.TaskParameters;

/**
 * Scientific parameters for the Solow-Swan stochastic growth model.
 */
public record EconomyParameters(
    double capitalShare,      // alpha (~0.33)
    double depreciationRate,  // delta (~0.05)
    double techGrowthRate,    // g (~0.02)
    double popGrowthRate,     // n (~0.01)
    double savingsRate,       // s (~0.2)
    double volatility         // sigma (stochastic shock intensity)
) implements TaskParameters {
    
    public static EconomyParameters standard() {
        return new EconomyParameters(0.33, 0.05, 0.02, 0.01, 0.2, 0.02);
    }
}
