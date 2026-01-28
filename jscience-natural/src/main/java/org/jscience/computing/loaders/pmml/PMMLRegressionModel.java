/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.computing.loaders.pmml;

import java.util.ArrayList;
import java.util.List;

public class PMMLRegressionModel extends PMMLModel {
    private PMMLRegressionTable regressionTable;

    public PMMLRegressionTable getRegressionTable() { return regressionTable; }
    public void setRegressionTable(PMMLRegressionTable table) { this.regressionTable = table; }
}

class PMMLRegressionTable {
    private double intercept;
    private final List<PMMLNumericPredictor> numericPredictors = new ArrayList<>();

    public double getIntercept() { return intercept; }
    public void setIntercept(double i) { this.intercept = i; }
    public List<PMMLNumericPredictor> getNumericPredictors() { return numericPredictors; }
    public void addNumericPredictor(PMMLNumericPredictor p) { numericPredictors.add(p); }
}

class PMMLNumericPredictor {
    private String name;
    private double coefficient;
    private double exponent = 1.0;

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public double getCoefficient() { return coefficient; }
    public void setCoefficient(double c) { this.coefficient = c; }
    public double getExponent() { return exponent; }
    public void setExponent(double e) { this.exponent = e; }
}
