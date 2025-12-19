package org.jscience.biology.biochemistry;

/**
 * Michaelis-Menten enzyme kinetics.
 */
public class EnzymeKinetics {

    private EnzymeKinetics() {
    }

    /**
     * Calculates reaction velocity using Michaelis-Menten equation.
     * V = Vmax * [S] / (Km + [S])
     * 
     * @param vMax                   Maximum velocity
     * @param km                     Michaelis constant
     * @param substrateConcentration [S]
     * @return Reaction velocity
     */
    public static double velocity(double vMax, double km, double substrateConcentration) {
        return vMax * substrateConcentration / (km + substrateConcentration);
    }

    /**
     * Lineweaver-Burk linearization (double reciprocal).
     * 1/V = (Km/Vmax)(1/[S]) + 1/Vmax
     * 
     * @return double[2] = {slope, intercept}
     */
    public static double[] lineweaverBurkParams(double vMax, double km) {
        double slope = km / vMax;
        double intercept = 1.0 / vMax;
        return new double[] { slope, intercept };
    }

    /**
     * Calculates Km from half-maximal velocity.
     * At V = Vmax/2, [S] = Km
     */
    public static double findKm(double[] substrateConcs, double[] velocities, double vMax) {
        double halfVmax = vMax / 2.0;
        // Find [S] where V is closest to Vmax/2
        int bestIdx = 0;
        double bestDiff = Double.MAX_VALUE;
        for (int i = 0; i < velocities.length; i++) {
            double diff = Math.abs(velocities[i] - halfVmax);
            if (diff < bestDiff) {
                bestDiff = diff;
                bestIdx = i;
            }
        }
        return substrateConcs[bestIdx];
    }

    /**
     * Competitive inhibition.
     * V = Vmax * [S] / (Km * (1 + [I]/Ki) + [S])
     */
    public static double velocityWithCompetitiveInhibitor(double vMax, double km,
            double substrateConc, double inhibitorConc, double ki) {
        double apparentKm = km * (1 + inhibitorConc / ki);
        return vMax * substrateConc / (apparentKm + substrateConc);
    }
}
