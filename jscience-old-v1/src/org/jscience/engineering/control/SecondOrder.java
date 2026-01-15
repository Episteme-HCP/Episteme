/*      Class SecondOrder
*
*       This class contains the constructor to create an instance of
*       a second order process,
*           a.d^2(output)/dt^2 + b.d(output)/dt + c.output  =  d.input
*       and the methods needed to use this process in simulation
*       of control loops.
*
*       This class is a subclass of the superclass BlackBox.
*
*       Author:  Michael Thomas Flanagan.
*
*       Created: March 2003
*       Updated: 23 April 2003, 3 May 2005
*
*
*       DOCUMENTATION:
*       See Michael T Flanagan's JAVA library on-line web page:
*       SecondOrder.html
*
*   Copyright (c) May 2005  Michael Thomas Flanagan
*
*   PERMISSION TO COPY:
*   Permission to use, copy and modify this software and its documentation for
*   NON-COMMERCIAL purposes is granted, without fee, provided that an acknowledgement
*   to the author, Michael Thomas Flanagan at www.ee.ac.uk/~mflanaga, appears in all copies.
*
*   Dr Michael Thomas Flanagan makes no representations about the suitability
*   or fitness of the software for any or for a particular purpose.
*   Michael Thomas Flanagan shall not be liable for any damages suffered
*   as a result of using, modifying or distributing this software or its derivatives.
*
***************************************************************************************/
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
public class SecondOrder extends BlackBox {
    /** DOCUMENT ME! */
    private double aConst; // a constant in differential equation above

    /** DOCUMENT ME! */
    private double bConst; // b constant in differential equation above

    /** DOCUMENT ME! */
    private double cConst; // c constant in differential equation above

    /** DOCUMENT ME! */
    private double dConst; // d constant in differential equation above

    /** DOCUMENT ME! */
    private double omegaN; // undamped natural frequency (resonant frequency)

    /** DOCUMENT ME! */
    private double zeta; // damping ratio

    /** DOCUMENT ME! */
    private double kConst; // the standard form gain constant

    /** DOCUMENT ME! */
    private double sigma; // attenuation (zeta*omegaN)

