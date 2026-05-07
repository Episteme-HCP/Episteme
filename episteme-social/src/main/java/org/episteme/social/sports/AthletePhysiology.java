/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sports;

import java.io.Serializable;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Simulates athlete physiology, modeling dynamic states such as fatigue, hydration, and recovery.
 * Provides methods to simulate exertion and recovery cycles based on intensity and environmental conditions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class AthletePhysiology {

    private AthletePhysiology() {}

    /**
     * Represents a snapshot of an athlete's physiological condition.
     */
    public static class PhysiologicalState implements Serializable {
        private static final long serialVersionUID = 1L;

        private double stamina;          // 0-100, where 100 is full energy
        private double muscularFatigue;  // 0-100, where 0 is fresh
        private double hydration;        // 0-100, where 100 is fully hydrated
        private double glycogenStores;   // 0-100, representing energy reserves
        private double heartRate;        // Beats per minute (bpm)

        /** Initializes a state representing a healthy, resting athlete. */
        public PhysiologicalState() {
            this.stamina = 100.0;
            this.muscularFatigue = 0.0;
            this.hydration = 100.0;
            this.glycogenStores = 100.0;
            this.heartRate = 60.0;
        }

        public double getStamina() { return stamina; }
        public double getMuscularFatigue() { return muscularFatigue; }
        public double getHydration() { return hydration; }
        public double getGlycogenStores() { return glycogenStores; }
        public double getHeartRate() { return heartRate; }

        /**
         * Calculates an aggregate physical condition score.
         * @return average score (0-100)
         */
        public double getOverallCondition() {
            return (stamina + (100 - muscularFatigue) + hydration + glycogenStores) / 4.0;
        }

        /**
         * Checks if the athlete is critically exhausted.
         * @return true if stamina or glycogen is critically low
         */
        public boolean isExhausted() {
            return stamina < 10 || glycogenStores < 5;
        }

        /**
         * Checks if the athlete is dehydrated.
         * @return true if hydration is below 20%
         */
        public boolean isDehydrated() {
            return hydration < 20;
        }
    }

    /**
     * Simulates the physiological impact of a period of physical exertion.
     * 
     * @param state              Current state of the athlete
     * @param intensityPercent   Intensity of the activity (0-100%)
     * @param durationMinutes    Duration of the activity in minutes
     * @param ambientTempCelsius Environmental temperature in Celsius
     * @return A new PhysiologicalState reflecting the post-exertion condition
     */
    public static PhysiologicalState simulateExertion(PhysiologicalState state,
            double intensityPercent, double durationMinutes, double ambientTempCelsius) {
        
        PhysiologicalState newState = new PhysiologicalState();
        
        double intensityFactor = intensityPercent / 100.0;
        // Heat stress factor: increases above 20C
        double heatFactor = Math.max(1.0, (ambientTempCelsius - 20) / 10.0);
        
        // Stamina depletion: faster with intensity
        double staminaLoss = intensityFactor * durationMinutes * 0.5;
        newState.stamina = Math.max(0, state.stamina - staminaLoss);
        
        // Muscular fatigue accumulation
        double fatigueGain = intensityFactor * durationMinutes * 0.3;
        newState.muscularFatigue = Math.min(100, state.muscularFatigue + fatigueGain);
        
        // Hydration loss: driven by intensity and heat
        double hydrationLoss = intensityFactor * durationMinutes * 0.2 * heatFactor;
        newState.hydration = Math.max(0, state.hydration - hydrationLoss);
        
        // Glycogen depletion
        double glycogenLoss = intensityFactor * durationMinutes * 0.4;
        newState.glycogenStores = Math.max(0, state.glycogenStores - glycogenLoss);
        
        // Heart rate response: simplified linear model based on intensity and fatigue
        // Max HR approx 200, Resting 60.
        // As stamina drops, HR for same intensity increases (cardiac drift)
        newState.heartRate = 60 + (140 * intensityFactor) + (10 * (1 - newState.stamina / 100));
        
        return newState;
    }

    /**
     * Simulates physiological recovery during a rest period.
     * 
     * @param state       Current state (presumably fatigued)
     * @param restMinutes Duration of rest
     * @param hydrating   True if the athlete is consuming fluids
     * @param eating      True if the athlete is consuming carbohydrates
     * @return A new PhysiologicalState reflecting recovery
     */
    public static PhysiologicalState simulateRecovery(PhysiologicalState state,
            double restMinutes, boolean hydrating, boolean eating) {
        
        PhysiologicalState newState = new PhysiologicalState();
        
        double recoveryFactor = restMinutes / 60.0; // Normalized to hours
        
        newState.stamina = Math.min(100, state.stamina + recoveryFactor * 20);
        newState.muscularFatigue = Math.max(0, state.muscularFatigue - recoveryFactor * 15);
        
        // Hydration recovery
        newState.hydration = hydrating 
            ? Math.min(100, state.hydration + recoveryFactor * 30)
            : state.hydration; // No passive hydration gain without drinking
            
        // Glycogen replenishment
        newState.glycogenStores = eating
            ? Math.min(100, state.glycogenStores + recoveryFactor * 25)
            : state.glycogenStores;
            
        // Heart rate return to resting
        newState.heartRate = 60 + (state.heartRate - 60) * Math.exp(-recoveryFactor * 2); 
        // Decay to resting HR
        
        return newState;
    }

    /**
     * Estimates VO2max (maximum oxygen uptake) using the Uth-SÃ¸rensen-Overgaard-Pedersen formula.
     * Formula: VO2max = 15.3 * (HRmax / HRrest)
     * 
     * @param restingHR Resting Heart Rate (bpm)
     * @param maxHR     Maximum Heart Rate (bpm)
     * @return Estimated VO2max in mL/(kgÂ·min)
     */
    public static Real estimateVO2Max(double restingHR, double maxHR) {
        if (restingHR <= 0) return Real.ZERO;
        double vo2max = 15.3 * (maxHR / restingHR);
        return Real.of(vo2max);
    }

    /**
     * Calculates a performance multiplier based on physiological state.
     * Used to scale athletic performance in simulations (e.g., speed or accuracy reduction).
     * 
     * @param state The athlete's current state
     * @return Multiplier (0.5 to 1.0), where 1.0 is peak performance
     */
    public static Real performanceMultiplier(PhysiologicalState state) {
        double condition = state.getOverallCondition(); // 0-100
        // Map 0-100 (condition) to 0.5-1.0 (multiplier)
        double multiplier = 0.5 + (condition / 200.0);
        return Real.of(multiplier);
    }
}

