package org.jscience.computing.ai.agents;

import java.util.Set;


/**
 * A class representing a simulated space in which objects have free
 * positions.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class ContinuousEnvironment extends Environment {
    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     * @param distance the radius around position
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getContentsAt(double[] position, double distance);

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     * @param contents DOCUMENT ME!
     */
    public abstract void setContentsAt(double[] position, Set contents);
}
