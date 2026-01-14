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

/* -----------------------------
 * TraversalListenerAdapter.java
 * -----------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: TraversalListenerAdapter.java,v 1.3 2007-10-23 18:16:33 virtualcall Exp $
 *
 * Changes
 * -------
 * 06-Aug-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org.jscience.computing.graph.events;

/**
 * An empty do-nothing implementation of the {@link TraversalListener}
 * interface used for subclasses.
 *
 * @author Barak Naveh
 *
 * @since Aug 6, 2003
 */
public class TraversalListenerAdapter implements TraversalListener {
    /**
     * 
     * @see TraversalListener#connectedComponentFinished(ConnectedComponentTraversalEvent)
     */
    public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
    }

    /**
     * 
     * @see TraversalListener#connectedComponentStarted(ConnectedComponentTraversalEvent)
     */
    public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
    }

    /**
     * 
     * @see TraversalListener#edgeTraversed(EdgeTraversalEvent)
     */
    public void edgeTraversed(EdgeTraversalEvent e) {
    }

    /**
     * 
     * @see TraversalListener#vertexTraversed(VertexTraversalEvent)
     */
    public void vertexTraversed(VertexTraversalEvent e) {
    }
}
