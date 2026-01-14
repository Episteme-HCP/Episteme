/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
