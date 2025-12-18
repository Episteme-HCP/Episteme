package org.jscience.chemistry.polymer;

/**
 * Polymer chemistry calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Polymer {

    private final String name;
    private final String repeatUnit;
    private final double repeatUnitMass; // g/mol
    private final double Mn; // Number-average molecular weight
    private final double Mw; // Weight-average molecular weight

    public Polymer(String name, String repeatUnit, double repeatUnitMass,
            double Mn, double Mw) {
        this.name = name;
        this.repeatUnit = repeatUnit;
        this.repeatUnitMass = repeatUnitMass;
        this.Mn = Mn;
        this.Mw = Mw;
    }

    /**
     * Creates polymer from degree of polymerization.
     */
    public static Polymer fromDP(String name, String repeatUnit,
            double repeatUnitMass, int DP) {
        double M = repeatUnitMass * DP;
        return new Polymer(name, repeatUnit, repeatUnitMass, M, M);
    }

    /**
     * Polydispersity index: PDI = Mw/Mn
     * PDI = 1 for monodisperse, >1 for polydisperse
     */
    public double polydispersityIndex() {
        return Mw / Mn;
    }

    /**
     * Number-average degree of polymerization.
     * DPn = Mn / M0
     */
    public double degreeOfPolymerization() {
        return Mn / repeatUnitMass;
    }

    /**
     * Mark-Houwink equation: intrinsic viscosity.
     * [η] = K · M^a
     * 
     * @param M Molecular weight
     * @param K Mark-Houwink constant (depends on polymer-solvent-temperature)
     * @param a Mark-Houwink exponent (0.5-0.8 for flexible chains)
     * @return Intrinsic viscosity (dL/g)
     */
    public static double intrinsicViscosity(double M, double K, double a) {
        return K * Math.pow(M, a);
    }

    /**
     * End-to-end distance for ideal chain.
     * <R²>^(1/2) = l·sqrt(n) for freely-jointed chain
     * 
     * @param n Number of segments
     * @param l Segment length (Å)
     * @return RMS end-to-end distance (Å)
     */
    public static double endToEndDistance(int n, double l) {
        return l * Math.sqrt(n);
    }

    /**
     * Radius of gyration for ideal chain.
     * Rg = <R²>^(1/2) / sqrt(6)
     */
    public static double radiusOfGyration(int n, double l) {
        return endToEndDistance(n, l) / Math.sqrt(6);
    }

    /**
     * Flory-Huggins interaction parameter estimation.
     * χ = V_s(δ_p - δ_s)² / RT
     * 
     * @param Vs     Molar volume of solvent (cm³/mol)
     * @param deltaP Solubility parameter of polymer (cal/cm³)^0.5
     * @param deltaS Solubility parameter of solvent
     * @param T      Temperature (K)
     * @return Flory-Huggins χ parameter
     */
    public static double floryHugginsParameter(double Vs, double deltaP,
            double deltaS, double T) {
        double R = 1.987; // cal/(mol·K)
        double diff = deltaP - deltaS;
        return Vs * diff * diff / (R * T);
    }

    /**
     * Glass transition temperature estimation (Fox equation for copolymers).
     * 1/Tg = w1/Tg1 + w2/Tg2
     * 
     * @param w1  Weight fraction of component 1
     * @param Tg1 Tg of homopolymer 1 (K)
     * @param Tg2 Tg of homopolymer 2 (K)
     * @return Estimated Tg (K)
     */
    public static double foxEquation(double w1, double Tg1, double Tg2) {
        double w2 = 1 - w1;
        return 1.0 / (w1 / Tg1 + w2 / Tg2);
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRepeatUnit() {
        return repeatUnit;
    }

    public double getRepeatUnitMass() {
        return repeatUnitMass;
    }

    public double getMn() {
        return Mn;
    }

    public double getMw() {
        return Mw;
    }

    /**
     * Generates a representative single-chain Molecule with the specified Degree of
     * Polymerization.
     * <p>
     * Note: Currently generates a simplified carbon backbone structure for
     * visualization.
     * Future versions should parse the 'repeatUnit' string.
     * </p>
     * 
     * @param dp Degree of Polymerization (number of repeat units)
     * @return A Molecule representing a single polymer chain
     */
    public org.jscience.chemistry.Molecule generateMolecule(int dp) {
        org.jscience.chemistry.Molecule mol = new org.jscience.chemistry.Molecule(name + "_DP" + dp);

        // Simple heuristic: Linear chain of Carbon atoms along X-axis
        org.jscience.chemistry.Element carbon = org.jscience.chemistry.PeriodicTable.CARBON;

        org.jscience.chemistry.Atom prev = null;
        for (int i = 0; i < dp; i++) {
            // Place atoms in a zigzag or helix? Simple linear for now.
            // visual: x = i * 1.5, zigzag y
            double x = i * 1.5;
            double y = (i % 2 == 0) ? 0 : 1.0;
            double z = 0;

            // Convert to meters for storage (Atom expects meters in Vector3D usually,
            // but Vector3D constructor is raw. Let's assume Angstroms for viewer logic
            // or convert. Atom.distanceTo uses meters.
            // Let's store in METERS.
            double scale = 1e-10;

            org.jscience.chemistry.Atom atom = new org.jscience.chemistry.Atom(
                    carbon,
                    new org.jscience.mathematics.geometry.Vector3D(x * scale, y * scale, z * scale));

            mol.addAtom(atom);

            if (prev != null) {
                // Add bond
                mol.addBond(new org.jscience.chemistry.Bond(prev, atom, org.jscience.chemistry.Bond.BondOrder.SINGLE));
            }
            prev = atom;
        }

        return mol;
    }

    // --- Common polymers (repeat unit mass in g/mol) ---

    /** Polyethylene (-CH2-CH2-) */
    public static final double PE_REPEAT_MASS = 28.05;

    /** Polypropylene (-CH2-CH(CH3)-) */
    public static final double PP_REPEAT_MASS = 42.08;

    /** Polystyrene (-CH2-CH(C6H5)-) */
    public static final double PS_REPEAT_MASS = 104.15;

    /** PVC (-CH2-CHCl-) */
    public static final double PVC_REPEAT_MASS = 62.50;

    /** PMMA (-CH2-C(CH3)(COOCH3)-) */
    public static final double PMMA_REPEAT_MASS = 100.12;

    /** Nylon 6,6 (-NH-(CH2)6-NH-CO-(CH2)4-CO-) */
    public static final double NYLON66_REPEAT_MASS = 226.32;
}
