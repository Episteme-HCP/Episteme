package org.jscience.chemistry;

/**
 * The ChemicalReactor class works out chemical reactions.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//understand that this is a basic, ideal chemical reactor.
public class ChemicalReactor extends Reactor {
    /** DOCUMENT ME! */
    private ChemicalReaction chemicalReaction;

    /** DOCUMENT ME! */
    private ChemicalSubstrate initialConditions;

    /** DOCUMENT ME! */
    private ChemicalSubstrate currentConditions;

    //you want to study the chemicalReaction that happens in the chemicalSubstrate
    /**
     * Creates a new ChemicalReactor object.
     *
     * @param chemicalReaction DOCUMENT ME!
     * @param chemicalSubstrate DOCUMENT ME!
     */
    public ChemicalReactor(ChemicalReaction chemicalReaction,
        ChemicalSubstrate chemicalSubstrate) {
        if ((chemicalReaction != null) && (chemicalSubstrate != null)) {
            this.chemicalReaction = chemicalReaction;
            initialConditions = chemicalSubstrate;
            currentConditions = chemicalSubstrate;
        } else {
            throw new IllegalArgumentException(
                "ChemicalReactor doesn't accept null arguments.");
        }
    }

    //this is the data you initially put in and where changed due to calls to react() or getEquilibrium()
    /**
     * DOCUMENT ME!
     */
    public void revertToInitialConditions() {
        currentConditions = initialConditions;
    }

    //this is the simulator part which transforms part of the reactants into products
    /**
     * DOCUMENT ME!
     *
     * @param dt DOCUMENT ME!
     */
    public void react(double dt) {
    }

    //proceeds to the end equilibrium by reacting and reacting again from the current parameters to the final equilibrium
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChemicalSubstrate getEquilibriumCondition() {
        //entropy
    }

    //the complete energy produced in that reaction from the initial conditions to the equilibrium
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSumEnergy() {
    }
}
