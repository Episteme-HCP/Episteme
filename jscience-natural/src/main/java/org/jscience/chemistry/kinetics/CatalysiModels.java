package org.jscience.chemistry.kinetics;

/**
 * Models for catalysis kinetics.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class CatalysiModels {

    /**
     * Michaelis-Menten Kinetics.
     * v = (Vmax * [S]) / (Km + [S])
     * 
     * @param vmax Maximum rate
     * @param km   Michaelis constant
     * @param s    Substrate concentration
     * @return Reaction rate v
     */
    public static double michaelisMenten(double vmax, double km, double s) {
        return (vmax * s) / (km + s);
    }

    /**
     * Lineweaver-Burk plot transformation.
     * 1/v = (Km/Vmax) * (1/[S]) + 1/Vmax
     * 
     * @return Slope (Km/Vmax)
     */
    public static double lineweaverBurkSlope(double vmax, double km) {
        return km / vmax;
    }

    /**
     * Langmuir Adsorption Isotherm (Surface Catalysis).
     * theta = (K * P) / (1 + K * P)
     * 
     * @param K Adsorption equilibrium constant
     * @param P Partial pressure of gas
     * @return Fractional coverage theta
     */
    public static double langmuirAdsorption(double K, double P) {
        return (K * P) / (1.0 + K * P);
    }
}
