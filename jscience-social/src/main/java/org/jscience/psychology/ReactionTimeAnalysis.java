package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Laws of human reaction time and motor control.
 */
public final class ReactionTimeAnalysis {

    private ReactionTimeAnalysis() {}

    /**
     * Hick's Law: RT = a + b * log2(n + 1)
     * Models time taken to make a decision as a function of the number of choices.
     * 
     * @param a Base reaction time (simple RT)
     * @param b Processing speed (time per bit)
     * @param n Number of choices
     */
    public static Real hicksLaw(double a, double b, int n) {
        return Real.of(a + b * (Math.log(n + 1) / Math.log(2)));
    }

    /**
     * Fitts's Law: MT = a + b * log2(2D / W)
     * Models time taken to move to a target.
     * 
     * @param d Distance to target
     * @param w Width of target
     */
    public static Real fittsLaw(double a, double b, double d, double w) {
        return Real.of(a + b * (Math.log(2 * d / w) / Math.log(2)));
    }

    /**
     * Simple Reaction Time typical values by age.
     */
    public static Real typicalSimpleRT(int age) {
        // Base approximation: 200ms at age 20, +2ms per year after
        double base = 0.200;
        if (age > 20) {
            base += (age - 20) * 0.002;
        }
        return Real.of(base);
    }

    /**
     * Calculates Index of Difficulty (ID).
     * ID = log2(2D / W)
     */
    public static double indexOfDifficulty(double d, double w) {
        return Math.log(2 * d / w) / Math.log(2);
    }

    /**
     * Accrual of info model (Luce's choice).
     * Probability of choosing i = v_i / sum(v_j)
     */
    public static double[] lucisChoiceProbability(double[] utilities) {
        double sum = Arrays.stream(utilities).sum();
        double[] probs = new double[utilities.length];
        for (int i = 0; i < utilities.length; i++) {
            probs[i] = utilities[i] / sum;
        }
        return probs;
    }
}
