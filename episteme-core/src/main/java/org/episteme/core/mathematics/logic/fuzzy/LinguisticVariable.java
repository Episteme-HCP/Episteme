package org.episteme.core.mathematics.logic.fuzzy;

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
    /** The name of this linguistic variable (e.g. "Temperature"). */
    private String mName;

    // Membership Functions added to this LV.

    /** Map of membership functions associated with this variable, keyed by function name. */
    private Hashtable<String, MembershipFunction<?>> mFunctions;

    // Input value for this LV used in fuzzification.

    /** The current crisp input value to be fuzzified. */
    private double mFuzzificationInputValue;

    /**
     * Creates a new LinguisticVariable object.
     *
     * @param name the name of the variable (case-insensitive)
     */
    public LinguisticVariable(String name) {
        mName = name.toLowerCase();
        mFunctions = new Hashtable<>();
    }

    /**
     * Adds a membership function to this variable.
     *
     * @param function the membership function to add
     */
    public void addMembershipFunction(MembershipFunction<?> function) {
        mFunctions.put(function.getName(), function);
    }

    /**
     * Retrieves a membership function by name.
     *
     * @param name the name of the function (e.g. "Hot", "Cold")
     * @return the membership function or null if not found
     */
    public MembershipFunction<?> getMembershipFuncion(String name) {
        return mFunctions.get(name);
    }

    /**
     * Sets the name of this linguistic variable.
     *
     * @param mName the new name
     */
    public void setName(String mName) {
        this.mName = mName;
    }

    /**
     * Gets the name of this linguistic variable.
     *
     * @return the name
     */
    public String getName() {
        return mName;
    }

    /**
     * Performs defuzzification on this variable to produce a single crisp output.
     * Uses the Center of Gravity/Area method based on active rules.
     *
     * @return the crisp output value
     * @throws NoRulesFiredException if no rules contributed to this variable's output
     */
    public double defuzzify() throws NoRulesFiredException {
        double total = 0;
        double divider = 0;
        MembershipFunction<?> mf;

        for (Enumeration<MembershipFunction<?>> _enum = mFunctions.elements();
             _enum.hasMoreElements();) {
            mf = _enum.nextElement();
            total = total +
                    (mf.getDeFuzzificationInputValue() * mf.getTypicalValue());
            divider = divider + mf.getDeFuzzificationInputValue();
        }

        System.out.println(total);
        System.out.println(divider);

        return (total / divider);
    }

    /**
     * Returns the name of the variable.
     *
     * @return string representation
     */
    public String toString() {
        return mName;
    }

    /**
     * Sets the input value for fuzzification (converting crisp input to fuzzy degrees).
     *
     * @param inputValue the crisp input value
     */
    public void setFuzzificationInputValue(double inputValue) {
        mFuzzificationInputValue = inputValue;
    }

    /**
     * Gets the current fuzzification input value.
     *
     * @return the input value
     */
    public double getFuzzificationInputValue() {
        return mFuzzificationInputValue;
    }
}
