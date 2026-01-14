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

/* -------------------------
 * DirectedWeightedEdge.java
 * -------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: DirectedWeightedEdge.java,v 1.3 2007-10-23 18:16:32 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 10-Aug-2003 : General edge refactoring (BN);
 *
 */
package org.jscience.computing.graph.edges;

/**
 * An implementation of directed weighted edge.
 *
 * @author Barak Naveh
 *
 * @since Jul 14, 2003
 */
public class DirectedWeightedEdge extends DirectedEdge {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3689070664137257523L;

    /** DOCUMENT ME! */
    private double m_weight = DEFAULT_EDGE_WEIGHT;

/**
     * @see DirectedEdge#DirectedEdge(Object,Object)
     */
    public DirectedWeightedEdge(Object sourceVertex, Object targetVertex) {
        super(sourceVertex, targetVertex);
    }

/**
     * Constructor for DirectedWeightedEdge.
     *
     * @param sourceVertex source vertex of the new edge.
     * @param targetVertex target vertex of the new edge.
     * @param weight       the weight of the new edge.
     */
    public DirectedWeightedEdge(Object sourceVertex, Object targetVertex,
        double weight) {
        super(sourceVertex, targetVertex);
        m_weight = weight;
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#setWeight(double)
     */
    public void setWeight(double weight) {
        m_weight = weight;
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#getWeight()
     */
    public double getWeight() {
        return m_weight;
    }
}
