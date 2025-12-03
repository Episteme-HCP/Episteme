/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.mathematics.optimization.*;
import org.jscience.mathematics.linear.Vector;
import org.jscience.mathematics.number.Real;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class OptimizationBenchmark {

    private Optimizer<Real> optimizer;
    private ObjectiveFunction<Real> func;
    private Vector<Real> start;

    @Setup
    public void setup() {
        optimizer = new GradientDescent<>();

        // Rosenbrock function: f(x,y) = (1-x)^2 + 100(y-x^2)^2
        func = new ObjectiveFunction<Real>() {
            @Override
            public Real value(Vector<Real> point) {
                Real x = point.get(0);
                Real y = point.get(1);
                Real t1 = Real.of(1).subtract(x);
                Real t2 = y.subtract(x.multiply(x));
                return t1.multiply(t1).add(Real.of(100).multiply(t2).multiply(t2));
            }

            @Override
            public Vector<Real> gradient(Vector<Real> point) {
                Real x = point.get(0);
                Real y = point.get(1);

                // dx = -2(1-x) - 400x(y-x^2)
                Real term1 = Real.of(1).subtract(x);
                Real term2 = y.subtract(x.multiply(x));
                Real dx = Real.of(-2).multiply(term1).subtract(Real.of(400).multiply(x).multiply(term2));

                // dy = 200(y-x^2)
                Real dy = Real.of(200).multiply(term2);

                return Vector.dense(new Real[] { dx, dy });
            }
        };

        start = Vector.dense(new Real[] { Real.of(-1.2), Real.of(1.0) });
    }

    @Benchmark
    public void gradientDescent() {
        optimizer.optimize(func, start);
    }
}
