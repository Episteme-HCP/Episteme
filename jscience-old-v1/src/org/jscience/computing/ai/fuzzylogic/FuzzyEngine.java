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
 * Fuzzy engine implementation.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class FuzzyEngine {
    /**
     * DOCUMENT ME!
     */
    public static int DEFUZZIFICATION_CENTER_OF_MAXIMUM = 0;

    /**
     * DOCUMENT ME!
     */
    private LinguisticVariable[] mLinguisticVariables = null;

    /**
     * DOCUMENT ME!
     */
    private FuzzyRule[] mFuzzyRules = null;

    /**
     * Creates a new FuzzyEngine object.
     */
    public FuzzyEngine() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param rule DOCUMENT ME!
     * @throws RuleParsingException DOCUMENT ME!
     */
    public void evaluateRule(String rule) throws RuleParsingException {
        (new FuzzyRule(this, rule)).evaluate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param rule DOCUMENT ME!
     */
    public void evaluateRule(FuzzyRule rule) {
        rule.evaluate();
    }

    /**
     * DOCUMENT ME!
     */
    public void evaluateRules() {
        for (int i = 0; i < mFuzzyRules.length; i++) {
            mFuzzyRules[i].evaluate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rule DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws RuleParsingException DOCUMENT ME!
     */
    public FuzzyRule addRule(String rule) throws RuleParsingException {
        return addRule(new FuzzyRule(this, rule));
    }

    /**
     * DOCUMENT ME!
     *
     * @param rule DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public FuzzyRule addRule(FuzzyRule rule) {
        if (mFuzzyRules == null) {
            mFuzzyRules = new FuzzyRule[1];
            mFuzzyRules[0] = rule;
        } else {
            FuzzyRule[] tmp = new FuzzyRule[mFuzzyRules.length + 1];

            for (int i = 0; i < mFuzzyRules.length; i++) {
                tmp[i] = mFuzzyRules[i];
            }

            tmp[mFuzzyRules.length] = rule;
            mFuzzyRules = tmp;
        }

        return rule;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lv DOCUMENT ME!
     */
    public void addLinguisticVariable(LinguisticVariable lv) {
        if (mLinguisticVariables == null) {
            mLinguisticVariables = new LinguisticVariable[1];
            mLinguisticVariables[0] = lv;
        } else {
            LinguisticVariable[] tmp = new LinguisticVariable[mLinguisticVariables.length +
                    1];

            for (int i = 0; i < mLinguisticVariables.length; i++) {
                tmp[i] = mLinguisticVariables[i];
            }

            tmp[mLinguisticVariables.length] = lv;
            mLinguisticVariables = tmp;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lv DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public LinguisticVariable getLinguisticVariable(String lv) {
        for (int i = 0; i < mLinguisticVariables.length; i++) {
            if (mLinguisticVariables[i].getName().equals(lv)) {
                return mLinguisticVariables[i];
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumRules() {
        return mFuzzyRules.length;
    }
}
