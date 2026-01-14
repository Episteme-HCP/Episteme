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

package org.jscience.physics.nuclear.kinematics.math.statistics;

import org.jscience.physics.nuclear.kinematics.math.Matrix;
import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.math.analysis.GJENice;

import java.io.*;

import java.util.Vector;


/**
 * Given a data set, performs a linear regression, then can be queried for
 * the results. Based on code in Numerical Recipes in C
 *
 * @author Dale Visser
 */
public class PolynomialFitErrY {
    /**
     * DOCUMENT ME!
     */
    private double[] a; // fit coefficients y=a[0]+a[1]*x+a[2]*x^2+...

    /**
     * DOCUMENT ME!
     */
    private double[] siga; //sqrt(diagonal elements of covar)

    /**
     * DOCUMENT ME!
     */
    private Matrix covar; // covariance matrix for coefficients

    /**
     * DOCUMENT ME!
     */
    private double chi2; // chi-sqared statistic

    /**
     * DOCUMENT ME!
     */
    private double q; // p-value

    /**
     * DOCUMENT ME!
     */
    private int dof; //degrees of freedom

    /**
     * DOCUMENT ME!
     */
    private int degree; //order of polynomial of fit

    /**
     * DOCUMENT ME!
     */
    private int ndata;

    /**
     * DOCUMENT ME!
     */
    private boolean weighted;

    /**
     * DOCUMENT ME!
     */
    private double[] x;

    /**
     * DOCUMENT ME!
     */
    private double[] y;

    /**
     * DOCUMENT ME!
     */
    private double[] sig;

    /**
     * DOCUMENT ME!
     */
    public double[] residual;

    /**
     * DOCUMENT ME!
     */
    private double meanx;

    /**
     * DOCUMENT ME!
     */
    private boolean centered;

    /**
     * DOCUMENT ME!
     */
    private double SSE;

    /**
     * DOCUMENT ME!
     */
    private double MSE;

    /**
     * DOCUMENT ME!
     */
    private double SSX;

/**
     * Creates and performs polynomial regression on weighted data set.
     *
     * @param x   the x coordinates of the points
     * @param y   the y coordinates of the points
     * @param sig the error bars on the y coordinates
     */
    public PolynomialFitErrY(int order, double[] x, double[] y, double[] sig,
        boolean centered) throws StatisticsException {
        if (x.length != y.length) {
            throw new StatisticsException(getClass().getName() +
                ": Array lengths not equal x[" + x.length + "] and y[" +
                y.length + "].");
        }

        if (x.length != sig.length) {
            throw new StatisticsException(getClass().getName() +
                ": Array lengths not equal x,y[" + x.length + "] and sig[" +
                sig.length + "].");
        }

        if (order < 1) {
            throw new StatisticsException(getClass().getName() +
                ": Order of fit polynomial must be 1 or greater.");
        }

        if (x.length <= (order + 1)) {
            throw new StatisticsException(getClass().getName() +
                ": Data set must be at least size " + (order + 2) +
                " to fit with degree " + order + " polynomial.");
        }

        ndata = x.length;
        degree = order;
        dof = ndata - degree - 1;
        a = new double[order + 1];
        siga = new double[order + 1];
        weighted = true;
        this.x = new double[x.length];
        System.arraycopy(x, 0, this.x, 0, x.length);
        this.centered = centered;

        if (centered) {
            adjustX();
        }

        this.y = new double[y.length];
        System.arraycopy(y, 0, this.y, 0, y.length);
        this.sig = new double[sig.length];
        System.arraycopy(sig, 0, this.sig, 0, sig.length);
        fit();
    }

    /**
     * Creates a new PolynomialFitErrY object.
     *
     * @param order DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param sig DOCUMENT ME!
     *
     * @throws StatisticsException DOCUMENT ME!
     */
    public PolynomialFitErrY(int order, double[] x, double[] y, double[] sig)
        throws StatisticsException {
        this(order, x, y, sig, false);
    }

