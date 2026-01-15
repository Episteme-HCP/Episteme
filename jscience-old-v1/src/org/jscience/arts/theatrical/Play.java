package org.jscience.arts.theatrical;

import org.jscience.biology.Individual;

import org.jscience.economics.Community;
import org.jscience.economics.money.Currency;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import org.jscience.arts.ArtsConstants;
import org.jscience.arts.Artwork;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing a show, or play. It can be anything from a musical
 * comedy, a danced show, an opera or a mime.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can also use org.jscience.arts.printed.Book to describe the plot (or use the StoryTelling class)
public class Play extends Artwork {
    /** DOCUMENT ME! */
    private Vector acts;

    /** DOCUMENT ME! */
    private Set directors; //the Set of individuals participating in the creation of the performance

    /** DOCUMENT ME! */
    private Set actors; //the Set of individuals participating in the acting of the performance

    //all the elements of the Vector should be acts.
    /**
     * Creates a new Play object.
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param producer DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param directors DOCUMENT ME!
     * @param authors DOCUMENT ME!
     * @param actors DOCUMENT ME!
     * @param acts DOCUMENT ME!
     */
    public Play(String name, String description, Community producer,
        Date productionDate, Identification identification, Set directors,
        Set authors, Set actors, Vector acts) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.THEATER);

        Iterator iterator;
        boolean valid;

        if ((acts != null) && (directors != null) && (directors.size() > 0) &&
                (actors != null) && (actors.size() > 0)) {
            iterator = acts.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Act;
            }

            if (valid) {
                iterator = directors.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Individual;
                }

                if (valid) {
                    iterator = actors.iterator();
                    valid = true;

                    while (iterator.hasNext() && valid) {
                        valid = iterator.next() instanceof Individual;
                    }

                    if (valid) {
                        this.acts = acts;
                        this.directors = directors;
                        this.actors = actors;
                    } else {
                        throw new IllegalArgumentException(
                            "The Set of actors should contain only Individuals.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "The Set of directors should contain only Individuals.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Vector of acts should contain only Acts.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Movie constructor can't have null arguments (and owners and directors shouldn't be empty).");
        }
    }

    //a Set of Individuals
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getDirectors() {
        return directors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param director DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addDirector(Individual director) {
        if (directors != null) {
            directors.add(director);
        } else {
            throw new IllegalArgumentException("You can't add a null director.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param director DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeDirector(Individual director) {
        if ((directors.size() > 1)) {
            directors.remove(director);
        } else {
            throw new IllegalArgumentException(
                "You can't remove last director.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param directors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setDirectors(Set directors) {
        Iterator iterator;
        boolean valid;

        if ((directors != null) && (directors.size() > 0)) {
            iterator = directors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.directors = directors;
            } else {
                throw new IllegalArgumentException(
                    "The directors Set must contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty directors set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getActors() {
        return actors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param actors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setActors(Set actors) {
        Iterator iterator;
        boolean valid;

        if ((actors != null)) {
            iterator = actors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.actors = actors;
            } else {
                throw new IllegalArgumentException(
                    "The Set of actors must contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of actors can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param actor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addActor(Individual actor) {
        if (actor != null) {
            actors.add(actor);
        } else {
            throw new IllegalArgumentException("You can't add a null actor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param actor DOCUMENT ME!
     */
    public void removeActor(Individual actor) {
        actors.remove(actor);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getActs() {
        return acts;
    }
}
