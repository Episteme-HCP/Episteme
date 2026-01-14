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

/* ---------
 * Edge.java
 * ---------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: Edge.java,v 1.3 2007-10-23 18:16:25 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 06-Nov-2003 : Change edge sharing semantics (JVS);
 *
 */
package org.jscience.computing.graph;

/**
 * An edge used with graph objects. This is the root interface in the edge
 * hierarchy.
 * <p/>
 * <p/>
 * NOTE: the source and target associations of an Edge must be immutable after
 * construction for all implementations.  The reason is that once an Edge is
 * added to a Graph, the Graph representation may be optimized via internal
 * indexing data structures; if the Edge associations were to change, these
 * structures would be corrupted.  However, other properties of an edge (such
 * as weight or label) may be mutable, although this still requires caution:
 * changes to Edges shared by multiple Graphs may not always be desired, and
 * indexing mechanisms for these properties may require a change notification
 * mechanism.
 * </p>
 *
 * @author Barak Naveh
 * @since Jul 14, 2003
 */
public interface Edge extends Cloneable {
    /** The default weight for an edge. */
    public static double DEFAULT_EDGE_WEIGHT = 1.0;

    /**
     * Returns the source vertex of this edge.
     *
     * @return the source vertex of this edge.
     */
    public Object getSource();

    /**
     * Returns the target vertex of this edge.
     *
     * @return the target vertex of this edge.
     */
    public Object getTarget();

    /**
     * Sets the weight of this edge. If this edge is unweighted an
     * <code>UnsupportedOperationException</code> is thrown.
     *
     * @param weight new weight.
     */
    public void setWeight(double weight);

    /**
     * Returns the weight of this edge. If this edge is unweighted the
     * value <code>1.0</code> is returned.
     *
     * @return the weight of this element.
     */
    public double getWeight();

    /**
     * Creates and returns a shallow copy of this edge. The vertices of
     * this edge are <i>not</i> cloned.
     *
     * @return a shallow copy of this edge.
     *
     * @see Cloneable
     */
    public Object clone();

    /**
     * Returns <tt>true</tt> if this edge contains the specified
     * vertex.  More formally, returns <tt>true</tt> if and only if the
     * following condition holds:<pre>
     *      this.getSource().equals(v) || this.getTarget().equals(v)</pre>
     *
     * @param v vertex whose presence in this edge is to be tested.
     *
     * @return <tt>true</tt> if this edge contains the specified vertex.
     */
    public boolean containsVertex(Object v);

    /**
     * Returns the vertex opposite to the specified vertex.
     *
     * @param v the vertex whose opposite is required.
     *
     * @return the vertex opposite to the specified vertex.
     */
    public Object oppositeVertex(Object v);
}
