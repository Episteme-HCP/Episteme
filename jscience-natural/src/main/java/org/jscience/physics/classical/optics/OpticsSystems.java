package org.jscience.physics.classical.optics;

/**
 * Optics Systems - Lenses and mirrors.
 */
public class OpticsSystems {

    private OpticsSystems() {
    }

    /**
     * Thin lens equation.
     * 1/f = 1/do + 1/di
     * 
     * @param objectDistance do (positive)
     * @param focalLength    f (positive for converging, negative for diverging)
     * @return Image distance di
     */
    public static double imageDistance(double objectDistance, double focalLength) {
        return 1.0 / (1.0 / focalLength - 1.0 / objectDistance);
    }

    /**
     * Magnification.
     * M = -di/do = hi/ho
     */
    public static double magnification(double objectDistance, double imageDistance) {
        return -imageDistance / objectDistance;
    }

    /**
     * Lensmaker's equation for focal length.
     * 1/f = (n-1) * (1/R1 - 1/R2)
     * 
     * @param refractiveIndex n
     * @param radius1         R1 (positive for convex towards object)
     * @param radius2         R2
     * @return Focal length
     */
    public static double focalLengthFromRadii(double refractiveIndex, double radius1, double radius2) {
        return 1.0 / ((refractiveIndex - 1) * (1.0 / radius1 - 1.0 / radius2));
    }

    /**
     * Mirror equation (same as lens equation).
     * 1/f = 1/do + 1/di
     */
    public static double mirrorImageDistance(double objectDistance, double focalLength) {
        return imageDistance(objectDistance, focalLength);
    }

    /**
     * Spherical mirror focal length.
     * f = R/2
     */
    public static double mirrorFocalLength(double radiusOfCurvature) {
        return radiusOfCurvature / 2;
    }

    /**
     * Snell's law.
     * n1 * sin(θ1) = n2 * sin(θ2)
     * 
     * @return Angle of refraction (radians)
     */
    public static double snellRefraction(double n1, double n2, double incidentAngleRadians) {
        double sinTheta2 = (n1 / n2) * Math.sin(incidentAngleRadians);
        if (Math.abs(sinTheta2) > 1) {
            return Double.NaN; // Total internal reflection
        }
        return Math.asin(sinTheta2);
    }

    /**
     * Critical angle for total internal reflection.
     * θc = arcsin(n2/n1)
     */
    public static double criticalAngle(double n1, double n2) {
        if (n1 <= n2)
            return Double.NaN; // No TIR possible
        return Math.asin(n2 / n1);
    }

    /**
     * Optical power (diopters).
     * P = 1/f (f in meters)
     */
    public static double opticalPower(double focalLengthMeters) {
        return 1.0 / focalLengthMeters;
    }
}
