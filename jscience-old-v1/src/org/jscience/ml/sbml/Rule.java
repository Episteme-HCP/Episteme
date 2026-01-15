package org.jscience.ml.sbml;

/**
 * Rules put constraints on variables in cases for which the constraint
 * cannot be expressed as a reacion or initial value.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public abstract class Rule extends SBase implements MathElement {
    /** DOCUMENT ME! */
    private String math;

/**
     * Creates a new Rule object.
     */
    public Rule() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMath() {
        return math;
    }

    /**
     * DOCUMENT ME!
     *
     * @param math DOCUMENT ME!
     */
    public void setMath(String math) {
        assert math.startsWith("<math:math>");
        this.math = math;
    }
}
