package org.jscience.biology.cycles;

import org.jscience.biology.molecules.CO2;
import org.jscience.biology.molecules.H2O;
import org.jscience.biology.molecules.O2;
import org.jscience.biology.molecules.carbohydrates.Glucose;

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the photosynthesis process that takes place into
 * chloroplastes. Photosynthesis needs light (also it does not appear here).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//6 CO2 + 12 H2O ----> Glucose + 6 O2 + 6 H2O
public class Photosynthesis extends ChemicalReaction {
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

        for (i = 1; i < 13; i++) {
            water = new H2O();
            reactants.add(water);
        }

        for (i = 1; i < 7; i++) {
            carbonDioxide = new CO2();
            reactants.add(carbonDioxide);
        }

        glucose = new Glucose();
        products.add(glucose);

        for (i = 1; i < 7; i++) {
            dioxygen = new O2();
            products.add(dioxygen);
        }

        for (i = 1; i < 13; i++) {
            water = new H2O();
            products.add(water);
        }
    }

    /*
    * Constructs a Photosynthesis.
    */
    public Photosynthesis() {
        super(reactants, products);
    }
}
