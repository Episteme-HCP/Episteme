package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.geometry.Point2D;

/**
 * Represents a hyperbola in 2D space.
 * <p>
 * A hyperbola is defined by the equation: (x-h)²/a² - (y-k)²/b² = 1
 * where (h,k) is the center, a is the semi-transverse axis, and b is the
 * semi-conjugate axis.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class Hyperbola {

    private final double centerX, centerY;
    private final double a; // Semi-transverse axis
    private final double b; // Semi-conjugate axis
    private final double rotation;

    /**
     * Creates a hyperbola centered at (h, k) with semi-axes a and b.
     */
    public Hyperbola(double centerX, double centerY, double a, double b) {
        this(centerX, centerY, a, b, 0);
    }

    /**
     * Creates a rotated hyperbola.
     */
    public Hyperbola(double centerX, double centerY, double a, double b, double rotation) {
        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException("Semi-axes must be positive");
        }
        this.centerX = centerX;
        this.centerY = centerY;
        this.a = a;
        this.b = b;
        this.rotation = rotation;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getSemiMajorAxis() {
        return a;
    }

    public double getSemiMinorAxis() {
        return b;
    }

    public double getRotation() {
        return rotation;
    }

    /**
     * Returns the foci distance from center: c = sqrt(a² + b²).
     */
    public double getFocalDistance() {
        return Math.sqrt(a * a + b * b);
    }

    /**
     * Returns the right focus.
     */
    public Point2D getRightFocus() {
        double c = getFocalDistance();
        double fx = c * Math.cos(rotation);
        double fy = c * Math.sin(rotation);
        return Point2D.of(centerX + fx, centerY + fy);
    }

    /**
     * Returns the left focus.
     */
    public Point2D getLeftFocus() {
        double c = getFocalDistance();
        double fx = c * Math.cos(rotation);
        double fy = c * Math.sin(rotation);
        return Point2D.of(centerX - fx, centerY - fy);
    }

    /**
     * Returns the eccentricity e = c/a.
     */
    public double getEccentricity() {
        return getFocalDistance() / a;
    }

    /**
     * Returns a point on the right branch at parameter t.
     */
    public Point2D pointOnRightBranch(double t) {
        double x = a * Math.cosh(t);
        double y = b * Math.sinh(t);
        return transformPoint(x, y);
    }

    /**
     * Returns a point on the left branch at parameter t.
     */
    public Point2D pointOnLeftBranch(double t) {
        double x = -a * Math.cosh(t);
        double y = b * Math.sinh(t);
        return transformPoint(x, y);
    }

    private Point2D transformPoint(double x, double y) {
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);
        double rx = x * cos - y * sin;
        double ry = x * sin + y * cos;
        return Point2D.of(centerX + rx, centerY + ry);
    }

    /**
     * Returns the asymptote slopes: ±b/a
     */
    public double[] getAsymptoteSlopes() {
        double slope = b / a;
        return new double[] { slope, -slope };
    }

    /**
     * Checks if a point is on the hyperbola (within tolerance).
     */
    public boolean containsPoint(Point2D p, double tolerance) {
        double dx = p.getX().doubleValue() - centerX;
        double dy = p.getY().doubleValue() - centerY;

        double cos = Math.cos(-rotation);
        double sin = Math.sin(-rotation);
        double lx = dx * cos - dy * sin;
        double ly = dx * sin + dy * cos;

        double value = (lx * lx) / (a * a) - (ly * ly) / (b * b);
        return Math.abs(value - 1.0) < tolerance;
    }

    @Override
    public String toString() {
        return String.format("Hyperbola[center=(%.2f,%.2f), a=%.2f, b=%.2f, e=%.3f]",
                centerX, centerY, a, b, getEccentricity());
    }
}
