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

/* -------------------
 * GraphGenerator.java
 * -------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: GraphGenerator.java,v 1.3 2007-10-23 18:16:34 virtualcall Exp $
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (JVS);
 *
 */
package org.jscience.computing.graph.generators;

import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.VertexFactory;

import java.util.Map;


/**
 * GraphGenerator defines an interface for generating new graph structures.
 *
 * @author John V. Sichi
 * @since Sep 16, 2003
 */
public interface GraphGenerator {
    /**
     * Generate a graph structure. The topology of the generated graph
     * is dependent on the implementation.  For graphs in which not all
     * vertices share the same automorphism equivalence class, the generator
     * may produce a labeling indicating the roles played by generated
     * elements. This is the purpose of the resultMap parameter.  For example,
     * a generator for a wheel graph would designate a hub vertex.  Role names
     * used as keys in resultMap should be declared as public static final
     * Strings by implementation classes.
     *
     * @param target receives the generated edges and vertices; if this is
     *        non-empty on entry, the result will be a disconnected graph
     *        since generated elements will not be connected to existing
     *        elements
     * @param vertexFactory called to produce new vertices
     * @param resultMap if non-null, receives implementation-specific mappings
     *        from String roles to graph elements (or collections of graph
     *        elements)
     */
    public void generateGraph(Graph target, VertexFactory vertexFactory,
        Map resultMap);
}
