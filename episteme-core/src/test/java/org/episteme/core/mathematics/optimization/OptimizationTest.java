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

package org.episteme.core.mathematics.optimization;

import org.episteme.core.mathematics.analysis.Function;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.optimization.swarm.PSO;
import org.episteme.core.mathematics.optimization.swarm.ACO;
import org.episteme.core.mathematics.optimization.evolutionary.NSGA2;
import org.episteme.core.mathematics.optimization.evolutionary.MultiobjectiveSolution;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Optimization algorithms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OptimizationTest {

    @Test
    public void testGradientDescent() {
        // Function: f(x) = x^2
        // Gradient: f'(x) = 2x
        // Minimum at x = 0

        Function<Real, Real> f = new Function<Real, Real>() {
            @Override
            public Real evaluate(Real x) {
                return x.multiply(x);
            }
        };

        Function<Real, Real> gradient = new Function<Real, Real>() {
            @Override
            public Real evaluate(Real x) {
                return x.multiply(Real.of(2));
            }
        };

        Real start = Real.of(10); // Start at x=10
        Real learningRate = Real.of(0.1);
        Real tolerance = Real.of(0.001);
        int maxIterations = 100;

        Real result = org.episteme.core.mathematics.optimization.Optimizer.gradientDescent(f, gradient, start, learningRate,
                tolerance, maxIterations);

        // Should be close to 0
        assertTrue(result.abs().compareTo(Real.of(0.1)) < 0);
    }

    @Test
    public void testPSO() {
        // Sphere function: minimize sum(x^2)
        // We maximize, so we use -sum(x^2)
        PSO pso = new PSO(30, 2, -10, 10, vars -> {
            double sum = 0;
            for (double v : vars) sum += v * v;
            return -sum;
        });

        pso.solve(100);
        assertTrue(pso.getBestFitness() > -0.1, "PSO failed to converge on sphere function. Best fitness: " + pso.getBestFitness());
    }

    @Test
    public void testACO() {
        // 4 nodes TSP in a square
        double[][] distances = {
            {0, 1, 1.414, 1},
            {1, 0, 1, 1.414},
            {1.414, 1, 0, 1},
            {1, 1.414, 1, 0}
        };

        ACO aco = new ACO(4, distances, 10);
        for(int i=0; i<50; i++) aco.step();

        assertEquals(4.0, aco.getBestTourLength(), 0.1, "ACO failed to find optimal TSP tour");
    }

    @Test
    public void testNSGA2() {
        // Schaffer function N.1: minimize f1=x^2, f2=(x-2)^2
        NSGA2 nsga2 = new NSGA2(50, 1, 2, (vars, objs) -> {
            double x = vars[0] * 4 - 1; // Map [0,1] to [-1, 3]
            objs[0] = x * x;
            objs[1] = (x - 2) * (x - 2);
        });

        for (int i = 0; i < 50; i++) nsga2.evolve();

        List<MultiobjectiveSolution> pop = nsga2.getPopulation();
        assertNotNull(pop);
        // Verify diversity: some solutions should be better in f1, others in f2
        boolean hasF1Better = false;
        boolean hasF2Better = false;
        for (MultiobjectiveSolution sol : pop) {
            if (sol.getObjectives()[0] < 0.5) hasF1Better = true;
            if (sol.getObjectives()[1] < 0.5) hasF2Better = true;
        }
        assertTrue(hasF1Better && hasF2Better, "NSGA2 failed to find diverse Pareto front");
    }
}