    // Constructor
    /**
     * Creates a new SecondOrder object.
     */
    public SecondOrder() {
        this.aConst = 1.0D;
        this.bConst = 1.0D;
        this.cConst = 1.0D;
        this.dConst = 1.0D;
        this.omegaN = 1.0D;
        this.zeta = 0.5D;
        this.kConst = 1.0D;
        this.sigma = 1.0D;
        super.sNumerDeg = 0;
        super.sDenomDeg = 2;
        super.sNumer = new ComplexPolynomial(new Complex[] { new Complex(1.0D) });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(1.0D), new Complex(1.0D), new Complex(1.0D)
                });
        super.sPoles = new Complex[] { Complex.ZERO, Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "Second Order Process";
        super.fixedName = "Second Order Process";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Constructor
    /**
     * Creates a new SecondOrder object.
     *
     * @param aa DOCUMENT ME!
     * @param bb DOCUMENT ME!
     * @param cc DOCUMENT ME!
     * @param dd DOCUMENT ME!
     */
    public SecondOrder(double aa, double bb, double cc, double dd) {
        this.aConst = aa;
        this.bConst = bb;
        this.cConst = cc;
        this.dConst = dd;

        if (this.cConst > 0.0D) {
            this.standardForm();
        }

        super.sNumerDeg = 0;
        super.sDenomDeg = 2;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(this.dConst)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(this.cConst), new Complex(this.bConst),
                    new Complex(this.aConst)
                });
        super.sPoles = new Complex[] { Complex.ZERO, Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "Second Order Process";
        super.fixedName = "Second Order Process";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Set a, b, c and d
    /**
     * DOCUMENT ME!
     *
     * @param aa DOCUMENT ME!
     * @param bb DOCUMENT ME!
     * @param cc DOCUMENT ME!
     * @param dd DOCUMENT ME!
     */
    public void setCoeff(double aa, double bb, double cc, double dd) {
        this.aConst = aa;
        this.bConst = bb;
        this.cConst = cc;
        this.dConst = dd;

        if (this.cConst > 0.0D) {
            this.standardForm();
        }

        Complex[] num = new Complex[] { Complex.ZERO };
        num[0] = new Complex(this.dConst, 0.0);
        super.sNumer = new ComplexPolynomial(ArrayMathUtils.copy(num));

        Complex[] den = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
        den[0] = new Complex(this.cConst, 0.0);
        den[1] = new Complex(this.bConst, 0.0);
        den[2] = new Complex(this.aConst, 0.0);
        super.sDenom = new ComplexPolynomial(ArrayMathUtils.copy(den));
        super.fixedName = "Second Order Process";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Private method for setting the contants of the ntural frequency standard form
    /**
     * DOCUMENT ME!
     */
    private void standardForm() {
        this.omegaN = Math.sqrt(this.cConst / this.aConst);
        this.zeta = this.bConst / (2.0D * this.aConst * this.omegaN);
        this.kConst = this.dConst / this.cConst;
        this.sigma = this.zeta * this.omegaN;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aa DOCUMENT ME!
     */
    public void setA(double aa) {
        this.aConst = aa;

        Complex co = new Complex(this.aConst, 0.0);
        super.sDenom.setCoefficientAsComplex(2, co);

        if (this.cConst > 0.0D) {
            this.standardForm();
        }

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
        super.sDenom.setCoefficientAsComplex(1, co);

        if (this.cConst > 0.0D) {
            this.standardForm();
        }

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
        super.sDenom.setCoefficientAsComplex(0, co);

        if (this.cConst > 0.0D) {
            this.standardForm();
        }

        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param dd DOCUMENT ME!
     */
    public void setD(double dd) {
        this.dConst = dd;

        Complex co = new Complex(this.dConst, 0.0);
        super.sNumer.setCoefficientAsComplex(0, co);

        if (this.cConst > 0.0D) {
            this.standardForm();
        }

        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param zet DOCUMENT ME!
     * @param omega DOCUMENT ME!
     * @param kk DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setStandardForm(double zet, double omega, double kk) {
        if (omega <= 0) {
            throw new IllegalArgumentException(
                "zero or negative natural frequency");
        }

        if (zet < 0) {
            throw new IllegalArgumentException("negative damping ratio");
        }

        this.zeta = zet;
        this.omegaN = omega;
        this.kConst = kk;
        this.sigma = this.omegaN * this.zeta;
        this.reverseStandard();
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param zet DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setZeta(double zet) {
        if (zet < 0) {
            throw new IllegalArgumentException("negative damping ratio");
        }

        this.zeta = zet;
        this.sigma = this.omegaN * this.zeta;
        this.reverseStandard();
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param omega DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOmegaN(double omega) {
        if (omega <= 0) {
            throw new IllegalArgumentException(
                "zero or negative natural frequency");
        }

        this.omegaN = omega;
        this.sigma = this.omegaN * this.zeta;
        this.reverseStandard();
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param kk DOCUMENT ME!
     */
    public void setK(double kk) {
        this.kConst = kk;
        this.reverseStandard();
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Private method for obtaining a, b c and d from zeta, omegan and k
    /**
     * DOCUMENT ME!
     */
    private void reverseStandard() {
        this.aConst = this.omegaN * this.omegaN;
        this.bConst = 2.0D * this.zeta * this.omegaN;
        this.cConst = 1.0D;
        this.dConst = this.kConst * this.aConst;

        Complex[] num = new Complex[] { Complex.ZERO };
        num[0] = new Complex(this.dConst, 0.0);
        super.sNumer = new ComplexPolynomial(ArrayMathUtils.copy(num));

        Complex[] den = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
        den[0] = new Complex(this.cConst, 0.0);
        den[1] = new Complex(this.bConst, 0.0);
        den[2] = new Complex(this.aConst, 0.0);
        super.sDenom = new ComplexPolynomial(ArrayMathUtils.copy(den));
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
    public double getD() {
        return this.dConst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getOmegaN() {
        return this.omegaN;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZeta() {
        return this.zeta;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getK() {
        return this.kConst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAttenuation() {
        return this.sigma;
    }

    // Calculate the zeros and poles in the s-domain
    /**
     * DOCUMENT ME!
     */
    protected void calcPolesZerosS() {
        super.sPoles = super.sDenom.roots();
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

    // Perform z transform using an already set delta T
    /**
     * DOCUMENT ME!
     */
    public void zTransform() {
        if (super.deltaT == 0.0D) {
            System.out.println(
                "z-transform attempted in SecondOrder with a zero sampling period");
        }

        if (ztransMethod == 0) {
            this.mapstozAdHoc();
        } else {
            Complex[] ncoef = null;
            Complex[] dcoef = null;
            double bT = this.bConst * this.deltaT;
            double t2 = this.deltaT * this.deltaT;
            double cT2 = this.cConst * t2;
            double dT2 = this.dConst * t2;

            switch (this.integMethod) {
            // Trapezium Rule
            case 0:
                ncoef = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
                ncoef[0] = new Complex(dT2 / 4.0D, 0.0D);
                ncoef[1] = new Complex(dT2 / 2.0D, 0.0D);
                ncoef[2] = new Complex(dT2 / 4.0D, 0.0D);
                super.zNumer = new ComplexPolynomial(new Complex[] {
                            new Complex(2.0D)
                        });
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(ncoef));
                super.zNumerDeg = 2;
                dcoef = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
                dcoef[0] = new Complex(this.aConst - bT + (cT2 / 4.0D), 0.0D);
                dcoef[1] = new Complex((-2.0D * this.aConst) + bT +
                        (cT2 / 2.0D), 0.0D);
                dcoef[2] = new Complex(this.aConst + (cT2 / 4.0D), 0.0D);
                super.zDenom = new ComplexPolynomial(new Complex[] {
                            new Complex(2.0D)
                        });
                super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(dcoef));
                super.zDenomDeg = 2;
                super.zZeros = zNumer.roots();
                super.zPoles = zDenom.roots();

                break;

            //  Backward Rectangular Rule
            case 1:
                ncoef = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
                ncoef[0] = new Complex(0.0D, 0.0D);
                ncoef[1] = new Complex(0.0D, 0.0D);
                ncoef[2] = new Complex(dT2, 0.0D);
                super.zNumer = new ComplexPolynomial(new Complex[] {
                            new Complex(2.0D)
                        });
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(ncoef));
                super.zNumerDeg = 2;
                dcoef = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
                dcoef[0] = new Complex(this.aConst - bT, 0.0D);
                dcoef[1] = new Complex(-2.0D * this.aConst, 0.0D);
                dcoef[2] = new Complex(this.aConst + bT + cT2, 0.0D);
                super.zDenom = new ComplexPolynomial(new Complex[] {
                            new Complex(2.0D)
                        });
                super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(dcoef));
                super.zDenomDeg = 2;
                super.zPoles = zDenom.roots();
                super.zZeros = new Complex[] { Complex.ZERO, Complex.ZERO };
                super.zZeros[0] = new Complex(0.0D, 0.0D);
                super.zZeros[1] = new Complex(0.0D, 0.0D);

                break;

            // Foreward Rectangular Rule
            case 2:
                ncoef = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
                ncoef[0] = new Complex(0.0D, 0.0D);
                ncoef[1] = new Complex(0.0D, 0.0D);
                ncoef[2] = new Complex(dT2, 0.0D);
                super.zNumer = new ComplexPolynomial(new Complex[] {
                            new Complex(2.0D)
                        });
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(ncoef));
                super.zNumerDeg = 2;
                dcoef = new Complex[] { Complex.ZERO, Complex.ZERO, Complex.ZERO };
                dcoef[0] = new Complex(this.aConst - bT + cT2, 0.0D);
                dcoef[1] = new Complex((-2.0D * this.aConst) + bT, 0.0D);
                dcoef[2] = new Complex(this.aConst, 0.0D);
                super.zDenom = new ComplexPolynomial(new Complex[] {
                            new Complex(2.0D)
                        });
                super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(dcoef));
                super.zDenomDeg = 2;
                super.zPoles = zDenom.roots();
                super.zZeros = new Complex[] { Complex.ZERO, Complex.ZERO };
                super.zZeros[0] = new Complex(0.0D, 0.0D);
                super.zZeros[1] = new Complex(0.0D, 0.0D);

                break;

            default:
                System.out.println(
                    "Integration method option in SecondOrder must be 0,1 or 2");
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
        super.deadTimeWarning("zTransform");
        zTransform();
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
        super.outputT[sampLen - 1] = ((this.cConst * super.inputT[sampLen - 1]) +
            ((this.bConst * (super.inputT[sampLen - 1] -
            super.inputT[sampLen - 3])) / super.deltaT) +
            ((this.cConst * (super.inputT[sampLen - 1] -
            (2.0D * super.inputT[sampLen - 2]) + super.inputT[sampLen - 3])) / (super.deltaT * super.deltaT))) / this.dConst;
    }

    // Get the s-domain zeros
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex[] getSzeros() {
        System.out.println(
            "This standard second order process (class SecondOrder) has no s-domain zeros");

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
            "Transfer numerator is set by a constructor or special methods in SecondOrder; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in SecondOrder; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in SecondOrder; Not by setSnumer()");
    }

    // Seting the transfer function denominator in the s-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(double[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in SecondOrder; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(Complex[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in SecondOrder; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in SecondOrder; Not by setSdenom()");
    }

    // Setting the transfer function numerator in the z-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in SecondOrder; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in SecondOrder; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in SecondOrder; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in SecondOrder; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in SecondOrder; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in SecondOrder; Not by setZdenom()");
    }
}
