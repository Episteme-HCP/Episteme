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


import org.jscience.util.UniversalDataModel;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Time;
import java.util.Map;
import java.util.HashMap;

/**
 * Provides mathematical models for optimizing athlete recovery and tapering phases.
 * Based on the Banister Impulse-Response performance model.
 */
public final class FatigueRecoveryModel implements UniversalDataModel {

    private final String athleteName;
    private double fitness = 100.0;
    private double fatigue = 0.0;
    private double tau1 = 45.0; // Fitness decay (days)
    private double tau2 = 15.0; // Fatigue decay (days)
    private double baselinePerformance = 500.0;

    public FatigueRecoveryModel(String athleteName) {
        this.athleteName = athleteName;
    }

    public void setFitness(double f) { this.fitness = f; }
    public void setFatigue(double f) { this.fatigue = f; }
    public void setDecayConstants(double t1, double t2) { this.tau1 = t1; this.tau2 = t2; }

    /**
     * Predicts athletic performance using Banister's Performance Model.
     */
    public Quantity<?> predictPerformance(Quantity<Time> restTime) {
        double t = restTime.to(Units.DAY).getValue().doubleValue();
        double perf = baselinePerformance + (fitness * Math.exp(-t / tau1)) - 
                           (fatigue * Math.exp(-t / tau2));
        return Quantities.create(perf, Units.ONE);
    }

    @Override
    public String getModelType() {
        return "ATHLETE_RECOVERY_BANISTER";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("athlete_name", athleteName);
        meta.put("tau_fitness", tau1);
        meta.put("tau_fatigue", tau2);
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        Map<String, Quantity<?>> q = new HashMap<>();
        q.put("current_fitness", Quantities.create(fitness, Units.ONE));
        q.put("current_fatigue", Quantities.create(fatigue, Units.ONE));
        q.put("predicted_immediate_performance", predictPerformance(Quantities.create(0, Units.DAY)));
        return q;
    }
}

