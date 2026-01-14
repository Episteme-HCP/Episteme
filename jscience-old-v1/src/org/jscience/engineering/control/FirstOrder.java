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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class FirstOrder extends BlackBox {
    /** DOCUMENT ME! */
    private double aConst; // a constant in differential equation above

    /** DOCUMENT ME! */
    private double bConst; // b constant in differential equation above

    /** DOCUMENT ME! */
    private double cConst; // c constant in differential equation above

    // Constructor
    /**
     * Creates a new FirstOrder object.
     */
    public FirstOrder() {
        this.aConst = 1.0D;
        this.bConst = 1.0D;
        this.cConst = 1.0D;
        super.sNumerDeg = 0;
        super.sDenomDeg = 1;
        super.sNumer = new ComplexPolynomial(new Complex[] { new Complex(1.0D) });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(1.0D), new Complex(1.0D)
                });
        super.sPoles = new Complex[] { Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "First Order Process";
        super.fixedName = "First Order Process";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Constructor
    /**
     * Creates a new FirstOrder object.
     *
     * @param aa DOCUMENT ME!
     * @param bb DOCUMENT ME!
     * @param cc DOCUMENT ME!
     */
    public FirstOrder(double aa, double bb, double cc) {
        this.aConst = aa;
        this.bConst = bb;
        this.cConst = cc;
        super.sNumerDeg = 0;
        super.sDenomDeg = 1;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(this.cConst)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(this.bConst), new Complex(this.aConst)
                });
        super.sPoles = new Complex[] { Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "First Order Process";
        super.fixedName = "First Order Process";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param aa DOCUMENT ME!
     * @param bb DOCUMENT ME!
     * @param cc DOCUMENT ME!
     */
    public void setCoeff(double aa, double bb, double cc) {
        this.aConst = aa;
        this.bConst = bb;
        this.cConst = cc;

        Complex[] num = new Complex[] { Complex.ZERO };
        num[0] = new Complex(this.cConst, 0.0);
        super.sNumer = new ComplexPolynomial(ArrayMathUtils.copy(num));

        Complex[] den = new Complex[] { Complex.ZERO, Complex.ZERO };
        den[0] = new Complex(this.bConst, 0.0);
        den[1] = new Complex(this.aConst, 0.0);
        super.sDenom = new ComplexPolynomial(ArrayMathUtils.copy(den));
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param aa DOCUMENT ME!
     */
    public void setA(double aa) {
        this.aConst = aa;

        Complex co = new Complex(this.aConst, 0.0);
        super.sDenom.setCoefficientAsComplex(1, co);
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param bb DOCUMENT ME!
     */
    public void setB(double bb) {
        this.bConst = bb;

        Complex co = new Complex(this.bConst, 0.0);
        super.sDenom.setCoefficientAsComplex(0, co);
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param cc DOCUMENT ME!
     */
    public void setC(double cc) {
        this.cConst = cc;

        Complex co = new Complex(this.cConst, 0.0);
        super.sNumer.setCoefficientAsComplex(0, co);
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getA() {
        return this.aConst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getB() {
        return this.bConst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getC() {
        return this.cConst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTimeConstant() {
        return this.aConst / this.bConst;
    }

    // Calculate the zeros and poles in the s-domain
    /**
     * DOCUMENT ME!
     */
    protected void calcPolesZerosS() {
        super.sPoles[0] = new Complex(-bConst / aConst, super.sPoles[0].imag());
    }

    // Perform z transform using an already set delta T
    /**
     * DOCUMENT ME!
     */
    public void zTransform() {
        if (super.deltaT == 0.0D) {
            System.out.println(
                "z-transform attempted in FirstOrder with a zero sampling period");
        }

        super.deadTimeWarning("zTransform");

        if (ztransMethod == 0) {
            this.mapstozAdHoc();
        } else {
            Complex[] ncoef = null;
            Complex[] dcoef = null;

            switch (this.integMethod) {
            // Trapezium rule
            case 0:
                ncoef = new Complex[] { Complex.ZERO, Complex.ZERO };
                ncoef[0] = new Complex(this.deltaT * this.cConst, 0.0D);
                ncoef[1] = new Complex(this.deltaT * this.cConst, 0.0D);
                super.zNumer = new ComplexPolynomial(new Complex[] {
                            new Complex(1.0D)
                        });
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(ncoef));
                super.zNumerDeg = 1;
                dcoef = new Complex[] { Complex.ZERO, Complex.ZERO };
                dcoef[0] = new Complex((this.bConst * this.deltaT) -
                        (2 * this.aConst), 0.0D);
                dcoef[1] = new Complex((this.bConst * this.deltaT) +
                        (2 * this.aConst), 0.0D);
                super.zDenom = new ComplexPolynomial(new Complex[] {
                            new Complex(1.0D)
                        });
                super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(dcoef));
                super.zDenomDeg = 1;
                super.zZeros = new Complex[] { Complex.ZERO };
                super.zZeros[0] = new Complex(-1.0D, 0.0D);
                super.zPoles = new Complex[] { Complex.ZERO };
                super.zPoles[0] = new Complex(((2.0D * this.aConst) -
                        (super.deltaT * this.bConst)) / ((2.0D * this.aConst) +
                        (super.deltaT * this.bConst)), 0.0D);

                break;

            // Backward rectangulr rule
            case 1:
                ncoef = new Complex[] { Complex.ZERO, Complex.ZERO };
                ncoef[0] = new Complex(0.0D, 0.0D);
                ncoef[1] = new Complex(this.cConst * this.deltaT, 0.0D);
                super.zNumer = new ComplexPolynomial(new Complex[] {
                            new Complex(1.0D)
                        });
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(ncoef));
                super.zNumerDeg = 1;
                dcoef = new Complex[] { Complex.ZERO, Complex.ZERO };
                dcoef[0] = new Complex((this.bConst * this.deltaT) +
                        this.aConst, 0.0D);
                dcoef[1] = new Complex(this.aConst, 0.0D);
                super.zDenom = new ComplexPolynomial(new Complex[] {
                            new Complex(1.0D)
                        });
                super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(dcoef));
                super.zDenomDeg = 1;
                super.zZeros = new Complex[] { Complex.ZERO };
                super.zZeros[0] = new Complex(0.0D, 0.0D);
                super.zPoles = new Complex[] { Complex.ZERO };
                super.zPoles[0] = new Complex(this.aConst / ((super.deltaT * this.bConst) +
                        this.aConst), 0.0D);

                break;

            // Foreward rectangular rule
            case 2:
                ncoef = new Complex[] { Complex.ZERO };
                ncoef[0] = new Complex(this.cConst * this.deltaT, 0.0D);
                super.zNumer = new ComplexPolynomial(new Complex[] {
                            new Complex(0)
                        });
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(ncoef));
                super.zNumerDeg = 0;
                dcoef = new Complex[] { Complex.ZERO, Complex.ZERO };
                dcoef[0] = new Complex(-this.aConst, 0.0D);
                dcoef[1] = new Complex((this.bConst * this.deltaT) -
                        this.aConst, 0.0D);
                super.zDenom = new ComplexPolynomial(new Complex[] {
                            new Complex(1.0D)
                        });
                super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(dcoef));
                super.zDenomDeg = 1;
                super.zPoles = new Complex[] { Complex.ZERO };
                super.zPoles[0] = new Complex(this.aConst / ((super.deltaT * this.bConst) -
                        this.aConst), 0.0D);

                break;

            default:
                System.out.println(
                    "Integration method option in FirstOrder must be 0,1 or 2");
                System.out.println("It was set at " + integMethod);
                System.out.println("z-transform not performed");
            }
        }
    }

    // Perform z transform setting delta T
    /**
     * DOCUMENT ME!
     *
     * @param deltaT DOCUMENT ME!
     */
    public void zTransform(double deltaT) {
        super.deltaT = deltaT;
        zTransform();
    }

    //  Get the s-domain output for a given s-value and a given input.
    /**
     * DOCUMENT ME!
     *
     * @param sValue DOCUMENT ME!
     * @param iinput DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex getOutputS(Complex sValue, Complex iinput) {
        super.sValue = sValue;
        super.inputS = iinput;

        return this.getOutputS();
    }

    //  Get the s-domain output for the stored input and  s-value.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex getOutputS() {
        Complex num = Complex.ONE;
        num = num.multiply(this.cConst);

        Complex den = new Complex(Complex.ZERO);
        den = this.sValue.multiply(this.aConst);

        Complex term = new Complex(Complex.ZERO);
        term = num.divide(den);
        super.outputS = term.multiply(super.inputS);

        if (super.deadTime != 0.0D) {
            super.outputS = super.outputS.multiply(Complex.exp(
                        super.sValue.multiply(-super.deadTime)));
        }

        return super.outputS;
    }

    //  Calculate the current time domain output for a given input and given time
    /**
     * DOCUMENT ME!
     *
     * @param ttime DOCUMENT ME!
     * @param inp DOCUMENT ME!
     */
    public void calcOutputT(double ttime, double inp) {
        if (ttime <= time[this.sampLen - 1]) {
            throw new IllegalArgumentException(
                "Current time equals or is less than previous time");
        }

        super.deltaT = ttime - super.time[this.sampLen - 1];
        super.sampFreq = 1.0D / super.deltaT;
        super.deadTimeWarning("calcOutputT(time, input)");

        for (int i = 0; i < (super.sampLen - 2); i++) {
            super.time[i] = super.time[i + 1];
            super.inputT[i] = super.inputT[i + 1];
            super.outputT[i] = super.outputT[i + 1];
        }

        super.time[super.sampLen - 1] = ttime;
        super.inputT[super.sampLen - 1] = inp;
        super.outputT[super.sampLen - 1] = Double.NaN;
        this.calcOutputT();
    }

    //  Get the output for the stored sampled input, time and deltaT.
    /**
     * DOCUMENT ME!
     */
    public void calcOutputT() {
        super.deadTimeWarning("calcOutputT()");
        super.outputT[sampLen - 1] = ((this.bConst * super.inputT[sampLen - 1]) +
            ((this.aConst * (super.inputT[sampLen - 1] -
            super.inputT[sampLen - 2])) / super.deltaT)) / this.cConst;
    }

    // Get the s-domain zeros
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex[] getSzeros() {
        System.out.println(
            "This standard first order process (class FirstOrder) has no s-domain zeros");

        return null;
    }

    // REDUNDANT BlackBox METHODS
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in FirstOrder; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in FirstOrder; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in FirstOrder; Not by setSnumer()");
    }

    // Seting the transfer function denominator in the s-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(double[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in FirstOrder; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(Complex[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in FirstOrder; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in FirstOrder; Not by setSdenom()");
    }

    // Setting the transfer function numerator in the z-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in FirstOrder; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in FirstOrder; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in FirstOrder; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in FirstOrder; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in FirstOrder; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in FirstOrder; Not by setZdenom()");
    }
}
