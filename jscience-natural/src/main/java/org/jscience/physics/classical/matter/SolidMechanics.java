package org.jscience.physics.classical.matter;

/**
 * Solid Mechanics equations (Stress, Strain, Elasticity).
 * <p>
 * Formerly MaterialProperties.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SolidMechanics {

    private SolidMechanics() {
    }

    /**
     * Hooke's law: stress-strain relation.
     * σ = E * ε
     * 
     * @param strain        Strain (dimensionless)
     * @param youngsModulus Young's modulus (Pa)
     * @return Stress (Pa)
     */
    public static double hookesLaw(double strain, double youngsModulus) {
        return youngsModulus * strain;
    }

    /**
     * Strain from stress.
     * ε = σ / E
     */
    public static double strainFromStress(double stress, double youngsModulus) {
        return stress / youngsModulus;
    }

    /**
     * Poisson's ratio relation.
     * ε_transverse = -ν * ε_axial
     */
    public static double transverseStrain(double axialStrain, double poissonsRatio) {
        return -poissonsRatio * axialStrain;
    }

    /**
     * Shear modulus from Young's modulus and Poisson's ratio.
     * G = E / (2 * (1 + ν))
     */
    public static double shearModulus(double youngsModulus, double poissonsRatio) {
        return youngsModulus / (2 * (1 + poissonsRatio));
    }

    /**
     * Bulk modulus from E and ν.
     * K = E / (3 * (1 - 2ν))
     */
    public static double bulkModulus(double youngsModulus, double poissonsRatio) {
        return youngsModulus / (3 * (1 - 2 * poissonsRatio));
    }

    /**
     * Thermal expansion.
     * ΔL = L0 * α * ΔT
     */
    public static double thermalExpansion(double length, double alpha, double deltaT) {
        return length * alpha * deltaT;
    }

    /**
     * Thermal stress (constrained expansion).
     * σ = E * α * ΔT
     */
    public static double thermalStress(double youngsModulus, double alpha, double deltaT) {
        return youngsModulus * alpha * deltaT;
    }

    /**
     * Bending stress in a beam.
     * σ = M * y / I
     * 
     * @param moment          Bending moment (N·m)
     * @param y               Distance from neutral axis (m)
     * @param momentOfInertia Second moment of area (m⁴)
     */
    public static double bendingStress(double moment, double y, double momentOfInertia) {
        return moment * y / momentOfInertia;
    }

    /**
     * Critical buckling load (Euler formula).
     * P_cr = π² * E * I / L²
     */
    public static double eulerBucklingLoad(double youngsModulus, double momentOfInertia,
            double length) {
        return Math.PI * Math.PI * youngsModulus * momentOfInertia / (length * length);
    }

    /**
     * Hardness conversion: Brinell to Rockwell C (approximate).
     */
    public static double brinellToRockwellC(double brinell) {
        // Approximate empirical formula
        return 100 - 0.014 * (brinell - 100);
    }
}
