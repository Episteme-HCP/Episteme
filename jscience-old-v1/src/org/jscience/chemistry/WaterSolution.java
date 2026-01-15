package org.jscience.chemistry;

import java.util.HashMap;


/**
 * The WaterSolution class defines the solution in which many biological
 * processes occur.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//the solution is already filled with water at pH7
public final class WaterSolution extends Solution {
    //one liter
    public WaterSolution() {
        this(1.0d);
    }

    /**
     * Creates a new WaterSolution object.
     *
     * @param mass DOCUMENT ME!
     */
    public WaterSolution(double mass) {
        Element hydrogenElement;
        Element oxygenElement;
        Atom hydrogenAtom1;
        Atom hydrogenAtom2;
        Atom oxygenAtom;
        Bond bond1;
        Bond bond2;

        HashMap contents;

        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        oxygenAtom = new Atom(oxygenElement);

        bond1 = new Bond(oxygenAtom, hydrogenAtom1);
        bond2 = new Bond(oxygenAtom, hydrogenAtom2);

        contents = new HashMap();
        contents.put(new Molecule(oxygenAtom), new Double(mass));
        super(contents, ChemicalConditions.getDefaultChemicalConditions());

        //pH, the logarithmic concentration of dissociated particles
        //made of the dissociation of H20 into H+ and OH-
        //how do I set it ?
        //and manage concentration so that there is a huge amount of water
    }
}
