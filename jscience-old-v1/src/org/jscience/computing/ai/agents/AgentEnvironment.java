package org.jscience.computing.ai.agents;

import java.util.Set;


/**
 * The Environment interface is to be used in simulations with agents.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface AgentEnvironment {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension(); //usually 1, 2 or 3

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBounded(); //if has limits

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCurved(); //if opposite sides are joined together back

    /**
     * DOCUMENT ME!
     */
    public void initialize(); //or seed

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getContents(); //everything it contains (whether agents or landscape elements)
}
