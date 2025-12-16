package org.jscience.chemistry.computational;

/**
 * Molecular mechanics force field calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ForceField {

    /**
     * Lennard-Jones 12-6 potential for van der Waals interactions.
     * V(r) = ε * [(σ/r)^12 - 2*(σ/r)^6]
     * 
     * @param r       Distance between atoms
     * @param epsilon Well depth (energy at minimum)
     * @param sigma   Distance at which V = 0
     * @return Potential energy
     */
    public static double lennardJones(double r, double epsilon, double sigma) {
        double ratio = sigma / r;
        double r6 = Math.pow(ratio, 6);
        double r12 = r6 * r6;
        return epsilon * (r12 - 2 * r6);
    }

    /**
     * Lennard-Jones force (negative derivative of potential).
     * F(r) = 12ε/r * [(σ/r)^12 - (σ/r)^6]
     */
    public static double lennardJonesForce(double r, double epsilon, double sigma) {
        double ratio = sigma / r;
        double r6 = Math.pow(ratio, 6);
        double r12 = r6 * r6;
        return 12 * epsilon / r * (r12 - r6);
    }

    /**
     * Harmonic bond stretching potential.
     * V(r) = 0.5 * k * (r - r0)²
     * 
     * @param r  Current bond length
     * @param r0 Equilibrium bond length
     * @param k  Force constant
     * @return Potential energy
     */
    public static double bondStretch(double r, double r0, double k) {
        double dr = r - r0;
        return 0.5 * k * dr * dr;
    }

    /**
     * Harmonic angle bending potential.
     * V(θ) = 0.5 * k * (θ - θ0)²
     * 
     * @param theta  Current angle (radians)
     * @param theta0 Equilibrium angle (radians)
     * @param k      Force constant
     * @return Potential energy
     */
    public static double angleBend(double theta, double theta0, double k) {
        double dTheta = theta - theta0;
        return 0.5 * k * dTheta * dTheta;
    }

    /**
     * Torsional (dihedral) potential.
     * V(φ) = V_n/2 * [1 + cos(n*φ - γ)]
     * 
     * @param phi   Dihedral angle (radians)
     * @param Vn    Barrier height
     * @param n     Periodicity
     * @param gamma Phase offset (radians)
     * @return Potential energy
     */
    public static double torsion(double phi, double Vn, int n, double gamma) {
        return Vn / 2.0 * (1 + Math.cos(n * phi - gamma));
    }

    /**
     * Coulomb electrostatic interaction.
     * V(r) = (q1 * q2) / (4πε0 * r)
     * 
     * @param q1 Charge 1 (in elementary charges)
     * @param q2 Charge 2 (in elementary charges)
     * @param r  Distance (Angstroms)
     * @return Energy in kcal/mol (using dielectric constant = 1)
     */
    public static double coulomb(double q1, double q2, double r) {
        // 332.06 converts to kcal/mol when charges in e and r in Angstroms
        return 332.06 * q1 * q2 / r;
    }

    /**
     * Morse potential for bond stretching (more accurate than harmonic).
     * V(r) = D_e * [1 - exp(-a*(r-r_e))]²
     * 
     * @param r  Current bond length
     * @param re Equilibrium bond length
     * @param De Dissociation energy
     * @param a  Width parameter = sqrt(k_e / 2*D_e)
     * @return Potential energy
     */
    public static double morse(double r, double re, double De, double a) {
        double exp = Math.exp(-a * (r - re));
        return De * Math.pow(1 - exp, 2);
    }

    // --- Common parameters (Angstroms, kcal/mol) ---

    /** LJ parameters for C-C interaction */
    public static final double CC_EPSILON = 0.066;
    public static final double CC_SIGMA = 3.4;

    /** LJ parameters for O-O interaction */
    public static final double OO_EPSILON = 0.152;
    public static final double OO_SIGMA = 3.12;

    /** C-C single bond equilibrium length (Å) */
    public static final double CC_BOND_LENGTH = 1.54;

    /** C=C double bond equilibrium length (Å) */
    public static final double CC_DOUBLE_LENGTH = 1.34;

    /** C-H bond equilibrium length (Å) */
    public static final double CH_BOND_LENGTH = 1.09;

    /**
     * Calculates total potential energy of a molecule.
     * Includes Bond stretching and Non-bonded interactions (LJ only for now).
     * 
     * @param molecule The molecule
     * @return Total potential energy in kcal/mol
     */
    public static double calculatePotentialEnergy(org.jscience.chemistry.Molecule molecule) {
        double totalEnergy = 0.0;

        // 1. Bond Stretching
        for (org.jscience.chemistry.Bond bond : molecule.getBonds()) {
            double rMeter = bond.getLength().to(org.jscience.measure.Units.METER).getValue().doubleValue();
            double rAngstrom = rMeter * 1e10;

            // Heuristic parameter selection
            double r0 = CC_BOND_LENGTH;
            // Simple check for bond type could go here, for now assumes generic single bond
            // length
            if (bond.getOrder() == org.jscience.chemistry.Bond.BondOrder.DOUBLE)
                r0 = CC_DOUBLE_LENGTH;

            // k usually ~300-500 kcal/mol/A^2
            totalEnergy += bondStretch(rAngstrom, r0, 300.0);
        }

        // 2. Non-bonded (LJ)
        // Naive N^2 loop
        java.util.List<org.jscience.chemistry.Atom> atoms = molecule.getAtoms();
        for (int i = 0; i < atoms.size(); i++) {
            for (int j = i + 1; j < atoms.size(); j++) {
                org.jscience.chemistry.Atom a1 = atoms.get(i);
                org.jscience.chemistry.Atom a2 = atoms.get(j);

                // Exclude bonded atoms? (Bonded atoms usually excluded from non-bonded calc
                // 1-2, 1-3)
                // We check if a bond exists between them.
                boolean bonded = false;
                for (org.jscience.chemistry.Bond b : molecule.getBondsFor(a1)) {
                    if (b.getOtherAtom(a1) == a2) {
                        bonded = true;
                        break;
                    }
                }
                if (bonded)
                    continue;

                double distMeter = a1.distanceTo(a2).to(org.jscience.measure.Units.METER).getValue().doubleValue();
                double distAngstrom = distMeter * 1e10;

                if (distAngstrom < 0.1)
                    distAngstrom = 0.1; // Clamp

                // Use generic C-C parameters for everything for now
                totalEnergy += lennardJones(distAngstrom, CC_EPSILON, CC_SIGMA);
            }
        }

        return totalEnergy;
    }
}
