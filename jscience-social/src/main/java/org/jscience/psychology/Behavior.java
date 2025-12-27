/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a psychological behavior pattern.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
    private final Real intensity; // 0.0 to 1.0

    public Behavior(String name, String description, Type type, Frequency frequency, Real intensity) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.frequency = frequency;
        this.intensity = intensity.max(Real.ZERO).min(Real.ONE);
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

    public Real getIntensity() {
        return intensity;
    }

    public boolean isAdaptive() {
        return type == Type.ADAPTIVE;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s, intensity=%.2f)", name, type, frequency, intensity.doubleValue());
    }
}
