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
 * GraphIterator.java
 * ------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: GraphIterator.java,v 1.3 2007-10-23 18:16:38 virtualcall Exp $
 *
 * Changes
 * -------
 * 31-Jul-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org.jscience.computing.graph.iterators;

import org.jscience.computing.graph.events.TraversalListener;

import java.util.Iterator;


/**
 * A graph iterator.
 *
 * @author Barak Naveh
 * @since Jul 31, 2003
 */
public interface GraphIterator extends Iterator {
    /**
     * Test whether this iterator is set to traverse the grpah across
     * connected components.
     *
     * @return <code>true</code> if traverses across connected components,
     *         otherwise <code>false</code>.
     */
    public boolean isCrossComponentTraversal();

    /**
     * Sets a value the <code>reuseEvents</code> flag. If the
     * <code>reuseEvents</code> flag is set to <code>true</code> this class
     * will reuse previously fired events and will not create a new object for
     * each event. This option increases performance but should be used with
     * care, especially in multithreaded environment.
     *
     * @param reuseEvents whether to reuse previously fired event objects
     *        instead of creating a new event object for each event.
     */
    public void setReuseEvents(boolean reuseEvents);

    /**
     * Tests whether the <code>reuseEvents</code> flag is set. If the
     * flag is set to <code>true</code> this class will reuse previously fired
     * events and will not create a new object for each event. This option
     * increases performance but should be used with care, especially in
     * multithreaded environment.
     *
     * @return the value of the <code>reuseEvents</code> flag.
     */
    public boolean isReuseEvents();

    /**
     * Adds the specified traversal listener to this iterator.
     *
     * @param l the traversal listener to be added.
     */
    public void addTraversalListener(TraversalListener l);

    /**
     * Unsupported.
     */
    public void remove();

    /**
     * Removes the specified traversal listener from this iterator.
     *
     * @param l the traversal listener to be removed.
     */
    public void removeTraversalListener(TraversalListener l);
}
