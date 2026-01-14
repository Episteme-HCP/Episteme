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

/* Rule.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.Executable;
import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Hashtable;
import java.util.Observable;


/**
 * Rule class.  This class implements a contained component of the
 * knowledge source class.  A rule is defined as a conditional element coupled
 * with a sequence of actions.
 *
 * @author:        Paul Brown
 * @version:        1.3, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Rule extends org.jscience.computing.ai.blackboard.Condition
    implements org.jscience.computing.ai.blackboard.util.EnquirableExecutable,
        org.jscience.computing.ai.blackboard.util.Prioritised,
        java.util.Observer {
    /** A reference to the containing knowledge source. */
    private KnowledgeSource ks;

    /** A priority value. */
    private int priority;

    /** A list of sequentially ordered actions. */
    private Executable[] actions;

/**
         * Constructs a new rule with the specified condition and associated
         * actions and assigning that rule the specified priority value.
         * @param blackboard a reference to the blackboard
         * @param ks a reference to the containing knowledge source
         * @param priority a priority value
         * @param disjunctions an initialised conditional structure
         * @param actions a sequence of actions
         */
    public Rule(Hashtable blackboard, KnowledgeSource ks, int priority,
        ValuePair[] disjunctions, Executable[] actions) {
        super(blackboard, disjunctions);
        this.ks = ks;
        this.priority = priority;
        this.actions = actions;
        ks.register(this);
    }

/**
         * Constructs a new rule with the specified condition and associated
         * actions, also providing a default priority value.
         * @param blackboard a reference to the blackboard
         * @param ks a reference to the containing knowledge source
         * @param disjunctions an initialised conditional structure
         * @param actions a sequence of actions
         */
    public Rule(Hashtable blackboard, KnowledgeSource ks,
        ValuePair[] disjunctions, Executable[] actions) {
        this(blackboard, ks, 0, disjunctions, actions);
    }

    /**
     * Executes the rule, this involves the sequential execution of
     * it's contained actions.
     *
     * @param arg variable bindings instantiated by rule condition evaluation
     */
    public void execute(Object arg) {
        boolean quit = false;

        for (int i = 0; ((i < actions.length) && (!quit)); i++) {
            actions[i].execute(arg);
            quit = (actions[i] instanceof Quit);
        }
    }

    /**
     * An accessor method to return the actions of this rule.
     *
     * @return the rule's actions
     */
    public Executable[] actions() {
        return (actions);
    }

    /**
     * An accessor method to return the priority of this rule.
     *
     * @return the rule priority
     */
    public int priority() {
        return (priority);
    }

    /**
     * This method is called by rule actions observed by this rule,
     * this call signals a blackboard manipulation that may affect the
     * evaluation of this rule's condition.  This method invokes a
     * re-evaluation of the rule's condition.
     *
     * @param o DOCUMENT ME!
     * @param arg DOCUMENT ME!
     */
    public void update(Observable o, Object arg) {
        ks.updateAgenda(this);
    }
}
