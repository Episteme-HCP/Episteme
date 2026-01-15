package org.jscience.biology.human;

import org.jscience.biology.HistoricalIndividual;

import org.jscience.psychology.Behavior;

import java.util.*;


/**
 * A class representing an individual from the Human species.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//if you fake identity for a reason or another, you are actually considered to be another person.
//it is up to you to add features such as sex, eye color, hair color, skin color, birthplace, ethnic origin, size, weight, disabilities, address, money...
//also see org.jscience.sociology.Person
public class Human extends HistoricalIndividual {
    /** DOCUMENT ME! */
    private String givenName;

    /** DOCUMENT ME! */
    private String lastName; //the birthname

    /** DOCUMENT ME! */
    private String usedName;

    /** DOCUMENT ME! */
    private Set availableBehaviors; //specific behaviors for this individual whose behaviors may be very different from one another: this is high level behaviors or skills

    //the data used here should match that of the biography (if any) (there is no check made)
    /**
     * Creates a new Human object.
     *
     * @param givenName DOCUMENT ME!
     * @param lastName DOCUMENT ME!
     */
    public Human(String givenName, String lastName) {
        super(new HumanSpecies(), Calendar.getInstance().getTime());

        if ((givenName != null) && (lastName != null) &&
                (givenName.length() > 0) && (lastName.length() > 0)) {
            this.givenName = givenName;
            this.lastName = lastName;
            this.usedName = null;
            this.availableBehaviors = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Human constructor can't have null or empty arguments.");
        }
    }

/**
     * Creates a new Human object.
     *
     * @param givenName   DOCUMENT ME!
     * @param lastName    DOCUMENT ME!
     * @param dateOfBirth DOCUMENT ME!
     */
    public Human(String givenName, String lastName, Date dateOfBirth) {
        super(new HumanSpecies(), dateOfBirth);
        this.givenName = givenName;
        this.lastName = lastName;
        this.usedName = null;
        this.availableBehaviors = Collections.EMPTY_SET;
    }

    /**
     * The given name, also known as firstname in some places
     *
     * @return DOCUMENT ME!
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUsedName() {
        return lastName;
    }

    //don't forget to set to null after a divorce
    /**
     * DOCUMENT ME!
     *
     * @param usedName DOCUMENT ME!
     */
    public void setUsedName(String usedName) {
        this.usedName = usedName;
    }

    //returns UsedName if any and if not birthname
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBestName() {
        if (usedName != null) {
            return usedName;
        } else {
            return lastName;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAvailableBehaviors() {
        return availableBehaviors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param behaviors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setAvailableBehaviors(Set behaviors) {
        Iterator iterator;
        boolean valid;

        if (behaviors != null) {
            iterator = behaviors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Behavior;
            }

            if (valid) {
                this.availableBehaviors = behaviors;
            } else {
                throw new IllegalArgumentException(
                    "The Set of behaviors should contain only Behaviors.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of behaviors shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param behavior DOCUMENT ME!
     */
    public void addAvailableBehavior(Behavior behavior) {
        availableBehaviors.add(behavior);
    }

    /**
     * DOCUMENT ME!
     *
     * @param behavior DOCUMENT ME!
     */
    public void removeAvailableBehavior(Behavior behavior) {
        availableBehaviors.remove(behavior);
    }
}
