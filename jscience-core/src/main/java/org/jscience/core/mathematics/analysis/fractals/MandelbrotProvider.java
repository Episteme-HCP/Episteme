/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.analysis.fractals;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Mandelbrot set generation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MandelbrotProvider extends AlgorithmProvider {

    int[][] compute(double xMin, double xMax, double yMin, double yMax, int width, int height, int maxIterations);

    int[][] computeReal(Real xMin, Real xMax, Real yMin, Real yMax, int width, int height, int maxIterations);

    @Override
    default String getName() {
        return "Mandelbrot Provider";
    }
}
