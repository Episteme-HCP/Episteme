package org.jscience.physics.kinematics.force;

/**
 * Interface for numerical integration routines. These routines are used
 * to update the position and velocity of items in response to forces
 * over a given time step.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public interface ForceIntegrator {

    public void integrate(ForceSimulator sim, long timestep);
    
} // end of interface Integrator
