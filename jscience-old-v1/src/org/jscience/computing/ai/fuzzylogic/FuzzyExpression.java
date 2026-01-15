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
