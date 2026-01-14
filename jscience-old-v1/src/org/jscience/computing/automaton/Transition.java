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

import java.io.Serializable;
import java.util.Comparator;

/**
 * <tt>Automaton</tt> transition.
 * <p/>
 * A transition, which belongs to a source state, consists of a Unicode character interval
 * and a destination state.
 *
 * @author Anders M&oslash;ller &lt;<a href="mailto:amoeller@brics.dk">amoeller@brics.dk</a>&gt;
 */
public class Transition implements Serializable, Cloneable {

    static final long serialVersionUID = 40001;

    /*
      * CLASS INVARIANT: min<=max
      */

    char min;
    char max;

    State to;

    /**
     * Constructs new singleton interval transition.
     *
     * @param c  transition character
     * @param to destination state
     */
    public Transition(char c, State to) {
        min = max = c;
        this.to = to;
    }

    /**
     * Constructs new transition.
     * Both end points are included in the interval.
     *
     * @param min transition interval minimum
     * @param max transition interval maximum
     * @param to  destination state
     */
    public Transition(char min, char max, State to) {
        if (max < min) {
            char t = max;
            max = min;
            min = t;
        }
        this.min = min;
        this.max = max;
        this.to = to;
    }

    /**
     * Returns minimum of this transition interval.
     */
    public char getMin() {
        return min;
    }

    /**
     * Returns maximum of this transition interval.
     */
    public char getMax() {
        return max;
    }

    /**
     * Returns destination of this transition.
     */
    public State getDest() {
        return to;
    }

    /**
     * Checks for equality.
     *
     * @param obj object to compare with
     * @return true if <tt>obj</tt> is a transition with same
     *         character interval and destination state as this transition.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transition) {
            Transition t = (Transition) obj;
            return t.min == min && t.max == max && t.to == to;
        } else
            return false;
    }

    /**
     * Returns hash code.
     * The hash code is based on the character interval (not the destination state).
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return min * 2 + max * 3;
    }

    /**
     * Clones this transition.
     *
     * @return clone with same character interval and destination state
     */
    @Override
    public Object clone() {
        return new Transition(min, max, to);
    }

    static void appendCharString(char c, StringBuilder b) {
        if (c >= 0x21 && c <= 0x7e && c != '\\' && c != '"')
            b.append(c);
        else {
            b.append("\\u");
            String s = Integer.toHexString(c);
            if (c < 0x10)
                b.append("000").append(s);
            else if (c < 0x100)
                b.append("00").append(s);
            else if (c < 0x1000)
                b.append("0").append(s);
            else
                b.append(s);
        }
    }

    /**
     * Returns string describing this state. Normally invoked via
     * {@link Automaton#toString()}.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        appendCharString(min, b);
        if (min != max) {
            b.append("-");
            appendCharString(max, b);
        }
        b.append(" -> ").append(to.number);
        return b.toString();
    }

    void appendDot(StringBuilder b) {
        b.append(" -> ").append(to.number).append(" [label=\"");
        appendCharString(min, b);
        if (min != max) {
            b.append("-");
            appendCharString(max, b);
        }
        b.append("\"]\n");
    }
}

class TransitionComparator implements Comparator<Transition> {

    boolean to_first;

    TransitionComparator(boolean to_first) {
        this.to_first = to_first;
    }

    /**
     * Compares by (min, reverse max, to) or (to, min, reverse max).
     */
    public int compare(Transition t1, Transition t2) {
        if (to_first) {
            if (t1.to != t2.to) {
                if (t1.to == null)
                    return -1;
                else if (t2.to == null)
                    return 1;
                else if (t1.to.number < t2.to.number)
                    return -1;
                else if (t1.to.number > t2.to.number)
                    return 1;
            }
        }
        if (t1.min < t2.min)
            return -1;
        if (t1.min > t2.min)
            return 1;
        if (t1.max > t2.max)
            return -1;
        if (t1.max < t2.max)
            return 1;
        if (!to_first) {
            if (t1.to != t2.to) {
                if (t1.to == null)
                    return -1;
                else if (t2.to == null)
                    return 1;
                else if (t1.to.number < t2.to.number)
                    return -1;
                else if (t1.to.number > t2.to.number)
                    return 1;
            }
        }
        return 0;
    }
}
