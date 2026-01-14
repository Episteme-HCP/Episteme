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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PropIntDeriv extends BlackBox {
    /** DOCUMENT ME! */
    private double kp = 1.0D; //  proportional gain

    /** DOCUMENT ME! */
    private double ti = Double.POSITIVE_INFINITY; //  integral time constant

    /** DOCUMENT ME! */
    private double ki = 0.0D; //  integral gain

    /** DOCUMENT ME! */
    private double td = 0.0D; //  derivative time constant

    /** DOCUMENT ME! */
    private double kd = 0.0D; //  derivative gain

    // Constructor - unit proportional gain, zero integral gain, zero derivative gain
    /**
     * Creates a new PropIntDeriv object.
     */
    public PropIntDeriv() {
        super.sNumerDeg = 2;
        super.sDenomDeg = 1;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(0.0D), new Complex(1.0D), new Complex(0.0D)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(0.0D), new Complex(1.0D)
                });
        super.sZeros = new Complex[] { Complex.ZERO, Complex.ZERO };
        super.sPoles = new Complex[] { Complex.ZERO };
        super.zNumerDeg = 1;
        super.zDenomDeg = 1;
        super.zNumer = new ComplexPolynomial(new Complex[] { new Complex(2.0D) });
        super.zDenom = new ComplexPolynomial(new Complex[] { new Complex(2.0D) });
        super.zZeros = new Complex[] { Complex.ZERO, Complex.ZERO };
        super.zPoles = new Complex[] { Complex.ZERO, Complex.ZERO };
        super.ztransMethod = 1;
        super.name = "PID";
        super.fixedName = "PID";
        this.calcPolesZerosS();
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
        super.sNumer.setCoefficientAsComplex(1, new Complex(kp, 0.0));
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Set the integral gain
    /**
     * DOCUMENT ME!
     *
     * @param ki DOCUMENT ME!
     */
    public void setKi(double ki) {
        this.ki = ki;
        this.ti = this.kp / ki;
        super.sNumer.setCoefficientAsComplex(0, new Complex(ki, 0.0));
        this.calcPolesZerosS();
        super.addDeadTimeExtras();
    }

    // Set the integral time constant
    /**
     * DOCUMENT ME!
     *
     * @param ti DOCUMENT ME!
     */
    public void setTi(double ti) {
        this.ti = ti;
        this.ki = this.kp / ti;
        super.sNumer.setCoefficientAsComplex(0, new Complex(ki, 0.0));
        this.calcPolesZerosS();
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
        super.sNumer.setCoefficientAsComplex(2, new Complex(kd, 0.0));
        this.calcPolesZerosS();
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
        this.kd = this.kp * td;
        super.sNumer.setCoefficientAsComplex(2, new Complex(kd, 0.0));
        this.calcPolesZerosS();
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

    // Get the integral gain
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getKi() {
        return this.ki;
    }

    // Get the integral time constant
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTi() {
        return this.ti;
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

    // Calculate the zeros and poles in the s-domain
    /**
     * DOCUMENT ME!
     */
    protected void calcPolesZerosS() {
        super.sZeros = super.sNumer.roots();
        super.sPoles[0] = new Complex(0.0D, super.sPoles[0].imag());
    }

    // Perform z transform using an already set delta T
    /**
     * DOCUMENT ME!
     */
    public void zTransform() {
        if (super.deltaT == 0.0D) {
            System.out.println(
                "z-transform attempted in PropIntDeriv with a zero sampling period");
        }

        super.deadTimeWarning("zTransform");

        if (super.ztransMethod == 0) {
            this.mapstozAdHoc();
        } else {
            double kit = this.ki * super.deltaT;
            double kdt = this.kd / super.deltaT;
            Complex[] coef = new Complex[] {
                    Complex.ZERO, Complex.ZERO, Complex.ZERO
                };
            coef[0] = new Complex(0.0D, 0.0D);
            coef[1] = new Complex(-1.0D, 0.0D);
            coef[2] = new Complex(1.0D, 0.0D);
            super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(coef));

            switch (this.integMethod) {
            // Trapezium rule
            case 0:
                coef[0] = new Complex(kdt, 0.0D);
                coef[1] = new Complex((kit / 2.0D) - (2.0D * kdt) - this.kp,
                        0.0D);
                coef[2] = new Complex(this.kp + (kit / 2.0D) + kdt, 0.0);
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(coef));

                break;

            // Backward rectangular rule
            case 1:
                coef[0] = new Complex(kdt, 0.0D);
                coef[1] = new Complex((-2.0D * kdt) - this.kp, 0.0D);
                coef[2] = new Complex(this.kp + kit + kdt, 0.0);
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(coef));

                break;

            // Foreward tectangular rule
            case 2:
                coef[0] = new Complex(kdt, 0.0D);
                coef[1] = new Complex(kit - (2.0D * kdt) - this.kp, 0.0D);
                coef[2] = new Complex(this.kp + kdt, 0.0);
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(coef));

                break;

            default:
                System.out.println(
                    "Integration method option in PropIntDeriv must be 0,1 or 2");
                System.out.println("It was set at " + integMethod);
                System.out.println("z-transform not performed");
            }
        }

        super.zZeros = super.zNumer.roots();
        super.zPoles = super.zDenom.roots();
    }

    // Perform z transform setting delta T
    /**
     * DOCUMENT ME!
     *
     * @param deltaT DOCUMENT ME!
     */
    public void zTransform(double deltaT) {
        super.deltaT = deltaT;
        this.zTransform();
    }

    // Calculate the pole and the zero in the z-domain for an already set sampling period
    /**
     * DOCUMENT ME!
     */
    public void calcPolesZerosZ() {
        if (super.deltaT == 0.0D) {
            System.out.println(
                "z-pole and z-zero calculation attempted in PropIntDeriv.calcPolesZerosZ( with a zero sampling period");
        }

        this.zTransform();
        super.zPoles[0] = new Complex(0.0D, 0.0D);
        super.zPoles[1] = new Complex(1.0D, 0.0D);
        super.zZeros = super.zNumer.roots();
    }

    // Calculate the pole and the zero in the z-domain setting the  sampling period
    /**
     * DOCUMENT ME!
     *
     * @param deltaT DOCUMENT ME!
     */
    public void calcPolesZerosZ(double deltaT) {
        this.deltaT = deltaT;
        this.calcPolesZerosZ();
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
        double sum = 0.0D; // integration sum

        cdata[0][0] = 0.0D;

        for (int i = 1; i < n; i++) {
            cdata[0][i] = cdata[0][i - 1] + incrT;
        }

        double kpterm = this.kp * stepMag;

        for (int i = 0; i < n; i++) {
            sum += (ki * incrT * stepMag);
            cdata[1][i] = kpterm + sum;
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
        // Check if really a step input
        if (rampOrder == 0) {
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
                sum += ((ki * rampGradient * (Math.pow(cdata[0][i],
                    rampOrder + 1) - Math.pow(cdata[0][i - 1], rampOrder + 1))) / (double) (rampOrder +
                1));
                cdata[1][i] = (this.kp * rampGradient * Math.pow(cdata[0][i],
                        rampOrder)) + sum;
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

        Complex term1 = Complex.ONE;
        Complex term2 = Complex.ONE;
        Complex term3 = Complex.ONE;
        term1 = term1.multiply(this.kp);
        term2 = term2.multiply(this.ki);
        term2 = term2.divide(this.sValue);
        term3 = term3.multiply(this.kd);
        term3 = term3.multiply(super.sValue);

        Complex term = term1.add(term2.add(term3));
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
        Complex term1 = Complex.ONE;
        Complex term2 = Complex.ONE;
        Complex term3 = Complex.ONE;
        term1 = term1.multiply(this.kp);
        term2 = term2.multiply(this.ki);
        term2 = term2.divide(this.sValue);
        term3 = term3.multiply(this.kd);
        term3 = term3.multiply(super.sValue);

        Complex term = term1.add(term2.add(term3));
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
        super.deadTimeWarning("zTransform");

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

    //  Calculate the output for the stored sampled input and time
    /**
     * DOCUMENT ME!
     */
    public void calcOutputT() {
        super.deadTimeWarning("zTransform");

        // proportional term
        super.outputT[super.sampLen - 1] = this.kp * super.inputT[super.sampLen -
            1];

        // + integral term
        if (super.forgetFactor == 1.0D) {
            switch (super.integMethod) {
            // trapezium Rule
            case 0:
                super.integrationSum += (((super.inputT[super.sampLen - 1] +
                super.inputT[super.sampLen - 2]) * super.deltaT) / 2.0D);

                break;

            // backward rectangular rule
            case 1:
                super.integrationSum += (super.inputT[super.sampLen - 1] * super.deltaT);

                break;

            // foreward rectangular rule
            case 2:
                super.integrationSum += (super.inputT[super.sampLen - 2] * super.deltaT);

                break;

            default:
                System.out.println(
                    "Integration method option in PropInt must be 0,1 or 2");
                System.out.println("It was set at " + super.integMethod);
                System.out.println("getOutput not performed");
            }
        } else {
            switch (super.integMethod) {
            // trapezium Rule
            case 0:
                super.integrationSum = 0.0D;

                for (int i = 1; i < super.sampLen; i++) {
                    super.integrationSum += ((Math.pow(super.forgetFactor,
                        super.sampLen - 1 - i) * (super.inputT[i - 1] +
                    super.inputT[i]) * super.deltaT) / 2.0D);
                }

                ;

                break;

            // backward rectangular rule
            case 1:
                super.integrationSum = 0.0D;

                for (int i = 1; i < sampLen; i++) {
                    super.integrationSum += (Math.pow(super.forgetFactor,
                        super.sampLen - 1 - i) * (super.inputT[i]) * super.deltaT);
                }

                ;

                break;

            // foreward rectangular rule
            case 2:
                super.integrationSum = 0.0D;

                for (int i = 1; i < super.sampLen; i++) {
                    super.integrationSum += (Math.pow(super.forgetFactor,
                        super.sampLen - 1 - i) * (super.inputT[i - 1]) * super.deltaT);
                }

                ;

                break;

            default:
                System.out.println(
                    "Integration method option in PropInt must be 0,1 or 2");
                System.out.println("It was set at " + super.integMethod);
                System.out.println("getOutput not performed");
            }
        }

        super.outputT[super.sampLen - 1] += (this.ki * super.integrationSum);

        // + derivative term
        super.outputT[sampLen - 1] += ((this.kd * (super.inputT[sampLen - 1] -
        super.inputT[sampLen - 2])) / super.deltaT);
    }

    // REDUNDANT BlackBox METHODS
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropIntDeriv; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropIntDeriv; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropIntDeriv; Not by setSnumer()");
    }

    // Seting the transfer function denominator in the s-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(double[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropIntDeriv; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(Complex[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropIntDeriv; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropIntDeriv; Not by setSdenom()");
    }

    // Setting the transfer function numerator in the z-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropIntDeriv; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropIntDeriv; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropIntDeriv; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropIntDeriv; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropIntDeriv; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropIntDeriv; Not by setZdenom()");
    }
}
