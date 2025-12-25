package org.jscience.ui.physics.fluids;

/**
 * Interface for Fluid Dynamics Solvers.
 * Allows comparison between different implementation strategies (e.g. Primitive
 * vs Object).
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
