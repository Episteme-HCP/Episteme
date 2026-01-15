package org.jscience.computing.ai.fuzzylogic;

/**
 * <p/>
 * Abstraction for fuzzy membership functions.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public interface MembershipFunction {
    /** DOCUMENT ME! */
    public static int TYPE_TRIANGULAR = 0;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName();

    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double fuzzify(double input);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTypicalValue();

    /**
     * DOCUMENT ME!
     *
     * @param inputValue DOCUMENT ME!
     */
    public void setDeFuzzificationInputValue(double inputValue);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDeFuzzificationInputValue();
}
