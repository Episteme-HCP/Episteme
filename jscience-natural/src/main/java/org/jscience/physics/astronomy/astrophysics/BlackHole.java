package org.jscience.physics.astronomy.astrophysics;

/**
 * Black hole physics calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class BlackHole {

    /** Gravitational constant (m³/(kg·s²)) */
    public static final double G = 6.67430e-11;

    /** Speed of light (m/s) */
    public static final double C = 2.998e8;

    /** Solar mass (kg) */
    public static final double M_SUN = 1.989e30;

    /**
     * Schwarzschild radius (event horizon for non-rotating black hole).
     * r_s = 2GM/c²
     * 
     * @param mass Black hole mass (kg)
     * @return Schwarzschild radius (m)
     */
    public static double schwarzschildRadius(double mass) {
        return 2 * G * mass / (C * C);
    }

    /**
     * Schwarzschild radius in solar masses.
     * r_s ≈ 2.95 km per solar mass
     */
    public static double schwarzschildRadiusSolarMass(double solarMasses) {
        return 2953 * solarMasses; // meters
    }

    /**
     * Gravitational time dilation near Schwarzschild black hole.
     * Δt_far = Δt_near / √(1 - r_s/r)
     * 
     * @param r  Distance from center (m)
     * @param rs Schwarzschild radius (m)
     * @return Time dilation factor (>1 means time runs slower)
     */
    public static double timeDilation(double r, double rs) {
        if (r <= rs)
            return Double.POSITIVE_INFINITY;
        return 1.0 / Math.sqrt(1 - rs / r);
    }

    /**
     * Gravitational redshift.
     * z = 1/√(1 - r_s/r) - 1
     */
    public static double gravitationalRedshift(double r, double rs) {
        if (r <= rs)
            return Double.POSITIVE_INFINITY;
        return 1.0 / Math.sqrt(1 - rs / r) - 1;
    }

    /**
     * Innermost stable circular orbit (ISCO) for Schwarzschild.
     * r_ISCO = 3 * r_s = 6GM/c²
     */
    public static double iscoRadius(double mass) {
        return 3 * schwarzschildRadius(mass);
    }

    /**
     * Photon sphere radius (light can orbit).
     * r_photon = 1.5 * r_s
     */
    public static double photonSphereRadius(double mass) {
        return 1.5 * schwarzschildRadius(mass);
    }

    /**
     * Hawking temperature.
     * T = ℏc³ / (8πGMk_B)
     * 
     * @param mass Black hole mass (kg)
     * @return Temperature (Kelvin)
     */
    public static double hawkingTemperature(double mass) {
        double hbar = 1.054571817e-34;
        double kB = 1.380649e-23;
        return hbar * C * C * C / (8 * Math.PI * G * mass * kB);
    }

    /**
     * Bekenstein-Hawking entropy.
     * S = (k_B * c³ * A) / (4 * G * ℏ)
     * where A = 4π r_s²
     */
    public static double entropy(double mass) {
        double hbar = 1.054571817e-34;
        double kB = 1.380649e-23;
        double rs = schwarzschildRadius(mass);
        double A = 4 * Math.PI * rs * rs;
        return kB * C * C * C * A / (4 * G * hbar);
    }

    /**
     * Kerr black hole outer horizon radius.
     * r+ = GM/c² + √((GM/c²)² - a²)
     * where a = J/(Mc) is spin parameter
     * 
     * @param mass Black hole mass (kg)
     * @param J    Angular momentum (kg·m²/s)
     */
    public static double kerrOuterHorizon(double mass, double J) {
        double rs = schwarzschildRadius(mass);
        double a = J / (mass * C); // Spin parameter
        double rg = rs / 2; // GM/c²
        return rg + Math.sqrt(rg * rg - a * a);
    }

    /**
     * Kerr black hole ergosphere radius at equator.
     * r_ergo = GM/c² + √((GM/c²)² - a²cos²θ)
     * At equator (θ=π/2): r_ergo = 2GM/c² = r_s
     */
    public static double kerrErgosphereEquator(double mass) {
        return schwarzschildRadius(mass);
    }

    /**
     * Black hole luminosity from Hawking radiation.
     * L = ℏc⁶ / (15360πG²M²)
     */
    public static double hawkingLuminosity(double mass) {
        double hbar = 1.054571817e-34;
        return hbar * Math.pow(C, 6) / (15360 * Math.PI * G * G * mass * mass);
    }

    /**
     * Black hole evaporation time.
     * t = 5120πG²M³ / (ℏc⁴)
     */
    public static double evaporationTime(double mass) {
        double hbar = 1.054571817e-34;
        return 5120 * Math.PI * G * G * Math.pow(mass, 3) / (hbar * Math.pow(C, 4));
    }
}
