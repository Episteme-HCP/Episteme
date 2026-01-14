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

/* PriorityQueue.java */
package org.jscience.computing.ai.blackboard.util;

/**
 * PriorityQueue class.  A multi-level priority queue of First-In-First-Out
 * (FIFO) queues of objects.  New priority levels are automatically created on
 * their initial specification.
 *
 * @author:        Paul Brown
 * @version:        1.3, 04/26/96
 *
 * @see java.util.Vector#size
 * @see java.util.Vector#toString
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class PriorityQueue extends java.util.Vector {
    /**
     * This variable indicates the highest priority non-empty level in
     * the queue.
     */
    private int top_priority;

/**
         * Constructs a new empty priority queue.
         */
    public PriorityQueue() {
        top_priority = 0;
    }

    /**
     * Retrieves the highest priority element in the queue.
     *
     * @return the retrieved element
     */
    public Object get() {
        PQueueNode node = (PQueueNode) elementAt(top_priority);
        Object item = node.queue().get();

        if (node.queue().empty()) {
            do
                top_priority++;
            while ((top_priority < size()) &&
                    (((PQueueNode) elementAt(top_priority)).queue().empty()));
        }

        return (item);
    }

    /**
     * Inserts a new element with the specified priority.
     *
     * @param item the element to insert
     * @param priority the element's priority
     */
    public void put(Object item, int priority) {
        int level = findLevel(priority);

        if ((level == size()) ||
                (((PQueueNode) elementAt(level)).priority() != priority)) {
            insertElementAt(new PQueueNode(priority), ++level);
        }

        ((PQueueNode) elementAt(level)).queue().put(item);

        if (top_priority > level) {
            top_priority = level;
        }
    }

    /**
     * Inserts the specified element into the queue, this method does
     * not permit duplicate elements.
     *
     * @param item the element to insert
     * @param priority the element's priority
     */
    public void putOnce(Object item, int priority) {
        int level = findLevel(priority);
        Queue queue;

        if ((level == size()) ||
                (((PQueueNode) elementAt(level)).priority() != priority)) {
            insertElementAt(new PQueueNode(priority), level);
            ((PQueueNode) elementAt(level)).queue().put(item);
        } else {
            queue = ((PQueueNode) elementAt(level)).queue();

            if (!queue.contains(item)) {
                queue.put(item);
            }
        }

        if (top_priority > level) {
            top_priority = level;
        }
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise
     */
    public boolean empty() {
        return (top_priority == size());
    }

    /**
     * Removes the specified element from the queue.
     *
     * @param item the element to remove
     * @param priority the element's priority
     *
     * @return true if the element was found, false otherwise
     */
    public boolean remove(Object item, int priority) {
        int level = findLevel(priority);
        PQueueNode node;
        boolean result = false;

        if (level < size()) {
            node = (PQueueNode) elementAt(level);

            if (node.priority() == priority) {
                result = node.queue().removeElement(item);
            }
        }

        if ((result == true) && (top_priority == level)) {
            while ((top_priority < size()) &&
                    (((PQueueNode) elementAt(top_priority)).queue().empty()))
                top_priority++;
        }

        return (result);
    }

    /**
     * Removes all elements from the queue.
     */
    public void clear() {
        for (int i = 0; i < size(); i++)
            ((PQueueNode) elementAt(i)).queue().removeAllElements();

        top_priority = size();
    }

    /**
     * Utility method used by other methods to locate a particular
     * priority level.
     *
     * @param priority DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int findLevel(int priority) {
        int level = 0;

        if (size() != 0) {
            if (top_priority != size()) {
                level = top_priority;
            }

            if (((PQueueNode) elementAt(level)).priority() < priority) {
                while ((level > 0) &&
                        (((PQueueNode) elementAt(level)).priority() < priority))
                    level--;
            } else {
                while ((level < size()) &&
                        (((PQueueNode) elementAt(level)).priority() > priority))
                    level++;
            }
        }

        return (level);
    }
}


/**
 * This class is used by the PriorityQueue class and is invisible to all
 * others.
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
final class PQueueNode {
    /** This variable refers to the node's priority. */
    private int priority;

    /**
     * A queue is held by every node; this represents a level of the
     * multi-level queue.
     */
    private Queue queue;

/**
         * Constructs a new node with the specified priority.
         */
    public PQueueNode(int priority_value) {
        priority = priority_value;
        queue = new Queue();
    }

    /**
     * Accessor method for querying the node's priority.
     *
     * @return DOCUMENT ME!
     */
    public int priority() {
        return (priority);
    }

    /**
     * Accessor method to return the node's associated queue.
     *
     * @return DOCUMENT ME!
     */
    public Queue queue() {
        return (queue);
    }

    /**
     * Returns a String representation of an instance of this class.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n\t[");
        buffer.append(Integer.toString(priority));
        buffer.append(" : ");
        buffer.append(queue.toString());
        buffer.append("]");

        return (buffer.toString());
    }
}
