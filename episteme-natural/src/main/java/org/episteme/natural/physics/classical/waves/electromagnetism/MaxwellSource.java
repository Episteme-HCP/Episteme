/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.waves.electromagnetism;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Interface for electromagnetic field sources.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MaxwellSource {
    double[] computeFields(double t, double x, double y, double z);
    Real[] computeFieldsReal(Real t, Real x, Real y, Real z);
    double[] getPosition();
}
