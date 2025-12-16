package org.jscience.engineering.structural;

/**
 * Structural analysis calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class StructuralAnalysis {

    /**
     * Simply supported beam: maximum deflection at center.
     * δ_max = (5 * w * L⁴) / (384 * E * I)
     * 
     * @param w Distributed load (N/m)
     * @param L Beam length (m)
     * @param E Young's modulus (Pa)
     * @param I Second moment of area (m⁴)
     * @return Maximum deflection (m)
     */
    public static double simpleBeamDeflectionUniform(double w, double L, double E, double I) {
        return 5 * w * Math.pow(L, 4) / (384 * E * I);
    }

    /**
     * Simply supported beam: deflection under point load at center.
     * δ_max = (P * L³) / (48 * E * I)
     */
    public static double simpleBeamDeflectionPoint(double P, double L, double E, double I) {
        return P * Math.pow(L, 3) / (48 * E * I);
    }

    /**
     * Cantilever beam: deflection at free end under uniform load.
     * δ_max = (w * L⁴) / (8 * E * I)
     */
    public static double cantileverDeflectionUniform(double w, double L, double E, double I) {
        return w * Math.pow(L, 4) / (8 * E * I);
    }

    /**
     * Cantilever beam: deflection at free end under point load.
     * δ_max = (P * L³) / (3 * E * I)
     */
    public static double cantileverDeflectionPoint(double P, double L, double E, double I) {
        return P * Math.pow(L, 3) / (3 * E * I);
    }

    /**
     * Maximum bending moment for simply supported beam with uniform load.
     * M_max = w * L² / 8
     */
    public static double simpleBeamMomentUniform(double w, double L) {
        return w * L * L / 8;
    }

    /**
     * Maximum bending moment for cantilever with uniform load.
     * M_max = w * L² / 2
     */
    public static double cantileverMomentUniform(double w, double L) {
        return w * L * L / 2;
    }

    /**
     * Bending stress.
     * σ = M * y / I
     */
    public static double bendingStress(double moment, double distanceFromNA, double momentOfInertia) {
        return moment * distanceFromNA / momentOfInertia;
    }

    /**
     * Shear stress in beam.
     * τ = V * Q / (I * b)
     */
    public static double shearStress(double shearForce, double firstMoment,
            double momentOfInertia, double width) {
        return shearForce * firstMoment / (momentOfInertia * width);
    }

    /**
     * Second moment of area for rectangle.
     * I = b * h³ / 12
     */
    public static double rectangleMomentOfInertia(double width, double height) {
        return width * Math.pow(height, 3) / 12;
    }

    /**
     * Second moment of area for circle.
     * I = π * r⁴ / 4
     */
    public static double circleMomentOfInertia(double radius) {
        return Math.PI * Math.pow(radius, 4) / 4;
    }

    /**
     * Section modulus.
     * S = I / y_max
     */
    public static double sectionModulus(double momentOfInertia, double distanceToExtreme) {
        return momentOfInertia / distanceToExtreme;
    }

    /**
     * Column buckling (Euler formula).
     * P_cr = π² * E * I / (K * L)²
     * 
     * @param E Young's modulus (Pa)
     * @param I Moment of inertia (m⁴)
     * @param L Column length (m)
     * @param K Effective length factor (1.0 for pinned-pinned)
     */
    public static double eulerBucklingLoad(double E, double I, double L, double K) {
        return Math.PI * Math.PI * E * I / Math.pow(K * L, 2);
    }

    /**
     * Slenderness ratio.
     * λ = K * L / r where r = √(I/A) is radius of gyration
     */
    public static double slendernessRatio(double K, double L, double radiusOfGyration) {
        return K * L / radiusOfGyration;
    }

    /**
     * Radius of gyration.
     * r = √(I / A)
     */
    public static double radiusOfGyration(double momentOfInertia, double area) {
        return Math.sqrt(momentOfInertia / area);
    }

    /**
     * Truss member force (method of joints - simplified).
     * For equilibrium: ΣFx = 0, ΣFy = 0
     */
    public static double trussMemberForce(double angle, double jointForce) {
        return jointForce / Math.cos(Math.toRadians(angle));
    }

    /**
     * Torsional shear stress in circular shaft.
     * τ = T * r / J where J = πr⁴/2
     */
    public static double torsionalShearStress(double torque, double radius) {
        double J = Math.PI * Math.pow(radius, 4) / 2;
        return torque * radius / J;
    }

    /**
     * Angle of twist.
     * θ = T * L / (G * J)
     */
    public static double angleOfTwist(double torque, double length, double shearModulus, double radius) {
        double J = Math.PI * Math.pow(radius, 4) / 2;
        return torque * length / (shearModulus * J);
    }
}
