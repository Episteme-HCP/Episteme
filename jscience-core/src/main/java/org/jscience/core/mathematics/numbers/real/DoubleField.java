/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.numbers.real;

import org.jscience.core.mathematics.structures.rings.Field;

/**
 * Field implementation for primitive Double wrappers.
 * Used for high-performance native interoperability.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class DoubleField implements Field<Double> {

    public static final DoubleField INSTANCE = new DoubleField();

    private DoubleField() {}

    @Override public boolean contains(Double element) { return element != null; }
    @Override public boolean isEmpty() { return false; }
    @Override public String description() { return "Double Field (64-bit float)"; }

    @Override public Double add(Double a, Double b) { return a + b; }
    @Override public Double multiply(Double a, Double b) { return a * b; }
    @Override public Double zero() { return 0.0; }
    @Override public Double one() { return 1.0; }
    @Override public Double negate(Double element) { return -element; }
    @Override public Double inverse(Double element) { return 1.0 / element; }
    @Override public int characteristic() { return 0; }
    @Override public boolean isMultiplicationCommutative() { return true; }
    @Override public Double operate(Double a, Double b) { return a + b; }
}
