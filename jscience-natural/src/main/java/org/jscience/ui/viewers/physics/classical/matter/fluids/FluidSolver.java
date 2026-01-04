/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.ui.viewers.physics.classical.matter.fluids;

/**
 * Interface for Fluid Dynamics Solvers.
 * Allows comparison between different implementation strategies (e.g. Primitive
 * vs Object).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FluidSolver {

    /**
     * Initializes the solver grid.
     * 
     * @param size  Grid size (N)
     * @param scale Grid scale
     */
    void initialize(int size, int scale);

    /**
     * Solves one time step.
     * 
     * @param speed     Simulation speed factor
     * @param viscosity Fluid viscosity
     * @param zOff      Noise offset for generation
     */
    void step(double speed, double viscosity, double zOff);

    /**
     * Gets the flow vector at a precise coordinate.
     * 
     * @param x X coordinate (pixels)
     * @param y Y coordinate (pixels)
     * @return double[2] {vx, vy}
     */
    double[] getFlowAt(double x, double y);

    /**
     * Adds an external force (e.g. mouse interaction).
     * 
     * @param x  X coordinate
     * @param y  Y coordinate
     * @param dx Force X component
     * @param dy Force Y component
     */
    void addForce(double x, double y, double dx, double dy);

    /**
     * Returns the name of the solver implementation.
     */
    String getName();
}


