package org.jscience.mathematics.wavelet;

import org.jscience.mathematics.statistics.EngineerMathUtils;


/**
 * This class allows to do some operations on wavelet coefficients
 *
 * @author Daniel Lemire
 */
public final class FWTCoefMathUtils extends Object {
/**
     * Creates a new FWTCoefMathUtils object.
     */
    private FWTCoefMathUtils() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean areCompatible(FWTCoef a, FWTCoef b) {
        if (a.getJ() != b.getJ()) {
            return (false);
        }

        for (int i = 0; i < a.getJ(); i++) {
            if (a.dimension(i) != b.dimension(i)) {
                return (false);
            }
        }

        return (true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean areCompatible(FWTCoef[] a) {
        if (a.length == 1) {
            return (true);
        }

        for (int i = 0; i < (a.length - 1); i++) {
            if (areCompatible(a[i], a[i + 1]) == false) {
                return (false);
            }
        }

        return (true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static int getJ(FWTCoef[] a) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The objects are not compatible.");
        }

        return (a[0].getJ());
    }

    /**
     * Compute the sum of the squares of the coefficients
     *
     * @param a DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double sumSquares(FWTCoef[] a, int i) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The objects are not compatible.");
        }

        if ((i < 0) || (i >= getJ(a))) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (getJ(a) - 1));
        }

        double ans = 0.0;

        for (int k = 0; k < a.length; k++) {
            ans += a[k].sumSquares(i);
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double variance(FWTCoef[] a, int i) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The objects are not compatible.");
        }

        if ((i < 0) || (i >= getJ(a))) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (getJ(a) - 1));
        }

        double ans = 0.0;

        for (int k = 0; k < a.length; k++) {
            ans += a[k].variance(i);
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double sumEnergies(FWTCoef[] a) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The Objects of type FWTCoef are not compatible.");
        }

        if (getJ(a) <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double ans = 0.0;

        for (int k = 0; k < a.length; k++) {
            ans += a[k].sumEnergies();
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double entropy(FWTCoef[] a, int i) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The Objects of type FWTCoef are not compatible.");
        }

        if ((i < 0) || (i >= getJ(a))) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (getJ(a) - 1));
        }

        double sumEnergies = sumSquares(a, i);
        int nombreDeCoefficients = a[0].coefs[i].length;
        double[] energyRatio = new double[nombreDeCoefficients];
        int pos = 0;

        for (int l = 0; l < a[0].coefs[i].length; l++) {
            for (int m = 0; m < a.length; m++) {
                energyRatio[pos] += (((a[m].coefs[i][l]) * (a[m].coefs[i][l])) / sumEnergies);
            }

            pos++;
        }

        return (EngineerMathUtils.icf(energyRatio));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double entropy(FWTCoef[] a) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The Objects of type FWTCoef are not compatible.");
        }

        if (getJ(a) <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double sumEnergies = sumEnergies(a);
        int nombreDeCoefficients = 0;

        for (int k = 1; k < a[0].coefs.length; k++) {
            nombreDeCoefficients += a[0].coefs[k].length;
        }

        double[] energyRatio = new double[nombreDeCoefficients];
        int pos = 0;

        for (int k = 1; k < a[0].coefs.length; k++) {
            for (int l = 0; l < a[0].coefs[k].length; l++) {
                for (int m = 0; m < a.length; m++) {
                    energyRatio[pos] += (((a[m].coefs[k][l]) * (a[m].coefs[k][l])) / sumEnergies);
                }

                pos++;
            }
        }

        return (EngineerMathUtils.icf(energyRatio));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double sumVariance(FWTCoef[] a) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException("The objects are not compatible");
        }

        if (getJ(a) <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double ans = 0.0;

        for (int k = 0; k < a.length; k++) {
            ans += a[k].sumVariance();
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double energyRatio(FWTCoef[] a, int i) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The Objects of type FWTCoef are not compatible.");
        }

        if (getJ(a) <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        if ((i < 1) || (i >= getJ(a))) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (getJ(a) - 1));
        }

        if (sumEnergies(a) == 0) {
            if (getJ(a) != 0) {
                return (1 / getJ(a));
            } else {
                throw new IllegalArgumentException("No energy!");
            }
        }

        return (sumSquares(a, i) / sumEnergies(a));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double varianceRatio(FWTCoef[] a, int i) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The Objects of type FWTCoef are not compatible.");
        }

        if (getJ(a) <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        if ((i < 1) || (i >= getJ(a))) {
            throw new IllegalArgumentException("The integer parameter " + i +
                " should be between 0 and " + (getJ(a) - 1));
        }

        if (sumVariance(a) == 0) {
            if (getJ(a) != 0) {
                return (1 / getJ(a));
            } else {
                throw new IllegalArgumentException("No energy!");
            }
        }

        return (variance(a, i) / sumVariance(a));
    }

    /**
     * Compute the Shannon entropy.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double icf(FWTCoef[] a) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The Objects of type FWTCoef are not compatible.");
        }

        if (getJ(a) <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double[] pe = new double[getJ(a) - 1];

        for (int j = 1; j < getJ(a); j++) {
            pe[j - 1] = energyRatio(a, j);
        }

        return (EngineerMathUtils.icf(pe));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static double icfVariance(FWTCoef[] a) {
        if (areCompatible(a) == false) {
            throw new IllegalArgumentException(
                "The Objects of type FWTCoef are not compatible.");
        }

        if (getJ(a) <= 1) {
            throw new IllegalArgumentException("No wavelet coefficients!");
        }

        double[] pv = new double[getJ(a) - 1];

        for (int j = 1; j < getJ(a); j++) {
            pv[j - 1] = varianceRatio(a, j);
        }

        return (EngineerMathUtils.icf(pv));
    }
}
