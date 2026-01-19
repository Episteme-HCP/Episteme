package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models ball aerodynamics including drag and Magnus effect.
 */
public final class BallAerodynamics {

    private BallAerodynamics() {}

    /**
     * Calculates the Magnus Force on a spinning ball.
     * Fm = Cl * rho * A * (r * omega) * v / 2
     * 
     * @param rho Air density (kg/m3)
     * @param radius Ball radius (m)
     * @param velocity m/s
     * @param spinRps Rotations per second
     * @return Force in Newtons
     */
    public static Real calculateMagnusForce(double rho, double radius, double velocity, double spinRps) {
        double area = Math.PI * radius * radius;
        double omega = 2 * Math.PI * spinRps;
        
        // Simplified Lift Coefficient for a spinning sphere
        double spinParameter = (radius * omega) / velocity;
        double cl = 0.3 * spinParameter; // Linear approximation for range
        
        double force = 0.5 * cl * rho * area * velocity * velocity;
        return Real.of(force);
    }

    /**
     * Calculates Drag Force.
     * Fd = 0.5 * Cd * rho * A * v^2
     */
    public static Real calculateDragForce(double rho, double radius, double velocity, double cd) {
        double area = Math.PI * radius * radius;
        double force = 0.5 * cd * rho * area * velocity * velocity;
        return Real.of(force);
    }

    /**
     * Projects trajectory including spin.
     */
    public static double[] projectImpact(double[] pos, double[] vel, double spin, double dt) {
        // Simplified 2D step
        double vx = vel[0];
        double vy = vel[1];
        
        // Magnus effect acts perpendicular to velocity
        // For topspin, it adds downward force
        double magForce = calculateMagnusForce(1.225, 0.033, Math.sqrt(vx*vx+vy*vy), spin).doubleValue();
        
        double ay = -9.81 - (magForce * vx); // Very rough approximation
        double ax = -(magForce * vy);
        
        return new double[]{ pos[0] + vx * dt, pos[1] + vy * dt, vx + ax * dt, vy + ay * dt };
    }
}
