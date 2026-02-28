/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.mechanics.simulation.providers;

import java.util.List;

/**
 * Record holding simulation tasks and parameters for execution.
 */
public record SimulationSource(List<Runnable> tasks, int parallelism) {
}
