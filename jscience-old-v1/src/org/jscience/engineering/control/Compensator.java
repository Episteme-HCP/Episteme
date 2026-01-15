/*      Class Compensator
*
*       This class contains the constructor to create an instance of
*       a generalised compensator,
*           K(a + s)/(b + s)
*       and the methods needed to use this process in simulation
*       of control loops.
*
*       This class is a subclass of the superclass BlackBox.
*
*       Author:  Michael Thomas Flanagan.
*
*       Created: 14 May 2005
*
*
*       DOCUMENTATION:
*       See Michael T Flanagan's JAVA library on-line web page:
*       Compensator.html
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
public class Compensator extends BlackBox {
    /** DOCUMENT ME! */
    private double kConst; // K constant in compensator equation above

    /** DOCUMENT ME! */
    private double aConst; // a constant in compensator equation above

    /** DOCUMENT ME! */
    private double bConst; // b constant in compensator equation above

    // Constructor
    /**
     * Creates a new Compensator object.
     */
    public Compensator() {
        this.aConst = 1.0D;
        this.bConst = 1.0D;
        this.kConst = 1.0D;
        super.sNumerDeg = 1;
        super.sDenomDeg = 1;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(1.0D), new Complex(1.0D)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(1.0D), new Complex(1.0D)
                });
        super.sZeros = new Complex[] { Complex.ZERO };
        super.sPoles = new Complex[] { Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "Compensator";
        super.fixedName = "Compensator";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Constructor
    /**
     * Creates a new Compensator object.
     *
     * @param kk DOCUMENT ME!
     * @param aa DOCUMENT ME!
     * @param bb DOCUMENT ME!
     */
    public Compensator(double kk, double aa, double bb) {
        this.aConst = aa;
        this.bConst = bb;
        this.kConst = kk;
        super.sNumerDeg = 1;
        super.sDenomDeg = 1;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(this.aConst * kConst), new Complex(kConst)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(this.bConst), new Complex(1.0D)
                });
        super.sZeros = new Complex[] { Complex.ZERO };
        super.sPoles = new Complex[] { Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "Compensator";
        super.fixedName = "Compensator";
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    /**
     * DOCUMENT ME!
     *
     * @param kk DOCUMENT ME!
     * @param aa DOCUMENT ME!
     * @param bb DOCUMENT ME!
     */
    public void setCoeff(double kk, double aa, double bb) {
        this.aConst = aa;
        this.bConst = bb;
        this.kConst = kk;

        Complex[] num = new Complex[] { Complex.ZERO, Complex.ZERO };
        num[0] = new Complex(this.aConst * this.kConst, 0.0D);
        num[1] = new Complex(this.kConst, 0.0D);
        super.sNumer = new ComplexPolynomial(ArrayMathUtils.copy(num));

        Complex[] den = new Complex[] { Complex.ZERO, Complex.ZERO };
        den[0] = new Complex(this.bConst, 0.0D);
        den[1] = new Complex(1.0D, 0.0D);
        super.sDenom = new ComplexPolynomial(ArrayMathUtils.copy(den));
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

        Complex co = new Complex(this.aConst * this.kConst, 0.0);
        super.sNumer.setCoefficientAsComplex(0, co);
        co = new Complex(this.kConst, 0.0);
        super.sNumer.setCoefficientAsComplex(1, co);
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

        Complex co = new Complex(this.aConst * this.kConst, 0.0);
        super.sNumer.setCoefficientAsComplex(0, co);
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
    public double getK() {
        return this.kConst;
    }

    // Calculate the zeros and poles in the s-domain
    /**
     * DOCUMENT ME!
     */
    protected void calcPolesZerosS() {
        super.sZeros[0] = new Complex(-aConst, super.sPoles[0].imag());
        super.sPoles[0] = new Complex(-bConst, super.sPoles[0].imag());
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
