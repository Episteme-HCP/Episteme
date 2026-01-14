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
 * EdgeListFactory.java
 * ----------------
 * (C) Copyright 2005, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: EdgeListFactory.java,v 1.3 2007-10-23 18:16:34 virtualcall Exp $
 *
 * Changes
 * -------
 * 01-Jun-2005 : Initial revision (JVS);
 *
 */
package org.jscience.computing.graph.graphs;

import java.util.List;


/**
 * A factory for edge lists.  This interface allows the creator of a graph to
 * choose the {@link java.util.List} implementation used internally by the
 * graph to maintain lists of edges.  This provides control over performance
 * tradeoffs between memory and CPU usage.
 *
 * @author John V. Sichi
 * @version $Id: EdgeListFactory.java,v 1.3 2007-10-23 18:16:34 virtualcall Exp $
 */
public interface EdgeListFactory {
    /**
     * Create a new edge list for a particular vertex.
     *
     * @param vertex the vertex for which the edge list is being created;
     *        sophisticated factories may be able to use this information to
     *        choose an optimal list representation (e.g. ArrayList for a
     *        vertex expected to have low degree, and TreeList for a vertex
     *        expected to have high degree)
     *
     * @return new list
     */
    public List createEdgeList(Object vertex);
}
// End EdgeListFactory.java
