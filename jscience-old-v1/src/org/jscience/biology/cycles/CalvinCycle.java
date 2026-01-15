package org.jscience.biology.cycles;

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the Calvin cycle that takes place into
 * chloroplasts.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//6 CO2 + 12 NADPH + 12 H+ + 18 ATP → C6H12O6 + 6 H2O + 12 NADP+ + 18 ADP + 18 Pi
public class CalvinCycle extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        //throw new RuntimeException("Not yet implemented.");
        //TODO
        //6 CO2 + 12 NADPH + 12 H+ + 18 ATP → C6H12O6 + 6 H2O + 12 NADP+ + 18 ADP + 18 Pi
    }

/**
     * Constructs a CalvinCycle.
     */
    public CalvinCycle() {
        super(reactants, products);
    }
}
