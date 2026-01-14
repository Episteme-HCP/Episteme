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

package org.jscience.physics.classical.waves.electromagnetism.circuit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node (connection point) in an electrical circuit.
 * Nodes are the points where circuit elements connect.
 * Each node is assigned a voltage during simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CircuitNode {

    /** X coordinate (for topological purposes, not physics) */
    public int x;

    /** Y coordinate (for topological purposes, not physics) */
    public int y;

    /** Whether this is an internal node (not an external terminal) */
    public boolean internal;

    /** Links to circuit elements connected to this node */
    public final List<CircuitNodeLink> links = new ArrayList<>();

    /**
     * Creates a new circuit node at the origin.
     */
    public CircuitNode() {
        this(0, 0);
    }

    /**
     * Creates a new circuit node at the specified coordinates.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     */
    public CircuitNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.internal = false;
    }
}


