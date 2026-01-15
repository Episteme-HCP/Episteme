package org.jscience.mathematics.wavelet;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.statistics.EngineerMathUtils;


/**
 * This class is used to encapsulate wavelet coefficients.
 *
 * @author Daniel Lemire
 */
public final class FWTCoef extends Object implements Cloneable {
    /** DOCUMENT ME! */
    final static double normalisation = 1.0 / MathConstants.SQRT2;

    /** DOCUMENT ME! */
    protected double[][] coefs;

/**
     * Creates a new FWTCoef object.
     */
    public FWTCoef() {
    }

/**
     * *******************************************
     * *********************************************
     */
    public FWTCoef(double[][] v) {
        coefs = v;
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
            FWTCoef fwt = (FWTCoef) super.clone();

            if (coefs != null) {
                fwt.coefs = ArrayMathUtils.copy(coefs);
            }

            return (fwt);
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
            throw new IllegalArgumentException(
                "This dimension doesn't exist : " + i + ", " + coefs.length);
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
     * Compute the L2 norm of the coefficients at "scale" i. Wavelet
     * coefficients are into the "scale" 1 to ... and the scale 0 is the
     * coarsest scale containing scaling functions coefficients
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
                " should be between 0 and " + (coefs.length - 1));
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
     * 
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
        double ans = 0;

        for (int k = 1; k < energies.length; k++) {
            ans += energies[k];
        }

        return (ans);
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double entropy() {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double se = sumEnergies();
        int nombreDeCoefficients = 0;

        for (int k = 1; k < coefs.length; k++) {
            nombreDeCoefficients += coefs[k].length;
        }

        double[] er = new double[nombreDeCoefficients];
        int pos = 0;

        for (int k = 1; k < coefs.length; k++) {
            for (int l = 0; l < coefs[k].length; l++) {
                er[pos] = (coefs[k][l] * coefs[k][l]) / se;
                pos++;
            }
        }

        return (EngineerMathUtils.icf(er));
    }

    /**
     * 
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
        double ans = 0;

        for (int k = 1; k < variances.length; k++) {
            ans += variances[k];
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
    public double energyRatio(int i) {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        if ((i < 1) || (i >= coefs.length)) {
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
     * 
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

        if ((i < 1) || (i >= coefs.length)) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (coefs.length - 1));
        }

        if (sumVariance() == 0) {
            if (coefs.length != 0) {
                return (1 / coefs.length);
            } else {
                throw new IllegalArgumentException("No energy!");
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

        for (int j = 1; j < coefs.length; j++) {
            pe[j - 1] = energyRatio(j);
        }

        return (EngineerMathUtils.icf(pe));
    }

    /**
     * 
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

        for (int j = 1; j < coefs.length; j++) {
            pv[j - 1] = varianceRatio(j);
        }

        return (EngineerMathUtils.icf(pv));
    }

    /**
     * 
     *
     * @param v DOCUMENT ME!
     */
    public void setCoefs(double[][] v) {
        coefs = v;
    }

    /**
     * 
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
     * 
     *
     * @param filtreprimaire DOCUMENT ME!
     * @param param DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void synthesize(Filter filtreprimaire, double[] param) {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No synthesis possible : " +
                coefs.length);
        }

        double[] V0 = filtreprimaire.lowpass(coefs[0], param);
        double[] W0 = filtreprimaire.highpass(coefs[coefs.length - 1], param);
        V0 = ArrayMathUtils.scalarMultiply(normalisation, V0);

        if (V0.length != W0.length) {
            throw new IllegalArgumentException(
                "Synthesis impossible : bad data/multiresolution?" +
                coefs[0].length + ", " + coefs[coefs.length - 1].length + ", " +
                V0.length + ", " + W0.length);
        }

        V0 = ArrayMathUtils.add(V0, W0);

        double[][] c = new double[coefs.length - 1][];

        for (int j = 1; j < (coefs.length - 1); j++) {
            c[j] = coefs[j];
        }

        c[0] = V0;
        coefs = c;
    }

    /**
     * 
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
     * 
     *
     * @param filtreprimaire DOCUMENT ME!
     * @param param DOCUMENT ME!
     */
    public void synthesizeAll(Filter filtreprimaire, double[] param) {
        synthesize(filtreprimaire, param, coefs.length - 1);
    }

    /**
     * 
     *
     * @param filtreprimaire DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void synthesize(Filter filtreprimaire) {
        if (coefs.length <= 1) {
            throw new IllegalArgumentException("No synthesis possible : " +
                coefs.length);
        }

        double[] V0 = filtreprimaire.lowpass(coefs[0]);
        double[] W0 = filtreprimaire.highpass(coefs[coefs.length - 1]);
        V0 = ArrayMathUtils.scalarMultiply(normalisation, V0);

        if (V0.length != W0.length) {
            throw new IllegalArgumentException(
                "Synthesis impossible : bad data/multiresolution?" +
                coefs[0].length + ", " + coefs[coefs.length - 1].length + ", " +
                V0.length + ", " + W0.length);
        }

        V0 = ArrayMathUtils.add(V0, W0);

        double[][] c = new double[coefs.length - 1][];

        for (int j = 1; j < (coefs.length - 1); j++) {
            c[j] = coefs[j];
        }

        c[0] = V0;
        coefs = c;
    }

    /**
     * 
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
     * 
     *
     * @param filtreprimaire DOCUMENT ME!
     */
    public void synthesizeAll(Filter filtreprimaire) {
        synthesize(filtreprimaire, coefs.length - 1);
    }

    /**
     * 
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
     * 
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
        if (p == 0) {
            return (v);
        }

        double[] ans = v;
        double seuil = ArrayMathUtils.percentile(ArrayMathUtils.abs(ans), 1 -
                p);

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) >= seuil) {
                ans[k] = 0;
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
        for (int k = 1; k < coefs.length; k++) {
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
        if (p == 0) {
            return (v);
        }

        double[] ans = v;
        double seuil = ArrayMathUtils.percentile(ArrayMathUtils.abs(ans), p);

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) <= seuil) {
                ans[k] = 0;
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
        for (int k = 1; k < coefs.length; k++) {
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
        if (seuil < 0) {
            throw new IllegalArgumentException(
                "The cutoff value must be positive.");
        }

        double[] ans = v;

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) >= seuil) {
                ans[k] = 0;
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
        for (int k = 1; k < coefs.length; k++) {
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
        if (seuil < 0) {
            throw new IllegalArgumentException(
                "The cutoff value must be positive.");
        }

        double[] ans = v;

        for (int k = 0; k < ans.length; k++) {
            if (Math.abs(ans[k]) <= seuil) {
                ans[k] = 0;
            }
        }

        return (ans);
    }
}
