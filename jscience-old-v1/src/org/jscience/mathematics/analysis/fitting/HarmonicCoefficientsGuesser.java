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

package org.jscience.mathematics.analysis.fitting;

import org.jscience.mathematics.analysis.ExhaustedSampleException;
import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.SampledMappingIterator;
import org.jscience.mathematics.analysis.ValuedPair;
import org.jscience.mathematics.analysis.estimation.EstimationException;
import org.jscience.mathematics.analysis.quadrature.EnhancedSimpsonIntegratorSampler;

import java.io.Serializable;

/**
 * This class guesses harmonic coefficients from a sample.
 * <p/>
 * <p>The algorithm used to guess the coefficients is as follows:</p>
 * <p/>
 * <p>We know f (t) at some sampling points ti and want to find a,
 * omega and phi such that f (t) = a cos (omega t + phi).
 * </p>
 * <p/>
 * <p>From the analytical expression, we can compute two primitives :
 * <pre>
 *     If2  (t) = int (f^2)  = a^2 * [t + S (t)] / 2
 *     If'2 (t) = int (f'^2) = a^2 * omega^2 * [t - S (t)] / 2
 *     where S (t) = sin (2 * (omega * t + phi)) / (2 * omega)
 * </pre>
 * </p>
 * <p/>
 * <p>We can remove S between these expressions :
 * <pre>
 *     If'2 (t) = a^2 * omega ^ 2 * t - omega ^ 2 * If2 (t)
 * </pre>
 * </p>
 * <p/>
 * <p>The preceding expression shows that If'2 (t) is a linear
 * combination of both t and If2 (t): If'2 (t) = A * t + B * If2 (t)
 * </p>
 * <p/>
 * <p>From the primitive, we can deduce the same form for definite
 * integrals between t1 and ti for each ti :
 * <pre>
 *   If2 (ti) - If2 (t1) = A * (ti - t1) + B * (If2 (ti) - If2 (t1))
 * </pre>
 * </p>
 * <p/>
 * <p>We can find the coefficients A and B that best fit the sample
 * to this linear expression by computing the definite integrals for
 * each sample points.
 * </p>
 * <p/>
 * <p>For a bilinear expression z (xi, yi) = A * xi + B * yi, the
 * coefficients a and b that minimize a least square criterion
 * Sum ((zi - z (xi, yi))^2) are given by these expressions:</p>
 * <pre>
 * <p/>
 *         Sum (yi^2) Sum (xi zi) - Sum (xi yi) Sum (yi zi)
 *     A = ------------------------------------------------
 *         Sum (xi^2) Sum (yi^2)  - Sum (xi yi) Sum (xi yi)
 * <p/>
 *         Sum (xi^2) Sum (yi zi) - Sum (xi yi) Sum (xi zi)
 *     B = ------------------------------------------------
 *         Sum (xi^2) Sum (yi^2)  - Sum (xi yi) Sum (xi yi)
 * </pre>
 * </p>
 * <p/>
 * <p/>
 * <p>In fact, we can assume both a and omega are positive and
 * compute them directly, knowing that A = a^2 * omega^2 and that
 * B = - omega^2. The complete algorithm is therefore:</p>
 * <pre>
 * <p/>
 * for each ti from t1 to t(n-1), compute:
 *   f  (ti)
 *   f' (ti) = (f (t(i+1)) - f(t(i-1))) / (t(i+1) - t(i-1))
 *   xi = ti - t1
 *   yi = int (f^2) from t1 to ti
 *   zi = int (f'^2) from t1 to ti
 *   update the sums Sum (xi^2), Sum (yi^2),
 *                   Sum (xi yi), Sum (xi zi)
 *                   and Sum (yi zi)
 * end for
 * <p/>
 *            |-------------------------------------------------
 *         \  | Sum (yi^2) Sum (xi zi) - Sum (xi yi) Sum (yi zi)
 * a     =  \ | ------------------------------------------------
 *           \| Sum (xi yi) Sum (xi zi) - Sum (xi^2) Sum (yi zi)
 * <p/>
 * <p/>
 *            |-------------------------------------------------
 *         \  | Sum (xi yi) Sum (xi zi) - Sum (xi^2) Sum (yi zi)
 * omega =  \ | ------------------------------------------------
 *           \| Sum (xi^2) Sum (yi^2)  - Sum (xi yi) Sum (xi yi)
 * <p/>
 * </pre>
 * </p>
 * <p/>
 * <p>Once we know omega, we can compute:
 * <pre>
 *    fc = omega * f (t) * cos (omega * t) - f' (t) * sin (omega * t)
 *    fs = omega * f (t) * sin (omega * t) + f' (t) * cos (omega * t)
 * </pre>
 * </p>
 * <p/>
 * <p>It appears that <code>fc = a * omega * cos (phi)</code> and
 * <code>fs = -a * omega * sin (phi)</code>, so we can use these
 * expressions to compute phi. The best estimate over the sample is
 * given by averaging these expressions.
 * </p>
 * <p/>
 * <p>Since integrals and means are involved in the preceding
 * estimations, these operations run in O(n) time, where n is the
 * number of measurements.</p>
 *
 * @author L. Maisonobe
 * @version $Id: HarmonicCoefficientsGuesser.java,v 1.2 2007-10-21 17:38:16 virtualcall Exp $
 */

