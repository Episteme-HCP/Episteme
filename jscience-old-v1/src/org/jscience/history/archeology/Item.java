package org.jscience.history.archeology;

import org.jscience.bibliography.BibRef;

import org.jscience.biology.human.Human;

import org.jscience.economics.Organization;
import org.jscience.economics.money.Currency;

import org.jscience.geography.Place;
import org.jscience.geography.Places;

import org.jscience.measure.Amount;
import org.jscience.measure.Analysis;
import org.jscience.measure.Description;
import org.jscience.measure.Identification;

import org.jscience.arts.Artwork;
import org.jscience.arts.Restoration;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing an item. This item can be about whatever you want,
 * usually a fossilized bone, or plant, a crafted artifact but also a pile of
 * stones.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Item extends org.jscience.economics.resources.Object {
    //what can be done on site
    //items have no name since this could be misleading the correct description of the object. Actuals users of this package, send me an email if really this is bothering you too much.
    /** DOCUMENT ME! */
    private Set discoverers; //A Set of Human, who found it

    /** DOCUMENT ME! */
    private Date discoveryDate; //date when it was found

    /** DOCUMENT ME! */
    private Place originalPosition; //where it was found, using GeoTransform external package, sorry

    //what is done at a latter stage
    /** DOCUMENT ME! */
    private Civilization civilization;

    //what may also be done
    /** DOCUMENT ME! */
    private Vector extraDescriptions; //Vector of Descriptions, further descriptions, by people not on site, should include the date/author by which each description is made

    /** DOCUMENT ME! */
    private Vector publications; //Vector of BibRef, publications

    /** DOCUMENT ME! */
    private Vector analysis; //a Vector of Analysis, analysis should include (test, date, lab who provides the test, result, comments)

    /** DOCUMENT ME! */
    private Vector restorations; //a Vector of Restorations, restorations should include (process, date, museum who provides the restoration, result, comments)

/**
     * Creates a new Item object.
     *
     * @param organization     DOCUMENT ME!
     * @param name             DOCUMENT ME!
     * @param description      DOCUMENT ME!
     * @param productionDate   DOCUMENT ME!
     * @param identification   DOCUMENT ME!
     * @param discoverers      DOCUMENT ME!
     * @param discoveryDate    DOCUMENT ME!
     * @param originalPosition DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Item(String name, String description, Organization organization,
        Date productionDate, Identification identification, Set discoverers,
        Date discoveryDate, Place originalPosition) {
        super(name, description, Amount.ONE, organization, Places.EARTH,
            productionDate, identification, Amount.valueOf(0, Currency.USD));

        Iterator iterator;
        boolean valid;

        if ((discoverers != null) && (discoverers.size() > 0) &&
                (discoveryDate != null) && (originalPosition != null)) {
            iterator = discoverers.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.discoverers = discoverers;
                this.discoveryDate = discoveryDate;
                this.originalPosition = originalPosition;
                this.civilization = null;
                this.extraDescriptions = new Vector();
                this.publications = new Vector();
                this.analysis = new Vector();
                this.restorations = new Vector();
            } else {
                throw new IllegalArgumentException(
                    "The Set of discoverers should contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Item constructor can't have null arguments (and identifiction and discoverers can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getDiscoverers() {
        return discoverers;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDateOfDiscovery() {
        return discoveryDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //you should use a TimedBoundary and the same Place Object for getPlace and getOriginalPosition
    //the original position should be the first boundary element but not necessarily, use the date of discovery to guess
    //the contemporary position should be the last boundary element
    public Place getOriginalPosition() {
        return originalPosition;
    }

    //time in seconds
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAge() {
        //does this work ???
        return new Date().getTime() - getProductionDate().getTime();
    }

    //may return null, unknown civilization
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Civilization getCivilization() {
        return civilization;
    }

    /**
     * DOCUMENT ME!
     *
     * @param civilization DOCUMENT ME!
     */
    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getExtraDescriptions() {
        return extraDescriptions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param extraDescriptions DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setExtraDescriptions(Vector extraDescriptions) {
        Iterator iterator;
        boolean valid;

        if (extraDescriptions != null) {
            iterator = extraDescriptions.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Description;
            }

            if (valid) {
                this.extraDescriptions = extraDescriptions;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of descriptions should contain only Descriptions.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of descriptions can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getPublications() {
        return publications;
    }

    /**
     * DOCUMENT ME!
     *
     * @param publications DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPublications(Vector publications) {
        Iterator iterator;
        boolean valid;

        if (publications != null) {
            iterator = publications.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof BibRef;
            }

            if (valid) {
                this.publications = publications;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of publications should contain only BibRefs.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of publications can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getAnalysis() {
        return analysis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param analysis DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setAnalysis(Vector analysis) {
        Iterator iterator;
        boolean valid;

        if (analysis != null) {
            iterator = analysis.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Analysis;
            }

            if (valid) {
                this.analysis = analysis;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of analysis should contain only Analysis.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of analysis can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getRestoration() {
        return restorations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param restorations DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setRestorations(Vector restorations) {
        Iterator iterator;
        boolean valid;

        if (restorations != null) {
            iterator = restorations.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Restoration;
            }

            if (valid) {
                this.restorations = restorations;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of restorations should contain only Restorations.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of restorations can't be null.");
        }
    }

    //just in case you want to use this item for an exposition, use this method to "convert" this item to an artwork
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Artwork getArtwork() {
        return new Artwork(getName(), getDescription(), getAmount(),
            getProducer(), getProductionPlace(), getProductionDate(),
            getIdentification(), getValue());
    }
}
