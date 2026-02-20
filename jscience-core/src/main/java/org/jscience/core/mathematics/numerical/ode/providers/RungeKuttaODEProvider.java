/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.numerical.ode.providers;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numerical.ode.ODEProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.util.function.BiFunction;

/**
 * Implementation of ODEProvider using classical Runge-Kutta 4 (RK4).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class RungeKuttaODEProvider implements ODEProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public double[] solve(BiFunction<Double, double[], double[]> f, double[] y0, double t0, double t1, int steps) {
        int dim = y0.length;
        double[] y = y0.clone();
        double t = t0;
        double h = (t1 - t0) / steps;

        for (int i = 0; i < steps; i++) {
            double[] k1 = f.apply(t, y);
            
            double[] yk2 = new double[dim];
            for (int j = 0; j < dim; j++) yk2[j] = y[j] + h * k1[j] * 0.5;
            double[] k2 = f.apply(t + h * 0.5, yk2);
            
            double[] yk3 = new double[dim];
            for (int j = 0; j < dim; j++) yk3[j] = y[j] + h * k2[j] * 0.5;
            double[] k3 = f.apply(t + h * 0.5, yk3);
            
            double[] yk4 = new double[dim];
            for (int j = 0; j < dim; j++) yk4[j] = y[j] + h * k3[j];
            double[] k4 = f.apply(t + h, yk4);
            
            for (int j = 0; j < dim; j++) {
                y[j] += (h / 6.0) * (k1[j] + 2.0 * k2[j] + 2.0 * k3[j] + k4[j]);
            }
            t += h;
        }
        return y;
    }

    @Override
    public Real[] solveReal(BiFunction<Real, Real[], Real[]> f, Real[] y0, Real t0, Real t1, int steps) {
        int dim = y0.length;
        Real[] y = y0.clone();
        Real t = t0;
        Real h = t1.subtract(t0).divide(Real.of(steps));
        
        Real half = Real.of(0.5);
        Real sixth = Real.of(1.0/6.0);
        Real two = Real.of(2.0);

        for (int i = 0; i < steps; i++) {
            Real[] k1 = f.apply(t, y);
            
            Real[] yk2 = new Real[dim];
            for (int j = 0; j < dim; j++) yk2[j] = y[j].add(h.multiply(k1[j]).multiply(half));
            Real[] k2 = f.apply(t.add(h.multiply(half)), yk2);
            
            Real[] yk3 = new Real[dim];
            for (int j = 0; j < dim; j++) yk3[j] = y[j].add(h.multiply(k2[j]).multiply(half));
            Real[] k3 = f.apply(t.add(h.multiply(half)), yk3);
            
            Real[] yk4 = new Real[dim];
            for (int j = 0; j < dim; j++) yk4[j] = y[j].add(h.multiply(k3[j]));
            Real[] k4 = f.apply(t.add(h), yk4);
            
            for (int j = 0; j < dim; j++) {
                Real sumK = k1[j].add(k2[j].multiply(two)).add(k3[j].multiply(two)).add(k4[j]);
                y[j] = y[j].add(h.multiply(sixth).multiply(sumK));
            }
            t = t.add(h);
        }
        return y;
    }
}
