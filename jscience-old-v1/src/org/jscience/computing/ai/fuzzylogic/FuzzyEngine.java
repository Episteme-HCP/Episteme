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
