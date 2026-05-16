/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.chemistry.loaders.animl;

public class AnIMLPeak {
    private double position;
    private double intensity;
    private double width;
    private String assignment;

    public double getPosition() { return position; }
    public void setPosition(double position) { this.position = position; }

    public double getIntensity() { return intensity; }
    public void setIntensity(double intensity) { this.intensity = intensity; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public String getAssignment() { return assignment; }
    public void setAssignment(String assignment) { this.assignment = assignment; }
}

