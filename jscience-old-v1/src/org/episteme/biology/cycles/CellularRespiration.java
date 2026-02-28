package org.episteme.biology.cycles;

import org.episteme.biology.molecules.CO2;
import org.episteme.biology.molecules.H2O;
import org.episteme.biology.molecules.O2;
import org.episteme.biology.molecules.carbohydrates.Glucose;

import org.episteme.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the degradation process that takes place into
 * cells.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//Glucose + 6 O2  ----> 6 CO2 + 6 H2O
public class CellularRespiration extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        O2 dioxygen;
        CO2 carbonDioxide;
        H2O water;
        Glucose glucose;
        int i;

        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        glucose = new Glucose();
        reactants.add(glucose);

        for (i = 1; i < 7; i++) {
            dioxygen = new O2();
            reactants.add(dioxygen);
        }

        for (i = 1; i < 7; i++) {
            water = new H2O();
            products.add(water);
        }

        for (i = 1; i < 7; i++) {
            carbonDioxide = new CO2();
            products.add(carbonDioxide);
        }
    }

/**
     * } Constructs a Cellular Respiration.
     */
    public CellularRespiration() {
        super(reactants, products);
    }
}
