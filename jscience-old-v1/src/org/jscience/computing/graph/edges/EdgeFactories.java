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
 * EdgeFactories.java
 * ------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: EdgeFactories.java,v 1.2 2007-10-21 21:07:53 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 04-Aug-2003 : Renamed from EdgeFactoryFactory & made utility class (BN);
 * 03-Nov-2003 : Made edge factories serializable (BN);
 *
 */
package org.jscience.computing.graph.edges;

import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.EdgeFactory;

import java.io.Serializable;

/**
 * This utility class is a container of various {@link
 * org.jscience.computing.graph.EdgeFactory} classes.
 * <p/>
 * <p/>
 * Classes included here do not have substantial logic. They are grouped
 * together in this container in order to avoid clutter.
 * </p>
 *
 * @author Barak Naveh
 * @since Jul 16, 2003
 */
public final class EdgeFactories {
    private EdgeFactories() {
    } // ensure non-instantiability.

    /**
     * An EdgeFactory for producing directed edges.
     *
     * @author Barak Naveh
     * @since Jul 14, 2003
     */
    public static class DirectedEdgeFactory extends AbstractEdgeFactory {
        private static final long serialVersionUID = 3618135658586388792L;

        /**
         * @see EdgeFactory#createEdge(Object,Object)
         */
        public Edge createEdge(Object source, Object target) {
            return new DirectedEdge(source, target);
        }
    }

    /**
     * An EdgeFactory for producing directed edges with weights.
     *
     * @author Barak Naveh
     * @since Jul 14, 2003
     */
    public static class DirectedWeightedEdgeFactory extends AbstractEdgeFactory {
        private static final long serialVersionUID = 3257002163870775604L;

        /**
         * @see EdgeFactory#createEdge(Object,Object)
         */
        public Edge createEdge(Object source, Object target) {
            return new DirectedWeightedEdge(source, target);
        }
    }

    /**
     * An EdgeFactory for producing undirected edges.
     *
     * @author Barak Naveh
     * @since Jul 14, 2003
     */
    public static class UndirectedEdgeFactory extends AbstractEdgeFactory {
        private static final long serialVersionUID = 3257007674431189815L;

        /**
         * @see EdgeFactory#createEdge(Object,Object)
         */
        public Edge createEdge(Object source, Object target) {
            return new UndirectedEdge(source, target);
        }
    }

    /**
     * An EdgeFactory for producing undirected edges with weights.
     *
     * @author Barak Naveh
     * @since Jul 14, 2003
     */
    public static class UndirectedWeightedEdgeFactory
            extends AbstractEdgeFactory {
        private static final long serialVersionUID = 4048797883346269237L;

        /**
         * @see EdgeFactory#createEdge(Object,Object)
         */
        public Edge createEdge(Object source, Object target) {
            return new UndirectedWeightedEdge(source, target);
        }
    }

    /**
     * A base class for edge factories.
     *
     * @author Barak Naveh
     * @since Nov 3, 2003
     */
    abstract static class AbstractEdgeFactory implements EdgeFactory,
            Serializable {
    }
}
