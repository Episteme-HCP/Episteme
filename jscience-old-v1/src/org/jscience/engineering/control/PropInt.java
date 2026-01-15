/*      Class PropInt
*
*       This class contains the constructor to create an instance of
*       a proportional plus integral(PI) controller and
*       the methods needed to use this controller in control loops in the
*       time domain, Laplace transform s domain or the z-transform z domain.
*
*       This class is a subclass of the superclass BlackBox.
*
*       Author:  Michael Thomas Flanagan.
*
*       Created: August 2002
*       Updated: 17 April 2003, 3 May 2005
*
*
*       DOCUMENTATION:
*       See Michael T Flanagan's JAVA library on-line web page:
*       PropInt.html
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
public class PropInt extends BlackBox {
    /** DOCUMENT ME! */
    private double kp = 1.0D; //  proportional gain

    /** DOCUMENT ME! */
    private double ti = Double.POSITIVE_INFINITY; //  integral time constant

    /** DOCUMENT ME! */
    private double ki = 0.0D; //  integral gain

    // Constructor - unit proportional gain, zero integral gain
    /**
     * Creates a new PropInt object.
     */
    public PropInt() {
        super.sNumerDeg = 1;
        super.sDenomDeg = 1;
        super.sNumer = new ComplexPolynomial(new Complex[] {
                    new Complex(0.0D), new Complex(1.0D)
                });
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(0.0D), new Complex(1.0D)
                });
        super.sZeros = new Complex[] { Complex.ZERO };
        super.sZeros[0] = new Complex(0.0D, 0.0D);
        super.sPoles = new Complex[] { Complex.ZERO };
        super.sPoles[0] = new Complex(0.0D, 0.0D);
        super.ztransMethod = 1;
        super.name = "PI";
        super.fixedName = "PI";
        super.addDeadTimeExtras();
    }

    // Set the proportional gain
    /**
     * DOCUMENT ME!
     *
     * @param kp DOCUMENT ME!
     */
    public void setKp(double kp) {
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

    // Calculate the zeros and poles in the s-domain
    /**
     * DOCUMENT ME!
     */
    protected void calcPolesZerosS() {
        super.sZeros[0] = new Complex(-this.ki / this.kp, 0.0D);
        super.sPoles[0] = new Complex(0.0D, 0.0D);
    }

    // Perform z transform using an already set delta T
    /**
     * DOCUMENT ME!
     */
    public void zTransform() {
        super.deadTimeWarning("zTransform");

        if (super.deltaT == 0.0D) {
            System.out.println(
                "z-transform attempted in PropInt with a zero sampling period");
        }

        if (super.ztransMethod == 0) {
            this.mapstozAdHoc();
        } else {
            super.zDenom = new ComplexPolynomial(new Complex[] { new Complex(
                            1.0D) });

            Complex[] coef = new Complex[] { Complex.ZERO, Complex.ZERO };
            coef[0] = new Complex(-1.0D, 0.0D);
            coef[1] = new Complex(1.0D, 0.0D);
            super.zDenom = new ComplexPolynomial(ArrayMathUtils.copy(coef));

            Complex[] zPoles = new Complex[] { Complex.ZERO };
            zPoles[0] = new Complex(1.0D, 0.0D);
            super.zNumer = new ComplexPolynomial(new Complex[] { new Complex(
                            1.0D) });

            Complex[] zZeros = new Complex[] { Complex.ZERO };
            double kit = this.ki * super.deltaT;

            switch (this.integMethod) {
            // trapezium rule
            case 0:
                coef[0] = new Complex((kit / 2.0D) - this.kp, 0.0D);
                coef[1] = new Complex((kit / 2.0D) + this.kp, 0.0D);
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(coef));
                zZeros[0] = new Complex((this.kp - (kit / 2.0D)) / (this.kp +
                        (kit / 2.0D)), 0.0D);

                break;

            // backward rectangular rule
            case 1:
                coef[0] = new Complex(-this.kp, 0.0D);
                coef[1] = new Complex(kit + this.kp, 0.0D);
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(coef));
                zZeros[0] = new Complex(this.kp / (this.kp + kit), 0.0D);

                break;

            // foreward rectangular rule
            case 2:
                coef[0] = new Complex(this.kp - kit, 0.0D);
                coef[1] = new Complex(this.kp, 0.0D);
                super.zNumer = new ComplexPolynomial(ArrayMathUtils.copy(coef));
                zZeros[0] = new Complex((this.kp - kit) / this.kp, 0.0D);

                break;

            default:
                System.out.println(
                    "Integration method option in PropInt must be 0,1 or 2");
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
        // Calculate time course outputs
        int n = 50; // number of points on plot
        double incrT = finalTime / (double) (n - 1); // plotting increment
        double[][] cdata = new double[2][n]; // plotting array
        double sum = 0.0D; // integration sum

        cdata[0][0] = 0.0D;
        cdata[1][0] = 0.0D;

        for (int i = 1; i < n; i++) {
            cdata[0][i] = cdata[0][i - 1] + incrT;
            sum += ((rampGradient * (Math.pow(cdata[0][i], rampOrder + 1) -
            Math.pow(cdata[0][i - 1], rampOrder + 1))) / (double) (rampOrder +
            1));
            cdata[1][i] = (this.kp * rampGradient * Math.pow(cdata[0][i],
                    rampOrder)) + sum;
        }

        for (int i = 0; i < n; i++) {
            cdata[0][i] += super.deadTime;
        }

        // Plot
        PlotGraph pg = new PlotGraph(cdata);

        pg.setGraphTitle("Ramp (a.t^n) Input Transient:   ramp gradient (a) = " +
            rampGradient + " ramp order (n) = " + rampOrder);
        pg.setGraphTitle2(this.getName());
        pg.setXaxisLegend("Time");
        pg.setXaxisUnitsName("s");
        pg.setYaxisLegend("Output");
        pg.setPoint(0);
        pg.plot();
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

        Complex term = super.sValue.multiply(this.kp);
        term = term.add(new Complex(this.ki));
        term = term.divide(super.sValue);
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
        Complex term = super.sValue.multiply(this.kp);
        term = term.add(new Complex(this.ki));
        term = term.divide(super.sValue);
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

    //  calculate the output for the stored sampled input and time.
    /**
     * DOCUMENT ME!
     */
    public void calcOutputT() {
        super.deadTimeWarning("calcOutputT()");

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
    }

    // REDUNDANT BlackBox METHODS
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropInt; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropInt; Not by setSnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by a constructor or special methods in PropInt; Not by setSnumer()");
    }

    // Seting the transfer function denominator in the s-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(double[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropInt; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(Complex[] coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropInt; Not by setSdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setSdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer denominator is set by a constructor or special methods in PropInt; Not by setSdenom()");
    }

    // Setting the transfer function numerator in the z-domain
    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropInt; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropInt; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZnumer(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropInt; Not by setZnumer()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(double[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropInt; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(Complex[] coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropInt; Not by setZdenom()");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff DOCUMENT ME!
     */
    public void setZdenom(ComplexPolynomial coeff) {
        System.out.println(
            "Transfer numerator is set by special methods in PropInt; Not by setZdenom()");
    }
}
