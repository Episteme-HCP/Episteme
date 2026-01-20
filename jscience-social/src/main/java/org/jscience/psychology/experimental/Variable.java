package org.jscience.psychology.experimental;

import org.jscience.util.Named;


/**
 * A class representing a psychology variable (whether intergroups or
 * intragroups). A variable is the thing you are working on or the thing you
 * measure.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Variable extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String unit; //useing the international system

/**
     * Creates a new Variable object.
     *
     * @param name DOCUMENT ME!
     * @param unit DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Variable(String name, String unit) {
        if ((name != null) && (unit != null) && (name.length() > 0)) {
            this.name = name;
            this.unit = unit;
        } else {
            throw new IllegalArgumentException(
                "The ValuedVariable constructor can't have null arguments (and name can't be empty).");
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
    public String getUnit() {
        return unit;
    }
}
