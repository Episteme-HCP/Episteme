package org.jscience.natural.climate;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Advanced Climate Model including Greenhouse Effect, Albedo Feedbacks, and Climate Sensitivity.
 */
public final class ClimateModel {

    private ClimateModel() {}

    private static final double SIGMA = 5.67e-8; // Stefan-Boltzmann constant
    private static final double SOLAR_CONSTANT = 1361.0; // W/m2

    /**
     * Calculates steady-state temperature with CO2 forcing and ice-albedo feedback.
     * 
     * @param co2ConcentrationPPM Current CO2 concentration
     * @param baseAlbedo Albedo without ice
     * @param climateSensitivity Warming per doubling of CO2 (e.g., 3.0)
     */
    public static Real estimateGlobalTemperature(double co2ConcentrationPPM, double baseAlbedo, double climateSensitivity) {
        double initialCo2 = 280.0;
        
        // Radiative Forcing: DeltaF = 5.35 * ln(C/C0)
        double forcing = 5.35 * Math.log(co2ConcentrationPPM / initialCo2);
        
        // Equilibrium Temperature: T = T0 + (Sensitivity/Forcing_2xCO2) * DeltaF
        // Forcing for 2xCO2 is approx 3.7 W/m2
        double deltaT = (climateSensitivity / 3.7) * forcing;
        
        // Iterative Albedo Feedback (Ice-Albedo)
        double currentTemp = 14.0 + deltaT; // Starting from baseline 14C
        double albedo = updateAlbedo(currentTemp, baseAlbedo);
        
        // Final energy balance: (1 - albedo) * S/4 = epsilon * sigma * T^4
        // epsilon (effective emissivity) reflects the greenhouse effect
        double s_quarter = SOLAR_CONSTANT / 4.0;
        double absorbed = s_quarter * (1 - albedo) + forcing;
        
        double kelvin = Math.pow(absorbed / (0.61 * SIGMA), 0.25);
        return Real.of(kelvin - 273.15);
    }

    private static double updateAlbedo(double tempCelsius, double baseAlbedo) {
        // Simple ice-albedo feedback: higher albedo if cold
        if (tempCelsius < -10) return 0.6; // Snow/Ice
        if (tempCelsius > 15) return baseAlbedo;
        // Linear interpolation between -10 and 15
        return 0.6 - (0.6 - baseAlbedo) * (tempCelsius + 10) / 25.0;
    }

    /**
     * Estimates heat capacity of the ocean for transient climate response.
     */
    public static Real thermalInertiaTimeConstant(double mixedLayerDepthMeters) {
        double cp_water = 4184; // J/(kg*K)
        double density = 1000;
        double oceanFraction = 0.7;
        return Real.of((density * cp_water * mixedLayerDepthMeters * oceanFraction) / (1e7)); // Rough years
    }
}
