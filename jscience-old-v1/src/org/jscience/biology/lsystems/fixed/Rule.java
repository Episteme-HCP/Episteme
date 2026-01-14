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

/*
---------------------------------------------------------------------
VIRTUAL PLANTS
==============

University of applied sciences Biel

hosted by Claude Schwab

programmed by Rene Gressly

March - July 1999

---------------------------------------------------------------------
*/
package org.jscience.biology.lsystems.fixed;

/**
 * To represent a rule of an LSystem containing a predecessor a successor
 * and a probability this class may be used.
 */
public class Rule {
    /** The prdecessor of the rule. This is just one character. */
    private char m_strPredecessor;

    /** The successor of the rule. */
    private String m_strSuccessor;

    /**
     * The probability of this rule. Values between zero and one may be
     * used where 1 means in every case and 0 means never. Remind that the
     * probabilities of all rules with the same predecessor added together
     * have to be 1
     */
    private float m_fProbability;

/**
     * Constructor allowing to initialize all three values
     *
     * @param strPredecessor The predecessor of this rule.
     * @param strSuccessor   The successor of the rule.
     * @param fProbability   The probability of the stochastic rule.
     */
    public Rule(char strPredecessor, String strSuccessor, float fProbability) {
        m_strPredecessor = strPredecessor;
        m_strSuccessor = strSuccessor;
        m_fProbability = fProbability;

        //Log.debug("Rule created: Pred=" + m_strPredecessor + " Succ=" + m_strSuccessor);
    }

/**
     * Constructor initializes predecessor and successor. The probability is
     * set to 1.
     *
     * @param strPredecessor The predecessor of this rule.
     * @param strSuccessor   The successor of the rule.
     */
    public Rule(char strPredecessor, String strSuccessor) {
        this(strPredecessor, strSuccessor, 1f);
    }

    /**
     * Stores a new predecessor in the member variable.
     *
     * @param strPredecessor The new predecessor
     */
    public void setPredecessor(char strPredecessor) {
        m_strPredecessor = strPredecessor;
    }

    /**
     * Stores a new sucessor in the member variable.
     *
     * @param strSuccessor The new sucessor
     */
    public void setSuccessor(String strSuccessor) {
        m_strSuccessor = strSuccessor;
    }

    /**
     * Stores a new probability in the member variable.
     *
     * @param fProbability The new probability
     */
    public void setProbability(float fProbability) {
        m_fProbability = fProbability;
    }

    /**
     * Returns the predecessor value of this rule.
     *
     * @return The predecessor.
     */
    public char getPredecessor() {
        return m_strPredecessor;
    }

    /**
     * Returns the successor value of this rule
     *
     * @return The successor value.
     */
    public String getSuccessor() {
        return m_strSuccessor;
    }

    /**
     * Returns the probability of this rule.
     *
     * @return The probability of this rule.
     */
    public float getProbability() {
        return m_fProbability;
    }

    /**
     * Converts the rule to a string version and returns it. The output
     * of the string is in the form predecessor, rulesign, successor.
     *
     * @return The rule as a string.
     */
    public String toString() {
        return m_strPredecessor + " " + FixedPlantsDefinitions.STR_RULESIGN +
        " " + m_strSuccessor;
    }
}
