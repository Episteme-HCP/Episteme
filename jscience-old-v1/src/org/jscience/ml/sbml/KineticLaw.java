package org.jscience.ml.sbml;

import java.util.ArrayList;
import java.util.List;

/**
 * The mathematics of the rate of the enclosing {@link Reaction}.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */

public final class KineticLaw extends SBase implements MathElement {
    private final List parameter;
    private SBase parametersElement;
    private String math;
    private String substanceUnits;
    private String timeUnits;

    public KineticLaw(String math) {
        this();
        setMath(math);
    }

    /**
     * Creates a new instance of KineticLaw
     */

    public KineticLaw() {
        parametersElement = new SBase();
        parameter = new ArrayList();
    }

    public void addParameter(Parameter ref) {
        parameter.add(ref);
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
     * Getter for property parameter.
     *
     * @return Value of property parameter.
     */

    public List getParameter() {
        return parameter;
    }

    /**
     * Getter for property parametersElement.
     *
     * @return Value of property parametersElement.
     */

    public SBase getParametersElement() {
        return parametersElement;
    }

    /**
     * Getter for property substanceUnits.
     *
     * @return Value of property substanceUnits.
     */

    public String getSubstanceUnits() {
        return substanceUnits;
    }

    /**
     * Getter for property timeUnits.
     *
     * @return Value of property timeUnits.
     */

    public String getTimeUnits() {
        return timeUnits;
    }

    /**
     * Sets the MathML expression for this {@link KineticLaw}.
     *
     * @param math New value of property math.
     */

    public void setMath(String math) {
        assert math.startsWith("<math:math>");
        this.math = math;
    }

    /**
     * Setter for property parameterElement.
     *
     * @param parameterElement New value of property parameterElement.
     */

    public void setParametersElement(SBase parametersElement) {
        this.parametersElement = parametersElement;
    }

    /**
     * Setter for property substanceUnits.
     *
     * @param substanceUnits New value of property substanceUnits.
     */

    public void setSubstanceUnits(String substanceUnits) {
        this.substanceUnits = substanceUnits;
    }

    /**
     * Setter for property timeUnits.
     *
     * @param timeUnits New value of property timeUnits.
     */

    public void setTimeUnits(String timeUnits) {
        this.timeUnits = timeUnits;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */

    public String toString() {
        StringBuffer s = new StringBuffer("<kineticLaw");
        if (timeUnits != null)
            s.append(" timeUnits=\"" + timeUnits + "\"");
        if (substanceUnits != null)
            s.append(" substanceUnits=\"" + substanceUnits + "\"");
        s.append(">\n");
        if (!math.matches("<math:math>\\p{Space}*</math:math>"))
            s.append(math);
        printList(s, parameter, "<listOfParameters>", "</listOfParameters>");
        s.append(super.toString());
        s.append("</kineticLaw>\n");
        return s.toString();
    }
}
