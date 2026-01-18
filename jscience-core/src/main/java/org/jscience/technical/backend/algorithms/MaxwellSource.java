/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.algorithms;

/**
 * Interface for electromagnetic field sources.
 */
public interface MaxwellSource {
    /**
     * Computes E and B field contributions at (t, x, y, z).
     * @return double array: [Ex, Ey, Ez, Bx, By, Bz]
     */
    double[] computeFields(double t, double x, double y, double z);

    /**
     * Returns the position of the source.
     */
    double[] getPosition();
}
