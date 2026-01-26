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

package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the aerodynamics of a spherical ball in flight, including Drag and Magnus effects.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class BallAerodynamics {

    private BallAerodynamics() {}

    /**
     * Calculates the Magnus Force (lift/swerve) on a spinning ball.
     * Formula: Fm = 0.5 * Cl * rho * A * (r * omega) * v
     * Note: This implementation simplifies the lift coefficient Cl approximation.
     * 
     * @param rho      Air density (kg/m^3)
     * @param radius   Ball radius (m)
     * @param velocity Ball velocity (m/s)
     * @param spinRps  Spin rate (revolutions per second)
     * @return The Magnus force magnitude in Newtons
     */
    public static Real calculateMagnusForce(double rho, double radius, double velocity, double spinRps) {
        double area = Math.PI * radius * radius;
        double omega = 2 * Math.PI * spinRps;
        
        // Simplified Lift Coefficient for a spinning sphere
        // Cl is roughly proportional to the spin parameter (r*omega/v) in typical sports ranges
        double spinParameter = (velocity > 0) ? (radius * omega) / velocity : 0;
        double cl = 0.3 * spinParameter; // Linear approximation for diverse sports regimes
        
        double force = 0.5 * cl * rho * area * velocity * velocity;
        return Real.of(force);
    }

    /**
     * Calculates the Aerodynamic Drag Force.
     * Formula: Fd = 0.5 * Cd * rho * A * v^2
     * 
     * @param rho      Air density (kg/m^3)
     * @param radius   Ball radius (m)
     * @param velocity Ball velocity (m/s)
     * @param cd       Drag Coefficient (dimensionless)
     * @return The Drag force magnitude in Newtons
     */
    public static Real calculateDragForce(double rho, double radius, double velocity, double cd) {
        double area = Math.PI * radius * radius;
        double force = 0.5 * cd * rho * area * velocity * velocity;
        return Real.of(force);
    }

    /**
     * projects the impact point of a ball over a short time step `dt`, accounting for Drag and Magnus effects.
     * Uses a simplified Euler integration for 2D motion (side view).
     * 
     * @param pos  Current position [x, y] in meters
     * @param vel  Current velocity [vx, vy] in m/s
     * @param spin Spin rate in revolutions per second (assumed pure backspin/topspin)
     * @param dt   Time step in seconds
     * @return A 4-element array: [newX, newY, newVx, newVy]
     */
    public static double[] projectImpact(double[] pos, double[] vel, double spin, double dt) {
        // Current state
        double x = pos[0];
        double y = pos[1];
        double vx = vel[0];
        double vy = vel[1];
        double velocity = Math.sqrt(vx * vx + vy * vy);
        
        // Constants for standard air and ball (approximate, e.g., tennis/baseball/soccer scale)
        double rho = 1.225; // Sea level air density
        double radius = 0.033; // Approx tennis ball radius
        
        // Forces (Magnus)
        // Magnitude
        double magForce = calculateMagnusForce(rho, radius, velocity, Math.abs(spin)).doubleValue();
        
        // Direction of Magnus force:
        // For pure topspin (positive spin param here assumed topspin for downward force),
        // Force is perpendicular to velocity.
        // If spin > 0 (Topspin): Force acts DOWN relative to trajectory
        // If spin < 0 (Backspin): Force acts UP relative to trajectory
        // This vector logic assumes standard coordinate system (Y up).
        
        // Unit velocity vector
        double uvx = (velocity > 0) ? vx / velocity : 0;
        double uvy = (velocity > 0) ? vy / velocity : 0;
        
        // Perpendicular vector (-y, x) for 90 deg rotation
        double pvx = -uvy;
        double pvy = uvx;
        
        // Direction multiplier: Topspin (positive spin input) -> force down -> needs to oppose lift?
        // Let's assume standard: Backspin (spin > 0) -> Lift (Up). Topspin (spin < 0) -> Dip (Down).
        // Let's stick to the method signature: spin is a scalar. 
        // We'll apply the force along the perpendicular vector scaled by spin sign.
        double forceX = magForce * pvx * Math.signum(spin);
        double forceY = magForce * pvy * Math.signum(spin);
        
        // Drag (opposes velocity)
        // Assuming constant Cd for simplicity
        double dragForceMag = calculateDragForce(rho, radius, velocity, 0.5).doubleValue();
        double dragX = -dragForceMag * uvx;
        double dragY = -dragForceMag * uvy;
        
        // Gravity
        // Force = m*g (mass approx 57g tennis ball). We need Acceleration = Force / Mass.
        double mass = 0.057; // kg
        
        double ax = (dragX + forceX) / mass;
        double ay = (dragY + forceY) / mass - 9.81; // Gravity acceleration directly
        
        // Update state (Euler)
        double newVx = vx + ax * dt;
        double newVy = vy + ay * dt;
        double newX = x + vx * dt;
        double newY = y + vy * dt;
        
        return new double[]{ newX, newY, newVx, newVy };
    }
}
