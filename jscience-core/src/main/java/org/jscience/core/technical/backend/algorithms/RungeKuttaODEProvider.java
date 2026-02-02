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

package org.jscience.core.technical.backend.algorithms;

import org.jscience.core.mathematics.analysis.Function;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Default implementation of ODEProvider using classical Runge-Kutta 4.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RungeKuttaODEProvider implements ODEProvider {

    @Override
    public String getName() {
        return "Runge-Kutta 4 Solver";
    }

    @Override
    public Real[] solve(Function<Real[], Real[]> f, Real t0, Real[] y0, Real tEnd, Real h) {
        // Implementation logic similar to ODESolver.rungeKutta4 but adapted for vector output function
        
        int steps = (int) Math.ceil(tEnd.subtract(t0).divide(h).doubleValue());
        Real[] y = y0.clone();
        Real t = t0;
        int dim = y0.length;
        
        Real half = Real.of(0.5);
        Real sixth = Real.of(1.0/6.0);
        Real two = Real.of(2.0);

        for (int i = 0; i < steps; i++) {
            // Adjust last step if needed? For now assume fixed step is ok, or exact steps.
            // If t + h > tEnd, clamp? ODESolver does ceiling logic.
            
            // k1 = f(t, y)
            Real[] input = new Real[dim + 1];
            input[0] = t;
            System.arraycopy(y, 0, input, 1, dim);
            Real[] k1 = f.evaluate(input);
            
            // k2 = f(t + h/2, y + h*k1/2)
            Real[] inputK2 = new Real[dim + 1];
            inputK2[0] = t.add(h.multiply(half));
            for(int j=0; j<dim; j++) inputK2[j+1] = y[j].add(h.multiply(k1[j]).multiply(half));
            Real[] k2 = f.evaluate(inputK2);
            
            // k3 = f(t + h/2, y + h*k2/2)
            Real[] inputK3 = new Real[dim + 1];
            inputK3[0] = t.add(h.multiply(half));
            for(int j=0; j<dim; j++) inputK3[j+1] = y[j].add(h.multiply(k2[j]).multiply(half));
            Real[] k3 = f.evaluate(inputK3);
            
            // k4 = f(t + h, y + h*k3)
            Real[] inputK4 = new Real[dim + 1];
            inputK4[0] = t.add(h);
            for(int j=0; j<dim; j++) inputK4[j+1] = y[j].add(h.multiply(k3[j]));
            Real[] k4 = f.evaluate(inputK4);
            
            // y_new
            for(int j=0; j<dim; j++) {
                Real sumK = k1[j].add(k2[j].multiply(two)).add(k3[j].multiply(two)).add(k4[j]);
                y[j] = y[j].add(h.multiply(sixth).multiply(sumK));
            }
            
            t = t.add(h);
        }
        
        return y;
    }
}
