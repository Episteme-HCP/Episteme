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
 * This class declares a variable for use in MathML structures. This code
 * is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for
 * more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class Parameter extends SBaseId {
    /** DOCUMENT ME! */
    private boolean constant = true;

    /** DOCUMENT ME! */
    private double value;

    /** DOCUMENT ME! */
    private String units;

/**
     * Creates a new Parameter object.
     *
     * @param id    DOCUMENT ME!
     * @param name  DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public Parameter(String id, String name, double value) {
        super(id, name);
        setValue(value);
    }

/**
     * Creates a new Parameter object.
     *
     * @param id   DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public Parameter(String id, String name) {
        this(id, name, Double.NaN);
    }

/**
     * Creates a new instance of Parameter
     */
    public Parameter() {
        this(null, null, Double.NaN);
    }

    /**
     * Getter for property units.
     *
     * @return Value of property units.
     */
    public String getUnits() {
        return units;
    }

    /**
     * Getter for property value.
     *
     * @return Value of property value.
     */
    public double getValue() {
        return value;
    }

    /**
     * Getter for property constant.
     *
     * @return Value of property constant.
     */
    public boolean isConstant() {
        return constant;
    }

    /**
     * Sets whether the parameter's value is constant throughout a
     * simulation.
     *
     * @param constant New value of property constant.
     */
    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    /**
     * Setter for property units.
     *
     * @param units New value of property units.
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * Setter for property value.
     *
     * @param value New value of property value.
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer("<parameter id=\"" + id + "\"");

        if (!Double.isNaN(value)) {
            s.append(" value=\"" + value + "\"");
        }

        if (constant != true) {
            s.append(" constant=\"" + constant + "\"");
        }

        if (name != null) {
            s.append(" name=\"" + name + "\"");
        }

        if ((units != null) && (units.length() != 0)) {
            s.append(" units=\"" + units + "\"");
        }

        printShortForm(s, "</parameter>");

        return s.toString();
    }
}
