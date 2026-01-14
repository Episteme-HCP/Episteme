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

/* Modify.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Hashtable;


/**
 * Modify class.  This is a blackboard entry manipulation rule action
 * class, an instantiation of this class represents a blackboard entry
 * modification.
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
public class Modify extends java.util.Observable implements org.jscience.computing.ai.blackboard.util.Executable {
    /** A reference to the blackboard. */
    private Hashtable blackboard;

    /** The level on which this action modifies entries. */
    private Integer level;

    /** A variable identifier representing the entry to be modified. */
    private Integer entry;

    /**
     * This variable contains a list of constant attribute value
     * modifications.
     */
    private ValuePair[] constants;

    /**
     * This list contains variable references representing attribute
     * value modifications.
     */
    private ValuePair[] variables;

/**
         * Constructs an entry modification action.
         * @param blackboard a reference to the blackboard
         * @param level the blackboard level upon which this action operates
         * @param entry a variable identifier used for obtaining an entry
         * identifier
         * @param constants a list of constant attribute value modifications
         * @param variables a list of variable attribute value modifications
         */
    public Modify(Hashtable blackboard, Integer level, Integer entry,
        ValuePair[] constants, ValuePair[] variables) {
        this.blackboard = blackboard;
        this.level = level;
        this.entry = entry;
        this.constants = constants;
        this.variables = variables;
    }

    /**
     * An accessor method to return the level that this action operates
     * on.
     *
     * @return the level identifier
     */
    public Integer level() {
        return (level);
    }

    /**
     * Executes this action, utilising the supplied variable bindings
     * as required.
     *
     * @param arg variable bindings from rule activation
     */
    public void execute(Object arg) {
        Hashtable bindings = (Hashtable) arg;
        ValuePair[] modifications;

        if (constants != null) {
            if (variables != null) {
                modifications = new ValuePair[constants.length +
                    variables.length];
                System.arraycopy(constants, 0, modifications, variables.length,
                    constants.length);

                for (int i = 0; i < variables.length; i++)
                    modifications[i] = new ValuePair(variables[i].key(),
                            bindings.get(variables[i].data()));
            } else {
                modifications = new ValuePair[constants.length];
                System.arraycopy(constants, 0, modifications, 0,
                    constants.length);
            }
        } else if (variables != null) {
            modifications = new ValuePair[variables.length];

            for (int i = 0; i < variables.length; i++)
                modifications[i] = new ValuePair(variables[i].key(),
                        bindings.get(variables[i].data()));
        } else {
            modifications = null;
        }

        ((BlackboardLevel) blackboard.get(level)).modify((Integer) bindings.get(
                entry), modifications);
        setChanged();
        notifyObservers();
    }
}
