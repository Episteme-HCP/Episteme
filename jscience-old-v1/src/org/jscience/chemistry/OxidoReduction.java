package org.jscience.chemistry;

/**
 * The OxydoReduction class provides support for ionic reaction.
 * @version 1.0
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 */

import org.jscience.physics.electricity.circuits.Generator;

import java.util.Set;

public class OxidoReduction extends ChemicalReaction {

    private double potential;

    public OxidoReduction(Set reactants, Set products, double potential) {

        super(reactants, products);
        this.potential = potential;

    }

    //the electrons involved in this reaction
    public int getNumElectrons() {

        XXX
    }

    public double getStandardPotential() {

        return potential;

    }

    //build out a pile using this reaction to define actual current
    public Generator getGenerator(double period, int signal) {
        XXXXXXXXXX DC
        and potential
        return new Generator(getPotential(), period, signal);

    }

    //ions are exchanged in the solution
    modify pH

    public double getPotential() {

        http:
//fr.encarta.msn.com/encyclopedia_761591014/oxydor%C3%A9duction.html
        E0 + RT / nF
        ln(Ox / Red)

    }

}
