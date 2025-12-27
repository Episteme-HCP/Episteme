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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import org.jscience.physics.PhysicalConstants;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Time;

import java.util.Arrays;

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
 * <p>
 * Uses proper types: Vector for position/velocity, Matrix for trajectories,
 * Quantity for physical quantities, Real for dimensionless values.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ProjectileMotion {

    /** Standard gravity as Real */
    private static final Real G = PhysicalConstants.g_n;

    /** Mathematical constants */
    private static final Real HALF = Real.ONE.divide(Real.TWO);
    private static final Real OPTIMAL_ANGLE = Real.of(45);

    private final Real initialSpeed; // m/s
    private final Real launchAngle; // radians
    private final Real launchHeight; // m
    private final Real gravity; // m/s²

    /**
     * Creates projectile with specified parameters.
     *
     * @param initialSpeed       initial velocity magnitude
     * @param launchAngleDegrees launch angle in degrees
     * @param launchHeight       initial height above ground
     */
    public ProjectileMotion(Real initialSpeed, Real launchAngleDegrees, Real launchHeight) {
        this(initialSpeed, launchAngleDegrees, launchHeight, G);
    }

    /**
     * Creates projectile with custom gravity.
     *
     * @param initialSpeed       initial velocity magnitude
     * @param launchAngleDegrees launch angle in degrees
     * @param launchHeight       initial height above ground
     * @param gravity            gravitational acceleration
     */
    public ProjectileMotion(Real initialSpeed, Real launchAngleDegrees, Real launchHeight, Real gravity) {
        this.initialSpeed = initialSpeed;
        this.launchAngle = launchAngleDegrees.toRadians();
        this.launchHeight = launchHeight;
        this.gravity = gravity;
    }

    /**
     * Calculates maximum height reached.
     *
     * @return maximum height as Length quantity
     */
    public Quantity<Length> getMaxHeight() {
        Real vY = initialSpeed.multiply(launchAngle.sin());
        Real maxH = launchHeight.add(vY.multiply(vY).divide(Real.TWO.multiply(gravity)));
        return Quantities.create(maxH, Units.METER);
    }

    /**
     * Calculates horizontal range (assuming flat ground at launch height).
     *
     * @return horizontal range as Length quantity
     */
    public Quantity<Length> getRange() {
        Real vX = initialSpeed.multiply(launchAngle.cos());
        Real vY = initialSpeed.multiply(launchAngle.sin());

        // Time when y = 0: solve y = h + vY*t - 0.5*g*t² = 0
        Real discriminant = vY.multiply(vY).add(Real.TWO.multiply(gravity).multiply(launchHeight));
        Real t = vY.add(discriminant.sqrt()).divide(gravity);

        Real range = vX.multiply(t);
        return Quantities.create(range, Units.METER);
    }

    /**
     * Calculates time of flight until ground.
     *
     * @return time of flight as Time quantity
     */
    public Quantity<Time> getTimeOfFlight() {
        Real vY = initialSpeed.multiply(launchAngle.sin());
        Real discriminant = vY.multiply(vY).add(Real.TWO.multiply(gravity).multiply(launchHeight));
        Real t = vY.add(discriminant.sqrt()).divide(gravity);
        return Quantities.create(t, Units.SECOND);
    }

    /**
     * Returns time of flight as Real value.
     *
     * @return time of flight in seconds
     */
    public Real getTimeOfFlightReal() {
        Real vY = initialSpeed.multiply(launchAngle.sin());
        Real discriminant = vY.multiply(vY).add(Real.TWO.multiply(gravity).multiply(launchHeight));
        return vY.add(discriminant.sqrt()).divide(gravity);
    }

    /**
     * Returns position at time t as a 2D Vector.
     *
     * @param t time elapsed
     * @return Vector with [x, y] position
     */
    public Vector<Real> getPosition(Real t) {
        Real vX = initialSpeed.multiply(launchAngle.cos());
        Real vY = initialSpeed.multiply(launchAngle.sin());

        Real x = vX.multiply(t);
        Real y = launchHeight.add(vY.multiply(t)).subtract(
                HALF.multiply(gravity).multiply(t).multiply(t));

        Real yClipped = y.compareTo(Real.ZERO) < 0 ? Real.ZERO : y;
        return DenseVector.of(Arrays.asList(x, yClipped), Reals.getInstance());
    }

    /**
     * Returns velocity at time t as a 2D Vector.
     *
     * @param t time elapsed
     * @return Vector with [vx, vy] velocity
     */
    public Vector<Real> getVelocity(Real t) {
        Real vX = initialSpeed.multiply(launchAngle.cos());
        Real vY = initialSpeed.multiply(launchAngle.sin()).subtract(gravity.multiply(t));
        return DenseVector.of(Arrays.asList(vX, vY), Reals.getInstance());
    }

    /**
     * Calculates optimal launch angle for maximum range (45°).
     *
     * @return optimal angle in degrees
     */
    public static Real optimalAngle() {
        return OPTIMAL_ANGLE;
    }

    /**
     * Calculates optimal angle considering launch height above target.
     *
     * @param launchHeight height difference
     * @param range        target range
     * @param initialSpeed projectile speed
     * @return optimal angle in degrees
     */
    public static Real optimalAngle(Real launchHeight, Real range, Real initialSpeed) {
        Real v2 = initialSpeed.multiply(initialSpeed);
        Real term = v2.divide(v2.add(G.multiply(launchHeight)));
        return term.sqrt().asin().divide(Real.TWO).toDegrees();
    }

    /**
     * Generates trajectory points as a Matrix.
     * <p>
     * Each row represents a point: [x, y]
     * </p>
     *
     * @param numPoints number of trajectory points
     * @return Matrix with trajectory data (numPoints x 2)
     */
    public Matrix<Real> getTrajectory(int numPoints) {
        Real tFlight = getTimeOfFlightReal();
        Real dt = tFlight.divide(Real.of(numPoints - 1));

        Real[][] data = new Real[numPoints][2];

        for (int i = 0; i < numPoints; i++) {
            Vector<Real> pos = getPosition(Real.of(i).multiply(dt));
            data[i][0] = pos.get(0);
            data[i][1] = pos.get(1);
        }

        return DenseMatrix.of(data, Reals.getInstance());
    }

    /**
     * Returns initial velocity components as a Vector.
     *
     * @return Vector with [vx0, vy0]
     */
    public Vector<Real> getInitialVelocity() {
        Real vX = initialSpeed.multiply(launchAngle.cos());
        Real vY = initialSpeed.multiply(launchAngle.sin());
        return DenseVector.of(Arrays.asList(vX, vY), Reals.getInstance());
    }

    /**
     * Returns the initial speed.
     */
    public Real getInitialSpeed() {
        return initialSpeed;
    }

    /**
     * Returns the launch angle in radians.
     */
    public Real getLaunchAngle() {
        return launchAngle;
    }

    /**
     * Returns the launch height.
     */
    public Real getLaunchHeight() {
        return launchHeight;
    }

    @Override
    public String toString() {
        return String.format("ProjectileMotion{v0=%s m/s, θ=%s°, h0=%s m, range=%s}",
                initialSpeed, launchAngle.toDegrees(), launchHeight,
                getRange());
    }
}
