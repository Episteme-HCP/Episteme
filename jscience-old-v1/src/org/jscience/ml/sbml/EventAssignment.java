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
