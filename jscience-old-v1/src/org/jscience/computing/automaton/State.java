/*
 * org.jscience.computing.automaton
 * Copyright (C) 2001-2005 Anders Moeller
 * All rights reserved.
 */
package org.jscience.computing.automaton;

import java.io.Serializable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * <tt>Automaton</tt> state.
 *
 * @author Anders M&oslash;ller &lt;<a
 *         href="mailto:amoeller@brics.dk">amoeller@brics.dk</a>&gt;
 */
public class State implements Serializable, Comparable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 30001;

    /**
     * DOCUMENT ME!
     */
    static int next_id;

    /**
     * DOCUMENT ME!
     */
    boolean accept;

    /**
     * DOCUMENT ME!
     */
    HashSet<Transition> transitions;

    /**
     * DOCUMENT ME!
     */
    int number;

    /**
     * DOCUMENT ME!
     */
    int id;

/**
     * Constructs new state. Initially, the new state is a reject state.
     */
    public State() {
        resetTransitions();
        id = next_id++;
    }

    /**
     * Resets transition set.
     */
    void resetTransitions() {
        transitions = new HashSet<Transition>();
    }

    /**
     * Returns set of outgoing transitions. Subsequent changes are
     * reflected in the automaton.
     *
     * @return transition set
     */
    public Set<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Adds outgoing transition.
     *
     * @param t transition
     */
    public void addTransition(Transition t) {
        transitions.add(t);
    }

    /**
     * Sets acceptance for this state.
     *
     * @param accept if true, this state is an accept state
     */
    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    /**
     * Returns acceptance status.
     *
     * @return true is this is an accept state
     */
    public boolean isAccept() {
        return accept;
    }

    /**
     * Performs lookup in transitions.
     *
     * @param c character to look up
     *
     * @return destination state, null if no matching outgoing transition
     */
    public State step(char c) {
        for (Transition t : transitions)
            if ((t.min <= c) && (c <= t.max)) {
                return t.to;
            }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param to DOCUMENT ME!
     */
    void addEpsilon(State to) {
        if (to.accept) {
            accept = true;
        }

        for (Transition t : to.transitions)
            transitions.add(t);
    }

    /**
     * Returns transitions sorted by (min, reverse max, to) or (to,
     * min, reverse max)
     *
     * @param to_first DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Transition[] getSortedTransitionArray(boolean to_first) {
        Transition[] e = transitions.toArray(new Transition[0]);
        Arrays.sort(e, new TransitionComparator(to_first));

        return e;
    }

    /**
     * DOCUMENT ME!
     *
     * @param to_first DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    List<Transition> getSortedTransitions(boolean to_first) {
        return Arrays.asList(getSortedTransitionArray(to_first));
    }

    /**
     * Returns string describing this state. Normally invoked via
     * {@link Automaton#toString()}.
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("state ").append(number);

        if (accept) {
            b.append(" [accept]");
        } else {
            b.append(" [reject]");
        }

        b.append(":\n");

        for (Transition t : transitions)
            b.append("  ").append(t.toString()).append("\n");

        return b.toString();
    }

    /**
     * Compares this object with the specified object for order. States
     * are ordered by the time of construction.
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Object o) {
        return ((State) o).id - id;
    }
}
