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

package org.jscience.computing.ai;

/**
 * A simple interface to allow pathfinders like the A algorithm to navigate
 * through the environment.
 *
 * @author James Matthews
 */
public interface Navigable {
    //    public double getCost(int x, int y);
    /**
     * Determines whether the given node is valid.
     *
     * @param node the node.
     *
     * @return the validity of the node.
     */
    public boolean isValid(Pathfinder.Node node);

    /**
     * Return the cost to travel from node 1 to node 2.
     *
     * @param n1 the first node.
     * @param n2 the second node.
     *
     * @return the cost required to travel.
     */
    public double getCost(Pathfinder.Node n1, Pathfinder.Node n2);

    /**
     * Return the distance between the node 1 and node 2. Note that
     * "distance" is not always in terms of Manhattan or Eucledian distances.
     *
     * @param n1 the first node.
     * @param n2 the second node.
     *
     * @return the distance between the two nodes.
     */
    public double getDistance(Pathfinder.Node n1, Pathfinder.Node n2);

    /**
     * Generate a unique ID for a given node. Note that the ID must be
     * tied to its properties, such as positional information. Nodes with the
     * same information should be assigned the same ID.
     *
     * @param node the node.
     *
     * @return the node's ID.
     */
    public int createNodeID(Pathfinder.Node node);
}
