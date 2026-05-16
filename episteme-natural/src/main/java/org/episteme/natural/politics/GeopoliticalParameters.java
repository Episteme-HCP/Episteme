/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.politics;

import org.episteme.core.distributed.TaskParameters;

/**
 * Parameters for the Geopolitical Engine simulation.
 */
public record GeopoliticalParameters(
    double stabilityDecay,      // How much stability drops over time
    double conflictProbability, // Chance of conflict between nations
    double militaryCostFactor,  // Economic cost of maintaining military
    double recoveryRate         // How fast nations recover stability
) implements TaskParameters {
    
    public static GeopoliticalParameters standard() {
        return new GeopoliticalParameters(0.01, 0.05, 0.001, 0.02);
    }
}
