package org.jscience.chemistry.electrochemistry;

/**
 * Electrochemistry calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class NernstEquation {

    /** Faraday constant (C/mol) */
    public static final double F = 96485.33212;

    /** Gas constant R (J/(mol·K)) */
    public static final double R = 8.314462618;

    /** Standard temperature (K) */
    public static final double T_STD = 298.15;

    /**
     * Nernst equation for cell potential.
     * E = E° - (R·T)/(n·F) · ln(Q)
     * 
     * @param E0 Standard cell potential (V)
     * @param n  Number of electrons transferred
     * @param Q  Reaction quotient
     * @param T  Temperature (K)
     * @return Cell potential (V)
     */
    public static double cellPotential(double E0, int n, double Q, double T) {
        return E0 - (R * T) / (n * F) * Math.log(Q);
    }

    /**
     * Simplified Nernst equation at 25°C using log₁₀.
     * E = E° - (0.0592/n) · log₁₀(Q)
     */
    public static double cellPotential25C(double E0, int n, double Q) {
        return E0 - (0.0592 / n) * Math.log10(Q);
    }

    /**
     * Calculates standard cell potential from half-cell potentials.
     * E°_cell = E°_cathode - E°_anode
     */
    public static double standardCellPotential(double E0_cathode, double E0_anode) {
        return E0_cathode - E0_anode;
    }

    /**
     * Calculates Gibbs energy from cell potential.
     * ΔG = -n·F·E
     * 
     * @param n Number of electrons
     * @param E Cell potential (V)
     * @return ΔG in J/mol
     */
    public static double gibbsFromPotential(int n, double E) {
        return -n * F * E;
    }

    /**
     * Faraday's first law of electrolysis.
     * m = (Q · M) / (n · F)
     * 
     * @param Q Charge passed (Coulombs)
     * @param M Molar mass (g/mol)
     * @param n Electrons per ion
     * @return Mass deposited (g)
     */
    public static double massDeposited(double Q, double M, int n) {
        return (Q * M) / (n * F);
    }

    /**
     * Calculates charge required to deposit a given mass.
     */
    public static double chargeRequired(double mass, double M, int n) {
        return (mass * n * F) / M;
    }

    /**
     * Electrode reaction spontaneity check.
     * 
     * @return true if spontaneous (E > 0)
     */
    public static boolean isSpontaneous(double E) {
        return E > 0;
    }

    // --- Standard reduction potentials (V vs SHE) ---

    /** Li⁺/Li */
    public static final double E0_LI = -3.04;

    /** K⁺/K */
    public static final double E0_K = -2.93;

    /** Na⁺/Na */
    public static final double E0_NA = -2.71;

    /** Mg²⁺/Mg */
    public static final double E0_MG = -2.37;

    /** Al³⁺/Al */
    public static final double E0_AL = -1.66;

    /** Zn²⁺/Zn */
    public static final double E0_ZN = -0.76;

    /** Fe²⁺/Fe */
    public static final double E0_FE = -0.44;

    /** H⁺/H₂ (SHE reference) */
    public static final double E0_H = 0.00;

    /** Cu²⁺/Cu */
    public static final double E0_CU = 0.34;

    /** Ag⁺/Ag */
    public static final double E0_AG = 0.80;

    /** Au³⁺/Au */
    public static final double E0_AU = 1.50;

    /** F₂/F⁻ */
    public static final double E0_F2 = 2.87;
}
