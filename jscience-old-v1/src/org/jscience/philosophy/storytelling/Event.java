package org.jscience.philosophy.storytelling;

import org.jscience.biology.Individual;

import org.jscience.geography.Place;

import java.util.Date;


/**
 * A class representing an event in a story.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//for example:
//Bob is playing football in the graveyard: new Event(new String("Playing football"), new Place(graveyardBoundingSphere), new Date(), new Person("Bob"));
//Jane is chatting with Phil: new Event(new String("chatting with Phil"), new Place(new BoundingSphere()), new Date(), new Person("Jane"));
//you should fire a new event each time your activity changes and mark the time
//so that you can compute duration of activity when subtracting two following events.
//of course you can always subdivide any event in many more events, arrange them in hierarchies or whatever
public class Event extends java.lang.Object {
    /** DOCUMENT ME! */
    private org.jscience.philosophy.storytelling.Object object;

    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Individual individual;

/**
     * Creates a new Event object.
     *
     * @param object     DOCUMENT ME!
     * @param place      DOCUMENT ME!
     * @param date       DOCUMENT ME!
     * @param individual DOCUMENT ME!
     */
    public Event(org.jscience.philosophy.storytelling.Object object,
        Place place, Date date, Individual individual) {
        this.object = object;
        this.place = place;
        this.date = date;
        this.individual = individual;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public org.jscience.philosophy.storytelling.Object getObject() {
        return object;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Individual getIndividual() {
        return individual;
    }

    //equals if all fields report to be equal
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Event event) {
        return ((this.getObject().equals(event.getObject())) &&
        (this.getPosition().equals(event.getPosition())) &&
        (this.getDate().equals(event.getDate())) &&
        (this.getIndividual().equals(event.getIndividual())));
    }
}
