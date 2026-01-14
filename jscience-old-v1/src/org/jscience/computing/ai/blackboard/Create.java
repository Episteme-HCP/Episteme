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

/* Create.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Hashtable;


/**
 * Create class.  This is a blackboard manipulation rule action class, it
 * is used for creating a new blackboard entry.
 *
 * @author:   Paul Brown
 * @version:  1.3, 04/26/96
 *
 * @see java.util.Observable#addObserver
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Create extends java.util.Observable implements org.jscience.computing.ai.blackboard.util.Executable {
    /** A reference to the blackboard. */
    private Hashtable blackboard;

    /** The level upon which this action will create an entry. */
    private Integer level;

    /** Initial values given to the new entry's attributes. */
    private ValuePair[] constants;

    /** Variable identifiers for referencing bound attribute values. */
    private ValuePair[] variables;

/**
         * Constructs a new create action.
         * @param blackboard a reference to the blackboard
         * @param level the level upon which the new entry is to be created
         * @param constants initialisation attribute values
         * @param variables a list of variable identifiers used to access bound
         * attribute values
         */
    public Create(Hashtable blackboard, Integer level, ValuePair[] constants,
        ValuePair[] variables) {
        this.blackboard = blackboard;
        this.level = level;
        this.constants = constants;
        this.variables = variables;
    }

    /**
     * An accessor function for returning the level accessed by this
     * action.
     *
     * @return DOCUMENT ME!
     */
    public Integer level() {
        return (level);
    }

    /**
     * Creates the specified new entry, possibly utilising variable
     * binding values, using variable identifiers.
     *
     * @param arg a hashtable of variable bindings, referenced by variable
     *        identifiers
     */
    public void execute(Object arg) {
        Hashtable bindings = (Hashtable) arg;
        ValuePair[] initial_values;

        if (constants != null) {
            if (variables != null) {
                initial_values = new ValuePair[constants.length +
                    variables.length];
                System.arraycopy(constants, 0, initial_values,
                    variables.length, constants.length);

                for (int i = 0; i < variables.length; i++)
                    initial_values[i] = new ValuePair(variables[i].key(),
                            bindings.get(variables[i].data()));
            } else {
                initial_values = new ValuePair[constants.length];
                System.arraycopy(constants, 0, initial_values, 0,
                    constants.length);
            }
        } else if (variables != null) {
            initial_values = new ValuePair[variables.length];

            for (int i = 0; i < variables.length; i++)
                initial_values[i] = new ValuePair(variables[i].key(),
                        bindings.get(variables[i].data()));
        } else {
            initial_values = null;
        }

        ((BlackboardLevel) blackboard.get(level)).put(initial_values);
        setChanged();
        notifyObservers();
    }
}
