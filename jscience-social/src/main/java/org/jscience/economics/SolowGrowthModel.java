/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Solow-Swan Growth Model for long-run economic growth.
 * Defines the production function as Y = A * K^alpha * L^(1-alpha).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public final class SolowGrowthModel {

    private SolowGrowthModel() {}

    /**
     * Represents the state of the economy at a given time point in the growth path.
     * @param year the time period
     * @param capital total capital stock (K)
     * @param labor total labor force (L)
     * @param output total output (Y)
     * @param consumption total consumption (C)
     * @param investment total investment (I)
     * @param capitalPerWorker capital per worker (k)
     * @param outputPerWorker output per worker (y)
     */
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
     * Simulates growth over time until N periods.
     * 
     * @param initialK Initial Capital
     * @param initialL Initial Labor
     * @param a Total Factor Productivity (TFP)
     * @param alpha Capital share (0 to 1)
     * @param s Savings rate (0 to 1)
     * @param d Depreciation rate (0 to 1)
     * @param n Labor growth rate (0 to 1)
     * @param g TFP growth rate (0 to 1)
     * @param periods number of periods to simulate
     * @return a list of growth states over the simulation periods
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
     * <p>k* = (sA / (n + d + g))^(1 / (1 - alpha))</p>
     *
     * @param a Total Factor Productivity
     * @param alpha Capital share
     * @param s Savings rate
     * @param d Depreciation rate
     * @param n Labor growth rate
     * @param g TFP growth rate
     * @return steady state capital per effective worker
     */
    public static Real calculateSteadyStateCapital(double a, double alpha, double s, 
            double d, double n, double g) {
        double power = 1.0 / (1.0 - alpha);
        double kStar = Math.pow((s * a) / (n + d + g), power);
        return Real.of(kStar);
    }

    /**
     * Calculates convergence speed to steady state.
     * <p>lambda = (1 - alpha)(n + d + g)</p>
     * 
     * @param alpha Capital share
     * @param n Labor growth rate
     * @param d Depreciation rate
     * @param g TFP growth rate
     * @return the convergence rate
     */
    public static Real convergenceSpeed(double alpha, double n, double d, double g) {
        return Real.of((1 - alpha) * (n + d + g));
    }
}
