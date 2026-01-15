package org.jscience.physics.kinematics.force;

/**
 * Represents a point particle in a force simulation, maintaining values for
 * mass, forces, velocity, and position.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public abstract class ForceItem {
    
    /** The mass value of this ForceItem. */
    public float   mass;
    /** The values of the forces acting on this ForceItem. */
    public float[] force;
    /** The velocity values of this ForceItem. */
    public float[] velocity;
    /** The location values of this ForceItem. */
    public float[] location;
    /** The previous location values of this ForceItem. */
    public float[] plocation;
    /** Temporary variables for Runge-Kutta integration */
    public float[][] k;
    /** Temporary variables for Runge-Kutta integration */
    public float[][] l;

} // end of class ForceItem