    /**
     * Creates a new PolynomialFitErrY object.
     *
     * @param batch DOCUMENT ME!
     * @param out DOCUMENT ME!
     * @param centered DOCUMENT ME!
     *
     * @throws StatisticsException DOCUMENT ME!
     */
    public PolynomialFitErrY(File batch, File out, boolean centered)
        throws StatisticsException {
        String temp = "\n";
        Vector fx = new Vector();
        Vector fy = new Vector();
        Vector fsig = new Vector();

        try {
            LineNumberReader lr = new LineNumberReader(new FileReader(batch));
            StreamTokenizer st = new StreamTokenizer(new BufferedReader(lr));
            st.eolIsSignificant(false); //treat end of line as white space
            st.nextToken();
            degree = (int) Math.round(st.nval);
            st.nextToken();

            String xname = st.sval;
            st.nextToken();

            String yname = st.sval;

            do {
                st.nextToken();

                if (st.ttype != StreamTokenizer.TT_EOF) {
                    fx.addElement(new Double(st.nval));
                    st.nextToken();
                    fy.addElement(new Double(st.nval));
                    //if (weighted) {
                    st.nextToken();
                    fsig.addElement(new Double(st.nval));
                    temp = " +/- " + fsig.lastElement() + "\n";
                    //}
                    System.out.print(xname + " = " + fx.lastElement() + ", " +
                        yname + " = " + fy.lastElement() + temp);
                }
            } while (st.ttype != StreamTokenizer.TT_EOF);

            int size = fx.size();
            x = new double[size];
            y = new double[size];
            /*if (weighted)*/
            sig = new double[size];

            for (int i = 0; i < size; i++) {
                x[i] = ((Double) fx.elementAt(i)).doubleValue();
                y[i] = ((Double) fy.elementAt(i)).doubleValue();
                /*if (weighted)*/
                sig[i] = ((Double) fsig.elementAt(i)).doubleValue();
            }

            ndata = x.length;

            PolynomialFitErrY inverse = invertFit(centered);

            if (centered) {
                this.adjustX();
            }

            dof = ndata - degree - 1;
            a = new double[degree + 1];
            siga = new double[degree + 1];
            weighted = true;
            fit();

            FileWriter fw = new FileWriter(out);
            fw.write(yname + " = a0 + a1 * (" + xname + " - " + meanx +
                ") +a 2 * ...\n");
            fw.write("term\tvalue\terror\n");

            double[] smod = getAdjustedCoeffErrors();

            for (int i = 0; i < a.length; i++) {
                fw.write(i + "\t" + a[i] + "\t" + smod[i] + "\n");
            }

            fw.write("\n" + xname + "\t" + yname + "\tsig\tresid\n");

            for (int i = 0; i < x.length; i++) {
                fw.write((x[i] + meanx) + "\t" + y[i] + "\t" + sig[i] + "\t" +
                    residual[i] + "\n");
            }

            fw.write("\n");
            System.out.println(this);
            fw.write(xname + " = b0 + b1 * (" + yname + " - " +
                inverse.getMeanX() + ") + b2 * ...\n");
            smod = inverse.getAdjustedCoeffErrors();

            double[] b = inverse.getFitCoefficients();

            for (int i = 0; i < a.length; i++) {
                fw.write(i + "\t" + b[i] + "\t" + smod[i] + "\n");
            }

            fw.write("\n" + yname + "\t" + xname + "\tsig\tresid\n");

            for (int i = 0; i < x.length; i++) {
                fw.write((inverse.x[i] + inverse.getMeanX()) + "\t" +
                    inverse.y[i] + "\t" + inverse.sig[i] + "\t" +
                    inverse.residual[i] + "\n");
            }

            fw.write("\n");
            System.out.println(inverse);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void adjustX() {
        double sx = 0.0;

        for (int i = 0; i < ndata; i++) {
            sx += x[i];
        }

        meanx = sx / ndata;

        for (int i = 0; i < ndata; i++) {
            x[i] -= meanx;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMeanX() {
        return meanx;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getFitCoefficients() {
        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getCoefficientErrors() {
        return siga;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getAdjustedCoeffErrors() {
        if (getReducedChiSq() > 1.0) {
            double[] smod = new double[siga.length];
            double fac = Math.sqrt(getReducedChiSq());

            for (int i = 0; i < siga.length; i++) {
                smod[i] = siga[i] * fac;
            }

            return smod;
        } else {
            return siga;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getChiSq() {
        return chi2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getReducedChiSq() {
        return chi2 / dof;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDegreesOfFreedom() {
        return dof;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_p_value() {
        return q;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double calculateY(double x) {
        double rval = 0.0;

        for (int i = 0; i <= degree; i++) {
            rval += (a[i] * Math.pow(x - meanx, i));
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     */
    public UncertainNumber calculateY(UncertainNumber x)
        throws org.jscience.physics.nuclear.kinematics.math.statistics.StatisticsException {
        double curveError = org.jscience.physics.nuclear.kinematics.math.statistics.MathUtils.findOneSigmaT(dof) * Math.sqrt(MSE * ((1 / this.x.length) +
                (Math.pow(x.value - meanx, 2) / SSX)));
        double value = calculateY(x.value);
        double xError = Math.abs(calculateY(x.value + x.error) - value);

        return new UncertainNumber(value,
            Math.sqrt((curveError * curveError) + (xError * xError)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSlope(double x) {
        double rval = 0.0;

        for (int i = 1; i <= degree; i++) {
            rval += (i * a[i] * Math.pow(x - meanx, i - 1));
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double calculateFitValue(int index) {
        return calculateY(x[index] + meanx);
    }

    /**
     * Inverts x and y data sets.  X error bars are determined using
     * sigy/y ratios.  Useful for inverting linear calibrations of positive
     * sets (e.g., silicon detector channel vs. energy).
     *
     * @param centered DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws StatisticsException DOCUMENT ME!
     */
    public PolynomialFitErrY invertFit(boolean centered)
        throws StatisticsException {
        double[] newsigy = new double[x.length];
        double[] newx = new double[x.length];
        double[] newy = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            newsigy[i] = sig[i] / y[i] * x[i];
            newx[i] = y[i];
            newy[i] = x[i];
        }

        return new PolynomialFitErrY(degree, newx, newy, newsigy, centered);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws StatisticsException DOCUMENT ME!
     */
    private void fit() throws StatisticsException {
        Matrix A = new Matrix(x.length, degree + 1); //A[ij] = x[j]/sig[i]
        Matrix b = new Matrix(x.length, 1); //b[i]=y[i]/sig[i]
        Matrix alpha; //alpha = A[transpose]*A
        Matrix beta; //beta = A[traspose]*b
        GJENice gj; //for quick diagonalization of matrices

        chi2 = 0.0;

        //assumed weighted fit
        for (int r = 0; r < A.element.length; r++) {
            b.element[r][0] = y[r] / sig[r];

            for (int c = 0; c < A.element[0].length; c++) {
                A.element[r][c] = Math.pow(x[r], c) / sig[r];
            }
        }

        alpha = new Matrix(A.transpose(), A, '*');
        beta = new Matrix(A.transpose(), b, '*');

        try {
            gj = new GJENice(alpha, beta);
            covar = gj.getInverse();

            for (int i = 0; i <= degree; i++) {
                a[i] = gj.getSolution().element[i][0];
            }
        } catch (org.jscience.physics.nuclear.kinematics.math.MathException me) {
            throw new StatisticsException(me.getMessage());
        }

        computeSigA();
        computeResiduals();
        q = MathUtils.gammq(0.5 * (ndata - 2), 0.5 * (chi2));
    }

    /**
     * DOCUMENT ME!
     */
    private void computeSigA() {
        for (int i = 0; i < siga.length; i++) {
            siga[i] = Math.sqrt(covar.element[i][i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void computeResiduals() {
        residual = new double[x.length];

        double weightSum = 0.0;
        SSE = 0.0;
        SSX = 0.0;
        chi2 = 0.0;

        for (int i = 0; i < x.length; i++) {
            residual[i] = y[i] - calculateFitValue(i);
            chi2 += Math.pow(residual[i] / sig[i], 2.0);

            double weight = Math.pow(sig[i], -2.0);
            weightSum += weight;
            SSX += (x[i] * x[i]);
        }

        SSE = chi2 / weightSum;
        MSE = SSE / dof;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getCovarianceMatrix() {
        return covar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getAdjustedCovarMatrix() {
        if (getReducedChiSq() > 1.0) {
            return new Matrix(Math.sqrt(getReducedChiSq()), covar, '*');
        } else {
            return covar;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rval = "Unweighted ";

        if (weighted) {
            rval = "Weighted ";
        }

        rval += ("polynomial (order " + degree + ") regression of " + (ndata) +
        " points:\n");
        rval += ("  Reduced Chi-Squared Statistic = " + getReducedChiSq() +
        "\n");
        rval += ("  P-Value = " + q + "\n");
        rval += ("  x-offset used: " + meanx + "\n");
        rval += "Coefficients:\n";

        for (int i = 0; i < a.length; i++) {
            rval += ("\t a" + i + ": " + a[i] + " +/- " + siga[i] + "\n");
        }

        rval += ("Covariance Matrix:\n" + covar.toString(5) + "\n");

        if (getReducedChiSq() > 1.0) {
            rval += "Adjusted for chisq value: \n";
            rval += "Coefficients:\n";

            double[] smod = getAdjustedCoeffErrors();

            for (int i = 0; i < a.length; i++) {
                rval += ("\t a" + i + ": " + a[i] + " +/- " + smod[i] + "\n");
            }

            rval += "Covariance Matrix:\n";
            rval += (getAdjustedCovarMatrix().toString(5) + "\n");
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getNormalizedResidual(int index) {
        return residual[index] / sig[index];
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new PolynomialFitErrY(new File(args[0]), new File(args[1]), true);
        } catch (StatisticsException me) {
            System.err.println(me);
        }
    }
}
