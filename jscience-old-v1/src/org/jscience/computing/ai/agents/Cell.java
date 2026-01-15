package org.jscience.computing.ai.agents;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an abstract cell in a simulated space.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//for cells and environments, see "Geodesic Discrete Global Grid Systems" : http://www.sou.edu/cs/sahr/dgg/pubs/gdggs03.pdf
public class Cell extends Object {
    /** DOCUMENT ME! */
    public final static int TRIANGULAR = 1;

    /** DOCUMENT ME! */
    public final static int SQUARE = 2;

    /** DOCUMENT ME! */
    public final static int HEXAGONAL = 3;

    /** DOCUMENT ME! */
    public final static int CUBIC = 11;

    /** DOCUMENT ME! */
    private DiscreteEnvironment environment;

    /** DOCUMENT ME! */
    private int[] position;

    /** A set of agents */
    private Set contents;

/**
     * Creates a new Cell object.
     *
     * @param environment DOCUMENT ME!
     * @param position    DOCUMENT ME!
     */
    protected Cell(DiscreteEnvironment environment, int[] position) {
        this.environment = environment;
        this.position = position;
        this.contents = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DiscreteEnvironment getEnvironment() {
        return environment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getPosition() {
        return position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getContents() {
        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param contents DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setContents(Set contents) {
        boolean good;
        good = true;

        Iterator i = contents.iterator();

        while (i.hasNext() && good) {
            good = i.next() instanceof Agent;
        }

        if (good) {
            this.contents = contents;
        } else {
            throw new IllegalArgumentException(
                "Contents should contain only Agents.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param method DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getNeighborCells(int method) {
        return environment.getNeighbors(this.getPosition(), method);
    }
}
