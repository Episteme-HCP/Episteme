package org.jscience.mathematics.numbers;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.complex.Complex;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexMathTest {

    @Test
    public void testExpLog() {
        Complex z = Complex.of(0, Math.PI); // i*pi
        Complex expZ = z.exp(); // e^(i*pi) = -1
        assertEquals(-1.0, expZ.real(), 1e-9);
        assertEquals(0.0, expZ.imaginary(), 1e-9);

        Complex one = Complex.ONE;
        Complex logOne = one.log(); // ln(1) = 0
        assertEquals(0.0, logOne.real(), 1e-9);
        assertEquals(0.0, logOne.imaginary(), 1e-9);
    }

    @Test
    public void testTrig() {
        Complex z = Complex.ZERO;
        assertEquals(0.0, z.sin().real(), 1e-9);
        assertEquals(1.0, z.cos().real(), 1e-9);
        assertEquals(0.0, z.tan().real(), 1e-9);
    }

    @Test
    public void testHyperbolic() {
        Complex z = Complex.ZERO;
        assertEquals(0.0, z.sinh().real(), 1e-9);
        assertEquals(1.0, z.cosh().real(), 1e-9);
        assertEquals(0.0, z.tanh().real(), 1e-9);
    }
}
