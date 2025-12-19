package org.jscience.engineering.thermal;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Power;

/**
 * Heat transfer calculations.
 */
public class HeatTransfer {

    private HeatTransfer() {
    }

    /**
     * Fourier's Law for heat conduction.
     * Q = k * A * ΔT / d
     * 
     * @param thermalConductivity   k (W/(m·K))
     * @param area                  Cross-sectional area (m²)
     * @param temperatureDifference ΔT (K)
     * @param thickness             d (m)
     * @return Heat transfer rate (W)
     */
    public static Quantity<Power> conduction(double thermalConductivity, double area,
            double temperatureDifference, double thickness) {
        double Q = thermalConductivity * area * temperatureDifference / thickness;
        return Quantities.create(Q, Units.WATT);
    }

    /**
     * Newton's Law of Cooling for convection.
     * Q = h * A * ΔT
     * 
     * @param convectionCoefficient h (W/(m²·K))
     * @param area                  Surface area (m²)
     * @param temperatureDifference ΔT (K)
     * @return Heat transfer rate (W)
     */
    public static Quantity<Power> convection(double convectionCoefficient, double area,
            double temperatureDifference) {
        double Q = convectionCoefficient * area * temperatureDifference;
        return Quantities.create(Q, Units.WATT);
    }

    /**
     * Stefan-Boltzmann Law for radiation.
     * Q = ε * σ * A * (T⁴ - T_surr⁴)
     * 
     * @param emissivity      ε (0-1)
     * @param area            Surface area (m²)
     * @param surfaceTemp     T (K)
     * @param surroundingTemp T_surr (K)
     * @return Heat transfer rate (W)
     */
    public static Quantity<Power> radiation(double emissivity, double area,
            double surfaceTemp, double surroundingTemp) {
        double sigma = 5.67e-8; // Stefan-Boltzmann constant
        double Q = emissivity * sigma * area * (Math.pow(surfaceTemp, 4) - Math.pow(surroundingTemp, 4));
        return Quantities.create(Q, Units.WATT);
    }

    /**
     * Thermal resistance for conduction.
     * R = d / (k * A)
     */
    public static double thermalResistance(double thickness, double thermalConductivity, double area) {
        return thickness / (thermalConductivity * area);
    }
}
