/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
