package org.jscience.chemistry.electrochemistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Standard Electrode Potentials (E°) at 25°C.
 * Relative to Standard Hydrogen Electrode (SHE).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class StandardPotentials {

    private static final Map<String, Double> POTENTIALS = new HashMap<>();

    static {
        // Halogens
        POTENTIALS.put("F2 + 2e- -> 2F-", 2.87);
        POTENTIALS.put("Cl2 + 2e- -> 2Cl-", 1.36);
        POTENTIALS.put("Br2 + 2e- -> 2Br-", 1.07);
        POTENTIALS.put("I2 + 2e- -> 2I-", 0.54);

        // Metals
        POTENTIALS.put("Ag+ + e- -> Ag", 0.80);
        POTENTIALS.put("Cu2+ + 2e- -> Cu", 0.34);
        POTENTIALS.put("Pb2+ + 2e- -> Pb", -0.13);
        POTENTIALS.put("Sn2+ + 2e- -> Sn", -0.14);
        POTENTIALS.put("Ni2+ + 2e- -> Ni", -0.25);
        POTENTIALS.put("Fe2+ + 2e- -> Fe", -0.44);
        POTENTIALS.put("Zn2+ + 2e- -> Zn", -0.76);
        POTENTIALS.put("Al3+ + 3e- -> Al", -1.66);
        POTENTIALS.put("Mg2+ + 2e- -> Mg", -2.37);
        POTENTIALS.put("Na+ + e- -> Na", -2.71);
        POTENTIALS.put("Ca2+ + 2e- -> Ca", -2.87);
        POTENTIALS.put("K+ + e- -> K", -2.93);
        POTENTIALS.put("Li+ + e- -> Li", -3.04);

        // Others
        POTENTIALS.put("2H+ + 2e- -> H2", 0.00); // SHE
        POTENTIALS.put("O2 + 4H+ + 4e- -> 2H2O", 1.23);
    }

    /**
     * Gets the standard potential for a given half-reaction representation.
     * 
     * @param halfReaction Key string (e.g., "Cu2+ + 2e- -> Cu")
     * @return E° in Volts, or null if not found
     */
    public static Double getPotential(String halfReaction) {
        return POTENTIALS.get(halfReaction);
    }

    /**
     * Lists all registered half-reactions.
     */
    public static java.util.Set<String> getAvailableReactions() {
        return POTENTIALS.keySet();
    }
}
