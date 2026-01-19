package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Advanced performance indicators for endurance and strength.
 */
public final class PerformanceModels {

    private PerformanceModels() {}

    /**
     * VO2 Max estimation methods.
     */
    public static final class VO2Max {
        
        /**
         * Cooper Test: VO2Max = (distance2400m - 504.9) / 44.73
         * @param distanceMeters Distance in 12 minutes
         */
        public static Real fromCooperTest(double distanceMeters) {
            return Real.of((distanceMeters - 504.9) / 44.73);
        }

        /**
         * Uth-SÃƒÂ¸rensen: VO2Max = 15.3 * (HRmax / HRrest)
         */
        public static Real fromHeartRates(int hrMax, int hrRest) {
            return Real.of(15.3 * ((double) hrMax / hrRest));
        }

        /**
         * Rockport Walk Test: Based on time, age, weight, and HR.
         */
        public static Real rockportWalk(double weightLb, int age, boolean male, 
                double timeMinutes, int finalHR) {
            double gender = male ? 1 : 0;
            double vo2 = 132.853 - (0.0769 * weightLb) - (0.3877 * age) + 
                         (6.315 * gender) - (3.2649 * timeMinutes) - (0.1565 * finalHR);
            return Real.of(vo2);
        }
    }

    /**
     * Critical Power (CP) and W' (anaerobic capacity).
     * P = CP + W'/t
     */
    public static final class CriticalPower {
        
        /**
         * Solves for CP and W' given two exhaustive efforts.
         * Effort: [time_seconds, power_watts]
         */
        public static Real[] solveFromTwoPoints(double[] effort1, double[] effort2) {
            double t1 = effort1[0], p1 = effort1[1];
            double t2 = effort2[0], p2 = effort2[1];
            
            // W' = (P1 - P2) / (1/T1 - 1/T2)
            double wPrime = (p1 - p2) / (1.0/t1 - 1.0/t2);
            double cp = p1 - wPrime / t1;
            
            return new Real[]{Real.of(cp), Real.of(wPrime)};
        }

        /**
         * Time to exhaustion at given power.
         */
        public static Real timeToExhaustion(double power, double cp, double wPrime) {
            if (power <= cp) return Real.of(Double.POSITIVE_INFINITY);
            return Real.of(wPrime / (power - cp));
        }
    }

    /**
     * One Rep Max (1RM) estimations.
     */
    public static final class Strength {
        
        /**
         * Epley Formula: 1RM = weight * (1 + reps/30)
         */
        public static Real epley1RM(double weight, int reps) {
            if (reps == 1) return Real.of(weight);
            return Real.of(weight * (1 + reps / 30.0));
        }

        /**
         * Brzycki Formula: 1RM = weight / (1.0278 - 0.0278 * reps)
         */
        public static Real brzycki1RM(double weight, int reps) {
            return Real.of(weight / (1.0278 - 0.0278 * reps));
        }
    }
}
