package org.jscience.biology.cycles;

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the glycolysis process that takes place into cells.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//Glucose + 2 ADP + 2 Pi + 2 NAD+ ----> 2 Pyruvate + 2 ATP + 2 NADH + 2 H+ + 2 H2O
public class Glycolysis extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        //throw new RuntimeException("Not yet implemented.");
        //TODO Glucose + 2 ADP + 2 Pi + 2 NAD+ ----> 2 Pyruvate + 2 ATP + 2 NADH + 2 H+ + 2 H2O
    }

/**
     * Constructs a Glycolysis.
     */
    public Glycolysis() {
        super(reactants, products);
    }
}
