package org.jscience.biology.cycles;

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the Krebs (citrate) respiration cycle that takes
 * place into mitochondries.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//When oxygen gas (O2) accepts the hydrogen
//atom, water forms, and the process is called aerobic
//respiration. When an inorganic molecule other than oxygen
//accepts the hydrogen, the process is called anaerobic
//respiration. When an organic molecule accepts the hydrogen
//atom, the process is called fermentation.
//Raven chapter 9 p 160
//2 Acetyl CoA + 6NAD+ + 2FAD + 2ADP + 2Pi + 2H2O ---->   4CO2 + 6NADH + 6H+ + 2FADH2 + 2ATP + 2 coenzyme A
public class CitricAcidCycle extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        //throw new RuntimeException("Not yet implemented.");
        //TODO
        //2 Acetyl CoA + 6NAD+ + 2FAD + 2ADP + 2Pi + 2H2O ---->   4CO2 + 6NADH + 6H+ + 2FADH2 + 2ATP + 2 coenzyme A
    }

/**
     * Constructs a Respiration.
     */
    public CitricAcidCycle() {
        super(reactants, products);
    }
}
