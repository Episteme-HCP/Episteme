package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Solow-Swan Growth Model for long-run economic growth.
 * Y = A * K^alpha * L^(1-alpha)
 */
public final class SolowGrowthModel {

    private SolowGrowthModel() {}

    public record GrowthState(
        int year,
        double capital,
        double labor,
        double output,
        double consumption,
        double investment,
        double capitalPerWorker,
        double outputPerWorker
    ) {}

    /**
     * Simulates growth over time until steady state or N periods.
     * 
     * @param initialK Initial Capital
     * @param initialL Initial Labor
     * @param a Total Factor Productivity (TFP)
     * @param alpha Capital share (0-1)
     * @param s Savings rate (0-1)
     * @param d Depreciation rate (0-1)
     * @param n Labor growth rate (0-1)
     * @param g TFP growth rate (0-1)
     */
    public static List<GrowthState> simulate(
            double initialK, double initialL, double a, double alpha,
            double s, double d, double n, double g, int periods) {
        
        List<GrowthState> history = new ArrayList<>();
        double k = initialK;
        double l = initialL;
        double tfp = a;

        for (int t = 0; t < periods; t++) {
            double y = tfp * Math.pow(k, alpha) * Math.pow(l, 1 - alpha);
            double i = s * y;
            double c = y - i;
            
            history.add(new GrowthState(t, k, l, y, c, i, k/l, y/l));

            // Accumulation equations
            k = k + i - d * k;
            l = l * (1 + n);
            tfp = tfp * (1 + g);
        }

        return history;
    }

    /**
     * Calculates Golden Rule level of capital (where consumption is maximized).
     * k* = (sA / (n + d + g))^(1 / (1 - alpha))
     */
    public static Real calculateSteadyStateCapital(double a, double alpha, double s, 
            double d, double n, double g) {
        double power = 1.0 / (1.0 - alpha);
        double kStar = Math.pow((s * a) / (n + d + g), power);
        return Real.of(kStar);
    }

    /**
     * Convergence speed to steady state.
     * lambda = (1 - alpha)(n + d + g)
     */
    public static Real convergenceSpeed(double alpha, double n, double d, double g) {
        return Real.of((1 - alpha) * (n + d + g));
    }
}
