/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */

/* -----------------------
 * UndirectedSubgraph.java
 * -----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: UndirectedSubgraph.java,v 1.3 2007-10-23 18:16:35 virtualcall Exp $
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.UndirectedGraph;

import java.util.Set;


/**
 * An undirected graph that is a subgraph on other graph.
 *
 * @see org.jscience.computing.graph.graphs.Subgraph
 */
public class UndirectedSubgraph extends Subgraph implements UndirectedGraph {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3256728359772631350L;

/**
     * Creates a new undirected subgraph.
     *
     * @param base         the base (backing) graph on which the subgraph will be
     *                     based.
     * @param vertexSubset vertices to include in the subgraph. If
     *                     <code>null</code> then all vertices are included.
     * @param edgeSubset   edges to in include in the subgraph. If
     *                     <code>null</code> then all the edges whose vertices found in the
     *                     graph are included.
     */
    public UndirectedSubgraph(UndirectedGraph base, Set vertexSubset,
        Set edgeSubset) {
        super(base, vertexSubset, edgeSubset);
    }
}
