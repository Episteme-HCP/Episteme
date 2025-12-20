package org.jscience.chemistry.couples;

import org.jscience.chemistry.Dissociation;
import org.jscience.chemistry.ChemicalReaction;

/**
 * A class storing known acid-base couples.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class AcidBaseCouples extends Object {

    //read an XML file containing reaction with couple for each one

    private AcidBaseCouples() {
    }

    public ChemicalReaction getHalfReaction(String name) {

        XXX
    }

    //using two half reactions
     public Dissociation getDissociation(ChemicalReaction reaction1, ChemicalReaction reaction2) {

        XX
    }

}
