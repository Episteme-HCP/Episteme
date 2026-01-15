package org.jscience.biology.cycles;

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the oxidative phosphorylation process that takes
 * place into cells.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class OxidativePhosphorylation extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        //throw new RuntimeException("Not yet implemented.");
        //TODO ADP3- + H+ + Pi ↔ ATP4- + H2O
    }

/**
     * }
     * Constructs a Oxidative Phosphorylation.
     */
    public OxidativePhosphorylation() {
        super(reactants, products);
    }
}
