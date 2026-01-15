package org.jscience.ml.sbml;

/**
 * The variable assignments made when an {@link Event} is executed.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */

public final class EventAssignment extends SBase implements MathElement {
    private String math;
    private String variable;

    /**
     * Creates a new instance of EventAssignment
     */

    public EventAssignment() {
    }

    /**
     * Getter for property math.
     *
     * @return Value of property math.
     */

    public String getMath() {
        return math;
    }

    /**
     * Getter for property variable.
     *
     * @return Value of property variable.
     */

    public String getVariable() {
        return variable;
    }

    /**
     * Sets the new value of <I> variable </I>.
     *
     * @param math The MathML expression to set <I>variable</I> equal to.
     */

    public void setMath(String math) {
        assert math.startsWith("<math:math>");
        this.math = math;
    }

    /**
     * Sets the identifier of <I>variable</I>.
     *
     * @param variable New value of property variable.
     */

    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */

    public String toString() {
        StringBuffer s = new StringBuffer("<eventAssignment variable=\"" + variable + "\"");
        s.append(">\n");
        s.append(math);
        s.append(super.toString());
        s.append("</eventAssignment>\n");
        return s.toString();
    }
}
