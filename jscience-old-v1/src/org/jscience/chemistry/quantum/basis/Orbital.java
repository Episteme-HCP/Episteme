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

/*
 * Orbital.java
 *
 * Created on July 25, 2004, 11:14 AM
 */
package org.jscience.chemistry.quantum.basis;

import java.util.ArrayList;


/**
 * Represents an orbital type and its coefficients and exponents in
 * <code>AtomicBasis</code>.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class Orbital {
    /** Holds value of property type. */
    private String type;

    /** Holds value of property coefficients. */
    private ArrayList coefficients;

    /** Holds value of property exponents. */
    private ArrayList exponents;

/**
     * Creates a new instance of Orbital
     *
     * @param type the type of this orbital (e.g. 'S', 'P', 'D' etc...)
     */
    public Orbital(String type) {
        this.type = type;

        coefficients = new ArrayList(10);
        exponents = new ArrayList(10);
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for property type.
     *
     * @param type New value of property type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for property coefficients.
     *
     * @return Value of property coefficients.
     */
    public ArrayList getCoefficients() {
        return this.coefficients;
    }

    /**
     * Setter for property coefficients.
     *
     * @param coefficients New value of property coefficients.
     */
    public void setCoefficients(ArrayList coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Getter for property exponents.
     *
     * @return Value of property exponents.
     */
    public ArrayList getExponents() {
        return this.exponents;
    }

    /**
     * Setter for property exponents.
     *
     * @param exponents New value of property exponents.
     */
    public void setExponents(ArrayList exponents) {
        this.exponents = exponents;
    }

    /**
     * adds a coefficient to this Orbital entry
     *
     * @param coeff the coefficient to be added
     */
    public void addCoefficient(double coeff) {
        coefficients.add(new Double(coeff));
    }

    /**
     * adds a exponent to this Orbital entry
     *
     * @param exp the exponent to be added
     */
    public void addExponent(double exp) {
        exponents.add(new Double(exp));
    }

    /**
     * adds a (coefficient, exponent) pair to this Orbital entry
     *
     * @param coeff the coefficient to be added
     * @param exp the exponent to be added
     */
    public void addEntry(double coeff, double exp) {
        addCoefficient(coeff);
        addExponent(exp);
    }
} // end of class Orbital
