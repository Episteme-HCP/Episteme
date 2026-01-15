package org.jscience.philosophy.storytelling;

/**
 * A class representing the object of an Event (what happens).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Object extends java.lang.Object {
    /** DOCUMENT ME! */
    private String object; //the description

/**
     * Creates a new Object object.
     *
     * @param object DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object(String object) {
        if ((object != null) && (object.length() > 0)) {
            this.object = object;
        } else {
            throw new IllegalArgumentException(
                "The Object constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getObject() {
        return object;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(java.lang.Object o) {
        if ((o != null) &&
                (o instanceof org.jscience.philosophy.storytelling.Object)) {
            return this.getObject().equals(((Object) o).getObject());
        } else {
            return false;
        }
    }

    //public boolean isActIncluded(org.jscience.philosophy.storytelling.Object object);
    //for example driving contains the act of using the car key
}
