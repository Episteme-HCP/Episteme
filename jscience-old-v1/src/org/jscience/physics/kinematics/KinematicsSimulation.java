package org.jscience.physics.kinematics;

import org.jscience.Simulation;

import java.util.Set;


/**
 * The KinematicsSimulation interface is the base class for simulation of
 * kinematics systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//a really good and simple introduction: http://www.schlitt.net/xstar/n-body.pdf
//for something closer to real problems, see
//http://www.cs.cmu.edu/afs/cs.cmu.edu/project/scandal/public/papers/dimacs-nbody.html
//as avaluable resource to tackle this problem
public interface KinematicsSimulation extends Simulation {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getInterval();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCurrentTime();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getFields();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getParticles();
}
