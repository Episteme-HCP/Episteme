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


import org.jscience.engineering.control.gui.PlotGraph;
import org.jscience.engineering.control.gui.PlotPoleZero;
import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.matrices.ComplexMatrix;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class BlackBox {
    /**
     * DOCUMENT ME!
     */
    protected int sampLen = 3; // Length of array of stored inputs, outputs and times

    /**
     * DOCUMENT ME!
     */
    protected double[] inputT = new double[this.sampLen]; // Array of input signal in the time domain

    /**
     * DOCUMENT ME!
     */
    protected double[] outputT = new double[this.sampLen]; // Array of output signal in the time domain

    /**
     * DOCUMENT ME!
     */
    protected double[] time = new double[this.sampLen]; // Array of time at which inputs were taken (seconds)

    /**
     * DOCUMENT ME!
     */
    protected double forgetFactor = 1.0D; // Forgetting factor, e.g. in exponential forgetting of error values

    /**
     * DOCUMENT ME!
     */
    protected double deltaT = 0.0D; // Sampling time (seconds)

    /**
     * DOCUMENT ME!
     */
    protected double sampFreq = 0.0D; // Sampling frequency (Hz)

    /**
     * DOCUMENT ME!
     */
    protected Complex inputS = new Complex(Complex.ZERO); // Input signal in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected Complex outputS = new Complex(Complex.ZERO); // Output signal in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected Complex sValue = new Complex(Complex.ZERO); // Laplacian s

    /**
     * DOCUMENT ME!
     */
    protected Complex zValue = new Complex(Complex.ZERO); // z-transform z

    /**
     * DOCUMENT ME!
     */
    protected ComplexPolynomial sNumer = new ComplexPolynomial(new Complex[]{new Complex(1.0D)}); // Transfer function numerator in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected ComplexPolynomial sDenom = new ComplexPolynomial(new Complex[]{new Complex(1.0D)}); // Transfer function denominator in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected ComplexPolynomial zNumer = new ComplexPolynomial(new Complex[]{new Complex(1.0D)}); // Transfer function numerator in the z-domain

    /**
     * DOCUMENT ME!
     */
    protected ComplexPolynomial zDenom = new ComplexPolynomial(new Complex[]{new Complex(1.0D)}); // Transfer function denominator in the z-domain

    /**
     * DOCUMENT ME!
     */
    protected Complex[] sPoles = null; // Poles in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected Complex[] sZeros = null; // Zeros in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected Complex[] zPoles = null; // Poles in the z-domain

    /**
     * DOCUMENT ME!
     */
    protected Complex[] zZeros = null; // Zeros in the z-domain

    /**
     * DOCUMENT ME!
     */
    protected int sNumerDeg = 0; // Degree of transfer function numerator in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected int sDenomDeg = 0; // Degree of transfer function denominator in the s-domain

    /**
     * DOCUMENT ME!
     */
    protected int zNumerDeg = 0; // Degree of transfer function numerator in the z-domain

    /**
     * DOCUMENT ME!
     */
    protected int zDenomDeg = 0; // Degree of transfer function denominator in the z-domain

    /**
     * DOCUMENT ME!
     */
    protected double deadTime = 0.0D; // Time delay between an input and the matching output [in s-domain = exp(-s.deadTime)]

    /**
     * DOCUMENT ME!
     */
    protected int orderPade = 2; // Order(1 to 4)of the pade approximation for exp(-sT)
    //   default option = 2

    /**
     * DOCUMENT ME!
     */
    protected ComplexPolynomial sNumerPade = new ComplexPolynomial(new Complex[]{new Complex(1.0D)}); // Transfer function numerator in the s-domain including Pade approximation

    /**
     * DOCUMENT ME!
     */
    protected ComplexPolynomial sDenomPade = new ComplexPolynomial(new Complex[]{new Complex(1.0D)}); // Transfer function denominator in the s-domain including Pade approximation

    /**
     * DOCUMENT ME!
     */
    protected Complex[] sPolesPade = null; // Poles in the s-domain including Pade approximation

    /**
     * DOCUMENT ME!
     */
    protected Complex[] sZerosPade = null; // Zeros in the s-domain including Pade approximation

    /**
     * DOCUMENT ME!
     */
    protected int sNumerDegPade = 0; // Degree of transfer function numerator in the s-domain including Pade approximation

    /**
     * DOCUMENT ME!
     */
    protected int sDenomDegPade = 0; // Degree of transfer function denominator in the s-domain including Pade approximation

    /**
     * DOCUMENT ME!
     */
    protected boolean maptozero = true; // if true  infinity s zeros map to zero
    // if false infinity s zeros map to minus one

    /**
     * DOCUMENT ME!
     */
    protected boolean padeAdded = false; // if true  Pade poles and zeros added
    // if false No Pade poles and zeros added

    /**
     * DOCUMENT ME!
     */
    protected double integrationSum = 0.0D; // Stored integration sum in numerical integrations

    /**
     * DOCUMENT ME!
     */
    protected int integMethod = 0; // numerical integration method
    //  = 0   Trapezium Rule [default option]
    //  = 1   Backward Rectangular Rule
    //  = 2   Foreward Rectangular Rule

    /**
     * DOCUMENT ME!
     */
    protected int ztransMethod = 0; // z trasform method
    //  = 0   s -> z mapping (ad hoc procedure) from the continuous time domain erived s domain functions
    //  = 1   specific z transform, e.g. of a difference equation

    /**
     * DOCUMENT ME!
     */
    protected String name = "BlackBox"; // Superclass or subclass name, e.g. pid, pd, firstorder.
    // user may rename an instance of the superclass or subclass

    /**
     * DOCUMENT ME!
     */
    protected String fixedName = "BlackBox"; // Super class or subclass permanent name, e.g. pid, pd, firstorder.

    // user must NOT change fixedName in any instance of the superclass or subclass
    // fixedName is used as an identifier in classes such as OpenPath, ClosedLoop

    /**
     * DOCUMENT ME!
     */
    protected int nPlotPoints = 100; // number of points used tp lot response curves, e.g. step input response curve

    // Constructor
    public BlackBox() {
    }

    // Set the transfer function numerator in the s-domain
    // Enter as an array of real (double) coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setSnumer(double[] coeff) {
        this.sNumerDeg = coeff.length - 1;
        this.sNumer = new ComplexPolynomial(ArrayMathUtils.toComplex(coeff));
        this.calcPolesZerosS();
        this.addDeadTimeExtras();
    }

    // Method to set extra terms to s-domain numerator and denominator and
    // to calculate extra zeros and poles if the dead time is not zero.
    protected void addDeadTimeExtras() {
        this.sNumerDegPade = this.sNumerDeg;
        this.sNumerPade = (ComplexPolynomial) this.sNumer.clone();
        this.sDenomDegPade = this.sDenomDeg;
        this.sDenomPade = (ComplexPolynomial) this.sDenom.clone();

        if (this.deadTime == 0.0D) {
            this.transferPolesZeros();
        } else {
            this.pade();
        }
    }

    // Set the transfer function numerator in the s-domain
    // Enter as an array of Complex coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setSnumer(Complex[] coeff) {
        this.sNumerDeg = coeff.length - 1;
        this.sNumer = new ComplexPolynomial(coeff);
        this.calcPolesZerosS();
        this.addDeadTimeExtras();
    }

    // Set the transfer function numerator in the s-domain
    // Enter as an existing instance of ComplexPolynomial
    public void setSnumer(ComplexPolynomial coeff) {
        this.sNumerDeg = coeff.degree();
        this.sNumer = (ComplexPolynomial) coeff.clone();
        this.calcPolesZerosS();
        this.addDeadTimeExtras();
    }

    // Set the transfer function denominator in the s-domain
    // Enter as an array of real (double) coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setSdenom(double[] coeff) {
        this.sDenomDeg = coeff.length - 1;
        this.sDenom = new ComplexPolynomial(ArrayMathUtils.toComplex(coeff));
        this.calcPolesZerosS();
        this.addDeadTimeExtras();
    }

    // Set the transfer function denomonator in the s-domain
    // Enter as an array of Complex coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setSdenom(Complex[] coeff) {
        this.sDenomDeg = coeff.length - 1;
        this.sDenom = new ComplexPolynomial(coeff);
        this.calcPolesZerosS();
        this.addDeadTimeExtras();
    }

    // Set the transfer function denominator in the s-domain
    // Enter as an existing instance of ComplexPolynomial
    public void setSdenom(ComplexPolynomial coeff) {
        this.sDenomDeg = coeff.degree();
        this.sDenom = (ComplexPolynomial) coeff.clone();
        this.calcPolesZerosS();
        this.addDeadTimeExtras();
    }

    // Set the dead time
    public void setDeadTime(double deadtime) {
        this.deadTime = deadtime;
        this.pade();
    }

    // Set the dead time and the Pade approximation order
    public void setDeadTime(double deadtime, int orderPade) {
        this.deadTime = deadtime;

        if (orderPade > 5) {
            orderPade = 4;
            System.out.println(
                    "BlackBox does not support Pade approximations above an order of 4");
            System.out.println("The order has been set to 4");
        }

        if (orderPade < 1) {
            orderPade = 1;
            System.out.println("Pade approximation order was less than 1");
            System.out.println("The order has been set to 1");
        }

        this.orderPade = orderPade;
        this.pade();
    }

    // Set the Pade approximation order
    public void setPadeOrder(int orderPade) {
        if (orderPade > 5) {
            orderPade = 4;
            System.out.println(
                    "BlackBox does not support Pade approximations above an order of 4");
            System.out.println("The order has been set to 4");
        }

        if (orderPade < 1) {
            orderPade = 2;
            System.out.println("Pade approximation order was less than 1");
            System.out.println("The order has been set to 2");
        }

        this.orderPade = orderPade;
        this.pade();
    }

    // Get the dead time
    public double getDeadTime() {
        return this.deadTime;
    }

    // Get the Pade approximation order
    public int getPadeOrder() {
        return this.orderPade;
    }

    // Resets the s-domain Pade inclusive numerator and denominator adding a Pade approximation
    // Also calculates and stores additional zeros and poles arising rfom the Pade approximation
    protected void pade() {
        ComplexPolynomial sNumerExtra = null;
        ComplexPolynomial sDenomExtra = null;
        Complex[] newZeros = null;
        Complex[] newPoles = null;

        switch (orderPade) {
            case 1:
                this.sNumerDegPade = this.sNumerDeg + 1;
                this.sDenomDegPade = this.sDenomDeg + 1;
                this.sNumerPade = new ComplexPolynomial(new Complex[]{new Complex(sNumerDegPade)});
                this.sDenomPade = new ComplexPolynomial(new Complex[]{new Complex(sDenomDegPade)});
                sNumerExtra = new ComplexPolynomial(new Complex[]{new Complex(1.0D), new Complex(-this.deadTime / 2.0D)});
                sDenomExtra = new ComplexPolynomial(new Complex[]{new Complex(1.0D), new Complex(this.deadTime / 2.0D)});
                this.sNumerPade = (ComplexPolynomial) this.sNumer.multiply(sNumerExtra);
                this.sDenomPade = (ComplexPolynomial) this.sDenom.multiply(sDenomExtra);
                newZeros = new Complex[]{Complex.ZERO};
                newZeros[0] = new Complex(2.0 / this.deadTime, 0.0D);
                newPoles = new Complex[]{Complex.ZERO};
                newPoles[0] = new Complex(-2.0 / this.deadTime, 0.0D);

                break;

            case 2:
                this.sNumerDegPade = this.sNumerDeg + 2;
                this.sDenomDegPade = this.sDenomDeg + 2;
                this.sNumerPade = new ComplexPolynomial(new Complex[]{new Complex(sNumerDegPade)});
                this.sDenomPade = new ComplexPolynomial(new Complex[]{new Complex(sDenomDegPade)});
                sNumerExtra = new ComplexPolynomial(new Complex[]{new Complex(1.0D), new Complex(-this.deadTime / 2.0D), new Complex(
                        Math.pow(this.deadTime, 2) / 12.0D)});
                sDenomExtra = new ComplexPolynomial(new Complex[]{new Complex(1.0D), new Complex(this.deadTime / 2.0D), new Complex(
                        Math.pow(this.deadTime, 2) / 12.0D)});
                this.sNumerPade = (ComplexPolynomial) this.sNumer.multiply(sNumerExtra);
                this.sDenomPade = (ComplexPolynomial) this.sDenom.multiply(sDenomExtra);
                newZeros = sNumerExtra.roots();
                newPoles = sDenomExtra.roots();

                break;

            case 3:
                this.sNumerDegPade = this.sNumerDeg + 3;
                this.sDenomDegPade = this.sDenomDeg + 3;
                this.sNumerPade = new ComplexPolynomial(new Complex[]{new Complex(sNumerDegPade)});
                this.sDenomPade = new ComplexPolynomial(new Complex[]{new Complex(sDenomDegPade)});

                double[] termn3 = new double[4];
                termn3[0] = 1.0D;
                termn3[1] = -this.deadTime / 2.0D;
                termn3[2] = Math.pow(this.deadTime, 2) / 10.0D;
                termn3[3] = -Math.pow(this.deadTime, 3) / 120.0D;
                sNumerExtra = new ComplexPolynomial(new Complex[]{new Complex(termn3[0]), new Complex(termn3[1]), new Complex(termn3[2]), new Complex(termn3[3])});
                this.sNumerPade = (ComplexPolynomial) this.sNumer.multiply(sNumerExtra);
                newZeros = sNumerExtra.roots();

                double[] termd3 = new double[4];
                termd3[0] = 1.0D;
                termd3[1] = this.deadTime / 2.0D;
                termd3[2] = Math.pow(this.deadTime, 2) / 10.0D;
                termd3[3] = Math.pow(this.deadTime, 3) / 120.0D;
                sDenomExtra = new ComplexPolynomial(new Complex[]{new Complex(termd3[0]), new Complex(termd3[1]), new Complex(termd3[2]), new Complex(termd3[3])});
                this.sDenomPade = (ComplexPolynomial) this.sDenom.multiply(sDenomExtra);
                newPoles = sDenomExtra.roots();

                break;

            case 4:
                this.sNumerDegPade = this.sNumerDeg + 4;
                this.sDenomDegPade = this.sDenomDeg + 4;
                this.sNumerPade = new ComplexPolynomial(new Complex[]{new Complex(sNumerDegPade)});
                this.sDenomPade = new ComplexPolynomial(new Complex[]{new Complex(sDenomDegPade)});

                double[] termn4 = new double[5];
                termn4[0] = 1.0D;
                termn4[1] = -this.deadTime / 2.0D;
                termn4[2] = (3.0D * Math.pow(this.deadTime, 2)) / 28.0D;
                termn4[3] = -Math.pow(this.deadTime, 3) / 84.0D;
                termn4[4] = Math.pow(this.deadTime, 4) / 1680.0D;
                sNumerExtra = new ComplexPolynomial(ArrayMathUtils.toComplex(termn4));
                this.sNumerPade = (ComplexPolynomial) this.sNumer.multiply(sNumerExtra);
                newZeros = sNumerExtra.roots();

                double[] termd4 = new double[5];
                termd4[0] = 1.0D;
                termd4[1] = this.deadTime / 2.0D;
                termd4[2] = (3.0D * Math.pow(this.deadTime, 2)) / 28.0D;
                termd4[3] = Math.pow(this.deadTime, 3) / 84.0D;
                termd4[4] = Math.pow(this.deadTime, 4) / 1680.0D;
                sDenomExtra = new ComplexPolynomial(ArrayMathUtils.toComplex(termd4));
                this.sDenomPade = (ComplexPolynomial) this.sDenom.multiply(sDenomExtra);
                newPoles = sDenomExtra.roots();

                break;

            default:
                this.orderPade = 2;
                this.sNumerDegPade = this.sNumerDeg + 2;
                this.sDenomDegPade = this.sDenomDeg + 2;
                this.sNumerPade = new ComplexPolynomial(new Complex[]{new Complex(sNumerDegPade)});
                this.sDenomPade = new ComplexPolynomial(new Complex[]{new Complex(sDenomDegPade)});
                sNumerExtra = new ComplexPolynomial(new Complex[]{new Complex(1.0D), new Complex(-this.deadTime / 2.0D), new Complex(
                        Math.pow(this.deadTime, 2) / 12.0D)});
                sDenomExtra = new ComplexPolynomial(new Complex[]{new Complex(1.0D), new Complex(this.deadTime / 2.0D), new Complex(
                        Math.pow(this.deadTime, 2) / 12.0D)});
                this.sNumerPade = (ComplexPolynomial) this.sNumer.multiply(sNumerExtra);
                this.sDenomPade = (ComplexPolynomial) this.sDenom.multiply(sDenomExtra);
                newZeros = sNumerExtra.roots();
                newPoles = sDenomExtra.roots();

                break;
        }

        // store zeros and poles arising from the Pade term
        if ((this.sNumerPade != null) && (this.sNumerDegPade > 0)) {
            sZerosPade = oneDarray(sNumerDegPade);

            for (int i = 0; i < sNumerDeg; i++) {
                sZerosPade[i] = new Complex(sZeros[i]);
            }

            for (int i = 0; i < this.orderPade; i++) {
                sZerosPade[i + sNumerDeg] = new Complex(newZeros[i]);
            }
        }

        if ((this.sDenomPade != null) && (this.sDenomDegPade > 0)) {
            sPolesPade = oneDarray(sDenomDegPade);

            for (int i = 0; i < sDenomDeg; i++) {
                sPolesPade[i] = new Complex(sPoles[i]);
            }

            for (int i = 0; i < this.orderPade; i++) {
                sPolesPade[i + sDenomDeg] = new Complex(newPoles[i]);
            }
        }

        this.zeroPoleCancellation();
        this.padeAdded = true;
    }

    // Copies s-domain poles and zeros from the s-domain arrays to the s-domain Pade arrays
    // used when deadTime is zero
    protected void transferPolesZeros() {
        this.sNumerDegPade = this.sNumerDeg;
        this.sNumerPade = (ComplexPolynomial) this.sNumer.clone();

        if (this.sNumerDeg > 0) {
            this.sZerosPade = oneDarray(this.sNumerDeg);

            for (int i = 0; i < this.sNumerDeg; i++)
                this.sZerosPade[i] = new Complex(this.sZeros[i]);
        }

        this.sDenomDegPade = this.sDenomDeg;
        this.sDenomPade = (ComplexPolynomial) this.sDenom.clone();

        if (this.sDenomDeg > 0) {
            this.sPolesPade = oneDarray(this.sDenomDeg);

            for (int i = 0; i < this.sDenomDeg; i++)
                this.sPolesPade[i] = new Complex(this.sPoles[i]);
        }

        this.zeroPoleCancellation();
        this.padeAdded = true;
    }

    // Get the Pade approximation order
    public int orderPade() {
        return this.orderPade;
    }

    // Warning message if dead time greater than sampling period
    protected boolean deadTimeWarning(String method) {
        boolean warning = false; // warning true if dead time is greater than the sampling period
        // false if not

        if (this.deadTime > this.deltaT) {
            System.out.println(this.name + "." + method +
                    ": The dead time is greater than the sampling period");
            System.out.println("Dead time:       " + this.deadTime);
            System.out.println("Sampling period: " + this.deltaT);
            System.out.println(
                    "!!! The results of this program may not be physically meaningful !!!");
            warning = true;
        }

        return warning;
    }

    // Perform z transform for a given delta T
    // Uses maptozAdHoc in this class but may be overridden in a subclass
    public void zTransform(double deltat) {
        this.mapstozAdHoc(deltat);
    }

    // Perform z transform using an already set delta T
    // Uses maptozAdHoc in this class but may be overridden in a subclass
    public void zTransform() {
        this.mapstozAdHoc();
    }

    // Map s-plane zeros and poles of the transfer function onto the z-plane using the ad-hoc method
    //  for a given sampling period.
    //  References:
    //  John Dorsey, Continuous and Discrete Control Systems, pp 490-491, McGraw Hill (2002)
    //  J R Leigh, Applied Digital Control, pp 78-80, Prentice-Hall (1985)
    public void mapstozAdHoc(double deltaT) {
        this.deltaT = deltaT;
        this.mapstozAdHoc();
    }

    // Map s-plane zeros and poles of the transfer function onto the z-plane using the ad-hoc method
    //  for an already set sampling period.
    //  References:
    //  John Dorsey, Continuous and Discrete Control Systems, pp 490-491, McGraw Hill (2002)
    //  J R Leigh, Applied Digital Control, pp 78-80, Prentice-Hall (1985)
    public void mapstozAdHoc() {
        this.deadTimeWarning("mapstozAdHoc");

        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        // Calculate z-poles
        this.zDenomDeg = this.sDenomDegPade;

        ComplexPolynomial root = new ComplexPolynomial(new Complex[]{new Complex(1.0D)});
        this.zDenom = new ComplexPolynomial(new Complex[]{new Complex(this.zDenomDeg)});

        if (zDenomDeg > 0) {
            this.zPoles = oneDarray(this.zDenomDeg);

            for (int i = 0; i < this.zDenomDeg; i++) {
                zPoles[i] = Complex.exp(this.sPolesPade[i].multiply(this.deltaT));
            }

            this.zDenom = ComplexPolynomial.rootsToPolynomial(zPoles);
        }

        // Calculate z-zeros
        // number of zeros from infinity poles
        int infZeros = this.sDenomDegPade;

        // check that total zeros does not exceed total poles
        if ((infZeros + this.sNumerDegPade) > this.sDenomDegPade) {
            infZeros = this.sDenomDegPade - this.sNumerDegPade;
        }

        // total number of zeros
        this.zNumerDeg = this.sNumerDegPade + infZeros;
        this.zNumer = new ComplexPolynomial(new Complex[]{new Complex(zNumerDeg)});
        this.zZeros = oneDarray(zNumerDeg);

        // zero values
        if (this.zNumerDeg > 0) {
            for (int i = 0; i < this.sNumerDegPade; i++) {
                zZeros[i] = Complex.exp(sZerosPade[i].multiply(this.deltaT));
            }

            if (infZeros > 0) {
                if (maptozero) {
                    for (int i = this.sNumerDegPade; i < this.zNumerDeg; i++) {
                        zZeros[i] = Complex.ZERO;
                    }
                } else {
                    for (int i = this.sNumerDegPade; i < this.zNumerDeg; i++) {
                        zZeros[i] = new Complex(-1, 0);
                    }
                }
            }

            this.zNumer = ComplexPolynomial.rootsToPolynomial(this.zZeros);
        }

        // Match s and z steady state gains
        this.sValue = Complex.ZERO;
        this.zValue = Complex.ONE;

        boolean testzeros = true;

        while (testzeros) {
            testzeros = false;

            if (this.sDenomDegPade > 0) {
                for (int i = 0; i < this.sDenomDegPade; i++) {
                    if (truncate(this.sPolesPade[i], 3).equals(truncate(this.sValue,
                            3))) {
                        testzeros = true;
                    }
                }
            }

            if (!testzeros && (this.sNumerDegPade > 0)) {
                for (int i = 0; i < this.sDenomDegPade; i++) {
                    if (truncate(this.sZerosPade[i], 3).equals(truncate(this.sValue,
                            3))) {
                        testzeros = true;
                    }
                }
            }

            if (!testzeros && (this.zDenomDeg > 0)) {
                for (int i = 0; i < this.zDenomDeg; i++) {
                    if (truncate(this.zPoles[i], 3).equals(truncate(this.zValue,
                            3))) {
                        testzeros = true;
                    }
                }
            }

            if (!testzeros && (this.zNumerDeg > 0)) {
                for (int i = 0; i < this.zDenomDeg; i++) {
                    if (truncate(this.zZeros[i], 3).equals(truncate(this.zValue,
                            3))) {
                        testzeros = true;
                    }
                }
            }

            if (testzeros) {
                this.sValue = truncate(this.sValue.add(Complex.I), 3);
                this.zValue = Complex.exp(truncate(this.sValue.multiply(this.deltaT), 3));
            }
        }

        Complex gs = this.evalTransFunctS(this.sValue);
        Complex gz = this.evalTransFunctZ(this.zValue);
        Complex constant = gs.divide(gz);
        ComplexPolynomial constantPoly = new ComplexPolynomial(new Complex[]{constant});
        this.zNumer = (ComplexPolynomial) this.zNumer.multiply(constantPoly);
    }

    // Set the map infinity zeros to zero or -1 option
    // maptozero:   if true  infinity s zeros map to zero
    //              if false infinity s zeros map to minus one
    // default value = false
    public void setMaptozero(boolean maptozero) {
        this.maptozero = maptozero;
    }

    // Set the transfer function numerator in the z-domain
    // Enter as an array of real (double) coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setZnumer(double[] coeff) {
        this.zNumerDeg = coeff.length - 1;
        this.sNumer = new ComplexPolynomial(ArrayMathUtils.toComplex(coeff));
        this.zZeros = this.zNumer.roots();
    }

    // Set the transfer function numerator in the z-domain
    // Enter as an array of Complex coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setZnumer(Complex[] coeff) {
        this.zNumerDeg = coeff.length - 1;
        this.zNumer = new ComplexPolynomial(coeff);
        this.zZeros = this.zNumer.roots();
    }

    // Set the transfer function numerator in the z-domain
    // Enter as an existing instance of ComplexPolynomial
    public void setZnumer(ComplexPolynomial coeff) {
        this.zNumerDeg = coeff.degree();
        this.zNumer = (ComplexPolynomial) coeff.clone();
        this.zZeros = this.zNumer.roots();
    }

    // Set the transfer function denominator in the z-domain
    // Enter as an array of real (double) coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setZdenom(double[] coeff) {
        this.zDenomDeg = coeff.length - 1;
        this.zDenom = new ComplexPolynomial(ArrayMathUtils.toComplex(coeff));
        this.zPoles = this.zDenom.roots();
    }

    // Set the transfer function denomonatot in the z-domain
    // Enter as an array of Complex coefficients of the polynomial a + bs +c.s.s + d.s.s.s + ....
    public void setZdenom(Complex[] coeff) {
        this.zDenomDeg = coeff.length - 1;
        this.zDenom = new ComplexPolynomial(coeff);
        this.zPoles = this.zDenom.roots();
    }

    // Set the transfer function denominator in the z-domain
    // Enter as an existing instance of ComplexPolynomial
    public void setZdenom(ComplexPolynomial coeff) {
        this.zDenomDeg = coeff.degree();
        this.zDenom = (ComplexPolynomial) coeff.clone();
        this.zPoles = this.zDenom.roots();
    }

    // Set the sampling period
    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
        this.sampFreq = 1.0D / this.deltaT;
        this.deadTimeWarning("setDeltaT");
    }

    // Set the forgetting factor
    public void setForgetFactor(double forget) {
        this.forgetFactor = forget;
    }

    // Set the sampling frequency
    public void setSampFreq(double sfreq) {
        this.sampFreq = sfreq;
        this.deltaT = 1.0D / sampFreq;
        this.deadTimeWarning("setSampFreq");
    }

    // Set the Laplacian s value (s - Complex)
    public void setS(Complex s) {
        this.sValue = new Complex(s);
    }

    // Set the Laplacian s value (s - real + imaginary parts)
    public void setS(double sr, double si) {
        this.sValue = new Complex(sr, si);
    }

    // Set the Laplacian s value (s - imag, real = 0.0)
    public void setS(double si) {
        this.sValue = new Complex(0.0D, si);
    }

    // Set the z-transform z value (z - Complex)
    public void setZ(Complex z) {
        this.zValue = new Complex(z);
    }

    // Set the z-transform z value (z - real + imaginary parts)
    public void setZ(double zr, double zi) {
        this.zValue = new Complex(zr, zi);
    }

    // Set the z transform method
    // 0 = s to z mapping (ad hoc procedure)
    // 1 = specific z transform, e.g. z transform of a difference equation
    public void setZtransformMethod(int ztransMethod) {
        if ((ztransMethod < 0) || (ztransMethod > 1)) {
            System.out.println("z transform method option number " +
                    ztransMethod + " not recognised");
            System.out.println(
                    "z tr methodansform option number set in BlackBox to the default value of 0 (s -> z ad hoc mapping)");
            this.integMethod = 0;
        } else {
            this.ztransMethod = ztransMethod;
        }
    }

    // Set the integration method  [number option]
    // 0 = trapezium, 1 = Backward rectangular, 2 = Foreward rectangular
    public void setIntegrateOption(int integMethod) {
        if ((integMethod < 0) || (integMethod > 2)) {
            System.out.println("integration method option number " +
                    integMethod + " not recognised");
            System.out.println(
                    "integration method option number set in BlackBox to the default value of 0 (trapezium rule)");
            this.integMethod = 0;
        } else {
            this.integMethod = integMethod;
        }
    }

    // Set the integration method  [String option]
    // trapezium; trapezium, tutin.  Backward rectangular; back  backward. Foreward rectangular; foreward, fore
    // Continuous time equivalent: continuous, cont
    public void setIntegrateOption(String integMethodS) {
        if (integMethodS.equals("trapezium") ||
                integMethodS.equals("Trapezium") ||
                integMethodS.equals("tutin") || integMethodS.equals("Tutin")) {
            this.integMethod = 0;
        } else {
            if (integMethodS.equals("backward") ||
                    integMethodS.equals("Backward") ||
                    integMethodS.equals("back") || integMethodS.equals("Back")) {
                this.integMethod = 1;
            } else {
                if (integMethodS.equals("foreward") ||
                        integMethodS.equals("Foreward") ||
                        integMethodS.equals("fore") ||
                        integMethodS.equals("Fore")) {
                    this.integMethod = 2;
                } else {
                    System.out.println("integration method option  " +
                            integMethodS + " not recognised");
                    System.out.println(
                            "integration method option number set in PID to the default value of 0 (trapezium rule)");
                    this.integMethod = 0;
                }
            }
        }
    }

    // Reset the length of the arrays storing the times, time domain inputs and time domain outputs
    public void setSampleLength(int samplen) {
        this.sampLen = samplen;
        this.time = new double[samplen];
        this.inputT = new double[samplen];
        this.outputT = new double[samplen];
    }

    // Reset the name of the black box
    public void setName(String name) {
        this.name = name;
    }

    // Enter current time domain time and input value
    public void setInputT(double ttime, double inputt) {
        for (int i = 0; i < (this.sampLen - 2); i++) {
            this.time[i] = this.time[i + 1];
            this.inputT[i] = this.inputT[i + 1];
        }

        this.time[this.sampLen - 1] = ttime;
        this.inputT[this.sampLen - 1] = inputt;
    }

    // Reset s-domain input
    public void setInputS(Complex input) {
        this.inputS = input;
    }

    // Reset all inputs, outputs and times to zero
    public void resetZero() {
        for (int i = 0; i < (this.sampLen - 1); i++) {
            this.outputT[i] = 0.0D;
            this.inputT[i] = 0.0D;
            this.time[i] = 0.0D;
        }

        this.outputS = Complex.ZERO;
        this.inputS = Complex.ZERO;
    }

    // Calculate the zeros and poles in the s-domain
    // does not include Pade approximation term
    protected void calcPolesZerosS() {
        if (this.sNumer != null) {
            if (this.sNumer.degree() > 0) {
                this.sZeros = this.sNumer.roots();
            }
        }

        if (this.sDenom != null) {
            if (this.sDenom.degree() > 0) {
                this.sPoles = this.sDenom.roots();
            }
        }
    }

    // Eliminates identical poles and zeros in the s-domain
    protected void zeroPoleCancellation() {
        boolean check = false;
        boolean testI = true;
        boolean testJ = true;
        int i = 0;
        int j = 0;

        if ((this.sNumerDegPade == 0) || (this.sDenomDegPade == 0)) {
            testI = false;
        }

        while (testI) {
            j = 0;

            while (testJ) {
                if (this.sZerosPade[i].equals(this.sPolesPade[j])) {
                    for (int k = j + 1; k < this.sDenomDegPade; k++)
                        this.sPolesPade[k - 1] = new Complex(this.sPolesPade[k]);

                    this.sDenomDegPade--;

                    for (int k = i + 1; k < this.sNumerDegPade; k++)
                        this.sZerosPade[k - 1] = new Complex(this.sZerosPade[k]);

                    this.sNumerDegPade--;
                    check = true;
                    testJ = false;
                    i--;
                } else {
                    j++;

                    if (j > (this.sDenomDegPade - 1)) {
                        testJ = false;
                    }
                }
            }

            i++;

            if (i > (this.sNumerDegPade - 1)) {
                testI = false;
            }
        }

        if (check) {
            if (this.sNumerDegPade == 0) {
                this.sNumerPade = new ComplexPolynomial(new Complex[]{new Complex(1.0D)});
            } else {
                this.sNumerPade = ComplexPolynomial.rootsToPolynomial(this.sZerosPade);
            }

            if (this.sDenomDegPade == 0) {
                this.sDenomPade = new ComplexPolynomial(new Complex[]{new Complex(1.0D)});
            } else {
                this.sDenomPade = ComplexPolynomial.rootsToPolynomial(this.sPolesPade);
            }
        }
    }

    // Evaluate the s-domain tranfer function for the present value of s
    public Complex evalTransFunctS() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return num.divide(den).multiply(lagterm);
    }

    // Evaluate the s-domain tranfer function for a given Complex value of s
    public Complex evalTransFunctS(Complex sValue) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        this.sValue = new Complex(sValue);

        Complex num = this.sNumerPade.map(sValue);
        Complex den = this.sDenomPade.map(sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return num.divide(den).multiply(lagterm);
    }

    // Evaluate the s-domain tranfer function for a sine wave input at a given frequency (s^-1)
    public Complex evalTransFunctS(double freq) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        this.sValue = new Complex(0.0D, 2.0D * Math.PI * freq);

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return num.divide(den).multiply(lagterm);
    }

    // Evaluate the magnitude of the s-domain tranfer function for the present value of s
    public double evalMagTransFunctS() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return (num.divide(den).multiply(lagterm)).abs();
    }

    // Evaluate the magnitude of the s-domain tranfer function for a given Complex value of s
    public double evalMagTransFunctS(Complex sValue) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        this.sValue = new Complex(sValue);

        Complex num = this.sNumerPade.map(sValue);
        Complex den = this.sDenomPade.map(sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return (num.divide(den).multiply(lagterm)).abs();
    }

    // Evaluate the magnitude of the s-domain tranfer function for a sine wave input at a given frequency (s^-1)
    public double evalMagTransFunctS(double freq) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        this.sValue = new Complex(0.0D, 2.0D * Math.PI * freq);

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return (num.divide(den).multiply(lagterm)).abs();
    }

    // Evaluate the phase of the s-domain tranfer function for the present value of s
    public double evalPhaseTransFunctS() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return (num.divide(den).multiply(lagterm)).arg();
    }

    // Evaluate the phase of the s-domain tranfer function for a given Complex value of s
    public double evalPhaseTransFunctS(Complex sValue) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        this.sValue = new Complex(sValue);

        Complex num = this.sNumerPade.map(sValue);
        Complex den = this.sDenomPade.map(sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return (num.divide(den).multiply(lagterm)).arg();
    }

    // Evaluate the phase of the s-domain tranfer function for a sine wave input at a given frequency (s^-1)
    public double evalPhaseTransFunctS(double freq) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        this.sValue = new Complex(0.0D, 2.0D * Math.PI * freq);

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        Complex lagterm = Complex.ONE;

        if (this.deadTime != 0) {
            lagterm = Complex.exp(this.sValue.multiply(-this.deadTime));
        }

        return (num.divide(den).multiply(lagterm)).arg();
    }

    // Evaluate the z-domain tranfer function for the present value of z
    public Complex evalTransFunctZ() {
        Complex num = this.zNumer.map(this.zValue);
        Complex den = this.zDenom.map(this.zValue);

        return num.divide(den);
    }

    // Evaluate the z-domain tranfer function for a given Complex value of z
    public Complex evalTransFunctZ(Complex zValue) {
        this.zValue = new Complex(zValue);

        Complex num = this.zNumer.map(zValue);
        Complex den = this.zDenom.map(zValue);

        return num.divide(den);
    }

    // Evaluate the magnitude of the z-domain tranfer function for the present value of z
    public double evalMagTransFunctZ() {
        Complex num = this.zNumer.map(this.zValue);
        Complex den = this.zDenom.map(this.zValue);

        return num.divide(den).abs();
    }

    // Evaluate the magnitude of the z-domain tranfer function for a given Complex value of z
    public double evalMagTransFunctZ(Complex zValue) {
        this.zValue = new Complex(zValue);

        Complex num = this.zNumer.map(zValue);
        Complex den = this.zDenom.map(zValue);

        return num.divide(den).abs();
    }

    // Evaluate the phase of the z-domain tranfer function for the present value of z
    public double evalPhaseTransFunctZ() {
        Complex num = this.zNumer.map(this.zValue);
        Complex den = this.zDenom.map(this.zValue);

        return num.divide(den).arg();
    }

    // Evaluate the phase of the z-domain tranfer function for a given Complex value of z
    public double evalPhaseTransFunctZ(Complex zValue) {
        this.zValue = new Complex(zValue);

        Complex num = this.zNumer.map(zValue);
        Complex den = this.zDenom.map(zValue);

        return num.divide(den).arg();
    }

    // Get the integration method option
    public int getIntegMethod() {
        return this.integMethod;
    }

    // Get the z transform method option
    public int getZtransformMethod() {
        return this.ztransMethod;
    }

    // Get the length of the time, input (time domain) and output (time domain) arrays
    public int getSampleLength() {
        return this.sampLen;
    }

    // Get the forgetting factor
    public double getForgetFactor() {
        return this.forgetFactor;
    }

    //  Get the current time
    public double getCurrentTime() {
        return this.time[this.sampLen - 1];
    }

    //  Get the  time array
    public double[] getTime() {
        return this.time;
    }

    //  Get the current time domain input
    public double getCurrentInputT() {
        return this.inputT[this.sampLen - 1];
    }

    //  Get the time domain input array
    public double[] getInputT() {
        return this.inputT;
    }

    //  Get the s-domain input
    public Complex getInputS() {
        return this.inputS;
    }

    // Get the sampling period
    public double getDeltaT() {
        return this.deltaT;
    }

    // Get the sampling frequency
    public double getSampFreq() {
        return this.sampFreq;
    }

    //  Get the Laplacian s value
    public Complex getS() {
        return this.sValue;
    }

    //  Get the z-transform z value
    public Complex getZ() {
        return this.zValue;
    }

    //  Get the degree of the s-domain numerator polynomial
    public int getSnumerDeg() {
        if (this.padeAdded) {
            return this.sNumerDegPade;
        } else {
            return this.sNumerDeg;
        }
    }

    //  Get the degree of the s-domain denominator polynomial
    public int getSdenomDeg() {
        if (this.padeAdded) {
            return this.sDenomDegPade;
        } else {
            return this.sDenomDeg;
        }
    }

    //  Get the s-domain numerator polynomial
    public ComplexPolynomial getSnumer() {
        if (this.padeAdded) {
            return this.sNumerPade;
        } else {
            return this.sNumer;
        }
    }

    //  Get the s-domain denominator polynomial
    public ComplexPolynomial getSdenom() {
        if (this.padeAdded) {
            return this.sDenomPade;
        } else {
            return this.sDenom;
        }
    }

    //  Get the degree of the z-domain numerator polynomial
    public int getZnumerDeg() {
        return this.zNumerDeg;
    }

    //  Get the degree of the z-domain denominator polynomial
    public int getZdenomDeg() {
        return this.zDenomDeg;
    }

    //  Get the z-domain numerator polynomial
    public ComplexPolynomial getZnumer() {
        return this.zNumer;
    }

    //  Get the z-domain denominator polynomial
    public ComplexPolynomial getZdenom() {
        return this.zDenom;
    }

    //  Get the s-domain zeros
    public Complex[] getZerosS() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        if (this.sZerosPade == null) {
            System.out.println("Method BlackBox.getZerosS:");
            System.out.println(
                    "There are either no s-domain zeros for this transfer function");
            System.out.println(
                    "or the s-domain numerator polynomial has not been set");
            System.out.println("null returned");

            return null;
        } else {
            return this.sZerosPade;
        }
    }

    //  Get the s-domain poles
    public Complex[] getPolesS() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        if (this.sPolesPade == null) {
            System.out.println("Method BlackBox.getPolesS:");
            System.out.println(
                    "There are either no s-domain poles for this transfer function");
            System.out.println(
                    "or the s-domain denominator polynomial has not been set");
            System.out.println("null returned");

            return null;
        } else {
            return this.sPolesPade;
        }
    }

    //  Get the z-domain zeros
    public Complex[] getZerosZ() {
        if (this.zZeros == null) {
            System.out.println("Method BlackBox.getZerosZ:");
            System.out.println(
                    "There are either no z-domain zeros for this transfer function");
            System.out.println(
                    "or the z-domain numerator polynomial has not been set");
            System.out.println("null returned");

            return null;
        } else {
            return this.zZeros;
        }
    }

    //  Get the z-domain poles
    public Complex[] getPolesZ() {
        if (this.zPoles == null) {
            System.out.println("Method BlackBox.getPolesZ:");
            System.out.println(
                    "There are either no z-domain poles for this transfer function");
            System.out.println(
                    "or the z-domain denominator polynomial has not been set");
            System.out.println("null returned");

            return null;
        } else {
            return this.zPoles;
        }
    }

    // Get the map infinity zeros to zero or -1 option
    // maptozero:   if true  infinity s zeros map to zero
    //              if false infinity s zeros map to minus one
    public boolean getMaptozero() {
        return this.maptozero;
    }

    // Get the name of the black box
    public String getName() {
        return this.name;
    }

    // Plot the poles and zeros of the BlackBox transfer function in the s-domain
    public void plotPoleZeroS() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        if (this.sNumerPade == null) {
            throw new IllegalArgumentException(
                    "s domain numerator has not been set");
        }

        if (this.sDenomPade == null) {
            throw new IllegalArgumentException(
                    "s domain denominator has not been set");
        }

        PlotPoleZero ppz = new PlotPoleZero(this.sNumerPade, this.sDenomPade);
        ppz.setS();
        ppz.pzPlot(this.name);
    }

    // Plot the poles and zeros of the BlackBox transfer function in the z-domain
    public void plotPoleZeroZ() {
        PlotPoleZero ppz = new PlotPoleZero(this.zNumer, this.zDenom);

        if (this.zNumer == null) {
            throw new IllegalArgumentException(
                    "z domain numerator has not been set");
        }

        if (this.zDenom == null) {
            throw new IllegalArgumentException(
                    "z domain denominator has not been set");
        }

        ppz.setZ();
        ppz.pzPlot(this.name);
    }

    // Bode plots for the magnitude and phase of the s-domain transfer function
    public void plotBode(double lowFreq, double highFreq) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        int nPoints = 100;
        double[][] cdata = new double[2][nPoints];
        double[] logFreqArray = new double[nPoints + 1];
        double logLow = MathUtils.log10(2.0D * Math.PI * lowFreq);
        double logHigh = MathUtils.log10(2.0D * Math.PI * highFreq);
        double incr = (logHigh - logLow) / ((double) nPoints - 1.0D);
        double freqArray = lowFreq;
        logFreqArray[0] = logLow;

        for (int i = 0; i < nPoints; i++) {
            freqArray = Math.pow(10, logFreqArray[i]);
            cdata[0][i] = logFreqArray[i];
            cdata[1][i] = 20.0D * MathUtils.log10(this.evalMagTransFunctS(
                    freqArray / (2.0 * Math.PI)));
            logFreqArray[i + 1] = logFreqArray[i] + incr;
        }

        PlotGraph pgmag = new PlotGraph(cdata);
        pgmag.setGraphTitle(
                "Bode Plot = magnitude versus log10[radial frequency]");
        pgmag.setGraphTitle2(this.name);
        pgmag.setXaxisLegend("Log10[radial frequency]");
        pgmag.setYaxisLegend("Magnitude[Transfer Function]");
        pgmag.setYaxisUnitsName("dB");
        pgmag.setPoint(0);
        pgmag.plot();

        for (int i = 0; i < nPoints; i++) {
            freqArray = Math.pow(10, logFreqArray[i]);
            cdata[0][i] = logFreqArray[i];
            cdata[1][i] = (this.evalPhaseTransFunctS(freqArray) * 180.0D) / Math.PI;
        }

        PlotGraph pgphase = new PlotGraph(cdata);
        pgphase.setGraphTitle(
                "Bode Plot = phase versus log10[radial frequency]");
        pgphase.setGraphTitle2(this.name);
        pgphase.setXaxisLegend("Log10[radial frequency]");
        pgphase.setYaxisLegend("Phase[Transfer Function]");
        pgphase.setYaxisUnitsName("degrees");
        pgphase.setPoint(0);
        pgphase.plot();
    }

    //  Get the current time domain output for a given input and given time
    //  resets deltaT
    public double getCurrentOutputT(double ttime, double inp) {
        if (ttime <= time[this.sampLen - 1]) {
            throw new IllegalArgumentException(
                    "Current time equals or is less than previous time");
        }

        this.deltaT = ttime - this.time[this.sampLen - 1];
        this.sampFreq = 1.0D / this.deltaT;
        this.deadTimeWarning("getCurrentOutputT(time,input)");

        for (int i = 0; i < (this.sampLen - 2); i++) {
            this.time[i] = this.time[i + 1];
            this.inputT[i] = this.inputT[i + 1];
        }

        this.time[this.sampLen - 1] = ttime;
        this.inputT[this.sampLen - 1] = inp;

        return this.getCurrentOutputT();
    }

    //  Get the current time domain output for the stored input
    public double getCurrentOutputT() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        Complex[][] coeffT = BlackBox.inverseTransform(this.sNumerPade,
                this.sDenomPade);
        Complex tempc = Complex.ZERO;

        for (int j = 0; j < coeffT[0].length; j++) {
            tempc = tempc.add(BlackBox.timeTerm(this.time[this.sampLen - 1],
                    coeffT[0][j], coeffT[1][j], coeffT[2][j]));
        }

        double outReal = tempc.real();
        double outImag = tempc.imag();
        double temp;
        boolean outTest = true;

        if (outImag == 0.0D) {
            outTest = false;
        }

        if (outTest) {
            temp = Math.max(Math.abs(outReal), Math.abs(outImag));

            if (Math.abs((outReal - outImag) / temp) > 1.e-5) {
                outTest = false;
            } else {
                System.out.println(
                        "output in Blackbox.getCurrentOutputT() has a significant imaginary part");
                System.out.println("time = " + this.time[this.sampLen - 1] +
                        "    real = " + outReal + "   imag = " + outImag);
                System.out.println("Output equated to the real part");
            }
        }

        for (int i = 0; i < (this.sampLen - 2); i++)
            this.outputT[i] = this.outputT[i + 1];

        this.outputT[this.sampLen - 1] = outReal * this.inputT[this.sampLen -
                1];

        return this.outputT[this.sampLen - 1];
    }

    //  Get the time domain output array
    public double[] getOutputT() {
        return this.outputT;
    }

    //  Get the s-domain output for the stored input and s value.
    public Complex getOutputS() {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        this.outputS = num.divide(den).multiply(this.inputS);

        if (this.deadTime != 0) {
            this.outputS = this.outputS.multiply(Complex.exp(this.sValue.multiply(
                    -this.deadTime)));
        }

        return this.outputS;
    }

    //  Get the s-domain output for a given s value and  input.
    public Complex getOutputS(Complex svalue, Complex inputs) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        this.inputS = inputs;
        this.sValue = svalue;

        Complex num = this.sNumerPade.map(this.sValue);
        Complex den = this.sDenomPade.map(this.sValue);
        this.outputS = num.divide(den).multiply(this.inputS);

        if (this.deadTime != 0) {
            this.outputS = this.outputS.multiply(Complex.exp(this.sValue.multiply(
                    -this.deadTime)));
        }

        return this.outputS;
    }

    // Reset the number of points used in plotting a response curve (default value  = 100)
    public void setNplotPoints(int nPoints) {
        this.nPlotPoints = nPoints;
    }

    // Return the number of points used in plotting a response curve (default value  = 100)
    public int getNplotPoints() {
        return this.nPlotPoints;
    }

    // Plots the time course for an impulse input
    public void impulseInput(double impulseMag, double finalTime) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        // Multiply transfer function by impulse magnitude (impulseMag)
        ComplexPolynomial impulseN = new ComplexPolynomial(new Complex[]{new Complex(0)});
        impulseN.setCoefficientAsComplex(0, Complex.ONE.multiply(impulseMag));

        ComplexPolynomial numerT = (ComplexPolynomial) this.sNumerPade.multiply(impulseN);
        ComplexPolynomial denomT = (ComplexPolynomial) this.sDenomPade.clone();
        String graphtitle1 = "Impulse Input Transient:   Impulse magnitude = " +
                impulseMag;
        String graphtitle2 = this.getName();

        BlackBox.transientResponse(this.nPlotPoints, finalTime, this.deadTime,
                numerT, denomT, graphtitle1, graphtitle2);
    }

    // Plots the time course for a unit impulse input
    public void impulseInput(double finalTime) {
        this.impulseInput(1.0D, finalTime);
    }

    // Plots the time course for a step input
    public void stepInput(double stepMag, double finalTime) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        // Multiply transfer function by step magnitude (stepMag)/s
        ComplexPolynomial stepN = new ComplexPolynomial(new Complex[]{new Complex(0)});
        stepN.setCoefficientAsComplex(0, Complex.ONE.multiply(stepMag));

        ComplexPolynomial numerT = (ComplexPolynomial) this.sNumerPade.multiply(stepN);
        ComplexPolynomial stepD = new ComplexPolynomial(new Complex[]{new Complex(1.0D)});
        stepD.setCoefficientAsComplex(0, Complex.ZERO);
        stepD.setCoefficientAsComplex(1, Complex.ONE);

        ComplexPolynomial denomT = (ComplexPolynomial) this.sDenomPade.multiply(stepD);
        String graphtitle1 = "Step Input Transient:   Step magnitude = " +
                stepMag;
        String graphtitle2 = this.getName();

        BlackBox.transientResponse(this.nPlotPoints, finalTime, this.deadTime,
                numerT, denomT, graphtitle1, graphtitle2);
    }

    // Plots the time course for a unit step input
    public void stepInput(double finalTime) {
        this.stepInput(1.0D, finalTime);
    }

    // Plots the time course for an nth order ramp input (a.t^n)
    public void rampInput(double rampGradient, int rampOrder, double finalTime) {
        if (!this.padeAdded) {
            this.transferPolesZeros();
        }

        // Multiply transfer function by ramp input (rampGradient)(rampOrder!)/s^(ramporder+1)
        ComplexPolynomial rampN = new ComplexPolynomial(new Complex[]{new Complex(0)});
        rampN.setCoefficientAsComplex(0,
                Complex.ONE.multiply(rampGradient * MathUtils.factorial(rampOrder)));

        ComplexPolynomial numerT = (ComplexPolynomial) this.sNumerPade.multiply(rampN);
        Complex[] ramp = oneDarray(rampOrder + 1);
        ComplexPolynomial rampD = ComplexPolynomial.rootsToPolynomial(ramp);
        ComplexPolynomial denomT = (ComplexPolynomial) this.sDenomPade.multiply(rampD);
        String graphtitle1 = "";

        if (rampGradient != 1.0D) {
            if (rampOrder != 1) {
                graphtitle1 += ("nth order ramp (at^n) input transient:   a = " +
                        rampGradient + "    n = " + rampOrder);
            } else {
                graphtitle1 += ("First order ramp (at) input transient:   a = " +
                        rampGradient);
            }
        } else {
            if (rampOrder != 1) {
                graphtitle1 += "Unit ramp (t) input transient";
            } else {
                graphtitle1 += ("nth order ramp (t^n) input transient:   n = " +
                        rampOrder);
            }
        }

        String graphtitle2 = this.getName();

        BlackBox.transientResponse(this.nPlotPoints, finalTime, this.deadTime,
                numerT, denomT, graphtitle1, graphtitle2);
    }

    // Plots the time course for an nth order ramp input (t^n)
    public void rampInput(int rampOrder, double finalTime) {
        double rampGradient = 1.0D;
        this.rampInput(rampGradient, rampOrder, finalTime);
    }

    // Plots the time course for a first order ramp input (at)
    public void rampInput(double rampGradient, double finalTime) {
        int rampOrder = 1;
        this.rampInput(rampGradient, rampOrder, finalTime);
    }

    // Plots the time course for a unit ramp input (t)
    public void rampInput(double finalTime) {
        double rampGradient = 1.0D;
        int rampOrder = 1;
        this.rampInput(rampGradient, rampOrder, finalTime);
    }

    // Plots the time course for a given transfer function from time t = zero for a quiescent system
    public static void transientResponse(int nPoints, double finalTime,
                                         double deadTime, ComplexPolynomial numerT, ComplexPolynomial denomT,
                                         String graphtitle1, String graphtitle2) {
        // Obtain coefficients and constants of an partial fraction expansion
        Complex[][] coeffT = BlackBox.inverseTransform(numerT, denomT);

        // Calculate time course outputs
        int m = denomT.degree(); // number of Aexp(-at) terms
        double incrT = finalTime / (double) (nPoints - 1); // plotting increment
        double[][] cdata = new double[2][nPoints]; // plotting array
        double temp = 0.0D; // working variable
        Complex tempc = new Complex(Complex.ZERO); // working variable
        double outReal = 0.0D; // real part of output
        double outImag = 0.0D; // imaginary part of output (should be zero)
        boolean outTest = true; // false if outImag=zero

        cdata[0][0] = 0.0D;

        for (int i = 1; i < nPoints; i++) {
            cdata[0][i] = cdata[0][i - 1] + incrT;
        }

        for (int i = 0; i < nPoints; i++) {
            outTest = true;
            tempc = Complex.ZERO;

            for (int j = 0; j < m; j++) {
                tempc = tempc.add(BlackBox.timeTerm(cdata[0][i], coeffT[0][j],
                        coeffT[1][j], coeffT[2][j]));
            }

            outReal = tempc.real();
            outImag = tempc.imag();

            if (outImag == 0.0D) {
                outTest = false;
            }

            if (outTest) {
                temp = Math.max(Math.abs(outReal), Math.abs(outImag));

                if (Math.abs((outReal - outImag) / temp) > 1.e-5) {
                    outTest = false;
                } else {
                    System.out.println(
                            "output in Blackbox.stepInput has a significant imaginary part");
                    System.out.println("time = " + cdata[0][i] + "    real = " +
                            outReal + "   imag = " + outImag);
                    System.out.println("Output equated to the real part");
                }
            }

            cdata[1][i] = outReal;
            cdata[0][i] += deadTime;
        }

        // Plot
        PlotGraph pg = new PlotGraph(cdata);

        pg.setGraphTitle(graphtitle1);
        pg.setGraphTitle2(graphtitle2);
        pg.setXaxisLegend("Time");
        pg.setXaxisUnitsName("s");
        pg.setYaxisLegend("Output");
        pg.setPoint(0);
        pg.setNoYoffset(true);

        if (deadTime < (cdata[0][nPoints - 1] - cdata[0][0])) {
            pg.setNoXoffset(true);
        }

        pg.setXlowFac(0.0D);
        pg.setYlowFac(0.0D);
        pg.plot();
    }

    // Returns the output term for a given time, coefficient, constant and power
    // for output = A.time^(n-1).exp(constant*time)/(n-1)!
    public static Complex timeTerm(double ttime, Complex coeff,
                                   Complex constant, Complex power) {
        Complex ret = new Complex(Complex.ZERO);
        int n = (int) power.real() - 1;
        ret = coeff.multiply(Math.pow(ttime, n));
        ret = ret.divide(MathUtils.factorial(n));
        ret = ret.multiply(Complex.exp(constant.multiply(ttime)));

        return ret;
    }

    // Returns the coefficients A, the constant a and the power n  in the f(A.exp(-at),n) term for the
    // the inverse Laplace transform of a complex polynolial divided
    // by a complex polynomial expanded as partial fractions
    // A and a are returnd as a 2 x n Complex array were n is the number of terms
    // in the partial fraction.  the first row contains the A values, the second the a values
    public static Complex[][] inverseTransform(ComplexPolynomial numer,
                                               ComplexPolynomial denom) {
        int polesN = denom.degree(); // number of poles
        int zerosN = numer.degree(); // numer of zeros

        if (zerosN >= polesN) {
            throw new IllegalArgumentException(
                    "The degree of the numerator is equal to or greater than the degree of the denominator");
        }

        Complex[][] ret = twoDarray(3, polesN); // array for returning coefficients, constants and powers
        Complex[] coeff = oneDarray(polesN); // coefficient array
        Complex[] poles = denom.roots(); // roots array
        int[] polePower = new int[polesN]; // power, n,  of each (s - root)^n term
        int[] poleIdent = new int[polesN]; // same integer for identical (s-root) terms; integer = index of first case of that root
        boolean[] poleSet = new boolean[polesN]; // true if root has been identified as equal to another root
        boolean[] termSet = new boolean[polesN]; // false if n in (s-root)^n is greater than 1 and less than maximum value of n for that root
        double identicalRootLimit = 1.0e-2; // roots treated as identical if equal to one part in identicalRootLimit

        // Find identical roots within identicalRootLimit and assign power n [ (s-a)^n]
        int power = 0;
        Complex identPoleAverage = new Complex(Complex.ZERO);

        for (int i = 0; i < polesN; i++)
            poleSet[i] = false;

        for (int i = 0; i < polesN; i++)
            termSet[i] = true;

        for (int i = 0; i < polesN; i++) {
            if (!poleSet[i]) {
                power = 1;
                identPoleAverage = Complex.ZERO;
                polePower[i] = 1;

                for (int j = i + 1; j < polesN; j++) {
                    if (!poleSet[j]) {
                        if (isEqualWithinLimits(poles[i], poles[j],
                                identicalRootLimit)) {
                            poleIdent[j] = i;
                            polePower[j] = ++power;
                            poleSet[j] = true;
                            poleSet[i] = true;
                        } else {
                            poleIdent[j] = j;
                            polePower[j] = 1;
                        }
                    }
                }

                // Set termSet to false if pole is recurring but not the recurring root term with the highest power
                if (power > 1) {
                    for (int k = power - 1; k > 0; k--) {
                        for (int j = 0; j < polesN; j++) {
                            if ((poleIdent[j] == i) && (polePower[j] == k)) {
                                termSet[j] = false;
                            }
                        }
                    }

                    // Replace roots within identicalRootLimit with average value
                    int kk = 0;

                    for (int j = 0; j < polesN; j++) {
                        if (poleIdent[j] == i) {
                            identPoleAverage = identPoleAverage.add(poles[j]);
                            kk++;
                        }
                    }

                    identPoleAverage = identPoleAverage.divide(kk);

                    for (int j = 0; j < polesN; j++) {
                        if (poleIdent[j] == i) {
                            poles[j] = identPoleAverage;
                        }
                    }
                }
            }
        }

        // calculate coefficients for non-recurring poles and highest order power of recurring poles
        // by substituting pole values ito the partial fraction equations
        int ndone = 0;

        for (int i = 0; i < polesN; i++) {
            if (termSet[i]) {
                coeff[i] = numer.map(poles[i]);

                for (int j = 0; j < polesN; j++) {
                    if ((i != j) && termSet[j]) {
                        coeff[i] = coeff[i].divide(poles[i].subtract(poles[j]).pow(polePower[j]));
                    }
                }

                ndone++;
            }
        }

        // calculate coefficients for lower order powers of recurring poles
        Complex denhold = new Complex(Complex.ZERO); // holds value of the denominator during calculations

        if (ndone != polesN) {
            // calculate exponent of the average of absolute values of poles
            double poleAv = 0.0D;

            for (int i = 0; i < polesN; i++)
                poleAv += poles[i].abs();

            poleAv /= polesN;
            poleAv = Math.pow(10.0D, Math.floor(MathUtils.log10(poleAv)));

            // fill simultaneous array matrix of partial fraction equations
            // with substituted s values that are not pole values but are of the same magnitude
            // as the above pole average
            int nsimul = polesN - ndone;
            Complex[][] mat = twoDarray(nsimul, nsimul);
            Complex[] vec = oneDarray(nsimul);
            Complex sValue = Complex.ZERO;
            double sValueReal = 0.0D;
            boolean testpole1 = true;
            boolean testpole2 = true;
            Complex temp = Complex.ZERO;

            // set up matrix to solve linear set giving lower power recurring root coefficients
            for (int i = 0; i < nsimul; i++) {
                // Find a value of s not equal to a pole and not already used
                testpole1 = true;

                while (testpole1) {
                    testpole2 = true;

                    for (int j = 0; j < polesN; j++) {
                        if (isEqualWithinLimits(sValue, poles[j], 1.0e-5)) {
                            testpole2 = false;
                        }
                    }

                    if (!testpole2) {
                        sValueReal += 1.0D;
                        sValue = new Complex(sValueReal, 0.0D);
                    } else {
                        testpole1 = false;
                    }
                }

                // evaluate vector of (denominator - sum of known coefficients*s terms)
                vec[i] = numer.map(sValue);
                denhold = denom.map(sValue);

                for (int j = 0; j < polesN; j++) {
                    if (termSet[j]) {
                        temp = coeff[j].multiply(denhold.divide(sValue.subtract(poles[j]).pow(polePower[j])));
                        vec[i] = vec[i].subtract(temp);
                    }
                }

                // evaluate matrix of sums of unknown coefficients s terms
                int k = 0;

                for (int j = 0; j < polesN; j++) {
                    temp = Complex.ONE;

                    if (!termSet[j]) {
                        temp = temp.multiply(denhold.divide(sValue.subtract(poles[j]).pow(polePower[j])));
                        mat[j][i] = temp;
                    }
                }

                sValueReal += 1.0D;
                sValue = new Complex(sValueReal, sValue.imag());
            }

            // Solve the set of linear equations
            if (nsimul == 1) {
                for (int i = 0; i < polesN; i++) {
                    if (!termSet[i]) {
                        coeff[i] = vec[0].divide(mat[0][0]);
                    }
                }
            } else {
                ComplexMatrix cmat = new ComplexMatrix(mat);
                Complex[] terms = solveLinearSet(cmat, vec);
                int j = -1;

                for (int i = 0; i < polesN; i++) {
                    if (!termSet[i]) {
                        coeff[i] = terms[++j];
                    }
                }
            }
        }

        // calculate constant converting product of pole terms to the value of the denominator
        // calculate mean of the poles
        Complex mean = new Complex(0.0D, 0.0);

        for (int i = 0; i < polesN; i++)
            mean = mean.add(poles[i]);

        mean = mean.divide(polesN);

        // check that mean != a pole; increase mean by 1.5 till != any pole
        boolean test = true;
        int ii = 0;

        while (test) {
            if (mean.equals(poles[ii])) {
                mean = mean.multiply(1.5D);
                ii = 0;
            } else {
                ii++;

                if (ii > (polesN - 1)) {
                    test = false;
                }
            }
        }

        // calculate product of poles-mean
        Complex product = new Complex(1.0D, 0.0);

        for (int i = 0; i < polesN; i++)
            product = product.multiply(mean.subtract(poles[i]));

        // evaluate the denominator at mean value
        Complex eval = denom.map(mean);

        // Calculate constant
        Complex conversionConstant = product.divide(eval);

        // fill ret for returning
        for (int i = 0; i < polesN; i++) {
            ret[0][i] = coeff[i].multiply(conversionConstant);
            ret[1][i] = poles[i];
            ret[2][i] = new Complex(polePower[i], 0.0D);
        }

        return ret;
    }

    // TRUNCATION
    // Rounds the mantissae of both the real and imaginary parts of Complex to prec places
    // Static method
    private static Complex truncate(Complex x, int prec) {
        if (prec < 0) return x;

        double xR = x.real();
        double xI = x.imag();

        xR = truncate(xR, prec);
        xI = truncate(xI, prec);

        return new Complex(xR, xI);
    }

    // MANTISSA ROUNDING (TRUNCATING)
    // Rounds the mantissa of a double to prec places
    private static double truncate(double x, int prec) {

        if (prec < 0) throw new IllegalArgumentException("precision less than zero places");

        if (x == 0.0D) return x;
        if (isNaN(x)) return x;
        if (x == Double.POSITIVE_INFINITY) return x;
        if (x == Double.NEGATIVE_INFINITY) return x;

        char sign = ' ';
        if (x < 0.0D) {
            sign = '-';
            x = -x;
        }

        Double xx = new Double(x);
        String xString = xx.toString();
        String newXstring = stringRound(xString.trim(), prec, sign);
        return Double.parseDouble(newXstring);
    }

    // Returns true if x is 'Not a Number' (NaN)
    // x is double
    private static boolean isNaN(double x) {
        boolean test = false;
        if (x != x) test = true;
        return test;
    }

    private static Complex[] oneDarray(int n) {
        Complex[] a = new Complex[n];
        for (int i = 0; i < n; i++) {
            a[i] = Complex.ZERO;
        }
        return a;
    }

    private static Complex[][] twoDarray(int n, int m) {
        Complex[][] a = new Complex[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                a[i][j] = Complex.ZERO;
            }
        }
        return a;
    }

    //Method for truncate
    private static String stringRound(String ss, int prec, char sign) {

        int posEnd = ss.length() - 1;
        int posE = ss.indexOf('E');
        int posDot = ss.indexOf('.');

        String returnString = null;
        String decPlaces = null;
        String exponent = null;
        String prePoint = ss.substring(0, posDot);
        int ppLen = prePoint.length();
        if (posE == -1) {
            decPlaces = ss.substring(posDot + 1);
        } else {
            decPlaces = ss.substring(posDot + 1, posE);
            exponent = ss.substring(posE + 1);
        }
        int dpLen = decPlaces.length();

        if (prec == 0) {
            returnString = prePoint + ".0";
            if (exponent != null) returnString = returnString + "E" + exponent;
            if (sign == '-') returnString = "-" + returnString;
            return returnString;
        }

        if (dpLen <= prec) {
            returnString = ss;
            if (sign == '-') returnString = "-" + ss;
            return returnString;
        }

        int[] iss = new int[dpLen];
        int carry = 0;
        for (int i = dpLen - 1; i >= prec; i--) {
            iss[i] = (int) decPlaces.charAt(i) + carry;
            if (iss[i] < 53) {
                carry = 0;
            } else {
                carry = 1;
            }
            iss[i] = 48;
        }
        boolean test = true;
        int ii = prec - 1;
        while (test) {
            iss[ii] = (int) decPlaces.charAt(ii) + carry;
            if (iss[ii] < 58) {
                test = false;
                for (int i = 0; i < ii; i++) {
                    iss[i] = (int) decPlaces.charAt(i);
                }
                carry = 0;
            } else {
                iss[ii] = 48;
                carry = 1;
                ii--;
                if (ii < 0) test = false;
            }
        }

        test = true;
        if (carry == 0) test = false;
        ii = posDot - 1;
        while (test) {
            iss[ii] = (int) prePoint.charAt(ii) + carry;
            if (iss[ii] < 58) {
                test = false;
                for (int i = 0; i < ii; i++) {
                    iss[i] = (int) prePoint.charAt(i);
                }
                carry = 0;
            } else {
                iss[ii] = 48;
                carry = 1;
                ii--;
                if (ii < 0) test = false;
            }
        }

        if (carry == 1) {
            prePoint = "1";
            for (int i = 0; i < ppLen; i++) prePoint = prePoint + "0";
            returnString = prePoint + ".0";

        } else {
            returnString = prePoint + ".";
            StringBuffer strbuff = new StringBuffer();
            for (int k = 0; k < prec; k++) {
                strbuff.append((char) iss[k]);
            }
            returnString = returnString + strbuff.toString();
        }

        if (exponent != null) returnString = returnString + "E" + exponent;
        if (sign == '-') returnString = "-" + returnString;

        return returnString;
    }

    private static boolean isEqualWithinLimits(Complex a, Complex b, double fract) {
        boolean test = false;

        double rb = b.real();
        double ra = a.real();
        double ib = b.imag();
        double ia = a.imag();
        double rdn = 0.0D;
        double idn = 0.0D;

        if (ra == 0.0D && ia == 0.0D && rb == 0.0D && ib == 0.0D) test = true;
        if (!test) {
            rdn = Math.abs(rb);
            if (Math.abs(ra) > rdn) rdn = Math.abs(ra);
            idn = Math.abs(ib);
            if (Math.abs(ia) > idn) idn = Math.abs(ia);
            if (Math.abs(ra - rb) / rdn < fract && Math.abs(ia - ia) / idn < fract) test = true;
        }

        return test;
    }

    // LU DECOMPOSITION OF COMPLEX MATRIX A
    // For details of LU decomposition
    // See Numerical Recipes, The Art of Scientific Computing
    // by W H Press, S A Teukolsky, W T Vetterling & B P Flannery
    // Cambridge University Press,   http://www.nr.com/
    // ComplexMatrix ludmat is the returned LU decompostion
    // int[] index is the vector of row permutations
    // dswap returns +1.0 for even number of row interchanges
    //       returns -1.0 for odd number of row interchanges
    private static ComplexMatrix luDecomp(ComplexMatrix matr, int[] indexx) {
        if (matr.numRows() != matr.numColumns()) throw new IllegalArgumentException("A matrix is not square");
        int n = matr.numRows();
        int imax = 0;
        double dum = 0.0D, temp = 0.0D, big = 0.0D;
        double[] vv = new double[n];
        Complex sum = Complex.ZERO;
        Complex dumm = Complex.ZERO;

        ComplexMatrix ludmat = new ComplexMatrix(matr);
        Complex[][] ludarray = ludmat.toPrimitiveArray();

        //ludmat.dswap=1.0;
        for (int i = 0; i < n; i++) {
            big = 0.0;
            for (int j = 0; j < n; j++) {
                if ((temp = ludarray[i][j].abs()) > big) big = temp;
            }
            if (big == 0.0) throw new ArithmeticException("Singular matrix");
            vv[i] = 1.0 / big;
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < j; i++) {
                sum = new Complex(ludarray[i][j]);
                for (int k = 0; k < i; k++) sum = sum.subtract(ludarray[i][k].multiply(ludarray[k][j]));
                ludarray[i][j] = new Complex(sum);
            }
            big = 0.0;
            for (int i = j; i < n; i++) {
                sum = new Complex(ludarray[i][j]);
                for (int k = 0; k < j; k++) {
                    sum = sum.subtract(ludarray[i][k].multiply(ludarray[k][j]));
                }
                ludarray[i][j] = new Complex(sum);
                if ((dum = vv[i] * sum.abs()) >= big) {
                    big = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (int k = 0; k < n; k++) {
                    dumm = new Complex(ludarray[imax][k]);
                    ludarray[imax][k] = new Complex(ludarray[j][k]);
                    ludarray[j][k] = new Complex(dumm);
                }
                //ludmat.dswap = -ludmat.dswap;
                vv[imax] = vv[j];
            }
            indexx[j] = imax;

            if (ludarray[j][j].equals(Complex.ZERO)) {
                ludarray[j][j] = new Complex(TINY, TINY);
            }
            if (j != n - 1) {
                dumm = new Complex(1.0).divide(ludarray[j][j]);
                for (int i = j + 1; i < n; i++) {
                    ludarray[i][j] = ludarray[i][j].multiply(dumm);
                }
            }
        }
        return ludmat;
    }

    private static final double TINY = 1.0e-30;

    // Solves the set of n linear complex equations A.X=B using not A but its LU decomposition
    // Complex bvec is the vector B (input)
    // Complex xvec is the vector X (output)
    // index is the permutation vector produced by luDecomp()
    private static Complex[] luBackSub(ComplexMatrix matr, Complex[] bvec, int[] indexx) {
        int ii = 0, ip = 0;
        int n = bvec.length;
        if (n != matr.numColumns())
            throw new IllegalArgumentException("vector length is not equal to matrix dimension");
        if (matr.numColumns() != matr.numRows()) throw new IllegalArgumentException("matrix is not square");
        Complex sum = Complex.ZERO;
        Complex[] xvec = new Complex[n];
        Complex[][] matrarray = matr.toPrimitiveArray();
        for (int i = 0; i < n; i++) {
            xvec[i] = new Complex(bvec[i]);
        }
        for (int i = 0; i < n; i++) {
            ip = indexx[i];
            sum = new Complex(xvec[ip]);
            xvec[ip] = new Complex(xvec[i]);
            if (ii == 0) {
                for (int j = ii; j <= i - 1; j++) {
                    sum = sum.subtract(matrarray[i][j].multiply(xvec[j]));
                }
            } else {
                if (sum.equals(Complex.ZERO)) ii = i;
            }
            xvec[i] = new Complex(sum);
        }
        for (int i = n - 1; i >= 0; i--) {
            sum = new Complex(xvec[i]);
            for (int j = i + 1; j < n; j++) {
                sum = sum.subtract(matrarray[i][j].multiply(xvec[j]));
            }
            xvec[i] = sum.divide(matrarray[i][i]);
        }
        return xvec;
    }

    // Solves the set of n linear complex equations A.X=B
    // Complex bvec is the vector B (input)
    // Complex xvec is the vector X (output)
    private static Complex[] solveLinearSet(ComplexMatrix matr, Complex[] bvec) {
        ComplexMatrix ludmat;
        int index[] = null;         // row permutation index

        for (int i = 0; i < matr.numRows(); i++) index[i] = i;
        ludmat = luDecomp(matr, index);
        return luBackSub(ludmat, bvec, index);
    }
}
