package org.jscience.mathematics.geometry;

import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;

//this class is not part of the API and should not be included in the javadoc
class GeometryPrivateUtils extends Object {

    private GeometryPrivateUtils() {
    }

    /**
     * ï¿½e?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½ï¿½Ü‚Þ“ñŽŸŒï¿½ï¿½zï¿½ï¿½ï¿½^ï¿½ï¿½ï¿½ÄƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
     * <p/>
     * coef[i][j] ï¿½É‚ï¿½ j ï¿½ï¿½ï¿½ï¿½ï¿½Ú‚Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ i ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½Ì’lï¿½ï¿½ï¿½iï¿½[ï¿½ï¿½ï¿½ï¿½Ä‚ï¿½ï¿½ï¿½ï¿½Ì‚Æ‚ï¿½ï¿½ï¿½?B
     * </p>
     * <p/>
     * dimension ï¿½Å—^ï¿½ï¿½ï¿½ï¿½ï¿½éŽŸï¿½ï¿½ï¿½É‘Î‰ï¿½ï¿½ï¿½ï¿½ï¿½W?ï¿½ï¿½lï¿½Å‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?B
     * ï¿½Â‚Ü‚ï¿½?A?ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½é‘½?ï¿½ï¿½ï¿½ï¿½ï¿½ i ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½ï¿½ coef[i][dimension] ï¿½É‚È‚ï¿½?B
     * </p>
     * <p/>
     * coef ï¿½ï¿½ null ï¿½ï¿½?ï¿½?ï¿½ï¿½É‚ï¿½
     * InvalidArgumentValueException ï¿½Ì—ï¿½Oï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?B
     * ï¿½Ü‚ï¿½?Acoef ï¿½Ì—vï¿½f?ï¿½ï¿½ï¿½ 1 ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½?ï¿½ï¿½É‚ï¿½
     * InvalidArgumentValueException ï¿½Ì—ï¿½Oï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?B
     * ï¿½ï¿½ï¿½ï¿½ï¿½?Adimension ï¿½Ì’lï¿½ï¿½ 0 ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½?A
     * ï¿½ï¿½ï¿½é‚¢ï¿½ï¿½ (coef[0].length - 1) ï¿½ï¿½ï¿½å‚«ï¿½ï¿½?ï¿½?ï¿½ï¿½É‚ï¿½
     * InvalidArgumentValueException ï¿½Ì—ï¿½Oï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?B
     * </p>
     *
     * @param coef      ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ÌŠe?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½Ì“ñŽŸŒï¿½ï¿½zï¿½ï¿½ [ï¿½ï¿½?ï¿½][ï¿½ï¿½ï¿½ï¿½]
     * @param dimension ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½?ï¿½ï¿½ï¿½ï¿½éŽŸï¿½ï¿½ (0 ï¿½x?[ï¿½X)
     */
    public static DoublePolynomial getDoublePolynomial(double[][] coef,
                                                       int dimension) throws InvalidArgumentValueException {
        if (coef == null)
            throw new InvalidArgumentValueException("Array of coefficients is null.");

        if (coef.length < 1)
            throw new InvalidArgumentValueException("Size of array of coefficients is zero.");

        if ((dimension < 0) || (coef[0].length <= dimension))
            throw new InvalidArgumentValueException("Wrong dimension.");

        double[] coefs = new double[coef.length];
        for (int i = 0; i < coef.length; i++)
            coefs[i] = coef[i][dimension];
        //this.normalized = false;
        return new DoublePolynomial(coefs);
    }

