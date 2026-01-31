/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.computing.ml;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a linear regression model.
 */
@Persistent
public class RegressionModel extends Model {
    private static final long serialVersionUID = 1L;

    @Attribute
    private double intercept;

    @Attribute
    private final Map<String, Double> coefficients = new HashMap<>();

    public RegressionModel() {
    }

    public RegressionModel(String name) {
        super(name);
    }

    public double getIntercept() {
        return intercept;
    }

    public void setIntercept(double intercept) {
        this.intercept = intercept;
    }

    public void addCoefficient(String name, double value) {
        coefficients.put(name, value);
    }

    public Map<String, Double> getCoefficients() {
        return coefficients;
    }
}

