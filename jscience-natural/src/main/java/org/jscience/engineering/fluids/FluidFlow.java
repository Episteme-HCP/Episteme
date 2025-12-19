package org.jscience.engineering.fluids;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Velocity;

/**
 * Fluid flow calculations.
 */
public class FluidFlow {

    private FluidFlow() {
    }

    /**
     * Reynolds number.
     * Re = ρ * v * L / μ = v * L / ν
     * 
     * @param velocity             Flow velocity (m/s)
     * @param characteristicLength L (m) - e.g., pipe diameter
     * @param kinematicViscosity   ν (m²/s)
     * @return Reynolds number (dimensionless)
     */
    public static double reynoldsNumber(double velocity, double characteristicLength,
            double kinematicViscosity) {
        return velocity * characteristicLength / kinematicViscosity;
    }

    /**
     * Determines flow regime from Reynolds number.
     */
    public static String flowRegime(double reynoldsNumber) {
        if (reynoldsNumber < 2300)
            return "Laminar";
        if (reynoldsNumber < 4000)
            return "Transitional";
        return "Turbulent";
    }

    /**
     * Bernoulli's equation: pressure difference.
     * P1 + 0.5*ρ*v1² + ρ*g*h1 = P2 + 0.5*ρ*v2² + ρ*g*h2
     * 
     * @return Pressure at point 2 given conditions at point 1
     */
    public static Quantity<Pressure> bernoulliPressure(
            Quantity<Pressure> p1, Quantity<Velocity> v1, double h1,
            Quantity<Velocity> v2, double h2, double density) {

        double P1 = p1.to(Units.PASCAL).getValue().doubleValue();
        double V1 = v1.to(Units.METERS_PER_SECOND).getValue().doubleValue();
        double V2 = v2.to(Units.METERS_PER_SECOND).getValue().doubleValue();
        double g = 9.80665;

        double P2 = P1 + 0.5 * density * (V1 * V1 - V2 * V2) + density * g * (h1 - h2);
        return Quantities.create(P2, Units.PASCAL);
    }

    /**
     * Hagen-Poiseuille equation for laminar pipe flow.
     * Q = π * ΔP * r⁴ / (8 * μ * L)
     * 
     * @param pressureDrop     ΔP (Pa)
     * @param radius           Pipe radius (m)
     * @param dynamicViscosity μ (Pa·s)
     * @param length           Pipe length (m)
     * @return Volumetric flow rate (m³/s)
     */
    public static double laminarPipeFlow(double pressureDrop, double radius,
            double dynamicViscosity, double length) {
        return Math.PI * pressureDrop * Math.pow(radius, 4) / (8 * dynamicViscosity * length);
    }

    /**
     * Friction factor for laminar flow.
     * f = 64 / Re
     */
    public static double laminarFrictionFactor(double reynoldsNumber) {
        return 64.0 / reynoldsNumber;
    }
}
