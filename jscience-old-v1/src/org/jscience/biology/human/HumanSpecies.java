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
