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

/* ----------------
 * DefaultEdge.java
 * ----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: DefaultEdge.java,v 1.3 2007-10-23 18:16:32 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 10-Aug-2003 : General edge refactoring (BN);
 *
 */
package org.jscience.computing.graph.edges;

import org.jscience.computing.graph.Edge;

import java.io.Serializable;


/**
 * A skeletal implementation of the <tt>Edge</tt> interface, to minimize
 * the effort required to implement the interface.
 *
 * @author Barak Naveh
 *
 * @since Jul 14, 2003
 */
public class DefaultEdge implements Edge, Cloneable, Serializable {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3258408452177932855L;

    /** DOCUMENT ME! */
    private Object m_source;

    /** DOCUMENT ME! */
    private Object m_target;

/**
     * Constructor for DefaultEdge.
     *
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     */
    public DefaultEdge(Object sourceVertex, Object targetVertex) {
        m_source = sourceVertex;
        m_target = targetVertex;
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#getSource()
     */
    public Object getSource() {
        return m_source;
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#getTarget()
     */
    public Object getTarget() {
        return m_target;
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#setWeight(double)
     */
    public void setWeight(double weight) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#getWeight()
     */
    public double getWeight() {
        return DEFAULT_EDGE_WEIGHT;
    }

    /**
     * 
     * @see Edge#clone()
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // shouldn't happen as we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#containsVertex(java.lang.Object)
     */
    public boolean containsVertex(Object v) {
        return m_source.equals(v) || m_target.equals(v);
    }

    /**
     * 
     * @see org.jscience.computing.graph.Edge#oppositeVertex(java.lang.Object)
     */
    public Object oppositeVertex(Object v) {
        if (v.equals(m_source)) {
            return m_target;
        } else if (v.equals(m_target)) {
            return m_source;
        } else {
            throw new IllegalArgumentException("no such vertex");
        }
    }
}
