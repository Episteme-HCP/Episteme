package org.jscience.philosophy;

/**
 * A class representing a philosophy targetted towards supernatural beings,
 * the meaning of life, etc.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Religion extends Belief {
    /** DOCUMENT ME! */
    public int numberOfBelievers;

/**
     * Creates a new Religion object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Religion(String name, String comments) {
        super(name, comments);
        numberOfBelievers = 0;
    }

    //number of people obeing to this religion/cult
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfBelievers() {
        return numberOfBelievers;
    }

    //must be >=0;
    /**
     * DOCUMENT ME!
     *
     * @param believers DOCUMENT ME!
     */
    public void setNumberOfBelievers(int believers) {
        if (believers >= 0) {
            this.numberOfBelievers = believers;
        } else {
            throw new IllegalArgumentException(
                "Number of believers can't be negative.");
        }
    }

    //a religion can be quite complex:
    //chief
    //organization (hierarchy, public organization...)
    //rules
    //ceremonies
    //contracts of faith (wedding...)
    //awards (for example canonization...)
    //status (believer, priest, monk...)
    //artifacts (holy tools and remains)
    //supernatural beings (names, roles)
    //holy places, and others historical facts
    //rituals
}
