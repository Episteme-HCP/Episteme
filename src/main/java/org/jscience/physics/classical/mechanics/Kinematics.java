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
package org.jscience.physics.kinematics;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.geometry.Vector2D;

/**
 * Classical kinematics - motion without considering forces.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class Kinematics {

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

    public static Real averageAcceleration(Real velocityChange, Real time) {
        return velocityChange.divide(time);
    }
}


