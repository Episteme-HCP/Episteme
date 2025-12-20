package org.jscience.chemistry;

import java.util.Set;

/**
 * The OxydoReduction class provides support for ionic reaction.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class Dissociation extends ChemicalReaction {

    private double pKa;

    public Dissociation(Set reactants, Set products, double pKa) {

        super(reactants, products);
        this.pKa = pKa;

    }

    //the electrons involved in this reaction
    public int getNumElectrons() {

        XXX
    }

    public double getStandardpKa() {

        return pKa;

    }

    //ions are exchanged in the solution
    modify pH

}
