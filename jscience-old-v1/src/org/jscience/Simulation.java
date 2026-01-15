package org.jscience;

/**
 * The Simulation interface is the base class for simulation systems.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also see org.jscience.util.Steppable
//or could be renamed Simulable
public interface Simulation {
    /**
     * DOCUMENT ME!
     */
    public void initialize();

    /**
     * DOCUMENT ME!
     */
    public void start();

    /**
     * DOCUMENT ME!
     */
    public void restart();

    //you must also provide a mean to step in the behavior
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRunning();

    /**
     * DOCUMENT ME!
     */
    public void stop();
}
