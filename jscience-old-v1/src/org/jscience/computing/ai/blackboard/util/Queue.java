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