public class HarmonicCoefficientsGuesser
        implements Serializable {
    public HarmonicCoefficientsGuesser(AbstractCurveFitter.FitMeasurement[] measurements) {
        this.measurements = measurements;
        a = Double.NaN;
        omega = Double.NaN;
    }

    /**
     * Estimate a first guess of the coefficients.
     *
     * @throws ExhaustedSampleException if the sample is exhausted.
     * @throws MappingException         if the integrator throws one.
     * @throws EstimationException      if the sample is too short or if
     *                                  the first guess cannot be computed (when the elements under the
     *                                  square roots are negative).
     */
    public void guess()
            throws ExhaustedSampleException, MappingException, EstimationException {
        guessAOmega();
        guessPhi();
    }

    /**
     * Estimate a first guess of the a and omega coefficients.
     *
     * @throws ExhaustedSampleException if the sample is exhausted.
     * @throws MappingException         if the integrator throws one.
     * @throws EstimationException      if the sample is too short or if
     *                                  the first guess cannot be computed (when the elements under the
     *                                  square roots are negative).
     */
    private void guessAOmega()
            throws ExhaustedSampleException, MappingException, EstimationException {

        // initialize the sums for the linear model between the two integrals
        double sx2 = 0.0;
        double sy2 = 0.0;
        double sxy = 0.0;
        double sxz = 0.0;
        double syz = 0.0;

        // build the integrals sampler
        F2FP2Iterator iter = new F2FP2Iterator(measurements);
        SampledMappingIterator sampler =
                new EnhancedSimpsonIntegratorSampler(iter);
        ValuedPair p0 = sampler.next();
        double p0X = p0.getX()[0].doubleValue();
        double[] p0Y = getNumbersAsDouble(p0.getY());

        // get the points for the linear model
        int i = 0;
        while (sampler.hasNext()) {

            ValuedPair point = sampler.next();
            double pX = point.getX()[0].doubleValue();
            double[] pY = getNumbersAsDouble(point.getY());

            double dx = pX - p0X;
            double dy0 = pY[0] - p0Y[0];
            double dy1 = pY[1] - p0Y[1];

            sx2 += dx * dx;
            sy2 += dy0 * dy0;
            sxy += dx * dy0;
            sxz += dx * dy1;
            syz += dy0 * dy1;

        }

        // compute the amplitude and pulsation coefficients
        double c1 = sy2 * sxz - sxy * syz;
        double c2 = sxy * sxz - sx2 * syz;
        double c3 = sx2 * sy2 - sxy * sxy;
        if ((c1 / c2 < 0.0) || (c2 / c3 < 0.0)) {
            throw new EstimationException("unable to guess a first estimate");
        }
        a = Math.sqrt(c1 / c2);
        omega = Math.sqrt(c2 / c3);

    }

    /**
     * Estimate a first guess of the phi coefficient.
     *
     * @throws ExhaustedSampleException if the sample is exhausted.
     * @throws MappingException         if the sampler throws one.
     */
    private void guessPhi()
            throws ExhaustedSampleException, MappingException {

        SampledMappingIterator iter = new FFPIterator(measurements);

        // initialize the means
        double fcMean = 0.0;
        double fsMean = 0.0;

        while (iter.hasNext()) {
            ValuedPair point = iter.next();
            double omegaX = omega * point.getX()[0].doubleValue();
            double[] pY = getNumbersAsDouble(point.getY());
            double cosine = Math.cos(omegaX);
            double sine = Math.sin(omegaX);
            fcMean += omega * pY[0] * cosine - pY[1] * sine;
            fsMean += omega * pY[0] * sine + pY[1] * cosine;
        }

        phi = Math.atan2(-fsMean, fcMean);

    }

    public double getOmega() {
        return omega;
    }

    public double getA() {
        return a;
    }

    public double getPhi() {
        return phi;
    }

    private double[] getNumbersAsDouble(Number[] numbers) {
        double[] result;
        result = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            result[i] = numbers[i].doubleValue();
        }
        return result;
    }

    private AbstractCurveFitter.FitMeasurement[] measurements;
    private double a;
    private double omega;
    private double phi;

}
