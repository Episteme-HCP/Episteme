/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for Mandelbrot set generation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MandelbrotProvider extends AlgorithmProvider {

    /**
     * Computes the Mandelbrot set using double precision.
     */
    int[][] compute(double xMin, double xMax, double yMin, double yMax, int width, int height, int maxIterations);

    /**
     * Computes the Mandelbrot set using Real precision.
     */
    int[][] computeReal(Real xMin, Real xMax, Real yMin, Real yMax, int width, int height, int maxIterations);

    @Override
    default String getName() {
        return "Mandelbrot Provider";
    }
}
