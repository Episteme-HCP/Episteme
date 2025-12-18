package org.jscience.mathematics.sequences;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import static org.junit.jupiter.api.Assertions.*;

public class SequenceTest {

    @Test
    public void testFibonacci() {
        FibonacciSequence fib = new FibonacciSequence();
        assertEquals(0, fib.get(0).longValue());
        assertEquals(1, fib.get(1).longValue());
        assertEquals(1, fib.get(2).longValue());
        assertEquals(2, fib.get(3).longValue());
        assertEquals(3, fib.get(4).longValue());
        assertEquals(5, fib.get(5).longValue());
        assertEquals(8, fib.get(6).longValue());
    }

    @Test
    public void testHarmonic() {
        HarmonicSeries harmonic = new HarmonicSeries();
        // H_1 = 1
        assertEquals(1.0, harmonic.get(1).doubleValue(), 1e-9);
        // H_2 = 1 + 1/2 = 1.5
        assertEquals(1.5, harmonic.get(2).doubleValue(), 1e-9);

        assertFalse(harmonic.isConvergent());
    }

    @Test
    public void testGeometric() {
        // 1 + 0.5 + 0.25 + ... -> 2
        GeometricSeries geo = new GeometricSeries(Real.ONE, Real.of(0.5));
        assertTrue(geo.isConvergent());
        assertEquals(2.0, geo.limit().doubleValue(), 1e-9);

        // Partial sum n=1: 1 + 0.5 = 1.5
        assertEquals(1.5, geo.get(1).doubleValue(), 1e-9);
    }
}
