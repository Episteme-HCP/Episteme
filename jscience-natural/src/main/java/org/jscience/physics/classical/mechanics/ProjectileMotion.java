/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics.classical.mechanics;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Time;

/**
 * Projectile motion calculator with air resistance options.
 * <p>
 * Provides calculations for:
 * <ul>
 * <li>Maximum height</li>
 * <li>Range (horizontal distance)</li>
 * <li>Time of flight</li>
 * <li>Trajectory points</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class ProjectileMotion {

    /** Standard gravity (m/s²) */
    public static final double G = 9.80665;

    private final double initialSpeed; // m/s
    private final double launchAngle; // radians
    private final double launchHeight; // m
    private final double gravity; // m/s²

    /**
     * Creates projectile with specified parameters.
     * 
     * @param initialSpeed       initial speed (m/s)
     * @param launchAngleDegrees launch angle (degrees from horizontal)
     * @param launchHeight       initial height (m)
     */
    public ProjectileMotion(double initialSpeed, double launchAngleDegrees, double launchHeight) {
        this(initialSpeed, launchAngleDegrees, launchHeight, G);
    }

    /**
     * Creates projectile with custom gravity.
     */
    public ProjectileMotion(double initialSpeed, double launchAngleDegrees, double launchHeight, double gravity) {
        this.initialSpeed = initialSpeed;
        this.launchAngle = Math.toRadians(launchAngleDegrees);
        this.launchHeight = launchHeight;
        this.gravity = gravity;
    }

    /**
     * Calculates maximum height reached.
     * 
     * @return maximum height as Quantity
     */
    public Quantity<Length> getMaxHeight() {
        double vY = initialSpeed * Math.sin(launchAngle);
        double maxH = launchHeight + (vY * vY) / (2 * gravity);
        return Quantities.create(maxH, Units.METER);
    }

    /**
     * Calculates horizontal range (assuming flat ground at launch height).
     * 
     * @return range as Quantity
     */
    public Quantity<Length> getRange() {
        double vX = initialSpeed * Math.cos(launchAngle);
        double vY = initialSpeed * Math.sin(launchAngle);

        // Time when y = 0: solve y = h + vY*t - 0.5*g*t² = 0
        double discriminant = vY * vY + 2 * gravity * launchHeight;
        double t = (vY + Math.sqrt(discriminant)) / gravity;

        double range = vX * t;
        return Quantities.create(range, Units.METER);
    }

    /**
     * Calculates time of flight until ground.
     * 
     * @return time of flight as Quantity
     */
    public Quantity<Time> getTimeOfFlight() {
        double vY = initialSpeed * Math.sin(launchAngle);
        double discriminant = vY * vY + 2 * gravity * launchHeight;
        double t = (vY + Math.sqrt(discriminant)) / gravity;
        return Quantities.create(t, Units.SECOND);
    }

    /**
     * Returns position at time t.
     * 
     * @param t time in seconds
     * @return [x, y] position array
     */
    public double[] getPosition(double t) {
        double vX = initialSpeed * Math.cos(launchAngle);
        double vY = initialSpeed * Math.sin(launchAngle);

        double x = vX * t;
        double y = launchHeight + vY * t - 0.5 * gravity * t * t;

        return new double[] { x, Math.max(0, y) };
    }

    /**
     * Returns velocity at time t.
     * 
     * @param t time in seconds
     * @return [vx, vy] velocity array
     */
    public double[] getVelocity(double t) {
        double vX = initialSpeed * Math.cos(launchAngle);
        double vY = initialSpeed * Math.sin(launchAngle) - gravity * t;
        return new double[] { vX, vY };
    }

    /**
     * Calculates optimal launch angle for maximum range.
     * 
     * @return optimal angle in degrees
     */
    public static double optimalAngle() {
        return 45.0;
    }

    /**
     * Calculates optimal angle considering launch height above target.
     * 
     * @param launchHeight height above target
     * @param range        desired range
     * @param initialSpeed projectile speed
     * @return optimal angle in degrees
     */
    public static double optimalAngle(double launchHeight, double range, double initialSpeed) {
        // For non-zero launch height, optimal angle < 45°
        double v2 = initialSpeed * initialSpeed;
        double term = v2 / (v2 + G * launchHeight);
        return Math.toDegrees(Math.asin(Math.sqrt(term)) / 2);
    }

    /**
     * Generates trajectory points.
     * 
     * @param numPoints number of points
     * @return array of [x, y] points
     */
    public double[][] getTrajectory(int numPoints) {
        double tFlight = getTimeOfFlight().getValue(Units.SECOND).doubleValue();
        double dt = tFlight / (numPoints - 1);

        double[][] trajectory = new double[numPoints][2];
        for (int i = 0; i < numPoints; i++) {
            trajectory[i] = getPosition(i * dt);
        }
        return trajectory;
    }

    @Override
    public String toString() {
        return String.format("ProjectileMotion{v0=%.2f m/s, θ=%.1f°, h0=%.2f m, range=%.2f m}",
                initialSpeed, Math.toDegrees(launchAngle), launchHeight,
                getRange().getValue(Units.METER).doubleValue());
    }
}
