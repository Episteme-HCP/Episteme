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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.Vector2D;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Energy;

/**
 * Classical kinematics - motion without considering forces.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class Kinematics {

    // --- Vector operations ---

    public static Vector<Real> positionConstantAccel(Vector<Real> initialPos, Vector<Real> initialVel,
            Vector<Real> accel, Real time) {
        Vector<Real> term1 = initialVel.multiply(time);
        Vector<Real> term2 = accel.multiply(time).multiply(time).multiply(Real.of(0.5));
        return initialPos.add(term1).add(term2);
    }

    public static Vector<Real> velocityConstantAccel(Vector<Real> initialVel, Vector<Real> accel, Real time) {
        return initialVel.add(accel.multiply(time));
    }

    public static Real kineticEnergy(Real mass, Vector<Real> velocity) {
        // KE = 0.5 * m * |v|^2
        Real v2 = velocity.norm().pow(2);
        // Note: norm() returns Euclidian norm. Square it.
        // Or if Vector has dot product: velocity.dot(velocity)
        return Real.of(0.5).multiply(mass).multiply(v2);
    }

    public static Quantity<Energy> kineticEnergy(Quantity<Mass> mass, Vector<Real> velocity) {
        Real v2 = velocity.norm().pow(2);
        double energyValue = 0.5 * mass.to(Units.KILOGRAM).getValue().doubleValue() * v2.doubleValue();
        return Quantities.create(energyValue, Units.JOULE);
    }

    // --- Scalar operations ---

    public static Real positionConstantAccel(Real initialPos, Real initialVel, Real accel, Real time) {
        Real term1 = initialVel.multiply(time);
        Real term2 = Real.of(0.5).multiply(accel).multiply(time).multiply(time);
        return initialPos.add(term1).add(term2);
    }

    public static Real velocityConstantAccel(Real initialVel, Real accel, Real time) {
        return initialVel.add(accel.multiply(time));
    }

    public static Real velocityFromDisplacement(Real initialVel, Real accel, Real displacement) {
        Real vSquared = initialVel.multiply(initialVel)
                .add(Real.TWO.multiply(accel).multiply(displacement));
        return vSquared.sqrt();
    }

    public static Real projectileMaxHeight(Real initialSpeed, Real launchAngle, Real g) {
        double sinTheta = Math.sin(launchAngle.doubleValue());
        Real verticalSpeed = initialSpeed.multiply(Real.of(sinTheta));
        return verticalSpeed.multiply(verticalSpeed).divide(Real.TWO.multiply(g));
    }

    public static Real projectileRange(Real initialSpeed, Real launchAngle, Real g) {
        double sin2Theta = Math.sin(2 * launchAngle.doubleValue());
        return initialSpeed.multiply(initialSpeed).multiply(Real.of(sin2Theta)).divide(g);
    }

    public static Real projectileTimeOfFlight(Real initialSpeed, Real launchAngle, Real g) {
        double sinTheta = Math.sin(launchAngle.doubleValue());
        return Real.TWO.multiply(initialSpeed).multiply(Real.of(sinTheta)).divide(g);
    }

    public static Vector2D projectilePosition(Real initialSpeed, Real launchAngle, Real g, Real time) {
        double cosTheta = Math.cos(launchAngle.doubleValue());
        double sinTheta = Math.sin(launchAngle.doubleValue());

        Real x = initialSpeed.multiply(Real.of(cosTheta)).multiply(time);
        Real y = initialSpeed.multiply(Real.of(sinTheta)).multiply(time)
                .subtract(Real.of(0.5).multiply(g).multiply(time).multiply(time));

        return Vector2D.of(x.doubleValue(), y.doubleValue());
    }

    public static Real centripetalAcceleration(Real velocity, Real radius) {
        return velocity.multiply(velocity).divide(radius);
    }

    public static Vector<Real> centripetalAcceleration(Vector<Real> velocity, Real radius, Vector<Real> position,
            Vector<Real> center) {
        // Direction towards center: center - position
        Vector<Real> rVec = center.subtract(position);

        // Ensure radius matches distance? Or trust parameter?
        // Let's rely on direction vector normalization.
        // a_c = (v^2 / radius) * normalized(rVec)

        Real vSq = velocity.norm().pow(2);
        Real mag = vSq.divide(radius);

        // Normalize direction
        Real dist = rVec.norm();
        if (dist.equals(Real.ZERO))
            return velocity.multiply(Real.ZERO); // Singularity

        Vector<Real> dir = rVec.multiply(dist.inverse());

        return dir.multiply(mag);
    }

    public static Real orbitalPeriod(Real radius, Real velocity) {
        return Real.of(2 * Math.PI).multiply(radius).divide(velocity);
    }

    public static Real angularVelocity(Real linearVelocity, Real radius) {
        return linearVelocity.divide(radius);
    }

    public static Real angularDisplacement(Real initialAngle, Real initialAngVel,
            Real angularAccel, Real time) {
        Real term1 = initialAngVel.multiply(time);
        Real term2 = Real.of(0.5).multiply(angularAccel).multiply(time).multiply(time);
        return initialAngle.add(term1).add(term2);
    }

    public static Real averageVelocity(Real displacement, Real time) {
        return displacement.divide(time);
    }

    public static Vector<Real> averageVelocity(Vector<Real> displacement, Real time) {
        return displacement.multiply(time.inverse());
    }

    public static Real averageAcceleration(Real velocityChange, Real time) {
        return velocityChange.divide(time);
    }

    public static Vector<Real> averageAcceleration(Vector<Real> velocityChange, Real time) {
        return velocityChange.multiply(time.inverse());
    }

    /**
     * Computes the cross product of two 3D vectors.
     * 
     * @param a Vector A
     * @param b Vector B
     * @return a x b
     */
    public static Vector<Real> crossProduct(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != 3 || b.dimension() != 3)
            throw new IllegalArgumentException("Cross product requires 3D vectors");

        Real ax = a.get(0);
        Real ay = a.get(1);
        Real az = a.get(2);

        Real bx = b.get(0);
        Real by = b.get(1);
        Real bz = b.get(2);

        // cx = ay*bz - az*by
        Real cx = ay.multiply(bz).subtract(az.multiply(by));
        // cy = az*bx - ax*bz
        Real cy = az.multiply(bx).subtract(ax.multiply(bz));
        // cz = ax*by - ay*bx
        Real cz = ax.multiply(by).subtract(ay.multiply(bx));

        // Use DenseVector via VectorFactory if possible, or just list
        return org.jscience.mathematics.linearalgebra.vectors.VectorFactory.<Real>create(
                java.util.Arrays.asList(cx, cy, cz), Real.ZERO);
    }
}
