package org.jscience.mathematics.optimization;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.number.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OptimizerTest {

    @Test
    public void testLinearProgramming() {
        // Maximize Z = 3x + 4y
        // Subject to:
        // x + 2y <= 14
        // 3x - y <= 0
        // x - y <= 2
        // x, y >= 0

        Real[] c = { Real.of(3), Real.of(4) };
        Real[][] A = {
                { Real.of(1), Real.of(2) },
                { Real.of(3), Real.of(-1) },
                { Real.of(1), Real.of(-1) }
        };
        Real[] b = { Real.of(14), Real.of(0), Real.of(2) };

        Real[] result = Optimizer.linearProgramming(c, A, b);

        // Expected solution: x = 2, y = 6 (Z = 6 + 24 = 30)
        // Check constraints
        // 2 + 12 = 14 <= 14 (Binding)
        // 6 - 6 = 0 <= 0 (Binding)
        // 2 - 6 = -4 <= 2

        assertEquals(2.0, result[0].doubleValue(), 1e-6);
        assertEquals(6.0, result[1].doubleValue(), 1e-6);
    }

    @Test
    public void testNelderMeadMultidimensional() {
        // Minimize Rosenbrock function: f(x,y) = (1-x)^2 + 100(y-x^2)^2
        // Minimum at (1,1)

        Function<Real[], Real> rosenbrock = (x) -> {
            double val = Math.pow(1 - x[0].doubleValue(), 2)
                    + 100 * Math.pow(x[1].doubleValue() - x[0].doubleValue() * x[0].doubleValue(), 2);
            return Real.of(val);
        };

        Real[][] initialSimplex = {
                { Real.of(0), Real.of(0) },
                { Real.of(1.2), Real.of(0) },
                { Real.of(0), Real.of(0.8) }
        };

        Real[] result = Optimizer.nelderMead(rosenbrock, initialSimplex, Real.of(1e-5), 1000);

        assertEquals(1.0, result[0].doubleValue(), 1e-2);
        assertEquals(1.0, result[1].doubleValue(), 1e-2);
    }
}
