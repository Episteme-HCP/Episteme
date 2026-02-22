/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.analysis.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Arrays;
import java.util.List;

/**
 * Automated baseline test for Polynomial.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolynomialTest {

    @Test
    public void testClassPresence() {
        assertNotNull(Polynomial.class);
    }

    @Test
    public void testNaiveMultiplication() {
        Polynomial<org.jscience.core.mathematics.numbers.real.Real> p1 = Polynomial.of(org.jscience.core.mathematics.sets.Reals.getInstance(), 
            org.jscience.core.mathematics.numbers.real.Real.of(1), org.jscience.core.mathematics.numbers.real.Real.of(2)); // 1 + 2x
        Polynomial<org.jscience.core.mathematics.numbers.real.Real> p2 = Polynomial.of(org.jscience.core.mathematics.sets.Reals.getInstance(), 
            org.jscience.core.mathematics.numbers.real.Real.of(3), org.jscience.core.mathematics.numbers.real.Real.of(4)); // 3 + 4x
        
        Polynomial<org.jscience.core.mathematics.numbers.real.Real> res = p1.multiply(p2); // (1+2x)(3+4x) = 3 + 10x + 8x^2
        
        assertEquals(3.0, res.getCoefficient(0).doubleValue(), 1e-9);
        assertEquals(10.0, res.getCoefficient(1).doubleValue(), 1e-9);
        assertEquals(8.0, res.getCoefficient(2).doubleValue(), 1e-9);
    }

    @Test
    public void testKaratsubaMultiplication() {
        // Use a size > THRESHOLD (32) to trigger Karatsuba
        int size = 64;
        org.jscience.core.mathematics.numbers.real.Real[] c1 = new org.jscience.core.mathematics.numbers.real.Real[size];
        org.jscience.core.mathematics.numbers.real.Real[] c2 = new org.jscience.core.mathematics.numbers.real.Real[size];
        for(int i=0; i<size; i++) {
            c1[i] = org.jscience.core.mathematics.numbers.real.Real.of(1.0);
            c2[i] = org.jscience.core.mathematics.numbers.real.Real.of(1.0);
        }
        
        Polynomial<org.jscience.core.mathematics.numbers.real.Real> p1 = Polynomial.of(org.jscience.core.mathematics.sets.Reals.getInstance(), c1);
        Polynomial<org.jscience.core.mathematics.numbers.real.Real> p2 = Polynomial.of(org.jscience.core.mathematics.sets.Reals.getInstance(), c2);
        
        Polynomial<org.jscience.core.mathematics.numbers.real.Real> res = p1.multiply(p2);
        
        // Product of (1+x+x^2...+x^63)^2 
        // Coeff of x^k is k+1 for k < size
        assertEquals(1.0, res.getCoefficient(0).doubleValue(), 1e-9);
        assertEquals(2.0, res.getCoefficient(1).doubleValue(), 1e-9);
        assertEquals(64.0, res.getCoefficient(63).doubleValue(), 1e-9);
        assertEquals(1.0, res.getCoefficient(126).doubleValue(), 1e-9);
    }
}

