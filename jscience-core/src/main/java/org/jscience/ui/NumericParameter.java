/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui;

import java.util.function.Consumer;

/**
 * Metadata for a numeric scientific parameter with bounds.
 */
public class NumericParameter extends Parameter<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumericParameter(String name, String description, double min, double max, double step, double defaultValue,
            Consumer<Double> onValueChange) {
        super(name, description, defaultValue, onValueChange);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }
}
