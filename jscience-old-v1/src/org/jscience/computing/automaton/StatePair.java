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

package org.jscience.computing.automaton;

/**
 * Pair of states.
 *
 * @author Anders M&oslash;ller &lt;<a
 *         href="mailto:amoeller@brics.dk">amoeller@brics.dk</a>&gt;
 */
public class StatePair {
    /**
     * DOCUMENT ME!
     */
    State s;

    /**
     * DOCUMENT ME!
     */
    State s1;

    /**
     * DOCUMENT ME!
     */
    State s2;

    /**
     * Creates a new StatePair object.
     *
     * @param s DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     * @param s2 DOCUMENT ME!
     */
    StatePair(State s, State s1, State s2) {
        this.s = s;
        this.s1 = s1;
        this.s2 = s2;
    }

/**
     * Constructs new state pair.
     *
     * @param s1 first state
     * @param s2 second state
     */
    public StatePair(State s1, State s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    /**
     * Returns first component of this pair.
     *
     * @return first state
     */
    public State getFirstState() {
        return s1;
    }

    /**
     * Returns second component of this pair.
     *
     * @return second state
     */
    public State getSecondState() {
        return s2;
    }

    /**
     * Checks for equality.
     *
     * @param obj object to compare with
     *
     * @return true if <tt>obj</tt> represents the same pair of states as this
     *         pair
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatePair) {
            StatePair p = (StatePair) obj;

            return (p.s1 == s1) && (p.s2 == s2);
        } else {
            return false;
        }
    }

    /**
     * Returns hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return s1.hashCode() + s2.hashCode();
    }
}
