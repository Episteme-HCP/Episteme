/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
