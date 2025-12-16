package org.jscience.mathematics.analysis.series;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.analysis.DifferentiableFunction;
import static org.junit.jupiter.api.Assertions.*;

public class TaylorSeriesTest {

    @Test
    public void testTaylorExponential() {
        // f(x) = exp(x), f'(x) = exp(x)
        // Taylor at 0: 1 + x + x^2/2 + ...

        // Mock differentiable function for exp(x)
        // Since we don't have a full symbolic engine, we'll use a simple wrapper that
        // knows its derivative

        class ExpFunction implements DifferentiableFunction<Real, Real> {
            @Override
            public Real evaluate(Real x) {
                return x.exp();
            }

            @Override
            public DifferentiableFunction<Real, Real> differentiate() {
                return this; // derivative of exp is exp
            }

            // Implement other methods if needed by interface...
            // Assuming DifferentiableFunction extends Function
            @Override
            public Real apply(Real t) {
                return evaluate(t);
            }
        }

        PowerSeries taylor = PowerSeries.taylor(new ExpFunction(), Real.ZERO);

        // Check first few terms
        // 0: exp(0)/0! = 1
        assertEquals(1.0, taylor.term(0).doubleValue(), 1e-9);
        // 1: exp(0)/1! * x = x. At x=1, term is 1.
        // Wait, term(k) depends on x in PowerSeries.
        // PowerSeries constructor takes coefficients.
        // term(k) = coeff(k) * x^k.
        // We need to set x for the series to evaluate term(k).

        PowerSeries atOne = taylor.at(Real.ONE);
        assertEquals(1.0, atOne.term(0).doubleValue(), 1e-9); // 1 * 1^0
        assertEquals(1.0, atOne.term(1).doubleValue(), 1e-9); // 1 * 1^1
        assertEquals(0.5, atOne.term(2).doubleValue(), 1e-9); // 0.5 * 1^2

        // Sum should approximate e
        assertEquals(Math.E, atOne.partialSum(10).doubleValue(), 1e-5);
    }
}
