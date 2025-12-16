package org.jscience.chemistry.crystallography;

/**
 * Unit cell and crystal system representation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class UnitCell {

    /**
     * The seven crystal systems.
     */
    public enum CrystalSystem {
        CUBIC("Cubic", "a=b=c, α=β=γ=90°"),
        TETRAGONAL("Tetragonal", "a=b≠c, α=β=γ=90°"),
        ORTHORHOMBIC("Orthorhombic", "a≠b≠c, α=β=γ=90°"),
        HEXAGONAL("Hexagonal", "a=b≠c, α=β=90°, γ=120°"),
        TRIGONAL("Trigonal", "a=b=c, α=β=γ≠90°"),
        MONOCLINIC("Monoclinic", "a≠b≠c, α=γ=90°, β≠90°"),
        TRICLINIC("Triclinic", "a≠b≠c, α≠β≠γ≠90°");

        private final String constraints;

        CrystalSystem(String name, String constraints) {
            this.constraints = constraints;
        }

        public String getConstraints() {
            return constraints;
        }
    }

    private final double a, b, c; // Lattice parameters (Å)
    private final double alpha, beta, gamma; // Angles (degrees)
    private final CrystalSystem system;

    public UnitCell(double a, double b, double c,
            double alpha, double beta, double gamma) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.system = determineSystem();
    }

    /**
     * Creates a cubic unit cell.
     */
    public static UnitCell cubic(double a) {
        return new UnitCell(a, a, a, 90, 90, 90);
    }

    /**
     * Creates a tetragonal unit cell.
     */
    public static UnitCell tetragonal(double a, double c) {
        return new UnitCell(a, a, c, 90, 90, 90);
    }

    /**
     * Creates an orthorhombic unit cell.
     */
    public static UnitCell orthorhombic(double a, double b, double c) {
        return new UnitCell(a, b, c, 90, 90, 90);
    }

    /**
     * Creates a hexagonal unit cell.
     */
    public static UnitCell hexagonal(double a, double c) {
        return new UnitCell(a, a, c, 90, 90, 120);
    }

    private CrystalSystem determineSystem() {
        boolean aEqB = Math.abs(a - b) < 0.001;
        boolean bEqC = Math.abs(b - c) < 0.001;
        boolean aEqC = Math.abs(a - c) < 0.001;
        boolean alpha90 = Math.abs(alpha - 90) < 0.1;
        boolean beta90 = Math.abs(beta - 90) < 0.1;
        boolean gamma90 = Math.abs(gamma - 90) < 0.1;
        boolean gamma120 = Math.abs(gamma - 120) < 0.1;

        if (aEqB && bEqC && alpha90 && beta90 && gamma90)
            return CrystalSystem.CUBIC;
        if (aEqB && !bEqC && alpha90 && beta90 && gamma90)
            return CrystalSystem.TETRAGONAL;
        if (!aEqB && !bEqC && alpha90 && beta90 && gamma90)
            return CrystalSystem.ORTHORHOMBIC;
        if (aEqB && !bEqC && alpha90 && beta90 && gamma120)
            return CrystalSystem.HEXAGONAL;
        if (aEqB && aEqC && !alpha90)
            return CrystalSystem.TRIGONAL;
        if (alpha90 && gamma90 && !beta90)
            return CrystalSystem.MONOCLINIC;
        return CrystalSystem.TRICLINIC;
    }

    /**
     * Calculates unit cell volume.
     * V = abc * sqrt(1 - cos²α - cos²β - cos²γ + 2cosα·cosβ·cosγ)
     */
    public double volume() {
        double cosA = Math.cos(Math.toRadians(alpha));
        double cosB = Math.cos(Math.toRadians(beta));
        double cosG = Math.cos(Math.toRadians(gamma));

        double factor = 1 - cosA * cosA - cosB * cosB - cosG * cosG + 2 * cosA * cosB * cosG;
        return a * b * c * Math.sqrt(factor);
    }

    /**
     * Calculates d-spacing for a given Miller index.
     * For cubic: d = a / sqrt(h² + k² + l²)
     */
    public double dSpacing(int h, int k, int l) {
        if (system == CrystalSystem.CUBIC) {
            return a / Math.sqrt(h * h + k * k + l * l);
        }
        // General formula for orthorhombic
        if (system == CrystalSystem.ORTHORHOMBIC || system == CrystalSystem.TETRAGONAL) {
            return 1.0 / Math.sqrt(h * h / (a * a) + k * k / (b * b) + l * l / (c * c));
        }
        // Simplified - use cubic approximation for others
        return a / Math.sqrt(h * h + k * k + l * l);
    }

    /**
     * Bragg's law: calculates diffraction angle.
     * nλ = 2d·sin(θ)
     * 
     * @param d          d-spacing (Å)
     * @param wavelength X-ray wavelength (Å)
     * @param n          Order of diffraction
     * @return Bragg angle θ in degrees
     */
    public static double braggAngle(double d, double wavelength, int n) {
        double sinTheta = n * wavelength / (2 * d);
        if (sinTheta > 1 || sinTheta < -1)
            return Double.NaN;
        return Math.toDegrees(Math.asin(sinTheta));
    }

    // Getters
    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getGamma() {
        return gamma;
    }

    public CrystalSystem getSystem() {
        return system;
    }

    // --- Common crystal structures ---

    /** NaCl (rock salt): a = 5.64 Å */
    public static final UnitCell NACL = cubic(5.64);

    /** Diamond: a = 3.567 Å */
    public static final UnitCell DIAMOND = cubic(3.567);

    /** Silicon: a = 5.431 Å */
    public static final UnitCell SILICON = cubic(5.431);

    /** Cu-Kα X-ray wavelength */
    public static final double CU_K_ALPHA = 1.5406;

    /** Mo-Kα X-ray wavelength */
    public static final double MO_K_ALPHA = 0.7107;
}
