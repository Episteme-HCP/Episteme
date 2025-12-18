package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an atomic nucleus with proton/neutron counts.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Nucleus {

    private final int protons; // Z (atomic number)
    private final int neutrons; // N
    private final String symbol;

    // Physical constants
    @SuppressWarnings("unused")
    private static final Real PROTON_MASS = Real.of(1.67262192369e-27); // kg
    @SuppressWarnings("unused")
    private static final Real NEUTRON_MASS = Real.of(1.67492749804e-27); // kg
    @SuppressWarnings("unused")
    private static final Real AMU = Real.of(1.66053906660e-27); // kg
    @SuppressWarnings("unused")
    private static final Real C_SQUARED = Real.of(299792458.0 * 299792458.0); // m^2/s^2

    public Nucleus(int protons, int neutrons, String symbol) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.symbol = symbol;
    }

    /**
     * Mass number: A = Z + N
     */
    public int getMassNumber() {
        return protons + neutrons;
    }

    /**
     * Approximate nuclear radius: $R = r_0 A^{1/3}$ where $r_0 \approx 1.2$ fm.
     */
    public Real getNuclearRadius() {
        Real r0 = Real.of(1.2e-15); // m
        return r0.multiply(Real.of(Math.pow(getMassNumber(), 1.0 / 3.0)));
    }

    /**
     * Semi-empirical binding energy (Weizsäcker formula).
     * $B = a_V A - a_S A^{2/3} - a_C Z(Z-1)/A^{1/3} - a_A (N-Z)^2/A + \delta$
     */
    public Real getBindingEnergy() {
        double A = getMassNumber();
        double Z = protons;
        double N = neutrons;

        // Coefficients in MeV
        double aV = 15.8; // Volume
        double aS = 18.3; // Surface
        double aC = 0.72; // Coulomb
        double aA = 23.2; // Asymmetry
        double aP = 12.0; // Pairing

        double B = aV * A
                - aS * Math.pow(A, 2.0 / 3.0)
                - aC * Z * (Z - 1) / Math.pow(A, 1.0 / 3.0)
                - aA * Math.pow(N - Z, 2) / A;

        // Pairing term
        double delta = 0;
        if ((int) Z % 2 == 0 && (int) N % 2 == 0) {
            delta = aP / Math.sqrt(A);
        } else if ((int) Z % 2 == 1 && (int) N % 2 == 1) {
            delta = -aP / Math.sqrt(A);
        }
        B += delta;

        return Real.of(B); // MeV
    }

    /**
     * Binding energy per nucleon: B/A in MeV.
     */
    public Real getBindingEnergyPerNucleon() {
        return getBindingEnergy().divide(Real.of(getMassNumber()));
    }

    // --- Common isotopes ---
    public static final Nucleus HYDROGEN_1 = new Nucleus(1, 0, "H");
    public static final Nucleus DEUTERIUM = new Nucleus(1, 1, "D");
    public static final Nucleus TRITIUM = new Nucleus(1, 2, "T");
    public static final Nucleus HELIUM_4 = new Nucleus(2, 2, "He");
    public static final Nucleus CARBON_12 = new Nucleus(6, 6, "C");
    public static final Nucleus IRON_56 = new Nucleus(26, 30, "Fe");
    public static final Nucleus URANIUM_235 = new Nucleus(92, 143, "U");
    public static final Nucleus URANIUM_238 = new Nucleus(92, 146, "U");

    // --- Accessors ---
    public int getProtons() {
        return protons;
    }

    public int getNeutrons() {
        return neutrons;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol + "-" + getMassNumber();
    }
}
