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
package org.jscience.physics.statistical;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an ensemble of particles in statistical mechanics.
 * <p>
 * Supports microcanonical (NVE), canonical (NVT), and grand canonical (μVT)
 * ensembles.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Ensemble {

    public enum Type {
        MICROCANONICAL, // Fixed N, V, E
        CANONICAL, // Fixed N, V, T
        GRAND_CANONICAL // Fixed μ, V, T
    }

    private final Type type;
    private final int particleCount; // N
    private final Real volume; // V
    private final Real temperature; // T (if applicable)
    private final Real totalEnergy; // E (if applicable)
    private final Real chemicalPotential; // μ (if applicable)

    private Ensemble(Type type, int particleCount, Real volume, Real temperature,
            Real totalEnergy, Real chemicalPotential) {
        this.type = type;
        this.particleCount = particleCount;
        this.volume = volume;
        this.temperature = temperature;
        this.totalEnergy = totalEnergy;
        this.chemicalPotential = chemicalPotential;
    }

    public static Ensemble microcanonical(int N, Real V, Real E) {
        return new Ensemble(Type.MICROCANONICAL, N, V, null, E, null);
    }

    public static Ensemble canonical(int N, Real V, Real T) {
        return new Ensemble(Type.CANONICAL, N, V, T, null, null);
    }

    public static Ensemble grandCanonical(Real mu, Real V, Real T) {
        return new Ensemble(Type.GRAND_CANONICAL, -1, V, T, null, mu);
    }

    // --- Accessors ---
    public Type getType() {
        return type;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public Real getVolume() {
        return volume;
    }

    public Real getTemperature() {
        return temperature;
    }

    public Real getTotalEnergy() {
        return totalEnergy;
    }

    public Real getChemicalPotential() {
        return chemicalPotential;
    }
}
