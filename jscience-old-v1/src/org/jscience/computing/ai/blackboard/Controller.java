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

/* Controller.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.Executable;
import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Controller class.  This represents the system controller component of
 * the blackboard architecture.  Instantiations of this class are responsible
 * for the execution of the blackboard system.
 *
 * @author:        Paul Brown
 * @version:        1.4, 04/26/96
 *
 * @see org.jscience.computing.ai.blackboard.util.PriorityQueue
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Controller extends org.jscience.computing.ai.blackboard.util.PriorityQueue {
    /** A reference to the blackboard. */
    private Hashtable blackboard;

    /** These entries are used by the controller to initiate execution. */
    private ValuePair[] initial_entries;

/**
         * Constructs a controller with the specified initial entries.
         * @param blackboard a reference to the blackboard
         * @param initial_entries blackboard entries for initiating execution
         */
    public Controller(Hashtable blackboard, ValuePair[] initial_entries) {
        this.blackboard = blackboard;
        this.initial_entries = initial_entries;
    }

    /**
     * Executes the blackboard system, this is the method called by the
     * BlackboardSystem class.
     *
     * @see org.jscience.computing.ai.blackboard.BlackboardSystem
     */
    public void execute() {
        BlackboardLevel level;
        ValuePair[] values;
        Enumeration levels;

        // put initial entries onto blackboard
        for (int i = 0; i < initial_entries.length; i++) {
            level = (BlackboardLevel) blackboard.get(initial_entries[i].key());
            values = (ValuePair[]) initial_entries[i].data();
            level.put(values);
        }

        // main loop
        while (!empty())
            ((Executable) get()).execute(null);

        // clear blackboard
        levels = blackboard.elements();

        while (levels.hasMoreElements())
            ((BlackboardLevel) levels.nextElement()).clear();
    }
}
