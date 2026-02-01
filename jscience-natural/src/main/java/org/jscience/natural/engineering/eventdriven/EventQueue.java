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

package org.jscience.natural.engineering.eventdriven;

import java.util.LinkedList;

/**
 * A synchronized priority queue for events.
 * Handles high-priority internal events and normal events.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EventQueue {
    
    private final LinkedList<Event> internalQueue = new LinkedList<>();
    private final LinkedList<Event> normalQueue = new LinkedList<>();
    
    /**
     * Dequeues the next event, blocking if empty.
     * Internal events take precedence.
     */
    public synchronized Event dequeue() throws InterruptedException {
        while (internalQueue.isEmpty() && normalQueue.isEmpty()) {
            wait();
        }
        
        if (!internalQueue.isEmpty()) {
            return internalQueue.removeFirst();
        } else {
            return normalQueue.removeFirst();
        }
    }
    
    public synchronized void queueNormal(Event event) {
        normalQueue.addLast(event);
        notifyAll();
    }
    
    public synchronized void queueInternal(Event event) {
        internalQueue.addLast(event);
        notifyAll();
    }
    
    public synchronized boolean isEmpty() {
        return internalQueue.isEmpty() && normalQueue.isEmpty();
    }
}
