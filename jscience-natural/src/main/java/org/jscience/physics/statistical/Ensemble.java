package org.jscience.physics.statistical;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an ensemble of particles in statistical mechanics.
 * <p>
 * Supports microcanonical (NVE), canonical (NVT), and grand canonical (μVT)
 * ensembles.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
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
