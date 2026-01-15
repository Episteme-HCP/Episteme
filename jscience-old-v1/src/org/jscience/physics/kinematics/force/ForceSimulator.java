package org.jscience.physics.kinematics.force;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manages a simulation of physical forces acting on bodies. To create a
 * custom ForceSimulator, add the desired {@link Force} functions and choose an
 * appropriate {@link ForceIntegrator}.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public interface ForceSimulator {

    /**
     * Get the speed limit, or maximum velocity value allowed by this
     * simulator.
     * @return the "speed limit" maximum velocity value
     */
    public float getSpeedLimit();
    
    /**
     * Set the speed limit, or maximum velocity value allowed by this
     * simulator.
     * @param limit the "speed limit" maximum velocity value to use
     */
    public void setSpeedLimit(float limit);
    
    /**
     * Get the Integrator used by this simulator.
     * @return the Integrator
     */
    public ForceIntegrator getIntegrator();
    
    /**
     * Set the Integrator used by this simulator.
     * @param intgr the Integrator to use
     */
    public void setIntegrator(ForceIntegrator intgr);
    
    /**
     * Clear this simulator, removing all ForceItem and Spring instances
     * for the simulator.
     */
    public void clear();
    
    /**
     * Add a new Force function to the simulator.
     * @param f the Force function to add
     */
    public void addForce(Force f);
    
    /**
     * Get an array of all the Force functions used in this simulator.
     * @return an array of Force functions
     */
    public Force[] getForces();

    /**
     * Get an iterator over all registered ForceItems.
     * @return an iterator over the ForceItems.
     */
    public Iterator getItems();
    
    /**
     * Get an iterator over all registered Springs.
     * @return an iterator over the Springs.
     */
    public Iterator getSprings() ;
    
    /**
     * Run the simulator for one timestep.
     * @param timestep the span of the timestep for which to run the simulator
     */
    public void runSimulator(long timestep);
    
    /**
     * Accumulate all forces acting on the items in this simulation
     */
    public void accumulate();
    
} // end of class ForceSimulator
