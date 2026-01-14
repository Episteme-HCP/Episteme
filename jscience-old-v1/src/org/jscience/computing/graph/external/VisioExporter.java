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

/* ------------------
 * VisioExporter.java
 * ------------------
 * (C) Copyright 2003, by Avner Linder and Contributors.
 *
 * Original Author:  Avner Linder
 * Contributor(s):   Barak Naveh
 *
 * $Id: VisioExporter.java,v 1.2 2007-10-21 21:07:55 virtualcall Exp $
 *
 * Changes
 * -------
 * 27-May-2004 : Initial Version (AL);
 *
 */
package org.jscience.computing.graph.external;

import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.Graph;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;

/**
 * Exports a graph to a csv format that can be imported into MS Visio.
 * <p/>
 * <p/>
 * <b>Tip:</b> By default, the exported graph doesn't show link directions. To
 * show link directions:<br>
 * <p/>
 * <ol>
 * <li>
 * Select All (Ctrl-A)
 * </li>
 * <li>
 * Right Click the selected items
 * </li>
 * <li>
 * Format/Line...
 * </li>
 * <li>
 * Line ends: End: (choose an arrow)
 * </li>
 * </ol>
 * </p>
 *
 * @author Avner Linder
 */
public class VisioExporter {
    private static final VertexNameProvider DEFAULT_VERTEX_NAME_PROVIDER =
            new VertexNameProvider() {
                public String getVertexName(Object vertex) {
                    return vertex.toString();
                }
            };

    private VertexNameProvider m_vertexNameProvider;

    /**
     * Creates a new VisioExporter object with the specified naming policy.
     *
     * @param vertexNameProvider the vertex name provider to be used for naming
     *                           the Visio shapes.
     */
    public VisioExporter(VertexNameProvider vertexNameProvider) {
        m_vertexNameProvider = vertexNameProvider;
    }

    /**
     * Creates a new VisioExporter object.
     */
    public VisioExporter() {
        this(DEFAULT_VERTEX_NAME_PROVIDER);
    }

    /**
     * Exports the specified graph into a Visio csv file format.
     *
     * @param output the print stream to which the graph to be exported.
     * @param g      the graph to be exported.
     */
    public void export(OutputStream output, Graph g) {
        PrintStream out = new PrintStream(output);

        for (Iterator i = g.vertexSet().iterator(); i.hasNext();) {
            exportVertex(out, i.next());
        }

        for (Iterator i = g.edgeSet().iterator(); i.hasNext();) {
            exportEdge(out, (Edge) i.next());
        }

        out.flush();
    }

    private void exportEdge(PrintStream out, Edge edge) {
        String sourceName =
                m_vertexNameProvider.getVertexName(edge.getSource());
        String targetName =
                m_vertexNameProvider.getVertexName(edge.getTarget());

        out.print("Link,");

        // create unique ShapeId for link
        out.print(sourceName);
        out.print("-->");
        out.print(targetName);

        // MasterName and Text fields left blank
        out.print(",,,");
        out.print(sourceName);
        out.print(",");
        out.print(targetName);
        out.print("\n");
    }

    private void exportVertex(PrintStream out, Object vertex) {
        String name = m_vertexNameProvider.getVertexName(vertex);

        out.print("Shape,");
        out.print(name);
        out.print(",,"); // MasterName field left empty
        out.print(name);
        out.print("\n");
    }

    /**
     * Assigns a display name for each of the graph vertices.
     */
    public interface VertexNameProvider {
        /**
         * Returns the shape name for the vertex as to be appeared in the Visio
         * diagram.
         *
         * @param vertex the vertex
         * @return vertex display name for Visio shape.
         */
        public String getVertexName(Object vertex);
    }
}
