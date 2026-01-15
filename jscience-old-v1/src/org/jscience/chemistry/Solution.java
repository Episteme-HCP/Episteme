package org.jscience.chemistry;

import java.util.HashMap;

/**
 * The Solution class defines a solution, that is a mix of big quantities of molecules in liquid state.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class Solution extends ChemicalSubstrate {

    public Solution(Molecule molecule, double mass, ChemicalConditions chemicalConditions) {

        super(molecule, mass, chemicalConditions);

    }

    //the molecule/mass for each of the components of the solution
    //each molecule must be liquid (: between fusion and vaporisation), this is unchecked
    public Solution(HashMap contents, ChemicalConditions chemicalConditions) {

        super(contents, chemicalConditions);

    }

//vapor pressure mixed liquids
    http://en.wikipedia.org/wiki/Raoult%27s_law

}
