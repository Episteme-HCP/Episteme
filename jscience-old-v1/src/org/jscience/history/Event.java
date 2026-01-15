package org.jscience.history;

import java.util.Date;


/**
 * A class representing the events that happen in history (or even
 * individual history) in an ordonned manner.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//if you want to set a very short event you should assume the SECOND duration, NOT MILLISECOND, which should be still very precise for biographical events
//you can for example call the 3 arguments constructor using startDate.add(Calendar.SECOND, 1) for the endDate argument.
public class Event extends Object {
    /** DOCUMENT ME! */
    private Date startDate;

    /** DOCUMENT ME! */
    private Date endDate;

    /** DOCUMENT ME! */
    private String happening;

    //this constructor assumes the end date has not yet elapsed
    /**
     * Creates a new Event object.
     *
     * @param startDate DOCUMENT ME!
     * @param happening DOCUMENT ME!
     */
    public Event(Date startDate, String happening) {
        if ((startDate != null) && (happening != null) &&
                (happening.length() > 0)) {
            this.startDate = startDate;
            this.endDate = null;
            this.happening = happening;
        } else {
            throw new IllegalArgumentException(
                "The Event constructor can't have null arguments (and happening can't be empty).");
        }
    }

    //if the HistoricalIndividual this events refers to a dead people
    /**
     * Creates a new Event object.
     *
     * @param startDate DOCUMENT ME!
     * @param endDate DOCUMENT ME!
     * @param happening DOCUMENT ME!
     */
    public Event(Date startDate, Date endDate, String happening) {
        if ((startDate != null) && (endDate != null) && (happening != null) &&
                (happening.length() > 0)) {
            if (startDate.before(endDate)) {
                this.startDate = startDate;
                this.endDate = endDate;
                this.happening = happening;
            } else {
                throw new IllegalArgumentException(
                    "Start date must be before end date.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Event constructor can't have null arguments (and happening can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getStartDate() {
        return startDate;
    }

    //must happen before end date
    /**
     * DOCUMENT ME!
     *
     * @param startDate DOCUMENT ME!
     */
    public void setStartDate(Date startDate) {
        if ((startDate != null) && (startDate.before(endDate))) {
            this.startDate = startDate;
        } else {
            throw new IllegalArgumentException(
                "Start date must be before end date.");
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getEndDate() {
        return endDate;
    }

    //must happen after end date
    /**
     * DOCUMENT ME!
     *
     * @param endDate DOCUMENT ME!
     */
    public void setEndDate(Date endDate) {
        if ((endDate != null) && (startDate.before(endDate))) {
            this.endDate = endDate;
        } else {
            throw new IllegalArgumentException(
                "Start date must be before end date.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHappening() {
        return happening;
    }

    //for all the following methods:
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean happensBefore(Event event) {
        if (getEndDate() != null) {
            return getEndDate().before(event.getStartDate());
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean happensAfter(Event event) {
        if (event.getEndDate() != null) {
            return event.getEndDate().before(getStartDate());
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isIncluded(Event event) {
        if ((getEndDate() != null) && (event.getEndDate() != null)) {
            return event.getStartDate().before(getStartDate()) &&
            event.getEndDate().before(getEndDate());
        } else {
            return false;
        }
    }

    //strict overlapping
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean overlaps(Event event) {
        if ((getEndDate() != null) && (event.getEndDate() != null)) {
            return (getStartDate().before(event.getStartDate()) &&
            event.getStartDate().before(getEndDate()) &&
            getEndDate().before(getEndDate())) ||
            (event.getStartDate().before(getStartDate()) &&
            getStartDate().before(event.getEndDate()) &&
            event.getEndDate().before(getEndDate()));
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSimultaneous(Event event) {
        if ((getEndDate() != null) && (event.getEndDate() != null)) {
            return getStartDate().equals(event.getStartDate()) &&
            getEndDate().equals(event.getEndDate());
        } else {
            return false;
        }
    }
}
