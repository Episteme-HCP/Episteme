package org.jscience.core.mathematics.logic.fuzzy;

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
public class FuzzyExpression<T> {
    /**
     * DOCUMENT ME!
     */
    private LinguisticVariable mLinguisticVariable = null; // Linguistic Variable

    /**
     * DOCUMENT ME!
     */
    private MembershipFunction<T> mMembershipFunction = null; // Membership Function

    /**
     * Creates a new FuzzyExpression object.
     *
     * @param lv DOCUMENT ME!
     * @param mf DOCUMENT ME!
     */
    public FuzzyExpression(LinguisticVariable lv, MembershipFunction<T> mf) {
        mLinguisticVariable = lv;
        mMembershipFunction = mf;
    }

    /**
     * Evaluates the expression against a specific input.
     * @param input the input value.
     * @return result of fuzzification.
     */
    public double evaluate(T input) {
        return mMembershipFunction.fuzzify(input);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @SuppressWarnings("unchecked")
    public double evaluateExpression() {
        return mMembershipFunction.fuzzify((T) Double.valueOf(mLinguisticVariable.getFuzzificationInputValue()));
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
    public MembershipFunction<T> getMembershipFunction() {
        return mMembershipFunction;
    }
}
