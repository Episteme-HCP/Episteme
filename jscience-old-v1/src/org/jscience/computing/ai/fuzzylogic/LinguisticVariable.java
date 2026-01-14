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

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * <p/>
 * Abstraction for fuzzy linguistic variables.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class LinguisticVariable {
    /**
     * DOCUMENT ME!
     */
    private String mName;

    // Membership Functions added to this LV.

    /**
     * DOCUMENT ME!
     */
    private Hashtable mFunctions;

    // Input value for this LV used in fuzzification.

    /**
     * DOCUMENT ME!
     */
    private double mFuzzificationInputValue;

    /**
     * Creates a new LinguisticVariable object.
     *
     * @param name DOCUMENT ME!
     */
    public LinguisticVariable(String name) {
        mName = name.toLowerCase();
        mFunctions = new Hashtable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param function DOCUMENT ME!
     */
    public void addMembershipFunction(MembershipFunction function) {
        mFunctions.put(function.getName(), function);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public MembershipFunction getMembershipFuncion(String name) {
        return (MembershipFunction) mFunctions.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mName DOCUMENT ME!
     */
    public void setName(String mName) {
        this.mName = mName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return mName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws NoRulesFiredException DOCUMENT ME!
     */
    public double defuzzify() throws NoRulesFiredException {
        double total = 0;
        double divider = 0;
        MembershipFunction mf;

        for (Enumeration _enum = mFunctions.elements();
             _enum.hasMoreElements();) {
            mf = (MembershipFunction) _enum.nextElement();
            total = total +
                    (mf.getDeFuzzificationInputValue() * mf.getTypicalValue());
            divider = divider + mf.getDeFuzzificationInputValue();
        }

        System.out.println(total);
        System.out.println(divider);

        return (total / divider);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return mName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param inputValue DOCUMENT ME!
     */
    public void setFuzzificationInputValue(double inputValue) {
        mFuzzificationInputValue = inputValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getFuzzificationInputValue() {
        return mFuzzificationInputValue;
    }
}
