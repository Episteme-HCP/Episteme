package org.jscience.chemistry.biochemistry;

/**
 * Enzyme kinetics calculations (Michaelis-Menten).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class EnzymeKinetics {

    /**
     * Michaelis-Menten equation.
     * v = (Vmax · [S]) / (Km + [S])
     * 
     * @param S    Substrate concentration
     * @param Vmax Maximum velocity
     * @param Km   Michaelis constant
     * @return Reaction velocity
     */
    public static double michaelismenten(double S, double Vmax, double Km) {
        return (Vmax * S) / (Km + S);
    }

    /**
     * Lineweaver-Burk (double reciprocal) transformation.
     * 1/v = (Km/Vmax) · (1/[S]) + 1/Vmax
     * Returns [slope, intercept].
     */
    public static double[] lineweaverBurkParams(double Vmax, double Km) {
        double slope = Km / Vmax;
        double intercept = 1.0 / Vmax;
        return new double[] { slope, intercept };
    }

    /**
     * Calculates Vmax and Km from Lineweaver-Burk plot slope and intercept.
     */
    public static double[] vMaxKmFromLB(double slope, double intercept) {
        double Vmax = 1.0 / intercept;
        double Km = slope * Vmax;
        return new double[] { Vmax, Km };
    }

    /**
     * Competitive inhibition: apparent Km increased.
     * Km_app = Km · (1 + [I]/Ki)
     */
    public static double competitiveKm(double Km, double I, double Ki) {
        return Km * (1 + I / Ki);
    }

    /**
     * Non-competitive inhibition: Vmax decreased.
     * Vmax_app = Vmax / (1 + [I]/Ki)
     */
    public static double noncompetitiveVmax(double Vmax, double I, double Ki) {
        return Vmax / (1 + I / Ki);
    }

    /**
     * Uncompetitive inhibition: both Km and Vmax decreased.
     */
    public static double[] uncompetitiveParams(double Vmax, double Km, double I, double Ki) {
        double factor = 1 + I / Ki;
        return new double[] { Vmax / factor, Km / factor };
    }

    /**
     * Turnover number (kcat): reactions per enzyme per second.
     * kcat = Vmax / [E]_total
     */
    public static double turnoverNumber(double Vmax, double enzymeConcentration) {
        return Vmax / enzymeConcentration;
    }

    /**
     * Catalytic efficiency.
     * η = kcat / Km
     * Higher is better (diffusion limit ~10⁸-10⁹ M⁻¹s⁻¹)
     */
    public static double catalyticEfficiency(double kcat, double Km) {
        return kcat / Km;
    }

    /**
     * Hill equation for cooperative binding.
     * θ = [S]^n / (K + [S]^n)
     * 
     * @param S Substrate concentration
     * @param K Apparent dissociation constant
     * @param n Hill coefficient (n>1: positive cooperativity)
     * @return Fractional saturation
     */
    public static double hillEquation(double S, double K, double n) {
        double Sn = Math.pow(S, n);
        return Sn / (K + Sn);
    }
}
