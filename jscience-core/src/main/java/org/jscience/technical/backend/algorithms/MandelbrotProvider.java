/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.AlgorithmProvider;

/**
 * Provider interface for Mandelbrot set generation.
 * Enables switching between Multicore (CPU) and potentially GPU/Distributed
 * implementations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MandelbrotProvider extends AlgorithmProvider {

    /**
     * Computes Mandelbrot set iterations using primitive doubles.
     * Suitable for standard zoom levels and high performance.
     *
     * @param xMin          minimum real coordinate
     * @param xMax          maximum real coordinate
     * @param yMin          minimum imaginary coordinate
     * @param yMax          maximum imaginary coordinate
     * @param width         image width
     * @param height        image height
     * @param maxIterations maximum iterations per pixel
     * @return 2D integer array [width][height] containing iteration counts
     */
    int[][] compute(double xMin, double xMax, double yMin, double yMax, int width, int height, int maxIterations);

    /**
     * Computes Mandelbrot set iterations using arbitrary precision Real numbers.
     * Required for deep zooms.
     *
     * @param xMin          minimum real coordinate
     * @param xMax          maximum real coordinate
     * @param yMin          minimum imaginary coordinate
     * @param yMax          maximum imaginary coordinate
     * @param width         image width
     * @param height        image height
     * @param maxIterations maximum iterations per pixel
     * @return 2D integer array [width][height] containing iteration counts
     */
    int[][] computeReal(Real xMin, Real xMax, Real yMin, Real yMax, int width, int height, int maxIterations);

}
