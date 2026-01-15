package org.jscience.ml.sbml;

/**
 * This code is licensed under the DARPA BioCOMP Open Source License.  See
 * LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class StoichiometryMath extends SBase implements MathElement {
    /** DOCUMENT ME! */
    private String math;

/**
     * Creates a new StoichiometryMath object.
     */
    public StoichiometryMath() {
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return math;
    }
}
