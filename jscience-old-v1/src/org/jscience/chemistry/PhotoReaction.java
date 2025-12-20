package org.jscience.chemistry;

import java.util.Set;

/**
 * The PhotoReaction class describes chemical reactions that happen in the presence of light.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example exposure to light
//capture of photons
//excitation of electrons
public class PhotoReaction extends ChemicalReaction {

    public PhotoReaction(Set reactants, Set products) {

        super(reactants, products);

    }

    //there must be electrons involved in the reaction
    //the reaction may return !valid and still be valid as a chemical reaction
    public boolean isValid() {

        XXX
    }

}
