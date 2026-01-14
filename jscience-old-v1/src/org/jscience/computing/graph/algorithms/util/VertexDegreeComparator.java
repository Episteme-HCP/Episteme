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

/* ---------------------------
 * VertexDegreeComparator.java
 * ---------------------------
 * (C) Copyright 2003, by Linda Buisman and Contributors.
 *
 * Original Author:  Linda Buisman
 * Contributor(s):   -
 *
 * $Id: VertexDegreeComparator.java,v 1.2 2007-10-21 21:07:52 virtualcall Exp $
 *
 * Changes
 * -------
 * 06-Nov-2003 : Initial revision (LB);
 *
 */
package org.jscience.computing.graph.algorithms.util;

import org.jscience.computing.graph.UndirectedGraph;

/**
 * Compares two vertices based on their degree.
 * <p/>
 * <p/>
 * Used by greedy algorithms that need to sort vertices by their degree. Two
 * vertices are considered equal if their degrees are equal.
 * </p>
 *
 * @author Linda Buisman
 * @since Nov 6, 2003
 */
public class VertexDegreeComparator implements java.util.Comparator {
    /**
     * The graph that contains the vertices to be compared.
     */
    private UndirectedGraph m_graph;

    /**
     * The sort order for vertex degree. <code>true</code>for ascending degree
     * order (smaller degrees first), <code>false</code> for descending.
     */
    private boolean m_ascendingOrder;

    /**
     * Creates a comparator for comparing the degrees of vertices in the
     * specified graph. The comparator compares in ascending order of degrees
     * (lowest first).
     *
     * @param g graph with respect to which the degree is calculated.
     */
    public VertexDegreeComparator(UndirectedGraph g) {
        this(g, true);
    }

    /**
     * Creates a comparator for comparing the degrees of vertices in the
     * specified graph.
     *
     * @param g              graph with respect to which the degree is calculated.
     * @param ascendingOrder true - compares in ascending order of degrees
     *                       (lowest first), false - compares in descending order of degrees
     *                       (highest first).
     */
    public VertexDegreeComparator(UndirectedGraph g, boolean ascendingOrder) {
        m_graph = g;
        m_ascendingOrder = ascendingOrder;
    }

    /**
     * Compare the degrees of <code>v1</code> and <code>v2</code>, taking into
     * account whether ascending or descending order is used.
     *
     * @param v1 the first vertex to be compared.
     * @param v2 the second vertex to be compared.
     * @return -1 if <code>v1</code> comes before <code>v2</code>,  +1 if
     *         <code>v1</code> comes after <code>v2</code>, 0 if equal.
     */
    public int compare(Object v1, Object v2) {
        int degree1 = m_graph.degreeOf(v1);
        int degree2 = m_graph.degreeOf(v2);

        if ((degree1 < degree2 && m_ascendingOrder)
                || (degree1 > degree2 && !m_ascendingOrder)) {
            return -1;
        } else if ((degree1 > degree2 && m_ascendingOrder)
                || (degree1 < degree2 && !m_ascendingOrder)) {
            return 1;
        } else {
            return 0;
        }
    }
}
