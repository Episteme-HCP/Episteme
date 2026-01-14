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

import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PropDeriv extends BlackBox {
    /** DOCUMENT ME! */
    private double kp = 1.0D; //  proportional gain

    /** DOCUMENT ME! */
    private double td = 0.0D; //  derivative time constant

    /** DOCUMENT ME! */
    private double kd = 0.0D; //  derivative gain

    // Constructor - unit proportional gain, zero derivative gain
    /**
     * Creates a new PropDeriv object.
     */
    public PropDeriv() {
        super.sNumerDeg = 1;
        super.sDenomDeg = 0;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(1.0D), new Complex(0.0D)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] { new Complex(1.0D) });
        super.sZeros = new Complex[] { Complex.ZERO };
        super.sZeros[0] = new Complex(-1.0D / 0.0D, 0.0D);
        super.ztransMethod = 1;
        super.name = "PD";
        super.fixedName = "PD";
        ;
        super.addDeadTimeExtras();
    }

    // Set the proportional gain
    /**
     * DOCUMENT ME!
     *
     * @param kp DOCUMENT ME!
     */
    public void setKp(double kp) {
        this.kp = kp;
        super.sNumer.setCoefficientAsComplex(0, new Complex(this.kp, 0.0D));
        super.sZeros[0] = new Complex(-this.kp / this.kd, 0.0D);
        super.addDeadTimeExtras();
    }

    // Set the derivative gain
    /**
     * DOCUMENT ME!
     *
     * @param kd DOCUMENT ME!
     */
    public void setKd(double kd) {
        this.kd = kd;
        this.td = kd / this.kp;
        super.sNumer.setCoefficientAsComplex(1, new Complex(this.kd, 0.0D));
        super.sZeros[0] = new Complex(-this.kp / this.kd, 0.0D);
        super.addDeadTimeExtras();
    }

    // Set the derivative time constant
    /**
     * DOCUMENT ME!
     *
     * @param td DOCUMENT ME!
     */
    public void setTd(double td) {
        this.td = td;
        this.kd = this.td * this.kp;
        super.sNumer.setCoefficientAsComplex(1, new Complex(this.kd, 0.0D));
        super.sZeros[0] = new Complex(-this.kp / this.kd, 0.0D);
        super.addDeadTimeExtras();
    }

    // Get the proprtional gain
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getKp() {
        return this.kp;
    }

    // Get the derivative gain
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getKd() {
        return this.kd;
    }

    // Get the derivative time constant
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTd() {
        return this.td;
    }

    // Perform z transform using an already set delta T
    /**
     * DOCUMENT ME!
     */
    public void zTransform() {
        if (super.deltaT == 0.0D) {
            System.out.println(
                "z-transform attempted in PropDeriv with a zero sampling period");
        }

        super.deadTimeWarning("zTransform");

        if (ztransMethod == 0) {
            this.mapstozAdHoc();
        } else {
            super.zNumerDeg = 1;
            super.zDenomDeg = 1;
            super.zNumer = new ComplexPolynomial(new Complex[] {
                        new Complex(-this.kd),
                        new Complex((this.kp * super.deltaT) + this.kd)
                    });
            super.zDenom = new ComplexPolynomial(new Complex[] {
                        new Complex(0.0D), new Complex(super.deltaT)
                    });
            super.zZeros = new Complex[] { Complex.ZERO };
            super.zZeros[0] = new Complex(this.kd / ((this.kp * super.deltaT) +
                    this.kd), 0.0D);
            super.zPoles = new Complex[] { Complex.ZERO };
            super.zPoles[0] = new Complex(0.0D, 0.0D);
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

    // Plots the time course for a step input
    /**
     * DOCUMENT ME!
     *
     * @param stepMag DOCUMENT ME!
     * @param finalTime DOCUMENT ME!
     */
    public void stepInput(double stepMag, double finalTime) {
        // Calculate time course outputs
        int n = 50; // number of points on plot
        double incrT = finalTime / (double) (n - 1); // plotting increment
        double[][] cdata = new double[2][n]; // plotting array

        cdata[0][0] = 0.0D;

        for (int i = 1; i < n; i++) {
            cdata[0][i] = cdata[0][i - 1] + incrT;
        }

        double kpterm = this.kp * stepMag;

        for (int i = 0; i < n; i++) {
            cdata[1][i] = kpterm;
        }

        if (super.deadTime != 0.0D) {
            for (int i = 0; i < n; i++)
                cdata[0][i] += super.deadTime;
        }

        // Plot
        PlotGraph pg = new PlotGraph(cdata);

        pg.setGraphTitle("Step Input Transient:   Step magnitude = " + stepMag);
        pg.setGraphTitle2(this.getName());
        pg.setXaxisLegend("Time");
        pg.setXaxisUnitsName("s");
        pg.setYaxisLegend("Output");
        pg.setPoint(0);
        pg.plot();
    }

    // Plots the time course for a unit step input
    /**
     * DOCUMENT ME!
     *
     * @param finalTime DOCUMENT ME!
     */
    public void stepInput(double finalTime) {
        this.stepInput(1.0D, finalTime);
    }

    // Plots the time course for an nth order ramp input (at^n)
    /**
     * DOCUMENT ME!
     *
     * @param rampGradient DOCUMENT ME!
     * @param rampOrder DOCUMENT ME!
     * @param finalTime DOCUMENT ME!
     */
    public void rampInput(double rampGradient, int rampOrder, double finalTime) {
        if (rampOrder == 0) {
            // Check if really a step input (rampOrder, n = 0)
            this.stepInput(rampGradient, finalTime);
        } else {
            // Calculate time course outputs
            int n = 50; // number of points on plot
            double incrT = finalTime / (double) (n - 1); // plotting increment
            double[][] cdata = new double[2][n]; // plotting array
            double sum = 0.0D; // integration sum

            cdata[0][0] = 0.0D;
            cdata[1][0] = 0.0D;

            for (int i = 1; i < n; i++) {
                cdata[0][i] = cdata[0][i - 1] + incrT;
                cdata[1][i] = rampGradient * Math.pow(cdata[0][i], rampOrder -
                        1) * ((this.kp * cdata[0][i]) + this.kd);
            }

            if (super.deadTime != 0.0D) {
                for (int i = 0; i < n; i++)
                    cdata[0][i] += super.deadTime;
            }

            // Plot
            PlotGraph pg = new PlotGraph(cdata);

            pg.setGraphTitle(
                "Ramp (a.t^n) Input Transient:   ramp gradient (a) = " +
                rampGradient + " ramp order (n) = " + rampOrder);
            pg.setGraphTitle2(this.getName());
            pg.setXaxisLegend("Time");
            pg.setXaxisUnitsName("s");
            pg.setYaxisLegend("Output");
            pg.setPoint(0);
            pg.plot();
        }
    }

    // Plots the time course for an nth order ramp input (t^n)
    /**
     * DOCUMENT ME!
     *
     * @param rampOrder DOCUMENT ME!
     * @param finalTime DOCUMENT ME!
     */
    public void rampInput(int rampOrder, double finalTime) {
        double rampGradient = 1.0D;
        this.rampInput(rampGradient, rampOrder, finalTime);
    }

    // Plots the time course for a first order ramp input (at)
    /**
     * DOCUMENT ME!
     *
     * @param rampGradient DOCUMENT ME!
     * @param finalTime DOCUMENT ME!
     */
    public void rampInput(double rampGradient, double finalTime) {
        int rampOrder = 1;
        this.rampInput(rampGradient, rampOrder, finalTime);
    }

    // Plots the time course for a unit ramp input (t)
    /**
     * DOCUMENT ME!
     *
     * @param finalTime DOCUMENT ME!
     */
    public void rampInput(double finalTime) {
        double rampGradient = 1.0D;
        int rampOrder = 1;
        this.rampInput(rampGradient, rampOrder, finalTime);
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

        Complex term = this.sValue.multiply(this.kd);
        term = term.add(new Complex(this.kp));
        super.outputS = term.multiply(super.inputS);

        if (super.deadTime != 0.0D) {
            super.outputS = super.outputS.multiply(Complex.exp(
                        super.sValue.multiply(-super.deadTime)));
        }

        return super.outputS;
    }

    //  Get the s-domain output for the stored input and  s-value.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex getOutputS() {
        Complex term = this.sValue.multiply(this.kd);
        term = term.add(new Complex(this.kp));
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
        // proportional term
        super.outputT[super.sampLen - 1] = this.kp * super.inputT[sampLen - 1];

        // + derivative term
        super.outputT[super.sampLen - 1] += ((this.kd * (super.inputT[super.sampLen -
        1] - super.inputT[super.sampLen - 2])) / super.deltaT);
    }

    // Get the s-domain poles
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex[] getSpoles() {
        System.out.println("PD controller has no s-domain poles");

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
            "Transfer numerator is set by a constructor or special methods in PropDeriv; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropDeriv; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropDeriv; Not by setSnumer()");
    }

    // Seting the transfer function denominator in the s-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(double[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropDeriv; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(Complex[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropDeriv; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropDeriv; Not by setSdenom()");
    }

    // Setting the transfer function numerator in the z-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropDeriv; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropDeriv; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropDeriv; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropDeriv; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropDeriv; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropDeriv; Not by setZdenom()");
    }
}
