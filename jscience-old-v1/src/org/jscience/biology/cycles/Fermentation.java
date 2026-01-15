package org.jscience.biology.cycles;

import org.jscience.biology.molecules.CO2;
import org.jscience.biology.molecules.carbohydrates.Glucose;

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the fermentation process that takes place into
 * cells.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//When yeast ferments, it breaks down the sugar(C6H12O6) into exactly two molecules of ethanol (C2H6O) and two molecules of carbon dioxide (CO2).
//Ethanol fermentation (done by yeast and some types of bacteria) breaks the pyruvate down into ethanol and carbon dioxide. It is important in bread-making, brewing, and wine-making. When the ferment has a high concentration of pectin, minute quantities of methanol can be produced. Usually only one of the products is desired; in bread the alcohol is baked out, and in alcohol production the carbon dioxide is released into the atmosphere.
//Lactic acid fermentation breaks down the pyruvate into lactic acid. It occurs in the muscles of animals when they need energy faster than the blood can supply oxygen. It also occurs in some bacteria and some fungi. It is this type of bacteria that convert lactose into lactic acid in yogurt, giving it its sour taste.
//
//also see:
//The word & symbol equation for the anaerobic respiration of glucose is:
//Glucose ---> Lactic Acid + Energy
//C6H12O6 ---> 2C3H6O3 + Energy
//The energy released is about 120kJ.
public class Fermentation extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        Ethanol ethanol;
        CO2 carbonDioxide;
        Glucose glucose;
        int i;

        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        glucose = new Glucose();
        reactants.add(glucose);

        for (i = 1; i < 2; i++) {
            ethanol = new Ethanol();
            products.add(ethanol);
        }

        for (i = 1; i < 2; i++) {
            carbonDioxide = new CO2();
            products.add(carbonDioxide);
        }
    }

/**
     * } Constructs a Cellular Respiration.
     */
    public Fermentation() {
        super(reactants, products);
    }
}
