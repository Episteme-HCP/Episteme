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