    /**
     * 2 ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ÌŠe?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½ï¿½^ï¿½ï¿½ï¿½ÄƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
     * <p/>
     * ï¿½ï¿½ï¿½ÌƒRï¿½ï¿½ï¿½Xï¿½gï¿½ï¿½ï¿½Nï¿½^ï¿½ï¿½?ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½é‘½?ï¿½ï¿½ï¿½ï¿½ÌŽï¿½?ï¿½ï¿½Í•Kï¿½ï¿½ 2 ï¿½ï¿½ï¿½Å‚ï¿½ï¿½ï¿½?B
     * </p>
     *
     * @param coef0 0 ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½l
     * @param coef1 1 ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½l
     * @param coef2 2 ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½l
     */
    public static DoublePolynomial getDoublePolynomial(double coef0,
                                                       double coef1,
                                                       double coef2) {
        double[] coef;
        coef = new double[3];
        coef[0] = coef0;
        coef[1] = coef1;
        coef[2] = coef2;
        return new DoublePolynomial(coef);
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ÌŽwï¿½ï¿½Ì”ÍˆÍ‚ÌŽï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½Ì’lï¿½ï¿½Ô‚ï¿½?B
     * <p/>
     * lower ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ upper ï¿½ï¿½ï¿½Ü‚Å‚ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½ï¿½Ô‚ï¿½?B
     * </p>
     *
     * @param lower ï¿½ï¿½?ï¿½ï¿½Ì‰ï¿½ï¿½ï¿½
     * @param upper ï¿½ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½
     * @return ï¿½wï¿½ï¿½ÌŽï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½Ì”zï¿½ï¿½
     */
    public static double[] coefficientsBetween(DoublePolynomial func, int lower,
                                               int upper) {
        int n = upper - lower + 1;
        double[] result = new double[n];
        for (int i = 0; i < n; i++)
            result[i] = func.getCoefficientAsDouble(lower + i);
        return result;
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ñŽŸ•ï¿½ï¿½ (A0 + A1 * t + A2 * t^2 = 0) ï¿½ï¿½ï¿½?B
     * <p/>
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ 2 ï¿½ï¿½ï¿½Å‚È‚ï¿½ï¿½ï¿½ï¿½ null ï¿½ï¿½Ô‚ï¿½?B
     * </p>
     * <p/>
     * ï¿½ï¿½ï¿½Ê‚Æ‚ï¿½ï¿½Ä“ï¿½ï¿½ï¿½ï¿½ï¿½zï¿½ï¿½ï¿½?Aï¿½ï¿½ï¿½ÌŽï¿½?ï¿½?ï¿½ï¿½ï¿½Ü‚ï¿½?B
     * ï¿½ï¿½Bï¿½?Aï¿½ï¿½ï¿½Ì”zï¿½ï¿½Ì’ï¿½ï¿½ï¿½ï¿½ï¿½ 0 ï¿½È‚ï¿½ï¿½ï¿½ 2 ï¿½Å‚ï¿½ï¿½ï¿½?B
     * ï¿½ï¿½?ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?Ý‚ï¿½ï¿½È‚ï¿½?ï¿½?ï¿½ï¿½Í’ï¿½ï¿½ï¿½ 0 ï¿½Ì”zï¿½ï¿½ï¿½Ô‚ï¿½?B
     * </p>
     * <p/>
     * mustHaveRoots ï¿½Ì’lï¿½ï¿½ true ï¿½ï¿½?ï¿½?ï¿½ï¿½É‚ï¿½?A
     * ï¿½ï¿½ï¿½ÊŽï¿½ï¿½ï¿½ï¿½ï¿½ï¿½É‚È‚é‚±ï¿½Æ‚ï¿½ï¿½È‚ï¿½?A
     * coef[2], coef[1] ï¿½ï¿½?ï¿½Î’lï¿½ï¿½ï¿½Æ‚ï¿½ï¿½
     * 0 ï¿½É‚È‚ï¿½È‚ï¿½ï¿½ï¿½Ì‚Æ‚ï¿½ï¿½Ä•ï¿½ï¿½ï¿½ï¿½?B
     * ï¿½ï¿½Bï¿½?Aï¿½ï¿½ï¿½ï¿½?ï¿½?ï¿½ï¿½É‚ï¿½?Aï¿½ï¿½ï¿½?ï¿½ï¿½Í•Kï¿½ï¿½ 1 ï¿½ï¿½?ï¿½Æ‚È‚ï¿½?B
     * </p>
     *
     * @param mustHaveRoots ï¿½Kï¿½ï¿½ï¿½ï¿½?ï¿½?ï¿½ï¿½ï¿½?ï¿½BÄ‚ï¿½ï¿½ï¿½Æ‚Ý‚È‚ï¿½ï¿½ï¿½ï¿½Ç‚ï¿½ï¿½ï¿½ï¿½î¦‚ï¿½ï¿½tï¿½ï¿½ï¿½O
     * @return ï¿½ï¿½?ï¿½?ï¿½ï¿½Ì”zï¿½ï¿½
     * @see #getRootsIfQuadric()
     * @see #getAlwaysRootsIfQuadric()
     */
    private static double[] getRootsIfQuadric(DoublePolynomial func, boolean mustHaveRoots) {
        if (func.degree() != 2)
            return null;

        double[] normalCoef = new double[3];    // normalized coefficients
        double maxValue;            // max. absolute value of coefficients
        double[] roots;                // solutions

        /*
        * normalize coefficients
        */
        maxValue = (Math.abs(func.getCoefficientAsDouble(2)) > Math.abs(func.getCoefficientAsDouble(1)))
                ? Math.abs(func.getCoefficientAsDouble(2)) : Math.abs(func.getCoefficientAsDouble(1));
        maxValue = (maxValue > Math.abs(func.getCoefficientAsDouble(0))) ? maxValue : Math.abs(func.getCoefficientAsDouble(0));

        normalCoef[2] = func.getCoefficientAsDouble(2) / maxValue;
        normalCoef[1] = func.getCoefficientAsDouble(1) / maxValue;
        normalCoef[0] = func.getCoefficientAsDouble(0) / maxValue;

        /*
        * look the magnitude of each coefficient
        */
        if (Math.abs(normalCoef[2]) < MachineEpsilon.DOUBLE) {
            if (!mustHaveRoots && Math.abs(normalCoef[1]) < MachineEpsilon.DOUBLE) {
                /*
                * A0 = 0
                */
                roots = new double[0];
            } else {
                /*
                * A0 + A1 * t = 0
                */
                roots = new double[1];
                roots[0] = -normalCoef[0] / normalCoef[1];
            }
        } else {
            /*
            * A0 + A1 * t + A2 * t^2 = 0
            */
            double discriminant = normalCoef[1] * normalCoef[1] - 4.0 * normalCoef[2] * normalCoef[0];

            if (!mustHaveRoots && discriminant < (-MachineEpsilon.DOUBLE)) {
                /*
                * roots are complex
                */
                roots = new double[0];
            } else {
                /*
                * roots are real
                */
                int nRoots; // # of solutions
                double[] twoRoots = new double[2];
                boolean secondByAdding;

                if (discriminant > (MachineEpsilon.DOUBLE * MachineEpsilon.DOUBLE)) {
                    nRoots = 2;
                } else {
                    discriminant = 0.0;
                    nRoots = 1;
                }
                discriminant = Math.sqrt(discriminant);

                if (normalCoef[1] > 0.0) {
                    twoRoots[0] = (-normalCoef[1]) - discriminant;
                    secondByAdding = true;
                } else {
                    twoRoots[0] = (-normalCoef[1]) + discriminant;
                    secondByAdding = false;
                }
                twoRoots[0] /= 2.0 * normalCoef[2];

                if (Math.abs(twoRoots[0]) > MachineEpsilon.DOUBLE) {
                    twoRoots[1] = (normalCoef[0] / normalCoef[2]) / twoRoots[0];
                } else {
                    if (secondByAdding == true) {
                        twoRoots[1] = (-normalCoef[1]) + discriminant;
                    } else {
                        twoRoots[1] = (-normalCoef[1]) - discriminant;
                    }
                    twoRoots[1] /= 2.0 * normalCoef[2];
                }

                roots = new double[nRoots];

                if (nRoots == 1) {
                    roots[0] = (twoRoots[0] + twoRoots[1]) / 2.0;
                } else {
                    roots[0] = twoRoots[0];
                    roots[1] = twoRoots[1];
                }
            }
        }

        return roots;
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ñŽŸ•ï¿½ï¿½ (A0 + A1 * t + A2 * t^2 = 0) ï¿½ï¿½ï¿½?B
     * <p/>
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ 2 ï¿½ï¿½ï¿½Å‚È‚ï¿½ï¿½ï¿½ï¿½ null ï¿½ï¿½Ô‚ï¿½?B
     * </p>
     * <p/>
     * ï¿½ï¿½ï¿½Ê‚Æ‚ï¿½ï¿½Ä“ï¿½ï¿½ï¿½ï¿½ï¿½zï¿½ï¿½ï¿½?Aï¿½ï¿½ï¿½ÌŽï¿½?ï¿½?ï¿½ï¿½ï¿½Ü‚ï¿½?B
     * ï¿½ï¿½Bï¿½?Aï¿½ï¿½ï¿½Ì”zï¿½ï¿½Ì’ï¿½ï¿½ï¿½ï¿½ï¿½ 0 ï¿½È‚ï¿½ï¿½ï¿½ 2 ï¿½Å‚ï¿½ï¿½ï¿½?B
     * ï¿½ï¿½?ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?Ý‚ï¿½ï¿½È‚ï¿½?ï¿½?ï¿½ï¿½Í’ï¿½ï¿½ï¿½ 0 ï¿½Ì”zï¿½ï¿½ï¿½Ô‚ï¿½?B
     * </p>
     *
     * @return ï¿½ï¿½?ï¿½?ï¿½ï¿½Ì”zï¿½ï¿½
     * @see #getAlwaysRootsIfQuadric()
     */
    public static double[] getRootsIfQuadric(DoublePolynomial func) {
        return getRootsIfQuadric(func, false);
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ñŽŸ•ï¿½ï¿½ (A0 + A1 * t + A2 * t^2 = 0) ï¿½ï¿½ï¿½?B
     * <p/>
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ 2 ï¿½ï¿½ï¿½Å‚È‚ï¿½ï¿½ï¿½ï¿½ null ï¿½ï¿½Ô‚ï¿½?B
     * </p>
     * <p/>
     * ï¿½ï¿½ï¿½Ìƒ?ï¿½\ï¿½bï¿½hï¿½ï¿½?A
     * ï¿½ï¿½{ï¿½Iï¿½É‚ï¿½ {@link #getRootsIfQuadric() getRootsIfQuadric()} ï¿½Æ“ï¿½ï¿½ï¿½ï¿½Å‚ï¿½ï¿½é‚ª?A
     * ï¿½ï¿½ï¿½ï¿½ï¿½Kï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½?ï¿½BÄ‚ï¿½ï¿½ï¿½Æ‚Ý‚È‚ï¿½ï¿½Ä‰ï¿½?B
     * ï¿½ï¿½ï¿½È‚í‚¿?Aï¿½ï¿½ï¿½ÊŽï¿½ï¿½ï¿½ï¿½ï¿½ï¿½É‚È‚é‚±ï¿½Æ‚ï¿½ï¿½È‚ï¿½?A
     * coef[2], coef[1] ï¿½ï¿½?ï¿½Î’lï¿½ï¿½ï¿½Æ‚ï¿½ï¿½
     * 0 ï¿½É‚È‚ï¿½È‚ï¿½ï¿½ï¿½Ì‚Æ‚ï¿½ï¿½Ä•ï¿½ï¿½ï¿½ï¿½?B
     * ï¿½ï¿½Bï¿½?Aï¿½ï¿½ï¿½?ï¿½ï¿½Í•Kï¿½ï¿½ 1 ï¿½È‚ï¿½ï¿½ï¿½ 2 ï¿½Æ‚È‚ï¿½?B
     * </p>
     *
     * @return ï¿½ï¿½?ï¿½?ï¿½ï¿½Ì”zï¿½ï¿½
     * @see #getRootsIfQuadric()
     */
    public static double[] getAlwaysRootsIfQuadric(DoublePolynomial func) {
        return getRootsIfQuadric(func, true);
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ Newton-Raphson ï¿½@ï¿½É‚ï¿½BÄˆï¿½Â‚ï¿½ï¿½ï¿½ï¿½?ï¿½ß‚ï¿½?B
     *
     * @param initialGuess ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l
     * @return ?ï¿½ï¿½ÌŽï¿½l
     * @throws NRNotConvergeException ï¿½ï¿½ï¿½Zï¿½ï¿½ï¿½ï¿½ï¿½È‚ï¿½ï¿½Bï¿½
     */
    public static double getOneRootByNR(DoublePolynomial func, double initialGuess)
            throws Exception {
        double root = initialGuess;
        double epsilon = MachineEpsilon.DOUBLE;
        int maxIteration = 50;
        double value;        /* value of polynomial */
        double absVal;        /* absolute of value */
        double deriv;        /* 1st derivative of polynomial */
        double tempVal;        /* temporary value */
        double absTempVal;    /* absolute of temporary value */
        double coef;        /* a coefficient */
        double delta;        /* delta at root */

        /*
        * iteractive refinement by Newton-Raphson method
        */
        for (int iteration = 0; iteration < maxIteration; iteration++) {
            value = func.getCoefficientAsDouble(func.degree());
            deriv = value;
            delta = 0.0;

            for (int j = func.degree() - 1; j >= 0; j--) {
                tempVal = value * root;
                coef = func.getCoefficientAsDouble(j);
                value = tempVal + coef;
                absTempVal = Math.abs(tempVal);
                if (j > 0)
                    deriv = (deriv * root) + value;
                delta = Math.abs(root) * delta +
                        epsilon * (absTempVal + MathUtils.maxOf3(Math.abs(coef),
                                absTempVal,
                                Math.abs(value)));
            }

            absVal = Math.abs(value);
            if (((absVal < epsilon) && (delta < epsilon)) || (absVal < delta)) {
                return root;
            }

            if (Math.abs(deriv) > epsilon) {
                root = root - value / deriv;
            } else {
                root = root - value / GeometryUtils.copySign(epsilon, deriv);
            }
        }

        throw new Exception();
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ÌŽwï¿½ï¿½Ì”ÍˆÍ‚ÌŽï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½Ì’lï¿½ï¿½Ô‚ï¿½?B
     * <p/>
     * lower ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ upper ï¿½ï¿½ï¿½Ü‚Å‚ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½ï¿½Ô‚ï¿½?B
     * </p>
     *
     * @param lower ï¿½ï¿½?ï¿½ï¿½Ì‰ï¿½ï¿½ï¿½
     * @param upper ï¿½ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½
     * @return ï¿½wï¿½ï¿½ÌŽï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½Ì”zï¿½ï¿½
     */
    public static Complex[] coefficientsBetween(ComplexPolynomial func, int lower,
                                                int upper) {
        int n = upper - lower + 1;
        Complex[] result = new Complex[n];
        for (int i = 0; i < n; i++)
            result[i] = func.getCoefficientAsComplex(lower + i);
        return result;
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ñŽŸ•ï¿½ï¿½ (A0 + A1 * t + A2 * t^2 = 0) ï¿½ï¿½ï¿½?B
     * <p/>
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ 2 ï¿½ï¿½ï¿½Å‚È‚ï¿½ï¿½ï¿½ï¿½ null ï¿½ï¿½Ô‚ï¿½?B
     * </p>
     * <p/>
     * ï¿½ï¿½ï¿½Ê‚Æ‚ï¿½ï¿½Ä“ï¿½ï¿½ï¿½ï¿½ï¿½zï¿½ï¿½ï¿½?Aï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½Ü‚ï¿½?B
     * ï¿½ï¿½Bï¿½?Aï¿½ï¿½ï¿½Ì”zï¿½ï¿½Ì’ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ 2 ï¿½Å‚ï¿½ï¿½ï¿½?B
     * </p>
     * <p/>
     * coef[2], coef[1] ï¿½ï¿½?ï¿½Î’lï¿½ï¿½ï¿½Æ‚ï¿½ï¿½ MachineEpsilon.DOUBLE ï¿½È‰ï¿½ï¿½Å‚ï¿½ï¿½ï¿½?ï¿½?ï¿½ï¿½É‚ï¿½?A
     * ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Æ‚ï¿½ï¿½ï¿½ (Double.NaN, 0.0) ï¿½ï¿½Ô‚ï¿½?B
     * </p>
     *
     * @return ?ï¿½ï¿½Ì”zï¿½ï¿½
     * @see MachineEpsilon#DOUBLE
     */
    public static Complex[] getRootsIfQuadric(ComplexPolynomial func) {
        if (func.degree() != 2)
            return null;

        Complex[] roots = new Complex[2];

        if (func.getCoefficientAsComplex(2).abs() > MachineEpsilon.DOUBLE) {
            //          discriminant = sqrt(coef[1] * coef[1] - 4.0 * coef[2] * coef[0])
            Complex discriminant = func.getCoefficientAsComplex(1).multiply(func.getCoefficientAsComplex(1)).subtract(func.getCoefficientAsComplex(2).multiply(func.getCoefficientAsComplex(0)).multiply(4.0)).sqrt();
            Complex minusCoef1 = (Complex) func.getCoefficientAsComplex(1).negate();
            boolean secondByAdding;

            if (func.getCoefficientAsComplex(1).real() > 0.0) {
                roots[0] = minusCoef1.subtract(discriminant);
                secondByAdding = true;
            } else {
                roots[0] = minusCoef1.add(discriminant);
                secondByAdding = false;
            }
            roots[0] = roots[0].divide(func.getCoefficientAsComplex(2).multiply(2.0));

            if (roots[0].abs() > MachineEpsilon.DOUBLE) {
                roots[1] = func.getCoefficientAsComplex(0).divide(func.getCoefficientAsComplex(2)).divide(roots[0]);
            } else {
                if (secondByAdding == true) {
                    roots[1] = minusCoef1.add(discriminant);
                } else {
                    roots[1] = minusCoef1.subtract(discriminant);
                }
                roots[1] = roots[1].divide(func.getCoefficientAsComplex(2).multiply(2.0));
            }
        } else if (func.getCoefficientAsComplex(1).abs() > MachineEpsilon.DOUBLE) {
            roots[0] = ((Complex) func.getCoefficientAsComplex(0).negate()).divide(func.getCoefficientAsComplex(1));
            roots[1] = new Complex(roots[0].real(), roots[0].imag());
        } else {
            roots[0] = new Complex(Double.NaN, 0.0);
            roots[1] = new Complex(roots[0].real(), roots[0].imag());
        }

        return roots;
    }

    /**
     * 2 ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ÌŠe?ï¿½ï¿½ÌŒW?ï¿½ï¿½lï¿½ï¿½^ï¿½ï¿½ï¿½ÄƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
     * <p/>
     * ï¿½ï¿½ï¿½ÌƒRï¿½ï¿½ï¿½Xï¿½gï¿½ï¿½ï¿½Nï¿½^ï¿½ï¿½?ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½é‘½?ï¿½ï¿½ï¿½ï¿½ÌŽï¿½?ï¿½ï¿½Í•Kï¿½ï¿½ 2 ï¿½ï¿½ï¿½Å‚ï¿½ï¿½ï¿½?B
     * </p>
     *
     * @param coef0 0 ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½l
     * @param coef1 1 ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½l
     * @param coef2 2 ï¿½ï¿½?ï¿½ï¿½ÌŒW?ï¿½ï¿½l
     */
    public static ComplexPolynomial getComplexPolynomial(Complex coef0,
                                                         Complex coef1,
                                                         Complex coef2) {
        Complex[] coef;
        coef = new Complex[3];
        coef[0] = coef0;
        coef[1] = coef1;
        coef[2] = coef2;
        return new ComplexPolynomial(coef);
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ Newton-Raphson ï¿½@ï¿½É‚ï¿½BÄˆï¿½Â‚ï¿½ï¿½ï¿½ï¿½?ï¿½ß‚ï¿½?B
     *
     * @param initialGuess ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l
     * @return ?ï¿½ï¿½ÌŽï¿½l
     * @throws NRNotConvergeException ï¿½ï¿½ï¿½Zï¿½ï¿½ï¿½ï¿½ï¿½È‚ï¿½ï¿½Bï¿½
     */
    public static Complex getOneRootByNR(ComplexPolynomial func, Complex initialGuess)
            throws NRNotConvergeException {
        Complex root = initialGuess;
        double epsilon = MachineEpsilon.DOUBLE;
        int maxIteration = 50;
        Complex value;    /* value of polynomial */
        double absVal;        /* absolute of value */
        Complex deriv;    /* 1st derivative of polynomial */
        Complex tempVal;    /* temporary area */
        double absTempVal;    /* absolute of temporary value */
        Complex theCoef;    /* a coefficient */
        double delta;        /* delta at root */

        /*
        * iteractive refinement by Newton-Raphson method
        */
        for (int iteration = 0; iteration < maxIteration; iteration++) {
            value = func.getCoefficientAsComplex(func.degree());
            deriv = new Complex(value.real(), value.imag());
            delta = 0.0;

            for (int j = func.degree() - 1; j >= 0; j--) {
                tempVal = value.multiply(root);
                theCoef = func.getCoefficientAsComplex(j);
                value = tempVal.add(theCoef);
                absTempVal = tempVal.abs();
                if (j > 0)
                    deriv = deriv.multiply(root).add(value);
                delta = root.abs() * delta +
                        epsilon * (absTempVal + MathUtils.maxOf3(theCoef.abs(),
                                absTempVal,
                                value.abs()));
            }

            absVal = value.abs();
            if (((absVal < epsilon) && (delta < epsilon)) || (absVal < delta)) {
                return root;
            }

            if (deriv.abs() > epsilon) {
                root = root.subtract(value.divide(deriv));
            } else {
                root = root.subtract(value.divide(deriv.getEpsilon()));
            }
        }

        throw new NRNotConvergeException(root);
    }

    /**
     * ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ Newton-Raphson ï¿½@ï¿½É‚ï¿½BÄ‹?ï¿½ß‚æ‚¤ï¿½Æ‚ï¿½ï¿½ï¿½?Û‚ï¿½?A
     * ï¿½ï¿½ï¿½ÌŽï¿½ï¿½Zï¿½ÉŽï¿½ï¿½sï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Æ‚î¦‚ï¿½ï¿½ï¿½Oï¿½ï¿½ (ï¿½Õ”) ï¿½Nï¿½ï¿½ï¿½X?B
     */
    public static class NRNotConvergeException extends Exception {
        /**
         * ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l?B
         *
         * @serial
         */
        private Complex value;

        /**
         * ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½lï¿½ï¿½^ï¿½ï¿½ï¿½ÄƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
         *
         * @param value ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l
         */
        private NRNotConvergeException(Complex value) {
            super();
            this.value = value;
        }

        /**
         * ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½lï¿½ï¿½Ô‚ï¿½?B
         *
         * @return ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l
         */
        public Complex getValue() {
            return this.value;
        }
    }

    /**
     * ï¿½ï¿½ï¿½é‘½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½sï¿½ï¿½Å‚ï¿½ï¿½é‚±ï¿½Æ‚î¦‚ï¿½ï¿½ï¿½Oï¿½ï¿½ (ï¿½Õ”) ï¿½Nï¿½ï¿½ï¿½X?B
     * <p/>
     * ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ÌŽï¿½?ï¿½ï¿½ï¿½ 0 ï¿½ï¿½?A0 ï¿½ï¿½ï¿½ÌŒW?ï¿½ï¿½ï¿½ (0, 0) ï¿½Å‚ï¿½ï¿½é‚±ï¿½Æ‚î¦‚ï¿½?B
     * </p>
     */
    public static class IndefiniteEquationException extends Exception {
        /**
         * ?Ö¾ï¿½ï¿½^ï¿½ï¿½ï¿½ï¿½ï¿½ÉƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
         */
        public IndefiniteEquationException() {
            super();
        }

        /**
         * ?Ö¾ï¿½ï¿½^ï¿½ï¿½ï¿½ÄƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
         *
         * @param s ?Ö¾
         */
        public IndefiniteEquationException(String s) {
            super(s);
        }
    }

    /**
     * ï¿½ï¿½ï¿½é‘½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½sï¿½\ï¿½Å‚ï¿½ï¿½é‚±ï¿½Æ‚î¦‚ï¿½ï¿½ï¿½Oï¿½ï¿½ (ï¿½Õ”) ï¿½Nï¿½ï¿½ï¿½X?B
     * <p/>
     * ï¿½ï¿½?ï¿½ï¿½ï¿½ï¿½ÌŽï¿½?ï¿½ï¿½ï¿½ 0 ï¿½ï¿½?A0 ï¿½ï¿½ï¿½ÌŒW?ï¿½ï¿½ï¿½ (0, 0) ï¿½ÈŠOï¿½Å‚ï¿½ï¿½é‚±ï¿½Æ‚î¦‚ï¿½?B
     * </p>
     */
    public static class ImpossibleEquationException extends Exception {
        /**
         * ?Ö¾ï¿½ï¿½^ï¿½ï¿½ï¿½ï¿½ï¿½ÉƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
         */
        public ImpossibleEquationException() {
            super();
        }

        /**
         * ?Ö¾ï¿½ï¿½^ï¿½ï¿½ï¿½ÄƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
         *
         * @param s ?Ö¾
         */
        public ImpossibleEquationException(String s) {
            super(s);
        }
    }

    /**
     * ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½ Durand-Kerner ï¿½@ï¿½É‚ï¿½BÄ‹?ï¿½ß‚æ‚¤ï¿½Æ‚ï¿½ï¿½ï¿½?Û‚ï¿½?A
     * ï¿½ï¿½ï¿½ÌŽï¿½ï¿½Zï¿½ÉŽï¿½ï¿½sï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Æ‚î¦‚ï¿½ï¿½ï¿½Oï¿½ï¿½ (ï¿½Õ”) ï¿½Nï¿½ï¿½ï¿½X?B
     */
    public static class DKANotConvergeException extends Exception {
        /**
         * ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l?B
         *
         * @serial
         */
        private Complex[] values;

        /**
         * ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½lï¿½ï¿½^ï¿½ï¿½ï¿½ÄƒIï¿½uï¿½Wï¿½Fï¿½Nï¿½gï¿½ï¿½?\ï¿½zï¿½ï¿½ï¿½ï¿½?B
         *
         * @param values ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l
         */
        private DKANotConvergeException(Complex[] values) {
            super();
            this.values = values;
        }

        /**
         * ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½lï¿½ï¿½Ô‚ï¿½?B
         *
         * @return ?ï¿½ï¿½?ï¿½ï¿½Å‚ï¿½?Ø‚Bï¿½?Û‚ï¿½?ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½l
         */
        public Complex[] getValues() {
            return this.values;
        }
    }

    /**
     * ï¿½ï¿½ï¿½Ì‘ï¿½?ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ó‚Æ‚ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½Ì‚ï¿½ï¿½×‚Ä‚ï¿½ Durand-Kerner ï¿½@ï¿½É‚ï¿½BÄ‹?ï¿½ß‚ï¿½?B
     *
     * @return ?ï¿½ï¿½Ì”zï¿½ï¿½
     * @throws DKANotConvergeException     ï¿½ï¿½ï¿½Zï¿½ï¿½ï¿½ï¿½ï¿½È‚ï¿½ï¿½Bï¿½
     * @throws IndefiniteEquationException ï¿½ï¿½ï¿½ï¿½ï¿½sï¿½ï¿½Å‚ï¿½ï¿½ï¿½
     * @throws ImpossibleEquationException ï¿½ï¿½ï¿½ï¿½ï¿½sï¿½\ï¿½Å‚ï¿½ï¿½ï¿½
     */
    public static Complex[] getRootsByDKA(ComplexPolynomial func)
            throws DKANotConvergeException, IndefiniteEquationException, ImpossibleEquationException {
        Complex[] result;

        //if (this.normalized != true) {
        //  return this.normalize().getRootsByDKA();
        //}

        int degree = func.degree();

        if (degree == 0) {
            if (func.getCoefficientAsComplex(degree).abs() < MachineEpsilon.DOUBLE) {
                throw new IndefiniteEquationException();
            } else {
                throw new ImpossibleEquationException();
            }
        }

        //Complex[] result = new Complex[degree];
        result = new Complex[degree];
        double radiusEps = 1.0e-8;

        int i;
        for (i = 0; i <= degree; i++) {
            if (func.getCoefficientAsComplex(i).abs() > MachineEpsilon.DOUBLE)
                break;
        }
        int nonZeroCoef = i;

        if (nonZeroCoef > degree) {
            throw new IndefiniteEquationException();
        }

        if (nonZeroCoef > 0) {
            int j, k;

            for (j = nonZeroCoef; j > 0; j--)
                result[degree - j] = new Complex(0.0, 0.0);

            int revDegree = degree - nonZeroCoef;
            if (revDegree == 0)
                return result;

            Complex[] coefs = new Complex[revDegree + 1];
            for (j = revDegree, k = degree; k >= nonZeroCoef; j--, k--)
                coefs[j] = func.getCoefficientAsComplex(k);
            func = new ComplexPolynomial(coefs);//new ComplexPolynomial(coefs, true);
            degree = func.degree();
        }

        /*  */
        if (degree == 1) {
            result[0] = ((Complex) func.getCoefficientAsComplex(degree - 1).negate()).divide(func.getCoefficientAsComplex(degree));
            return result;
        } else if (degree == 2) {
            Complex[] moreResult = getRootsIfQuadric(func);
            result[0] = moreResult[0];
            result[1] = moreResult[1];
            return result;
        }

        if (getAbrth(func, result) < radiusEps)
            return result;

        if (compDK3(func, result) != true)
            throw new DKANotConvergeException(result);

        return result;
    }

    /**
     * Durand-Kerner ï¿½@ï¿½ï¿½?ï¿½ï¿½ï¿½lï¿½ð“¾‚ï¿½?B
     *
     * @param result ?ï¿½ï¿½ï¿½lï¿½ï¿½iï¿½[ï¿½ï¿½ï¿½ï¿½zï¿½ï¿½ (?oï¿½Í—p)
     * @return ?ï¿½ï¿½ï¿½lï¿½Ì”ï¿½ï¿½a
     * @see #getRootsByDKA()
     */
    private static double getAbrth(ComplexPolynomial func, Complex[] result) {
        int degree = func.degree();
        Complex[] dcA = new Complex[degree + 1];
        for (int i = 0; i <= degree; i++)
            dcA[i] = func.getCoefficientAsComplex(degree - i);

        Complex[] ecC;    // coefficients after translation by beta
        double[] eCA;        // absolute of ecC

        Complex ecbeta;    // center of roots
        int jm;            // number of coefficients that are not zero
        double erng;        // initial range of roots
        double erng1 = 0.0;    // refinemented of erng
        double eCM;        // jm / eCA0
        double erv;        // each of value that determines erng
        int iter;        // counter for iteration
        int maxIteration = 50;
        double ereps;        // epsilon value for iteration
        double eQ0;        // value of C polynomial at erng
        double eQ1;        // 1st differential at eQ0
        double ep2n;        // 2-pai / order
        double eh3n;        // 3 / 2 / order
        int ijk;
        int klm;
        int mno;
        Complex ecci = new Complex(0.0, 1.0);    // unit imaginary

        ecC = new Complex[degree + 1];
        eCA = new double[degree + 1];

        /*
        * guess range of root
        */
        ecbeta = ((Complex) dcA[1].negate()).divide(dcA[0].multiply(degree));
        // ecbeta = -dcA[1] / (degree * dcA[0]);

        for (ijk = 0; ijk <= degree; ijk++)
            ecC[ijk] = new Complex(dcA[ijk].real(), dcA[ijk].imag());

        for (klm = 0; klm < degree; klm++) {
            mno = degree - klm;
            for (ijk = 1; ijk <= mno; ijk++) {
                ecC[ijk] = ecC[ijk].add(ecbeta.multiply(ecC[ijk - 1]));
                // ecC[ijk] += ecbeta * ecC[ijk-1];
            }
        }

        for (ijk = 0; ijk <= degree; ijk++)
            eCA[ijk] = ecC[ijk].abs();

        jm = degree;
        for (ijk = 1; ijk <= degree; ijk++)
            if (eCA[ijk] == 0.0)
                jm--;

        erng = 0.0;
        eCM = jm / eCA[0];
        for (ijk = 1; ijk <= degree; ijk++)
            if ((eCA[ijk] != 0.0) && ((erv = Math.exp(Math.log(eCA[ijk] * eCM) / ijk)) > erng))
                erng = erv;

        /*
        * iteractive refinement of range of roots by Newton-Raphson's method
        */
        ereps = erng * 0.01;

        for (iter = 0; iter < maxIteration; iter++) {
            eQ1 = eQ0 = eCA[0];
            for (klm = 1; klm < degree; klm++) {
                eQ0 = erng * eQ0 - eCA[klm];
                eQ1 = erng * eQ1 + eQ0;
            }
            eQ0 = erng * eQ0 - eCA[degree];

            if (Math.abs(eQ1) > MachineEpsilon.DOUBLE)
                erng1 = erng - eQ0 / eQ1;
            else
                erng1 = erng - eQ0 /
                        GeometryUtils.copySign(MachineEpsilon.DOUBLE, eQ1);

            if ((erng - erng1) <= ereps)
                break;

            erng = erng1;
        }

        /*
        * set initial approximation of roots
        */
        ep2n = (2.0 * Math.PI) / degree;
        eh3n = 3.0 / (2 * degree);
        for (ijk = 0; ijk < degree; ijk++) {
            result[ijk] = ecbeta.add(Complex.exp(ecci.multiply(ep2n * ijk + eh3n)).multiply(erng1));
            // result[ijk] = ecbeta + erng1 * exp(ecci*(ep2n*ijk+eh3n))
        }

        return erng1;
    }

    /**
     * do 3rd-order Durand-Kerner method.
     *
     * @param result ?ï¿½ï¿½Ì”zï¿½ï¿½ (ï¿½Ä‚ï¿½?oï¿½ï¿½ï¿½ï¿½ï¿½ï¿½?ï¿½ï¿½ï¿½lï¿½ï¿½?Ý’è‚³ï¿½ï¿½ï¿½Kï¿½vï¿½ï¿½ï¿½ï¿½ï¿½ï¿½)
     * @return ï¿½ï¿½ï¿½Zï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Û‚ï¿½
     * @see #getRootsByDKA()
     */
    private static boolean compDK3(ComplexPolynomial func, Complex[] result) {
        int degree = func.degree();
        Complex[] dcA = new Complex[degree + 1];
        for (int i = 0; i <= degree; i++)
            dcA[i] = func.getCoefficientAsComplex(degree - i);

        double[] eCA;        // absolute of coefficients
        Complex[] edcA;    // normalized coefficients
        Complex[] ecZN;    // work area

        int imax;        // index for coef. which absolute-value is max
        double emax;        // work area for imax
        int iter;        // counter for iteration
        int maxIteration = 50;
        int inconv;        // number of converged root
        double eZA;        // absolute of root
        Complex ecP0;    // value of polynomial at root
        Complex ecP1;    // 1st differential at ecP0
        double edelta;        // delta value for convergence
        Complex ecPT;    // work area
        Complex ecQA;    // work area
        Complex ecQB;    // work area
        int ijk;
        int klm;
        Complex ec1 = new Complex(1.0, 0.0);
        double ewrk;
        double secondEps = 1.0e-8;

        eCA = new double[degree + 1];
        edcA = new Complex[degree + 1];
        ecZN = new Complex[degree];

        /*
        * normalize the coefficients
        */
        imax = 0;
        emax = dcA[0].abs();
        for (ijk = 1; ijk <= degree; ijk++) {
            if ((ewrk = dcA[ijk].abs()) > emax) {
                imax = ijk;
                emax = ewrk;
            }
        }

        for (ijk = 0; ijk <= degree; ijk++) {
            if (ijk != imax) {
                edcA[ijk] = dcA[ijk].divide(dcA[imax]);
            } else {
                edcA[ijk] = new Complex(1.0, 0.0);
            }
            eCA[ijk] = edcA[ijk].abs();
        }

        /*
        * iteractive refinement by Durand-Kerner's method
        */
        for (iter = 0; iter < maxIteration; iter++) {
            inconv = 0;

            for (klm = 0; klm < degree; klm++) {
                ecP0 = new Complex(edcA[0].real(), edcA[0].imag());
                ecP1 = new Complex(ecP0.real(), ecP0.imag());
                edelta = 0.0;
                eZA = result[klm].abs();

                for (ijk = 1; ijk <= degree; ijk++) {
                    ecPT = result[klm].multiply(ecP0);
                    ecP0 = ecPT.add(edcA[ijk]);
                    edelta = eZA * edelta +
                            MachineEpsilon.DOUBLE *
                                    (ecPT.abs() + MathUtils.maxOf3(eCA[ijk], ecPT.abs(), ecP0.abs()));
                    if (ijk != degree)
                        ecP1 = result[klm].multiply(ecP1).add(ecP0);
                }

                ecQA = new Complex(0.0, 0.0);
                for (ijk = 0; ijk < degree; ijk++)
                    if (ijk != klm)
                        ecQA = ecQA.add(ec1.divide(result[klm].subtract(result[ijk])));

                ecQB = ecP0.divide(ecP1);
                ecZN[klm] = result[klm].subtract(ecQB.divide(ec1.subtract(ecQB.multiply(ecQA))));

                /*
                * convergence test
                */
                if (ecP0.abs() < edelta)
                    inconv++;
            }

            /*
            * second convergence test (this is looser than above one)
            */
            if (iter > 5) {
                double ec2CT = 0.0;
                for (ijk = 0; ijk < degree; ijk++)
                    ec2CT += ecZN[ijk].subtract(result[ijk]).abs();
                if (ec2CT < secondEps)
                    inconv = degree;
            }

            for (ijk = 0; ijk < degree; ijk++)
                result[ijk] = ecZN[ijk];

            /*
            * return if all is converged
            */
            if (inconv == degree)
                break;
        }

        return (iter == maxIteration) ? false : true;
    }

    /**
     * ***********************************************************************
     * <p/>
     * Debug
     * <p/>
     * ************************************************************************
     */
    /* Debug : getRootsIfQuadric */
    private static void debugGetRootsIfQuadric(String argv[]) {
        Complex[] coef = new Complex[argv.length - 2];
        for (int i = 0; i < argv.length - 2; i++)
            coef[i] = new Complex(Double.valueOf(argv[i]).doubleValue(), 0.0);

        try {
            ComplexPolynomial poly = new ComplexPolynomial(coef);
            Complex[] result = new GeometryPrivateUtils().getRootsIfQuadric(poly);
            for (int i = 0; i < result.length; i++)
                System.out.println("result : " + result[i] +
                        ", evaluate : " + poly.map(result[i]));
        } catch (InvalidArgumentValueException e) {
            System.err.println(e);
        }
    }

    /* Debug : getOneRootByNR */
    private static void debugGetOneRootByNR(String argv[]) {
        Complex[] coef = new Complex[argv.length - 2];
        for (int i = 0; i < argv.length - 2; i++)
            coef[i] = new Complex(Double.valueOf(argv[i]).doubleValue(), 0.0);

        try {
            ComplexPolynomial poly = new ComplexPolynomial(coef);
            Complex result =
                    new GeometryPrivateUtils().getOneRootByNR(poly, new Complex(Double.valueOf(argv[argv.length - 2]).doubleValue(),
                            Double.valueOf(argv[argv.length - 1]).doubleValue()));
            System.out.println("result : " + result + ", evaluate : " + poly.map(result));
        } catch (InvalidArgumentValueException e) {
            System.err.println(e);
        } catch (NRNotConvergeException e) {
            System.err.println(e);
        }
    }

    /* Debug : getRootsByDKA */
    private static void debugGetRootsByDKA(String argv[]) {
        Complex[] coef = new Complex[argv.length - 2];
        for (int i = 0; i < argv.length - 2; i++)
            coef[i] = new Complex(Double.valueOf(argv[i]).doubleValue(), 0.0);

        try {
            ComplexPolynomial poly = new ComplexPolynomial(coef);
            Complex[] result = new GeometryPrivateUtils().getRootsByDKA(poly);
            for (int i = 0; i < result.length; i++)
                System.out.println("result : " + result[i] +
                        ", evaluate : " + poly.map(result[i]));
        } catch (InvalidArgumentValueException e) {
            System.err.println(e);
        } catch (DKANotConvergeException e) {
            System.err.println(e);
        } catch (IndefiniteEquationException e) {
            System.err.println(e);
        } catch (ImpossibleEquationException e) {
            System.err.println(e);
        }
    }


}
