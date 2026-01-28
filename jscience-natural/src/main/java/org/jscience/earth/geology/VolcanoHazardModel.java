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

package org.jscience.earth.geology;


/**
 * Advanced Volcanic Hazard Model.
 * Includes Energy Cone models for Pyroclastic Flows and Ash Dispersion.
 */
import org.jscience.util.UniversalDataModel;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Velocity;
import java.util.Map;
import java.util.HashMap;

/**
 * Advanced Volcanic Hazard Model.
 * Includes Energy Cone models for Pyroclastic Flows and Ash Dispersion.
 */
public final class VolcanoHazardModel implements UniversalDataModel {

    private final String name;
    private Quantity<Length> ventHeight;
    private double mobilityRatio = 0.2; // H/L
    private Quantity<Velocity> windSpeed;
    private Quantity<?> massDischargeRate; // kg/s

    public VolcanoHazardModel(String name) {
        this.name = name;
    }

    public void setVentHeight(Quantity<Length> height) { this.ventHeight = height; }
    public void setMobilityRatio(double ratio) { this.mobilityRatio = ratio; }
    public void setWindSpeed(Quantity<Velocity> speed) { this.windSpeed = speed; }
    public void setMassDischargeRate(Quantity<?> rate) { this.massDischargeRate = rate; }

    /**
     * Predicts Pyroclastic Flow runout distance using the "Energy Cone" model (H/L ratio).
     */
    public Quantity<Length> getPyroclasticRunout() {
        if (ventHeight == null) return null;
        double height = ventHeight.to(Units.METER).getValue().doubleValue();
        return Quantities.create(height / mobilityRatio, Units.METER);
    }

    /**
     * Estimates ash fall thickness using a spatial decay model.
     * T(d) = T0 * exp(-k * d)
     */
    public Quantity<Length> getAshThickness(Quantity<Length> distance, Quantity<Length> thicknessAtVent) {
        double d = distance.to(Units.KILOMETER).getValue().doubleValue();
        double t0 = thicknessAtVent.to(Units.CENTIMETER).getValue().doubleValue();
        double ws = windSpeed != null ? windSpeed.to(Units.METER_PER_SECOND).getValue().doubleValue() : 0.0;
        
        double k = 0.5 / (1.0 + ws * 0.1);
        double t = t0 * Math.exp(-k * d);
        return Quantities.create(t, Units.CENTIMETER);
    }

    /**
     * Calculates the plume height using the Morton, Taylor and Turner (MTT) model.
     * H = 1.67 * (Q^0.25)
     */
    public Quantity<Length> getPlumeHeight() {
        if (massDischargeRate == null) return null;
        double Q = massDischargeRate.getValue().doubleValue();
        return Quantities.create(1.67 * Math.pow(Q, 0.25), Units.KILOMETER);
    }

    @Override
    public String getModelType() {
        return "VOLCANIC_HAZARD";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("name", name);
        meta.put("mobility_ratio", mobilityRatio);
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        Map<String, Quantity<?>> q = new HashMap<>();
        if (ventHeight != null) q.put("vent_height", ventHeight);
        if (windSpeed != null) q.put("wind_speed", windSpeed);
        Quantity<Length> runout = getPyroclasticRunout();
        if (runout != null) q.put("pyroclastic_runout", runout);
        Quantity<Length> plume = getPlumeHeight();
        if (plume != null) q.put("plume_height", plume);
        return q;
    }
}

