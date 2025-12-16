package org.jscience.physics.classical.thermodynamics;

/**
 * Heat transfer calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class HeatTransfer {

    /** Stefan-Boltzmann constant (W/(m²·K⁴)) */
    public static final double STEFAN_BOLTZMANN = 5.670374419e-8;

    /**
     * Conductive heat transfer (Fourier's law).
     * Q = k * A * ΔT / d
     * 
     * @param k         Thermal conductivity (W/(m·K))
     * @param area      Cross-sectional area (m²)
     * @param deltaT    Temperature difference (K)
     * @param thickness Material thickness (m)
     * @return Heat transfer rate (W)
     */
    public static double conduction(double k, double area, double deltaT, double thickness) {
        return k * area * deltaT / thickness;
    }

    /**
     * Convective heat transfer (Newton's law of cooling).
     * Q = h * A * (T_s - T_∞)
     * 
     * @param h      Heat transfer coefficient (W/(m²·K))
     * @param area   Surface area (m²)
     * @param deltaT Temperature difference (K)
     * @return Heat transfer rate (W)
     */
    public static double convection(double h, double area, double deltaT) {
        return h * area * deltaT;
    }

    /**
     * Radiative heat transfer (Stefan-Boltzmann law).
     * Q = ε * σ * A * (T₁⁴ - T₂⁴)
     * 
     * @param emissivity Surface emissivity (0-1)
     * @param area       Surface area (m²)
     * @param T1         Hot surface temperature (K)
     * @param T2         Cold surroundings temperature (K)
     * @return Heat transfer rate (W)
     */
    public static double radiation(double emissivity, double area, double T1, double T2) {
        return emissivity * STEFAN_BOLTZMANN * area * (Math.pow(T1, 4) - Math.pow(T2, 4));
    }

    /**
     * Thermal resistance for conduction.
     * R = d / (k * A)
     */
    public static double thermalResistanceConduction(double thickness, double k, double area) {
        return thickness / (k * area);
    }

    /**
     * Thermal resistance for convection.
     * R = 1 / (h * A)
     */
    public static double thermalResistanceConvection(double h, double area) {
        return 1.0 / (h * area);
    }

    /**
     * Overall heat transfer coefficient for composite wall.
     * 1/U = 1/h₁ + Σ(d_i/k_i) + 1/h₂
     */
    public static double overallHeatTransferCoefficient(double h1, double h2,
            double[] thicknesses, double[] conductivities) {
        double resistance = 1.0 / h1 + 1.0 / h2;
        for (int i = 0; i < thicknesses.length; i++) {
            resistance += thicknesses[i] / conductivities[i];
        }
        return 1.0 / resistance;
    }

    /**
     * Log Mean Temperature Difference (LMTD) for heat exchangers.
     * LMTD = (ΔT₁ - ΔT₂) / ln(ΔT₁/ΔT₂)
     */
    public static double lmtd(double deltaT1, double deltaT2) {
        if (Math.abs(deltaT1 - deltaT2) < 0.01) {
            return (deltaT1 + deltaT2) / 2; // Avoid division by zero
        }
        return (deltaT1 - deltaT2) / Math.log(deltaT1 / deltaT2);
    }

    /**
     * Heat exchanger effectiveness (NTU method).
     * ε = 1 - exp(-NTU) for C_min/C_max = 0
     */
    public static double heatExchangerEffectiveness(double NTU, double capacityRatio) {
        if (capacityRatio < 0.01) {
            return 1 - Math.exp(-NTU);
        }
        // Counterflow heat exchanger
        double exp = Math.exp(-NTU * (1 - capacityRatio));
        return (1 - exp) / (1 - capacityRatio * exp);
    }

    /**
     * Biot number: ratio of convection to conduction.
     * Bi = h * L_c / k
     * Bi < 0.1 allows lumped capacitance analysis
     */
    public static double biotNumber(double h, double characteristicLength, double k) {
        return h * characteristicLength / k;
    }

    /**
     * Fourier number: dimensionless time for transient conduction.
     * Fo = α * t / L²
     */
    public static double fourierNumber(double thermalDiffusivity, double time, double length) {
        return thermalDiffusivity * time / (length * length);
    }

    /**
     * Nusselt number for turbulent flow in a pipe (Dittus-Boelter).
     * Nu = 0.023 * Re^0.8 * Pr^n
     */
    public static double nusseltDittusBoelter(double Re, double Pr, boolean heating) {
        double n = heating ? 0.4 : 0.3;
        return 0.023 * Math.pow(Re, 0.8) * Math.pow(Pr, n);
    }

    // --- Thermal conductivities (W/(m·K)) ---

    public static final double K_COPPER = 401;
    public static final double K_ALUMINUM = 237;
    public static final double K_STEEL = 50;
    public static final double K_GLASS = 1.0;
    public static final double K_CONCRETE = 1.4;
    public static final double K_WOOD = 0.15;
    public static final double K_AIR = 0.026;
    public static final double K_WATER = 0.6;
}
