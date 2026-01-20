package org.jscience.sociology;

import org.jscience.linguistics.Language;

import org.jscience.philosophy.Belief;

import org.jscience.util.Commented;
import org.jscience.util.Named;

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing the common elements of a group of individuals, what
 * we usually also call civilization.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//for taboos, see http://en.wikipedia.org/wiki/Taboo
//although not defined here food is very important for culture and may be considered
//(you could tell where you are just by what you eat)
//A common way of understanding culture sees it as consisting of three elements:
//values 
//norms 
//artifacts.

//this class is really meant to be extended as needed and only cover some real basic information.
//yet, the idea is to keep it's use simple
//some more information could be:
//we could also add typical artwork, or typical artifacts (org.jscience.economics.Resource, or org.jscience.economics.decorum.Artifact)
//we could list hobbies and sports
//we could also list existing culture specific organizations (institutions)
//we could also list events on a typical day (org.jscience.history.Timeline or org.jscience.philosophy.storytelling.Event)
//we could also list some typical roles, situations
public class Culture extends Object implements Named, Commented {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Language language;

    /** DOCUMENT ME! */
    private int technologicalLevel; //please don't argue about the so called "progress thesis"

    /** DOCUMENT ME! */
    private Set beliefs;

    /** DOCUMENT ME! */
    private Set celebrations;

    /** DOCUMENT ME! */
    private Set rituals;

    /** DOCUMENT ME! */
    private int marritalType;

    /** DOCUMENT ME! */
    private String comments;

/**
     * Creates a new Culture object.
     *
     * @param name               DOCUMENT ME!
     * @param language           DOCUMENT ME!
     * @param technologicalLevel DOCUMENT ME!
     * @param beliefs            DOCUMENT ME!
     * @param celebrations       DOCUMENT ME!
     * @param rituals            DOCUMENT ME!
     * @param marritalType       DOCUMENT ME!
     * @param comments           DOCUMENT ME!
     */
    public Culture(String name, Language language, int technologicalLevel,
        Set beliefs, Set celebrations, Set rituals, int marritalType,
        String comments) {
        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (language != null) &&
                (beliefs != null) && (celebrations != null) &&
                (rituals != null) && (comments != null) &&
                (comments.length() > 0)) {
            iterator = beliefs.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Belief;
            }

            if (valid) {
                iterator = celebrations.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Celebration;
                }

                if (valid) {
                    iterator = celebrations.iterator();
                    valid = true;

                    while (iterator.hasNext() && valid) {
                        valid = iterator.next() instanceof Ritual;
                    }

                    if (valid) {
                        this.name = name;
                        this.language = language;
                        this.technologicalLevel = technologicalLevel;
                        this.beliefs = beliefs;
                        this.celebrations = celebrations;
                        this.rituals = rituals;
                        this.marritalType = marritalType;
                        this.comments = comments;
                    } else {
                        throw new IllegalArgumentException(
                            "The rituals Set should contain only Rituals.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "The celebrations Set should contain only Celebrations.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The beliefs Set should contain only Beliefs.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Culture constructor doesn't accept null or empty arguments (apart from beliefs, rituals and celebrations).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTechnologicalLevel() {
        return technologicalLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getBeliefs() {
        return beliefs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getRituals() {
        return rituals;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCelebrations() {
        return celebrations;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMarritalType() {
        return marritalType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    //we could also add typical artwork, or typical artifacts (org.jscience.economics.Resource, or org.jscience.economics.decorum.Artifact)
    //we could list hobbies and sports
    //we could also list existing culture specific organizations (institutions)
    //we could also list events on a typical day (org.jscience.history.Timeline or org.jscience.philosophy.Events.Event)
    //we could also list some typical roles, situations
}
