/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for electromagnetic field sources.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MaxwellSource {
    /**
     * Computes E and B field contributions at (t, x, y, z) using double precision.
     * @return double array: [Ex, Ey, Ez, Bx, By, Bz]
     */
    double[] computeFields(double t, double x, double y, double z);

    /**
     * Computes E and B field contributions at (t, x, y, z) using Real precision.
     * @return Real array: [Ex, Ey, Ez, Bx, By, Bz]
     */
    Real[] computeFieldsReal(Real t, Real x, Real y, Real z);

    /**
     * Returns the position of the source.
     */
    double[] getPosition();
}
