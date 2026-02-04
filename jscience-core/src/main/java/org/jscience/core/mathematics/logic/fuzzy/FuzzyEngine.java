package org.jscience.core.mathematics.logic.fuzzy;

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
     * Defuzzification method: Center of Maximum (CoM).
     * Calculates the center of the area of the maximum membership values.
     */
    public static int DEFUZZIFICATION_CENTER_OF_MAXIMUM = 0;

    /** The list of linguistic variables (inputs and outputs) managed by this engine. */
    private LinguisticVariable[] mLinguisticVariables = null;

    /** The list of fuzzy rules derived for inference. */
    private FuzzyRule[] mFuzzyRules = null;

    /**
     * Creates a new FuzzyEngine object.
     */
    public FuzzyEngine() {
    }

    /**
     * Evaluates a single rule provided as a string.
     * The rule string is parsed, executed, and the results affect the associated linguistic variables.
     *
     * @param rule the string representation of the fuzzy rule
     * @throws RuleParsingException if the rule syntax is invalid
     */
    public void evaluateRule(String rule) throws RuleParsingException {
        (new FuzzyRule(this, rule)).evaluate();
    }

    /**
     * Evaluates a pre-parsed FuzzyRule object.
     *
     * @param rule the fuzzy rule to evaluate
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
     * Parses and registers a new rule from its string representation.
     *
     * @param rule the rule string
     * @return the created FuzzyRule object
     * @throws RuleParsingException if the rule format is incorrect
     */
    public FuzzyRule addRule(String rule) throws RuleParsingException {
        return addRule(new FuzzyRule(this, rule));
    }

    /**
     * Registers an existing FuzzyRule object with the engine.
     *
     * @param rule the rule to add
     * @return the added rule
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
     * Registers a Linguistic Variable (LV) with the engine.
     * LVs represent concepts like "Temperature" or "Speed" with associated fuzzy sets.
     *
     * @param lv the linguistic variable to add
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
     * Retrieves a registered Linguistic Variable by its name.
     *
     * @param lv the name of the variable (e.g., "Pressure")
     * @return the variable object, or null if not found
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
     * Returns the number of rules currently registered in the engine.
     *
     * @return the rule count
     */
    public int getNumRules() {
        return mFuzzyRules.length;
    }
}
