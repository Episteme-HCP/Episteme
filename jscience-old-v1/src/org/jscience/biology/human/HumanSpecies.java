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

package org.jscience.biology.human;

import org.jscience.biology.BiologyConstants;
import org.jscience.biology.Individual;
import org.jscience.biology.Species;
import org.jscience.biology.taxonomy.SimpleTaxon;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a well known species.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also called mankind
public class HumanSpecies extends Species {
/**
     * Creates a new HumanSpecies object.
     */
    public HumanSpecies() {
        super(new SimpleTaxon("Homo Sapiens", "Human"));
        setReproductionMode(BiologyConstants.SEXUAL);
        setPredationMode(BiologyConstants.OMNIVORE);
    }

    //uses only the first element from the Set which must consist of individuals from HumanSpecies
    //this is completly NOT human reproduction
    //we should have sperm, ovum, gamete
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param individuals DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set reproduce(Individual individual, Set individuals) {
        Iterator iterator;
        boolean valid;
        HashSet result;
        Object currentElement;
        Individual resultingIndividual;

        if ((individual != null) && (individuals != null) &&
                (individuals.size() > 0)) {
            iterator = individuals.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                currentElement = iterator.next();
                valid = (currentElement instanceof Individual) &&
                    (((Individual) currentElement).getSpecies() instanceof HumanSpecies);
            }

            if (valid) {
                iterator = individuals.iterator();
                valid = individual.getStage() == BiologyConstants.ADULT;

                while (iterator.hasNext() && valid) {
                    valid = (((Individual) iterator.next()).getStage() == BiologyConstants.ADULT);
                }

                if (valid) {
                    result = new HashSet();
                    resultingIndividual = (Individual) individual.clone();
                    resultingIndividual.setStage(BiologyConstants.YOUNG);
                    result.add(resultingIndividual);
                } else {
                    throw new IllegalArgumentException(
                        "All mating individuals should be Individual.ADULT.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Set of individuals should contain only HumanSpecies.");
            }
        } else {
            throw new IllegalArgumentException(
                "Reproduction can't happen if null arguments.");
        }

        return result;
    }
}
