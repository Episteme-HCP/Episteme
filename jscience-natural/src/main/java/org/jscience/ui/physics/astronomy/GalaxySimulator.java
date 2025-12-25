package org.jscience.ui.physics.astronomy;

import java.util.List;

/**
 * Interface for Galaxy Physics Simulation.
 * Allows benchmarking Primitive vs Object-based Physics.
 */
public interface GalaxySimulator {

    /**
     * Initialize the simulator with the star field.
     */
    void init(List<GalaxyViewer.StarParticle> stars);

    /**
     * Update the simulation by one step.
     */
    void update(boolean collisionMode, double g2x, double g2y);

    /**
     * Updates the state of the second galaxy core.
     */
    void setGalaxy2State(double x, double y, double vx, double vy);

    String getName();
}
