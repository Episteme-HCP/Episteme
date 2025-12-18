package org.jscience.chemistry.crystallography;

/**
 * Space Groups (Simplified).
 * Represents crystal symmetry groups.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public enum SpaceGroup {
    P1("Triclinic", 1),
    P_1("Triclinic", 2),
    P2("Monoclinic", 3),
    Cm("Monoclinic", 8),
    P2_m("Monoclinic", 10),
    Pmm2("Orthorhombic", 25),
    P4("Tetragonal", 75),
    R3("Trigonal", 146),
    P6("Hexagonal", 168),
    P2_3("Cubic", 195),
    Fm_3m("Cubic", 225);

    private final String crystalSystem;
    private final int number;

    SpaceGroup(String system, int number) {
        this.crystalSystem = system;
        this.number = number;
    }

    public String getCrystalSystem() {
        return crystalSystem;
    }

    public int getNumber() {
        return number;
    }
}
