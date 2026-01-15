package org.jscience.computing.ai.agents;

/**
 * The MobileAgent interface provides a better support for social agents.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface MobileAgent extends Agent {
    /** DOCUMENT ME! */
    public final static int MOVE = 1; //non exclusive

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getPosition();
}
