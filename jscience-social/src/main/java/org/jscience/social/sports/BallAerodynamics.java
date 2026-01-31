/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.social.sports;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.quantity.*;

/**
 * Models the aerodynamics of a spherical ball in flight, including Drag and Magnus effects.
 * Modernized to use Quantity and Real for high-precision physical simulations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class BallAerodynamics {

    private BallAerodynamics() {}

    /**
     * Calculates the Magnus Force (lift/swerve) on a spinning ball.
     * Formula: Fm = 0.5 * Cl * rho * A * (r * omega) * v
     * 
     * @param rho      Air density
     * @param radius   Ball radius
     * @param velocity Ball velocity
     * @param spin     Spin rate (frequency)
     * @return The Magnus force
     */
    public static Quantity<Force> calculateMagnusForce(
            Quantity<MassDensity> rho, 
            Quantity<Length> radius, 
            Quantity<Velocity> velocity, 
            Quantity<Frequency> spin) {
        
        Real r = radius.getValue();
        Real v = velocity.getValue();
        Real s = spin.getValue();
        Real rhoVal = rho.getValue();
        
        Real area = Real.of(Math.PI).multiply(r.pow(2));
        Real omega = Real.of(2.0 * Math.PI).multiply(s);
        
        // spinParameter = (r * omega) / v
        Real spinParameter = v.isZero() ? Real.ZERO : r.multiply(omega).divide(v);
        Real cl = Real.of(0.3).multiply(spinParameter);
        
        // force = 0.5 * cl * rho * area * v^2
        Real forceMag = Real.of(0.5).multiply(cl).multiply(rhoVal).multiply(area).multiply(v.pow(2));
        
        return Quantities.create(forceMag, Units.NEWTON);
    }

    /**
     * Calculates the Aerodynamic Drag Force.
     * Formula: Fd = 0.5 * Cd * rho * A * v^2
     * 
     * @param rho      Air density
     * @param radius   Ball radius
     * @param velocity Ball velocity
     * @param cd       Drag Coefficient (dimensionless)
     * @return The Drag force
     */
    public static Quantity<Force> calculateDragForce(
            Quantity<MassDensity> rho, 
            Quantity<Length> radius, 
            Quantity<Velocity> velocity, 
            Real cd) {
        
        Real r = radius.getValue();
        Real v = velocity.getValue();
        Real rhoVal = rho.getValue();
        Real area = Real.of(Math.PI).multiply(r.pow(2));
        
        Real forceMag = Real.of(0.5).multiply(cd).multiply(rhoVal).multiply(area).multiply(v.pow(2));
        
        return Quantities.create(forceMag, Units.NEWTON);
    }

    /**
     * Projects the impact point of a ball over a short time step `dt`.
     * 
     * @param pos   Current position [x, y]
     * @param vel   Current velocity [vx, vy]
     * @param spin  Spin rate
     * @param dt    Time step
     * @return A 4-element array: [newX, newY, newVx, newVy]
     */
    public static Quantity<?>[] projectImpact(
            Quantity<Length>[] pos, 
            Quantity<Velocity>[] vel, 
            Quantity<Frequency> spin, 
            Quantity<Time> dt) {
        
        Real x = pos[0].getValue();
        Real y = pos[1].getValue();
        Real vx = vel[0].getValue();
        Real vy = vel[1].getValue();
        Real dtVal = dt.getValue();
        
        Real velocityMag = vx.pow(2).add(vy.pow(2)).sqrt();
        
        Quantity<MassDensity> rho = Quantities.create(1.225, Units.KILOGRAM_PER_CUBIC_METER);
        Quantity<Length> radius = Quantities.create(0.033, Units.METER);
        Quantity<Mass> mass = Quantities.create(0.057, Units.KILOGRAM);
        
        Quantity<Force> magForce = calculateMagnusForce(rho, radius, Quantities.create(velocityMag, Units.METER_PER_SECOND), spin);
        Real fMag = magForce.getValue();
        
        // Unit velocity vector
        Real uvx = velocityMag.isZero() ? Real.ZERO : vx.divide(velocityMag);
        Real uvy = velocityMag.isZero() ? Real.ZERO : vy.divide(velocityMag);
        
        // Perpendicular vector (-y, x)
        Real pvx = uvy.negate();
        Real pvy = uvx;
        
        // Magnus force components (sign of spin handled by calculateMagnusForce which takes spin magnitude implicitly via spinParameter logic above, 
        // but here spin is typed and has sign)
        Real forceX = fMag.multiply(pvx);
        Real forceY = fMag.multiply(pvy);
        
        // Drag
        Quantity<Force> dragForce = calculateDragForce(rho, radius, Quantities.create(velocityMag, Units.METER_PER_SECOND), Real.of(0.5));
        Real dMag = dragForce.getValue();
        Real dragX = dMag.multiply(uvx).negate();
        Real dragY = dMag.multiply(uvy).negate();
        
        // Acceleration = Force / Mass
        Real m = mass.getValue();
        Real ax = forceX.add(dragX).divide(m);
        Real ay = forceY.add(dragY).divide(m).subtract(Real.of(9.81));
        
        // Update state (Euler)
        Real nVx = vx.add(ax.multiply(dtVal));
        Real nVy = vy.add(ay.multiply(dtVal));
        Real nX = x.add(vx.multiply(dtVal));
        Real nY = y.add(vy.multiply(dtVal));
        
        return new Quantity<?>[]{
            Quantities.create(nX, Units.METER),
            Quantities.create(nY, Units.METER),
            Quantities.create(nVx, Units.METER_PER_SECOND),
            Quantities.create(nVy, Units.METER_PER_SECOND)
        };
    }
}

