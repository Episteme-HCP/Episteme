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

package org.jscience.computing.ai.fuzzylogic;

/**
 * <p/>
 * Abstraction for fuzzy expressions.
 * </p>
 * <p/>
 * <p/>
 * Contains single fuzzy expression in the form "LV  is MF".
 * </p>
 * <p/>
 * <p/>
 * LV: Linguistic Variable
 * </p>
 * <p/>
 * <p/>
 * MF: Membership Function
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class FuzzyExpression {
    /**
     * DOCUMENT ME!
     */
    private LinguisticVariable mLinguisticVariable = null; // Linguistic Variable

    /**
     * DOCUMENT ME!
     */
    private MembershipFunction mMembershipFunction = null; // Membership Function

    /**
     * Creates a new FuzzyExpression object.
     *
     * @param lv DOCUMENT ME!
     * @param mf DOCUMENT ME!
     */
    public FuzzyExpression(LinguisticVariable lv, MembershipFunction mf) {
        mLinguisticVariable = lv;
        mMembershipFunction = mf;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double evaluateExpression() {
        return mMembershipFunction.fuzzify(mLinguisticVariable.getFuzzificationInputValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (mLinguisticVariable + " is " + mMembershipFunction);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LinguisticVariable getLinguisticVariable() {
        return mLinguisticVariable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MembershipFunction getMembershipFunction() {
        return mMembershipFunction;
    }
}
