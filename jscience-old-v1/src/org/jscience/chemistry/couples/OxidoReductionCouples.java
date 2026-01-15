package org.jscience.chemistry.couples;

import org.jscience.chemistry.ChemicalReaction;
import org.jscience.chemistry.OxidoReduction;

/**
 * A class storing known oxydo-reduction couples.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class OxidoReductionCouples extends Object {

    //read an XML file containing reaction with electro couple for each one

    private OxidoReductionCouples() {
    }

    public ChemicalReaction getHalfReaction(String name) {

        XXX
    }

    //using two half reactions
    public OxidoReduction getOxidoReduction(ChemicalReaction reaction1, ChemicalReaction reaction2) {

        XX
    }

}
