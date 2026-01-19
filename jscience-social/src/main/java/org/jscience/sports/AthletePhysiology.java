package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulates athlete physiology including fatigue and recovery.
 */
public final class AthletePhysiology {

    private AthletePhysiology() {}

    /**
     * Represents an athlete's physiological state.
     */
    public static class PhysiologicalState {
        private double stamina;          // 0-100
        private double muscularFatigue;  // 0-100
        private double hydration;        // 0-100
        private double glycogenStores;   // 0-100
        private double heartRate;        // bpm

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

        public double getOverallCondition() {
            return (stamina + (100 - muscularFatigue) + hydration + glycogenStores) / 4.0;
        }

        public boolean isExhausted() {
            return stamina < 10 || glycogenStores < 5;
        }

        public boolean isDehydrated() {
            return hydration < 20;
        }
    }

    /**
     * Simulates the effect of physical exertion on an athlete.
     * 
     * @param state Current physiological state.
     * @param intensityPercent Intensity of effort (0-100).
     * @param durationMinutes Duration in minutes.
     * @param ambientTempCelsius Environmental temperature.
     * @return Updated state after exertion.
     */
    public static PhysiologicalState simulateExertion(PhysiologicalState state,
            double intensityPercent, double durationMinutes, double ambientTempCelsius) {
        
        PhysiologicalState newState = new PhysiologicalState();
        
        double intensityFactor = intensityPercent / 100.0;
        double heatFactor = Math.max(1.0, (ambientTempCelsius - 20) / 10.0);
        
        // Stamina depletion
        double staminaLoss = intensityFactor * durationMinutes * 0.5;
        newState.stamina = Math.max(0, state.stamina - staminaLoss);
        
        // Muscular fatigue accumulation
        double fatigueGain = intensityFactor * durationMinutes * 0.3;
        newState.muscularFatigue = Math.min(100, state.muscularFatigue + fatigueGain);
        
        // Hydration loss (increased by heat)
        double hydrationLoss = intensityFactor * durationMinutes * 0.2 * heatFactor;
        newState.hydration = Math.max(0, state.hydration - hydrationLoss);
        
        // Glycogen depletion
        double glycogenLoss = intensityFactor * durationMinutes * 0.4;
        newState.glycogenStores = Math.max(0, state.glycogenStores - glycogenLoss);
        
        // Heart rate response
        newState.heartRate = 60 + (140 * intensityFactor) + (10 * (1 - newState.stamina / 100));
        
        return newState;
    }

    /**
     * Simulates recovery during rest.
     */
    public static PhysiologicalState simulateRecovery(PhysiologicalState state,
            double restMinutes, boolean hydrating, boolean eating) {
        
        PhysiologicalState newState = new PhysiologicalState();
        
        double recoveryFactor = restMinutes / 60.0; // Per hour base
        
        newState.stamina = Math.min(100, state.stamina + recoveryFactor * 20);
        newState.muscularFatigue = Math.max(0, state.muscularFatigue - recoveryFactor * 15);
        newState.hydration = hydrating 
            ? Math.min(100, state.hydration + recoveryFactor * 30)
            : state.hydration;
        newState.glycogenStores = eating
            ? Math.min(100, state.glycogenStores + recoveryFactor * 25)
            : state.glycogenStores;
        newState.heartRate = 60 + (state.heartRate - 60) * Math.exp(-recoveryFactor);
        
        return newState;
    }

    /**
     * Calculates VO2max estimate from heart rate data.
     */
    public static Real estimateVO2Max(double restingHR, double maxHR) {
        // Uth-Sørensen-Overgaard-Pedersen formula
        double vo2max = 15.3 * (maxHR / restingHR);
        return Real.of(vo2max);
    }

    /**
     * Predicts performance degradation based on fatigue.
     */
    public static Real performanceMultiplier(PhysiologicalState state) {
        double condition = state.getOverallCondition();
        double multiplier = 0.5 + (condition / 200.0); // Range: 0.5 to 1.0
        return Real.of(multiplier);
    }
}
