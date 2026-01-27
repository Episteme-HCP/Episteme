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

package org.jscience.earth.atmosphere;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Simplified climate model for radiative balance and greenhouse effect simulations.
 */
public final class ClimateModel {

    private ClimateModel() {}

    public static final double SOLAR_CONSTANT = 1361.0;  // W/m²
    public static final double STEFAN_BOLTZMANN = 5.67e-8;  // W/(m²·K⁴)
    public static final double EARTH_ALBEDO = 0.30;

    public record ClimateState(
        double globalMeanTemperature,  // Kelvin
        double co2Concentration,       // ppm
        double ch4Concentration,       // ppb
        double radiativeForcing,       // W/m²
        double seaLevel,               // meters relative to 1990
        int year
    ) {}

    public record EmissionScenario(
        String name,
        double annualCO2EmissionGt,
        double annualCH4EmissionMt,
        double deforestationRate
    ) {}

    // Predefined scenarios
    public static final EmissionScenario RCP_26 = new EmissionScenario("RCP 2.6", 10, 300, 0.001);
    public static final EmissionScenario RCP_45 = new EmissionScenario("RCP 4.5", 25, 400, 0.005);
    public static final EmissionScenario RCP_85 = new EmissionScenario("RCP 8.5", 50, 600, 0.01);
    public static final EmissionScenario BUSINESS_AS_USUAL = new EmissionScenario("BAU", 40, 500, 0.008);

    /**
     * Calculates equilibrium temperature without greenhouse effect.
     */
    public static Real calculateBlackbodyTemperature(double albedo) {
        // Te = (S(1-α) / 4σ)^0.25
        double flux = SOLAR_CONSTANT * (1 - albedo) / 4;
        double temp = Math.pow(flux / STEFAN_BOLTZMANN, 0.25);
        return Real.of(temp);
    }

    /**
     * Calculates radiative forcing from CO2 concentration.
     * ΔF = 5.35 × ln(C/C₀)
     */
    public static Real co2RadiativeForcing(double currentCO2, double preindustrialCO2) {
        double forcing = 5.35 * Math.log(currentCO2 / preindustrialCO2);
        return Real.of(forcing);
    }

    /**
     * Calculates radiative forcing from methane.
     * ΔF = 0.036 × (sqrt(M) - sqrt(M₀))
     */
    public static Real ch4RadiativeForcing(double currentCH4, double preindustrialCH4) {
        double forcing = 0.036 * (Math.sqrt(currentCH4) - Math.sqrt(preindustrialCH4));
        return Real.of(forcing);
    }

    /**
     * Converts radiative forcing to temperature change.
     * ΔT = λ × ΔF (climate sensitivity parameter)
     */
    public static Real temperatureChange(Real radiativeForcing, double climateSensitivity) {
        return radiativeForcing.multiply(Real.of(climateSensitivity));
    }

    /**
     * Projects climate forward under a given scenario.
     */
    public static List<ClimateState> projectClimate(ClimateState initial, 
            EmissionScenario scenario, int years) {
        
        List<ClimateState> projection = new ArrayList<>();
        projection.add(initial);
        
        double co2 = initial.co2Concentration();
        double ch4 = initial.ch4Concentration();
        double temp = initial.globalMeanTemperature();
        double seaLevel = initial.seaLevel();
        
        double preindustrialCO2 = 280.0;
        double preindustrialCH4 = 700.0;
        double climateSensitivity = 0.8; // °C per W/m²
        
        for (int y = 1; y <= years; y++) {
            // Update concentrations
            // Airborne fraction of CO2 emissions
            co2 += scenario.annualCO2EmissionGt() * 0.45; // ~45% stays in atmosphere
            ch4 += scenario.annualCH4EmissionMt() * 0.001; // Simplified
            
            // CH4 has ~10 year lifetime
            ch4 *= 0.9;
            
            // Calculate forcing
            double co2Forcing = co2RadiativeForcing(co2, preindustrialCO2).doubleValue();
            double ch4Forcing = ch4RadiativeForcing(ch4, preindustrialCH4).doubleValue();
            double totalForcing = co2Forcing + ch4Forcing;
            
            // Temperature response (with thermal inertia)
            double equilibriumTemp = 288.0 + climateSensitivity * totalForcing;
            temp = temp + 0.03 * (equilibriumTemp - temp); // Slow approach
            
            // Sea level rise (simplified thermal expansion + ice melt)
            double tempAnomaly = temp - 288.0;
            seaLevel += 0.003 * tempAnomaly + 0.001 * Math.max(0, tempAnomaly - 1);
            
            projection.add(new ClimateState(
                temp, co2, ch4, totalForcing, seaLevel, initial.year() + y
            ));
        }
        
        return projection;
    }

    /**
     * Calculates cumulative carbon budget for a temperature target.
     */
    public static Real carbonBudget(double targetTempRise, double currentTempRise,
            double climateSensitivity) {
        
        // Remaining forcing budget
        double remainingForcing = (targetTempRise - currentTempRise) / climateSensitivity;
        
        // Convert to CO2 (approximately)
        double co2Budget = remainingForcing / 5.35 * 280 * 2.1; // GtC
        
        return Real.of(Math.max(0, co2Budget));
    }

    /**
     * Estimates ice sheet contribution to sea level based on warming.
     */
    public static Real iceSheetSeaLevelContribution(double warming, int years) {
        double greenlandRate = warming > 1.5 ? 0.003 : 0.001; // m/year
        double antarcticRate = warming > 2.0 ? 0.002 : 0.0005;
        
        double total = (greenlandRate + antarcticRate) * years;
        return Real.of(total);
    }

    /**
     * Creates initial state for present day (2024 approximate values).
     */
    public static ClimateState currentState() {
        return new ClimateState(
            288.5,    // ~15.35°C global mean
            420.0,    // CO2 in 2024
            1900.0,   // CH4 in ppb
            3.2,      // Total forcing
            0.20,     // Sea level rise since 1990
            2024
        );
    }
}
