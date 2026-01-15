package org.jscience.computing.ai.fuzzylogic;

/**
 * <p/>
 * Abstraction for fuzzy rules.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class FuzzyRule {
    /**
     * DOCUMENT ME!
     */
    public static int OPERATOR_AND = 0;

    /**
     * DOCUMENT ME!
     */
    public static int OPERATOR_OR = 1;

    /**
     * DOCUMENT ME!
     */
    private String mRule;

    /**
     * DOCUMENT ME!
     */
    private FuzzyEngine mFuzzyEngine;

    /**
     * DOCUMENT ME!
     */
    private FuzzyExpression[] mLeftFuzzyExpressions;

    /**
     * DOCUMENT ME!
     */
    private FuzzyExpression[] mRightFuzzyExpressions;

    /**
     * DOCUMENT ME!
     */
    private int[] mLeftOperators;

    /**
     * DOCUMENT ME!
     */
    private int[] mRightOperators;

    /**
     * Creates a new FuzzyRule object.
     *
     * @param engine DOCUMENT ME!
     * @param rule   DOCUMENT ME!
     * @throws RuleParsingException DOCUMENT ME!
     */
    public FuzzyRule(FuzzyEngine engine, String rule)
            throws RuleParsingException {
        mRule = rule.toLowerCase();
        mFuzzyEngine = engine;
        parseRule();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws RuleParsingException DOCUMENT ME!
     */
    private void parseRule() throws RuleParsingException {
        /*
         * Rule Syntax : if FE (or/and FE) then FE (or/and FE)
         *
         * FE: Fuzzy Expression (LV is MF)
         * LV: Linguistic Variable
         * MF: Membership Function
         *
         * Ex. FE: angle is negative, power is increased
         *
         */
        String tmpString = new String(mRule.trim());

        tmpString = tmpString.toLowerCase();
        tmpString = skipIF(tmpString);
        tmpString = getFE(tmpString, true);

        while (startsWithANDOR(tmpString)) {
            tmpString = skipANDOR(tmpString, true);
            tmpString = getFE(tmpString, true);
        }

        tmpString = skipTHEN(tmpString);
        tmpString = getFE(tmpString, false);

        while (startsWithANDOR(tmpString)) {
            tmpString = skipANDOR(tmpString, false);
            tmpString = getFE(tmpString, false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws RuleParsingException DOCUMENT ME!
     */
    private String skipIF(String input) throws RuleParsingException {
        if (!input.startsWith("if")) {
            throw new RuleParsingException("Parse error for the rule: \"" +
                    mRule + "\". \"if\" not found.");
        } else {
            return (input.substring(2)).trim();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param input    DOCUMENT ME!
     * @param isLeftOP DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private String skipANDOR(String input, boolean isLeftOP) {
        input = input.trim();

        if (input.substring(0, 3).equals("and")) {
            addOperand(FuzzyRule.OPERATOR_AND, isLeftOP);

            return (input.substring(3)).trim();
        } else if (input.substring(0, 2).equals("or")) {
            addOperand(FuzzyRule.OPERATOR_OR, isLeftOP);

            return (input.substring(2)).trim();
        } else {
            return input.trim();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private String skipTHEN(String input) {
        return (input.substring(4)).trim();
    }

    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private String skipIS(String input) {
        return (input.substring(2)).trim();
    }

    /**
     * DOCUMENT ME!
     *
     * @param input    DOCUMENT ME!
     * @param isLeftFE DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws RuleParsingException DOCUMENT ME!
     */
    private String getFE(String input, boolean isLeftFE)
            throws RuleParsingException {
        /*
         * FE: Fuzzy Expression (LV is MF)
         *
         * Ex. FE: angle is negative, power is increased
         *
         * LV: Linguistic Variable
         * MF: Membership Function
         */

        // Get Linguistic Variable
        int index = input.indexOf(' ');
        LinguisticVariable lv = mFuzzyEngine.getLinguisticVariable(input.substring(
                0, index));

        if (lv == null) {
            throw new RuleParsingException("\"" + input.substring(0, index) +
                    "\" linguistic variable is not added to the fuzzy engine.");
        }

        input = input.substring(index);
        input = input.trim();
        input = skipIS(input);

        // Get Membership Function
        index = input.indexOf(' ');

        if (index == -1) {
            index = input.length();
        }

        MembershipFunction mf = lv.getMembershipFuncion(input.substring(0, index));

        if (mf == null) {
            throw new RuleParsingException("\"" + input.substring(0, index) +
                    "\" membership function is not added to \"" + lv +
                    "\" linguistic variable.");
        }

        input = input.substring(index);

        // Add Fuzzy Expression(FE)
        addFE(new FuzzyExpression(lv, mf), isLeftFE);

        return input.trim();
    }

    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private boolean startsWithANDOR(String input) {
        if (input.startsWith("and")) {
            return true;
        }

        if (input.startsWith("or")) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param exp      DOCUMENT ME!
     * @param isLeftFE DOCUMENT ME!
     */
    private void addFE(FuzzyExpression exp, boolean isLeftFE) {
        if (isLeftFE) {
            if (mLeftFuzzyExpressions == null) {
                mLeftFuzzyExpressions = new FuzzyExpression[1];
                mLeftFuzzyExpressions[0] = exp;
            } else {
                FuzzyExpression[] tmp = new FuzzyExpression[mLeftFuzzyExpressions.length +
                        1];

                for (int i = 0; i < mLeftFuzzyExpressions.length; i++) {
                    tmp[i] = mLeftFuzzyExpressions[i];
                }

                tmp[mLeftFuzzyExpressions.length] = exp;
                mLeftFuzzyExpressions = tmp;
            }
        } else {
            if (mRightFuzzyExpressions == null) {
                mRightFuzzyExpressions = new FuzzyExpression[1];
                mRightFuzzyExpressions[0] = exp;
            } else {
                FuzzyExpression[] tmp = new FuzzyExpression[mRightFuzzyExpressions.length +
                        1];

                for (int i = 0; i < mRightFuzzyExpressions.length; i++) {
                    tmp[i] = mRightFuzzyExpressions[i];
                }

                tmp[mRightFuzzyExpressions.length] = exp;
                mRightFuzzyExpressions = tmp;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param op       DOCUMENT ME!
     * @param isLeftOP DOCUMENT ME!
     */
    private void addOperand(int op, boolean isLeftOP) {
        if (isLeftOP) {
            if (mLeftOperators == null) {
                mLeftOperators = new int[1];
                mLeftOperators[0] = op;
            } else {
                int[] tmp = new int[mLeftOperators.length + 1];

                for (int i = 0; i < mLeftOperators.length; i++) {
                    tmp[i] = mLeftOperators[i];
                }

                tmp[mLeftOperators.length] = op;
                mLeftOperators = tmp;
            }
        } else {
            if (mRightOperators == null) {
                mRightOperators = new int[1];
                mRightOperators[0] = op;
            } else {
                int[] tmp = new int[mRightOperators.length + 1];

                for (int i = 0; i < mRightOperators.length; i++) {
                    tmp[i] = mRightOperators[i];
                }

                tmp[mRightOperators.length] = op;
                mRightOperators = tmp;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FuzzyExpression[] getRightFuzzyExpressions() {
        return mRightFuzzyExpressions;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FuzzyExpression[] getLeftFuzzyExpressions() {
        return mLeftFuzzyExpressions;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getLeftOperators() {
        return mLeftOperators;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getRightOperators() {
        return mRightOperators;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return mRule;
    }

    /**
     * DOCUMENT ME!
     */
    public void evaluate() {
        double result = mLeftFuzzyExpressions[0].evaluateExpression();

        for (int i = 1; i < mLeftFuzzyExpressions.length; i++) {
            if (mLeftOperators[i - 1] == OPERATOR_AND) {
                // AND: MIN
                result = Math.min(mLeftFuzzyExpressions[i].evaluateExpression(),
                        result);
            } else {
                // OR: MAX
                result = Math.max(mLeftFuzzyExpressions[i].evaluateExpression(),
                        result);
            }
        }

        // The left-part of IF is completed,
        // now set this value as the input to the right-part
        for (int i = 0; i < mRightFuzzyExpressions.length; i++) {
            mRightFuzzyExpressions[i].getMembershipFunction()
                    .setDeFuzzificationInputValue(result);
        }
    }
}
