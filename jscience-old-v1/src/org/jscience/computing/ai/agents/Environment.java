package org.jscience.computing.ai.agents;

import java.util.Set;


/**
 * A class representing a simulated space.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Environment extends Object {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getContents();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isCurvedOnItself();
}
