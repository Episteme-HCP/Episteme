package org.jscience.earth;

import org.jscience.chemistry.Molecule;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * A class representing the chemical composition of a soil
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//to be used as support class for PlanetCellContents
//we don't check that the sum of contribution is equal to 1 although you should always be sure it does
public class SoilComposition extends Object {
    /** DOCUMENT ME! */
    private Map map;

/**
     * Creates a new SoilComposition object.
     */
    public SoilComposition() {
        map = Collections.EMPTY_MAP;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getComponents() {
        return map.keySet();
    }

    /**
     * DOCUMENT ME!
     *
     * @param molecule DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getContribution(Molecule molecule) {
        Object result;

        result = map.get(molecule);

        if (result != null) {
            return ((Double) result).doubleValue();
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param molecule DOCUMENT ME!
     * @param contribution DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addComponent(Molecule molecule, double contribution) {
        if (molecule != null) {
            map.put(molecule, new Double(contribution));
        } else {
            throw new IllegalArgumentException(
                "You can't addComponent of a null Molecule.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param molecule DOCUMENT ME!
     */
    public void removeComponent(Molecule molecule) {
        map.remove(molecule);
    }
}
