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

package org.jscience.natural.earth.atmosphere;

import java.util.*;

/**
 * Simplified climate model for radiative balance and greenhouse effect simulations.
 */
import org.jscience.core.util.UniversalDataModel;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Temperature;
import org.jscience.core.measure.quantity.Length;

/**
 * Simplified climate model for radiative balance and greenhouse effect simulations.
 */
public final class ClimateModel implements UniversalDataModel {

    public static final Quantity<?> SOLAR_CONSTANT = Quantities.create(1361.0, Units.WATT.divide(Units.SQUARE_METER));
    public static final Quantity<?> STEFAN_BOLTZMANN = Quantities.create(5.67e-8, Units.WATT.divide(Units.SQUARE_METER).divide(Units.KELVIN.pow(4)));
    public static final double EARTH_ALBEDO = 0.30;

    public record ClimateState(
        Quantity<Temperature> globalMeanTemperature,
        double co2Concentration,       // ppm
        double ch4Concentration,       // ppb
        Quantity<?> radiativeForcing,   // W/mÂ²
        Quantity<Length> seaLevel,
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

    private final List<ClimateState> history = new ArrayList<>();
    private final String name;

    public ClimateModel(String name) {
        this.name = name;
        this.history.add(currentState());
    }

    /**
     * Calculates equilibrium temperature without greenhouse effect.
     */
    public static Quantity<Temperature> calculateBlackbodyTemperature(double albedo) {
        // Te = (S(1-Î±) / 4Ïƒ)^0.25
        double S = SOLAR_CONSTANT.getValue().doubleValue();
        double sigma = STEFAN_BOLTZMANN.getValue().doubleValue();
        double flux = S * (1 - albedo) / 4;
        double temp = Math.pow(flux / sigma, 0.25);
        return Quantities.create(temp, Units.KELVIN);
    }

    /**
     * Calculates radiative forcing from CO2 concentration.
     * Î”F = 5.35 Ã— ln(C/Câ‚€)
     */
    public static Quantity<?> co2RadiativeForcing(double currentCO2, double preindustrialCO2) {
        double forcing = 5.35 * Math.log(currentCO2 / preindustrialCO2);
        return Quantities.create(forcing, Units.WATT.divide(Units.SQUARE_METER));
    }

    /**
     * Calculates radiative forcing from methane.
     * Î”F = 0.036 Ã— (sqrt(M) - sqrt(Mâ‚€))
     */
    public static Quantity<?> ch4RadiativeForcing(double currentCH4, double preindustrialCH4) {
        double forcing = 0.036 * (Math.sqrt(currentCH4) - Math.sqrt(preindustrialCH4));
        return Quantities.create(forcing, Units.WATT.divide(Units.SQUARE_METER));
    }

    /**
     * Converts radiative forcing to temperature change.
     * Î”T = Î» Ã— Î”F (climate sensitivity parameter)
     */
    public static Quantity<Temperature> temperatureChange(Quantity<?> radiativeForcing, double climateSensitivity) {
        double dt = radiativeForcing.getValue().doubleValue() * climateSensitivity;
        return Quantities.create(dt, Units.KELVIN);
    }

    /**
     * Projects climate forward under a given scenario.
     */
    public void simulate(EmissionScenario scenario, int years) {
        ClimateState last = history.get(history.size() - 1);
        
        double co2 = last.co2Concentration();
        double ch4 = last.ch4Concentration();
        double temp = last.globalMeanTemperature().getValue().doubleValue();
        double seaLevel = last.seaLevel().to(Units.METER).getValue().doubleValue();
        
        double preindustrialCO2 = 280.0;
        double preindustrialCH4 = 700.0;
        double climateSensitivity = 0.8; // Â°C per W/mÂ²
        
        for (int y = 1; y <= years; y++) {
            co2 += scenario.annualCO2EmissionGt() * 0.45;
            ch4 += scenario.annualCH4EmissionMt() * 0.001;
            ch4 *= 0.9;
            
            double co2Forcing = co2RadiativeForcing(co2, preindustrialCO2).getValue().doubleValue();
            double ch4Forcing = ch4RadiativeForcing(ch4, preindustrialCH4).getValue().doubleValue();
            double totalForcing = co2Forcing + ch4Forcing;
            
            double equilibriumTemp = 288.0 + climateSensitivity * totalForcing;
            temp = temp + 0.03 * (equilibriumTemp - temp);
            
            double tempAnomaly = temp - 288.0;
            seaLevel += 0.003 * tempAnomaly + 0.001 * Math.max(0, tempAnomaly - 1);
            
            history.add(new ClimateState(
                Quantities.create(temp, Units.KELVIN),
                co2, ch4, 
                Quantities.create(totalForcing, Units.WATT.divide(Units.SQUARE_METER)),
                Quantities.create(seaLevel, Units.METER),
                last.year() + y
            ));
        }
    }

    public List<ClimateState> getHistory() {
        return Collections.unmodifiableList(history);
    }

    @Override
    public String getModelType() {
        return "CLIMATE_PROJECTION";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("name", name);
        meta.put("history_size", history.size());
        if (!history.isEmpty()) {
            meta.put("current_year", history.get(history.size() - 1).year());
        }
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        Map<String, Quantity<?>> q = new HashMap<>();
        if (!history.isEmpty()) {
            ClimateState last = history.get(history.size() - 1);
            q.put("global_mean_temperature", last.globalMeanTemperature());
            q.put("radiative_forcing", last.radiativeForcing());
            q.put("sea_level_rise", last.seaLevel());
        }
        return q;
    }

    public static ClimateState currentState() {
        return new ClimateState(
            Quantities.create(288.5, Units.KELVIN),
            420.0,
            1900.0,
            Quantities.create(3.2, Units.WATT.divide(Units.SQUARE_METER)),
            Quantities.create(0.20, Units.METER),
            2024
        );
    }
}


