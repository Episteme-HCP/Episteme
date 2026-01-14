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

/* Queue.java */
package org.jscience.computing.ai.blackboard.util;

/**
 * Queue class.  A First-In-First-Out (FIFO) queue of objects.
 *
 * @author:        Paul Brown
 * @version:        1.2, 04/26/96
 *
 * @see java.util.Vector#removeAllElements
 * @see java.util.Vector#toString
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Queue extends java.util.Vector {
    /**
     * Adds an element to the end of the queue.
     *
     * @param item the element to be added
     *
     * @return the added element
     */
    public Object put(Object item) {
        addElement(item);

        return (item);
    }

    /**
     * Retrieves the element at the head of the queue.
     *
     * @return the retrieved element
     */
    public Object get() {
        Object item = firstElement();
        removeElementAt(0);

        return (item);
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise
     */
    public boolean empty() {
        return (size() == 0);
    }
}
