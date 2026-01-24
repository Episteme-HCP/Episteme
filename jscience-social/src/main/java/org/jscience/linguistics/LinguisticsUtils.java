package org.jscience.linguistics;

/**
 * Utility class providing mathematical models used in linguistics,
 * such as Zipf's law for word frequency distribution.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class LinguisticsUtils {

    private LinguisticsUtils() {}

    /**
     * Calculates Zipf's law probability.
     * P(k) = 1 / (k^s * H(n, s)) where H is the generalized harmonic number.
     *
     * @param k rank of the word
     * @param s value of the exponent characterizing the distribution
     * @param n total number of elements
     * @return the probability of the k-th element
     * @see <a href="http://en.wikipedia.org/wiki/Zipf%27s_law">Zipf's Law</a>
     */
    public static double calculateZipf(double k, double s, double n) {
        return 1.0 / (Math.pow(k, s) * harmonicNumber(n, s));
    }

    /**
     * Calculates the generalized harmonic number.
     * H(n, s) = sum_{i=1}^n (1 / i^s)
     */
    private static double harmonicNumber(double n, double s) {
        double result = 0;
        for (int i = 1; i <= n; i++) {
            result += 1.0 / Math.pow(i, s);
        }
        return result;
    }
}
