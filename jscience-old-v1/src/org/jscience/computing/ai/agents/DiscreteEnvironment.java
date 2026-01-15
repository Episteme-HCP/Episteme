package org.jscience.computing.ai.agents;

import java.util.Set;


/**
 * A class representing a simulated space in which objects position are
 * constrained to a finite number of values, usually on a "grid".
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//see http://en.wikipedia.org/wiki/Tessellation_of_space
//and http://en.wikipedia.org/wiki/List_of_uniform_planar_tilings for some possible additional implementations

//todo provide some implementations for triangular and hexagonal cells

//There is no platonic solid for the sphere above 20 faces and we can't therefore produce any regular tesselation of the sphere
//We can't neither provide a tesselation for a full ball.
//yet we should provide a class to do that the best we can, this would probably be very useful for astrophysics and climatology simulations.
public abstract class DiscreteEnvironment extends Environment {
    /** DOCUMENT ME! */
    public final static int MOORE_NEIGHBORHOOD = 1;

    /** DOCUMENT ME! */
    public final static int VON_NEUMANN_NEIGHBORHOOD = 2;

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getContentsAt(int[] position);

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     * @param contents DOCUMENT ME!
     */
    public abstract void setContentsAt(int[] position, Set contents);

    /**
     * Returns the available Cell neighbors.
     *
     * @param position DOCUMENT ME!
     * @param method DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getNeighbors(int[] position, int method);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getDimension();
}
