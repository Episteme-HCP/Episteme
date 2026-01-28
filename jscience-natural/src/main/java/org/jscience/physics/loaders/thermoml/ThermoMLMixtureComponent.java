/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.physics.loaders.thermoml;

public class ThermoMLMixtureComponent {
    private int compoundIndex;
    private double moleFraction;

    public int getCompoundIndex() { return compoundIndex; }
    public void setCompoundIndex(int i) { this.compoundIndex = i; }
    public double getMoleFraction() { return moleFraction; }
    public void setMoleFraction(double f) { this.moleFraction = f; }
}
