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

package org.jscience.engineering.control;

import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class HighPassPassive extends BlackBox {
    /** DOCUMENT ME! */
    private double resistance = 0.0D; // Resistance value, R ohms

    /** DOCUMENT ME! */
    private double capacitance = 0.0D; // Capacitance value, C farads

    /** DOCUMENT ME! */
    private double timeConstant = 0.0D; // Time constant, RC seconds

    /** DOCUMENT ME! */
    private boolean setR = false; // = true when resistance set

    /** DOCUMENT ME! */
    private boolean setC = false; // = true when capacitance set

    // Constructor
    /**
     * Creates a new HighPassPassive object.
     */
    public HighPassPassive() {
        this.timeConstant = 1.0D;
        super.sNumerDeg = 1;
        super.sDenomDeg = 1;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(0.0D), new Complex(1.0D)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(1.0D), new Complex(1.0D)
                });
        super.sZeros = new Complex[] { Complex.ZERO };
        super.sPoles = new Complex[] { Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "Passive High Pass Filter";
        super.fixedName = "Passive High Pass Filter";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param res DOCUMENT ME!
     */
    public void setResistance(double res) {
        this.resistance = res;
        this.timeConstant = res * this.capacitance;
        this.calcPolesZerosS();
        super.sNumer = ComplexPolynomial.rootsToPolynomial(this.sZeros);

        for (int i = 0; i <= super.sNumerDeg; i++)
            super.sNumer.setCoefficientAsComplex(i,
                ((Complex) super.sNumer.getCoefficientAsComplex(i).clone()).multiply(
                    Math.pow(this.timeConstant, i)));

        super.sDenom = ComplexPolynomial.rootsToPolynomial(this.sPoles);
        super.addDeadTimeExtras();
        this.setR = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cap DOCUMENT ME!
     */
    public void setCapacitance(double cap) {
        this.capacitance = cap;
        this.timeConstant = cap * this.resistance;
        this.calcPolesZerosS();
        super.sNumer = ComplexPolynomial.rootsToPolynomial(this.sZeros);

        for (int i = 0; i <= super.sNumerDeg; i++)
            super.sNumer.setCoefficientAsComplex(i,
                ((Complex) super.sNumer.getCoefficientAsComplex(i).clone()).multiply(
                    Math.pow(this.timeConstant, i)));

        super.sDenom = ComplexPolynomial.rootsToPolynomial(this.sPoles);
        super.addDeadTimeExtras();
        this.setC = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tau DOCUMENT ME!
     */
    public void setTimeConstant(double tau) {
        this.timeConstant = tau;
        this.calcPolesZerosS();
        super.sNumer = ComplexPolynomial.rootsToPolynomial(this.sZeros);

        for (int i = 0; i <= super.sNumerDeg; i++)
            super.sNumer.setCoefficientAsComplex(i,
                ((Complex) super.sNumer.getCoefficientAsComplex(i).clone()).multiply(
                    Math.pow(this.timeConstant, i)));

        super.sDenom = ComplexPolynomial.rootsToPolynomial(this.sPoles);
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getResistance() {
        if (this.setR) {
            return this.resistance;
        } else {
            System.out.println("Class; HighPassPassive, method: getResistance");
            System.out.println("No resistance has been entered; zero returned");

            return 0.0D;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCapacitance() {
        if (this.setC) {
            return this.capacitance;
        } else {
            System.out.println("Class; HighPassPassive, method: getCapacitance");
            System.out.println("No capacitance has been entered; zero returned");

            return 0.0D;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTimeConstant() {
        return this.timeConstant;
    }

    // Calculate the zeros and poles in the s-domain
    /**
     * DOCUMENT ME!
     */
    protected void calcPolesZerosS() {
        super.sZeros[0] = new Complex(0.0D, super.sZeros[0].imag());
        super.sPoles[0] = new Complex(-this.timeConstant, super.sPoles[0].imag());
    }

    // REDUNDANT BlackBox METHODS
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in HighPassPassive; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in HighPassPassive; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in HighPassPassive; Not by setSnumer()");
    }

    // Seting the transfer function denominator in the s-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(double[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in HighPassPassive; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(Complex[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in HighPassPassive; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in HighPassPassive; Not by setSdenom()");
    }

    // Setting the transfer function numerator in the z-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in HighPassPassive; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in HighPassPassive; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in HighPassPassive; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in HighPassPassive; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in HighPassPassive; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in HighPassPassive; Not by setZdenom()");
    }
}
