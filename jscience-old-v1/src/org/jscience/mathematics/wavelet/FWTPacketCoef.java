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

package org.jscience.mathematics.wavelet;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.statistics.EngineerMathUtils;


/**
 * This class is used to encapsulate wavelet packets coefficients.
 *
 * @author Daniel Lemire
 */
public final class FWTPacketCoef extends Object implements Cloneable {
    /** DOCUMENT ME! */
    final static double normalisation = 1.0 / MathConstants.SQRT2;

    /** DOCUMENT ME! */
    protected double[][] coefs;

    /** DOCUMENT ME! */
    protected boolean[] StandardChoice;

/**
     * Creates a new FWTPacketCoef object.
     */
    public FWTPacketCoef() {
    }

/**
     * *******************************************
     * *********************************************
     */
    public FWTPacketCoef(double[][] v, boolean[] b) {
        coefs = v;
        StandardChoice = b;

        if (b.length != (v.length - 1)) {
            throw new IllegalArgumentException(
                "boolean[].length must be exactly double[][].length -1: boolean[].length=" +
                b.length + " and double[][].length=" + v.length);
        }
    }

    /**
     * Return a copy of this object
     *
     * @return DOCUMENT ME!
     *
     * @throws InternalError DOCUMENT ME!
     */
    public Object clone() {
        try {
            FWTPacketCoef fwtp = (FWTPacketCoef) super.clone();

            if (coefs != null) {
                fwtp.coefs = ArrayMathUtils.copy(coefs);
            }

            if (StandardChoice != null) {
                fwtp.StandardChoice = new boolean[StandardChoice.length];
                System.arraycopy(StandardChoice, 0, fwtp.StandardChoice, 0,
                    StandardChoice.length);
            }

            return (fwtp);
        } catch (CloneNotSupportedException cnse) {
            throw new InternalError();
        }
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public int getJ() {
        return (coefs.length);
    }

    /**
     * 
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public int dimension(int i) {
        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("This scale doesn't exist : " +
                i + ", " + coefs.length);
        }

        return (coefs[i].length);
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public double[][] getCoefs() {
        return (coefs);
    }

    /**
     * Compute the L2 norm of the coefficients
     *
     * @return DOCUMENT ME!
     */
    public double[] norm() {
        double[] ans = new double[coefs.length];

        for (int j = 0; j < coefs.length; j++) {
            ans[j] = ArrayMathUtils.norm(coefs[j]);
        }

        return (ans);
    }

    /**
     * Compute the L2 norm of the coefficients at "scale" i.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double norm(int i) {
        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (coefs.length - 1));
        }

        double ans = ArrayMathUtils.norm(coefs[i]);

        return (ans);
    }

    /**
     * Compute the sum of the squares of the coefficients
     *
     * @return DOCUMENT ME!
     */
    private double[] sumSquares() {
        double[] ans = new double[coefs.length];

        for (int j = 0; j < coefs.length; j++) {
            ans[j] = ArrayMathUtils.sumSquares(coefs[j]);
        }

        return (ans);
    }

    /**
     * Compute the sum of the squares of the coefficients
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double sumSquares(int i) {
        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " must be between 0 et " + (coefs.length - 1));
        }

        double ans = ArrayMathUtils.sumSquares(coefs[i]);

        return (ans);
    }

    /**
     * 
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double mass(int i) {
        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (coefs.length - 1));
        }

        double ans = ArrayMathUtils.mass(coefs[i]);

        return (ans);
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    private double[] variance() {
        double[] ans = new double[coefs.length];

        for (int j = 0; j < coefs.length; j++) {
            ans[j] = ArrayMathUtils.variance(coefs[j]);
        }

        return (ans);
    }

    /**
     * 
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double variance(int i) {
        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (coefs.length - 1));
        }

        double ans = ArrayMathUtils.variance(coefs[i]);

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double sumEnergies() {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double[] energies = sumSquares();
        double ans = 0.0;

        for (int k = 0; k < energies.length; k++) {
            ans += energies[k];
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double entropy() {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double sumEnergies = sumEnergies();
        int nombreDeCoefficients = 0;

        for (int k = 0; k < coefs.length; k++) {
            nombreDeCoefficients += coefs[k].length;
        }

        double[] pourcentageDEnergie = new double[nombreDeCoefficients];
        int pos = 0;

        for (int k = 0; k < coefs.length; k++) {
            for (int l = 0; l < coefs[k].length; l++) {
                pourcentageDEnergie[pos] = (coefs[k][l] * coefs[k][l]) / sumEnergies;
                pos++;
            }
        }

        return (EngineerMathUtils.icf(pourcentageDEnergie));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double sumVariance() {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double[] variances = variance();
        double ans = 0.0;

        for (int k = 0; k < variances.length; k++) {
            ans += variances[k];
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double energyRatio(int i) {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (coefs.length - 1));
        }

        if (sumEnergies() == 0) {
            if (coefs.length != 0) {
                return (1 / coefs.length);
            } else {
                throw new IllegalArgumentException("No energy!");
            }
        }

        return (sumSquares(i) / sumEnergies());
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double varianceRatio(int i) {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parament " + i +
                " should be between 0 and " + (coefs.length - 1));
        }

        if (sumVariance() == 0) {
            if (coefs.length != 0) {
                return (1 / coefs.length);
            } else {
                throw new IllegalArgumentException("No variance!");
            }
        }

        return (variance(i) / sumVariance());
    }

    /**
     * Compute the Shannon entropy.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double icf() {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double[] pe = new double[coefs.length - 1];

        for (int j = 0; j < coefs.length; j++) {
            pe[j] = energyRatio(j);
        }

        return (EngineerMathUtils.icf(pe));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double varianceICF() {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double[] pv = new double[coefs.length - 1];

        for (int j = 0; j < coefs.length; j++) {
            pv[j] = varianceRatio(j);
        }

        return (EngineerMathUtils.icf(pv));
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void setCoefs(double[][] v) {
        coefs = v;
    }

    /**
     * Allows the user to set the Wavelet Packet chosen
     *
     * @param b DOCUMENT ME!
     */
    public void setPacket(boolean[] b) {
        StandardChoice = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCoefs(double[] v, int i) {
        if ((i < 0) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (coefs.length - 1));
        }

        coefs[i] = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     * @param param DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void synthesize(Filter filtreprimaire, double[] param) {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No synthesis left to do: " +
                coefs.length);
        }

        double[] V0;
        double[] W0;

        if (StandardChoice[StandardChoice.length - 1]) {
            V0 = filtreprimaire.lowpass(coefs[coefs.length - 1], param);

            // Lowpass filters must be renormalized.
            V0 = ArrayMathUtils.scalarMultiply(normalisation, V0);
            W0 = filtreprimaire.highpass(coefs[coefs.length - 2], param);
        } else {
            V0 = filtreprimaire.lowpass(coefs[coefs.length - 2], param);

            // Lowpass filters must be renormalized.
            V0 = ArrayMathUtils.scalarMultiply(normalisation, V0);
            W0 = filtreprimaire.highpass(coefs[coefs.length - 1], param);
        }

        if (V0.length != W0.length) {
            throw new IllegalArgumentException(
                "Synthesis is impossible : bad data/multiresolution? " +
                coefs[0].length + ", " + coefs[coefs.length - 1].length + ", " +
                V0.length + ", " + W0.length);
        }

        V0 = ArrayMathUtils.add(V0, W0);

        double[][] c = new double[coefs.length - 1][];

        for (int j = 0; j < (coefs.length - 2); j++) {
            c[j] = coefs[j];
        }

        c[coefs.length - 1] = V0;
        coefs = c;

        boolean[] b = new boolean[c.length - 1];

        for (int j = 0; j < b.length; j++) {
            b[j] = StandardChoice[j];
        }

        StandardChoice = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     * @param param DOCUMENT ME!
     * @param jmax DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void synthesize(Filter filtreprimaire, double[] param, int jmax) {
        if ((jmax < 0) || (jmax > (coefs.length - 1))) {
            throw new IllegalArgumentException("The integer parameter " + jmax +
                " must be between 0 and " + (coefs.length - 1));
        }

        for (int j = 0; j < jmax; j++) {
            synthesize(filtreprimaire, param);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     * @param param DOCUMENT ME!
     */
    public void synthesizeTout(Filter filtreprimaire, double[] param) {
        synthesize(filtreprimaire, param, coefs.length - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void synthesize(Filter filtreprimaire) {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No synthesis left to do: " +
                coefs.length);
        }

        double[] V0;
        double[] W0;

        if (StandardChoice[StandardChoice.length - 1]) {
            V0 = filtreprimaire.lowpass(coefs[coefs.length - 1]);

            // Lowpass filters must be renormalized.
            V0 = ArrayMathUtils.scalarMultiply(normalisation, V0);
            W0 = filtreprimaire.highpass(coefs[coefs.length - 2]);
        } else {
            V0 = filtreprimaire.lowpass(coefs[coefs.length - 2]);

            // Lowpass filters must be renormalized.
            V0 = ArrayMathUtils.scalarMultiply(normalisation, V0);
            W0 = filtreprimaire.highpass(coefs[coefs.length - 1]);
        }

        if (V0.length != W0.length) {
            throw new IllegalArgumentException(
                "Synthesis is impossible : bad data/multiresolution? " +
                coefs[0].length + ", " + coefs[coefs.length - 1].length + ", " +
                V0.length + ", " + W0.length);
        }

        V0 = ArrayMathUtils.add(V0, W0);

        double[][] c = new double[coefs.length - 1][];

        for (int j = 0; j < (coefs.length - 2); j++) {
            c[j] = coefs[j];
        }

        c[coefs.length - 1] = V0;
        coefs = c;

        boolean[] b = new boolean[c.length - 1];

        for (int j = 0; j < b.length; j++) {
            b[j] = StandardChoice[j];
        }

        StandardChoice = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     * @param jmax DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void synthesize(Filter filtreprimaire, int jmax) {
        if ((jmax < 0) || (jmax > (coefs.length - 1))) {
            throw new IllegalArgumentException("The integer parameter " + jmax +
                " must be between 0 and " + (coefs.length - 1));
        }

        for (int j = 0; j < jmax; j++) {
            synthesize(filtreprimaire);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     */
    public void synthesizeAll(Filter filtreprimaire) {
        synthesize(filtreprimaire, coefs.length - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Signal rebuildSignal(Filter filtreprimaire) {
        FWTCoef fwt = new FWTCoef(coefs); // copie
        fwt.synthesizeAll(filtreprimaire);

        return (new Signal(fwt.getCoefs()[0]));
    }

    /**
     * DOCUMENT ME!
     *
     * @param filtreprimaire DOCUMENT ME!
     * @param param DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Signal rebuildSignal(Filter filtreprimaire, double[] param) {
        FWTCoef fwt = new FWTCoef(coefs); // copie
        fwt.synthesizeAll(filtreprimaire, param);

        return (new Signal(fwt.getCoefs()[0]));
    }

    /**
     * Denoises by zero-ing any value above a given percentile cut-off.
     *
     * @param p percentile cut-off, must be between 0 and 1.
     */
    public void denoise(double p) {
        for (int k = 1; k < coefs.length; k++) {
            coefs[k] = denoise(coefs[k], p);
        }
    }

    /**
     * Denoises by zero-ing any value above a given percentile cut-off.
     *
     * @param p percentile cut-off, must be between 0 and 1.
     * @param k the index of the coefficient array to denoise.
     */
    public void denoise(double p, int k) {
        coefs[k] = denoise(coefs[k], p);
    }

    /**
     * Denoises by zero-ing any value above a given percentile cut-off.
     *
     * @param v an array to denoise.
     * @param p percentile cut-off, must be between 0 and 1.
     *
     * @return DOCUMENT ME!
     */
    public static double[] denoise(double[] v, double p) {
        if (p == 0.0) {
            return (v);
        }

        double[] ans = v;
        double seuil = ArrayMathUtils.percentile(ArrayMathUtils.abs(ans), 1 -
                p);

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) >= seuil) {
                ans[k] = 0.0;
            }
        }

        return (ans);
    }

    /**
     * Compresses by zero-ing any value below a given percentile
     * cut-off.
     *
     * @param p percentile cut-off, must be between 0 and 1.
     */
    public void compress(double p) {
        for (int k = 0; k < coefs.length; k++) {
            coefs[k] = compress(coefs[k], p);
        }
    }

    /**
     * Compresses by zero-ing any value below a given percentile
     * cut-off.
     *
     * @param p percentile cut-off, must be between 0 and 1.
     * @param k the index of the coefficient array to compress.
     */
    public void compress(double p, int k) {
        coefs[k] = compress(coefs[k], p);
    }

    /**
     * Compresses by zero-ing any value below a given percentile
     * cut-off.
     *
     * @param v an array to compress.
     * @param p percentile cut-off, must be between 0 and 1.
     *
     * @return DOCUMENT ME!
     */
    public static double[] compress(double[] v, double p) {
        if (p == 0.0) {
            return (v);
        }

        double[] ans = v;
        double seuil = ArrayMathUtils.percentile(ArrayMathUtils.abs(ans), p);

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) <= seuil) {
                ans[k] = 0.0;
            }
        }

        return (ans);
    }

    /**
     * Denoises by zero-ing any value above a given cut-off.
     *
     * @param p cut-off.
     */
    public void denoiseHard(double p) {
        for (int k = 0; k < coefs.length; k++) {
            coefs[k] = denoiseHard(coefs[k], p);
        }
    }

    /**
     * Denoises by zero-ing any value above a given cut-off.
     *
     * @param p cut-off.
     * @param k the index of the coefficient array to denoise.
     */
    public void denoiseHard(double p, int k) {
        coefs[k] = denoiseHard(coefs[k], p);
    }

    /**
     * Denoises by zero-ing any value above a given cut-off.
     *
     * @param v an array to denoise.
     * @param seuil cut-off/threshold.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double[] denoiseHard(double[] v, double seuil) {
        if (seuil < 0.0) {
            throw new IllegalArgumentException(
                "The cutoff value must be positive.");
        }

        double[] ans = v;

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) >= seuil) {
                ans[k] = 0.0;
            }
        }

        return (ans);
    }

    /**
     * Compresses by zero-ing any value below a given cut-off.
     *
     * @param p cut-off.
     */
    public void compressHard(double p) {
        for (int k = 0; k < coefs.length; k++) {
            coefs[k] = compressHard(coefs[k], p);
        }
    }

    /**
     * Compresses by zero-ing any value below a given cut-off.
     *
     * @param p cut-off.
     * @param k the index of the coefficient array to compress.
     */
    public void compressHard(double p, int k) {
        coefs[k] = compressHard(coefs[k], p);
    }

    /**
     * Compresses by zero-ing any value below a given cut-off.
     *
     * @param v an array to compress.
     * @param seuil cut-off/threshold.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double[] compressHard(double[] v, double seuil) {
        if (seuil < 0.0) {
            throw new IllegalArgumentException(
                "The cutoff value must be positive.");
        }

        double[] ans = v;

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) <= seuil) {
                ans[k] = 0.0;
            }
        }

        return (ans);
    }
}
