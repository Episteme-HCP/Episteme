/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.mathematics.logic.fuzzy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FuzzyLogicTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testMinimumTNorm() {
        assertEquals(0.3, TNorm.MINIMUM.apply(0.3, 0.7), EPSILON);
        assertEquals(0.5, TNorm.MINIMUM.apply(0.5, 0.8), EPSILON);
    }

    @Test
    public void testProductTNorm() {
        assertEquals(0.21, TNorm.PRODUCT.apply(0.3, 0.7), EPSILON);
        assertEquals(0.4, TNorm.PRODUCT.apply(0.5, 0.8), EPSILON);
    }

    @Test
    public void testLukasiewiczTNorm() {
        assertEquals(0.0, TNorm.LUKASIEWICZ.apply(0.3, 0.7), EPSILON);
        assertEquals(0.3, TNorm.LUKASIEWICZ.apply(0.5, 0.8), EPSILON);
    }

    @Test
    public void testDrasticTNorm() {
        assertEquals(0.3, TNorm.DRASTIC.apply(0.3, 1.0), EPSILON);
        assertEquals(0.0, TNorm.DRASTIC.apply(0.3, 0.7), EPSILON);
    }

    @Test
    public void testTConorm() {
        // S(a,b) = 1 - T(1-a, 1-b)
        double result = TNorm.MINIMUM.applyConorm(0.3, 0.7);
        assertEquals(0.7, result, EPSILON);
    }

    @Test
    public void testTNormProperties() {
        // Commutativity
        assertEquals(TNorm.MINIMUM.apply(0.3, 0.7), TNorm.MINIMUM.apply(0.7, 0.3), EPSILON);

        // Identity
        assertEquals(0.5, TNorm.MINIMUM.apply(0.5, 1.0), EPSILON);
    }
}
