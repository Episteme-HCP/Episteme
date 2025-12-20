/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.psychology;

/**
 * Represents a psychological behavior pattern.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Behavior {

    public enum Type {
        COGNITIVE, EMOTIONAL, SOCIAL, MOTOR, VERBAL, ADAPTIVE, MALADAPTIVE
    }

    public enum Frequency {
        RARE, OCCASIONAL, FREQUENT, CONSTANT
    }

    private final String name;
    private final String description;
    private final Type type;
    private final Frequency frequency;
    private final double intensity; // 0.0 to 1.0

    public Behavior(String name, String description, Type type, Frequency frequency, double intensity) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.frequency = frequency;
        this.intensity = Math.max(0.0, Math.min(1.0, intensity));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public double getIntensity() {
        return intensity;
    }

    public boolean isAdaptive() {
        return type == Type.ADAPTIVE;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s, intensity=%.2f)", name, type, frequency, intensity);
    }
}
