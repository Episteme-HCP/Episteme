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
 * A class describing a movie, which is basically a filmed play.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Movie extends Artwork {
    //genres retaken after www.imdb.com
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int ACTION = 1;

    /** DOCUMENT ME! */
    public final static int ADVENTURE = 2;

    /** DOCUMENT ME! */
    public final static int ANIMATION = 4;

    /** DOCUMENT ME! */
    public final static int FAMILY = 8;

    /** DOCUMENT ME! */
    public final static int COMEDY = 16;

    /** DOCUMENT ME! */
    public final static int CRIME = 32;

    /** DOCUMENT ME! */
    public final static int DOCUMENTARY = 64;

    /** DOCUMENT ME! */
    public final static int DRAMA = 128;

    /** DOCUMENT ME! */
    public final static int FANTASY = 256;

    /** DOCUMENT ME! */
    public final static int FILM_NOIR = 512;

    /** DOCUMENT ME! */
    public final static int HORROR = 1024;

    /** DOCUMENT ME! */
    public final static int INDEPENDANT = 2048;

    /** DOCUMENT ME! */
    public final static int MUSICAL = 4096;

    /** DOCUMENT ME! */
    public final static int MYSTERY = 8192;

    /** DOCUMENT ME! */
    public final static int ROMANCE = 16384;

    /** DOCUMENT ME! */
    public final static int SCIENCE_FICTION = 32768;

    /** DOCUMENT ME! */
    public final static int THRILLER = 65536;

    /** DOCUMENT ME! */
    public final static int WAR = 65536 * 2;

    /** DOCUMENT ME! */
    public final static int WESTERN = 65536 * 4;

    /** DOCUMENT ME! */
    private Vector scenes;

    /** DOCUMENT ME! */
    private Set directors; //the Set of individuals participating in the creation of the performance

    /** DOCUMENT ME! */
    private Set actors; //the Set of individuals participating in the acting of the performance

    /** DOCUMENT ME! */
    private int kind;

/**
     * Creates a new Movie object.
     *
     * @param name           DOCUMENT ME!
     * @param description    DOCUMENT ME!
     * @param producer       DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param directors      DOCUMENT ME!
     * @param authors        DOCUMENT ME!
     * @param actors         DOCUMENT ME!
     * @param kind           DOCUMENT ME!
     * @param scenes         DOCUMENT ME!
     */
    public Movie(String name, String description, Community producer,
        Date productionDate, Identification identification, Set directors,
        Set authors, Set actors, int kind, Vector scenes) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.CINEMA);

        Iterator iterator;
        boolean valid;

        if ((scenes != null) && (directors != null) && (directors.size() > 0) &&
                (actors != null) && (actors.size() > 0)) {
            iterator = scenes.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Scene;
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
                        this.scenes = scenes;
                        this.directors = directors;
                        this.actors = actors;
                        this.kind = kind;
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
                    "The Vector of scenes should contain only Scenes.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Movie constructor can't have null arguments (and owners and directors shouldn't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getScenes() {
        return scenes;
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
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */
    public void setKind(int kind) {
        this.kind = kind;
    }
}
