/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.fem;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Represents a node in the finite element mesh.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Node {

    private final int id;
    private final Vector<Real> coordinates;
    private int globalIndex = -1; // For DOF mapping

    /**
     * Creates a new node.
     * 
     * @param id          the unique identifier of the node
     * @param coordinates the coordinates of the node
     */
    public Node(int id, Vector<Real> coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }

    /**
     * Returns the unique identifier of the node.
     * 
     * @return the node ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the coordinates of the node.
     * 
     * @return the coordinates
     */
    public Vector<Real> getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the global index of the node in the system matrix.
     * 
     * @return the global index
     */
    public int getGlobalIndex() {
        return globalIndex;
    }

    /**
     * Sets the global index of the node.
     * 
     * @param globalIndex the new global index
     */
    public void setGlobalIndex(int globalIndex) {
        this.globalIndex = globalIndex;
    }

    @Override
    public String toString() {
        return "Node[" + id + "] " + coordinates;
    }
}