package org.jscience.linguistics;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an individual in an talking situation (cocktail,
 * etc.).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Locutor extends Role {
    /** DOCUMENT ME! */
    private Set knownLanguages; //we don't specify the degree of knowledge here, this is mainly to have a broad idea of the locutor capacities

    //may be you will only want to set mother tongues
    /**
     * Creates a new Locutor object.
     *
     * @param individual DOCUMENT ME!
     * @param situation DOCUMENT ME!
     */
    public Locutor(Individual individual, ChatSituation situation) {
        super(individual, "Locutor", situation, Role.CLIENT);
        this.knownLanguages = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getKnownLanguages() {
        return knownLanguages;
    }

    /**
     * DOCUMENT ME!
     *
     * @param language DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addLanguage(Language language) {
        if (knownLanguages != null) {
            knownLanguages.add(language);
        } else {
            throw new IllegalArgumentException("You can't add a null language.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param language DOCUMENT ME!
     */
    public void removeLanguage(Language language) {
        knownLanguages.remove(language);
    }

    /**
     * DOCUMENT ME!
     *
     * @param languages DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setLanguages(Set languages) {
        Iterator iterator;
        boolean valid;

        if (languages != null) {
            iterator = languages.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Language;
            }

            if (valid) {
                this.knownLanguages = languages;
            } else {
                throw new IllegalArgumentException(
                    "The Set of languages should contain only Languages.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of languages shouldn't be null.");
        }
    }
}
