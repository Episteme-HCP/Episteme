package org.jscience.util;

/**
 * A class representing a an Object which can take three values: true,
 * false and unknown. It is therefore a kind of extended boolean.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Trivaluate extends Object {
    //          The Trivaluate object corresponding to the primitive value false.
    /** DOCUMENT ME! */
    public static Trivaluate FALSE = new Trivaluate("False");

    //          The Trivaluate object corresponding to the primitive value true.
    /** DOCUMENT ME! */
    public static Trivaluate TRUE = new Trivaluate("True");

    //          The Trivaluate object corresponding to the primitive value unknown.
    /** DOCUMENT ME! */
    public static Trivaluate UNKNOWN = new Trivaluate("Unknown");

    //          The Class object representing the primitive type boolean.
    /** DOCUMENT ME! */
    public static Class TYPE = TRUE.getClass();

    /** DOCUMENT ME! */
    private boolean isValuated; // unknown (false) or valuated (true)

    /** DOCUMENT ME! */
    private boolean value; //true of false but meaningful only if isValuated = true

    //Constructor Summary
/**
     * Creates a new Trivaluate object.
     *
     * @param value DOCUMENT ME!
     */
    public Trivaluate(boolean value) {
        isValuated = true;
        this.value = value;
    }

    //          Allocates a Trivaluate object representing the value true if the string argument is not null and is equal, ignoring case, to the string "true", the value false if the string argument is not null and is equal, ignoring case, to the string "false", unknown otherwise.
/**
     * Creates a new Trivaluate object.
     *
     * @param s DOCUMENT ME!
     */
    public Trivaluate(String s) {
        String val;

        if (s != null) {
            val = s.toLowerCase();

            if (val == "true") {
                isValuated = true;
                value = true;
            } else if (val == "false") {
                isValuated = true;
                value = false;
            } else {
                isValuated = false;

                //value = false;
            }
        } else {
            isValuated = false;

            //value = false;
        }
    }

    //Method Summary
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean booleanValue() {
        return isValuated && value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isValuated() {
        return isValuated;
    }

    //you should first check if the Trivaluate is valuated
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getValue() {
        return value;
    }

    //degenerate and
    /**
     * DOCUMENT ME!
     *
     * @param t1 DOCUMENT ME!
     * @param t2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Trivaluate and(Trivaluate t1, Trivaluate t2) {
        if ((t1 != null) && (t2 != null)) {
            if ((t1.isValuated()) && (t2.isValuated())) {
                if ((t1.getValue()) && (t2.getValue())) {
                    return Trivaluate.TRUE;
                } else {
                    return Trivaluate.FALSE;
                }
            } else {
                return Trivaluate.UNKNOWN;
            }
        } else {
            return Trivaluate.UNKNOWN;
        }
    }

    //degenerate or
    /**
     * DOCUMENT ME!
     *
     * @param t1 DOCUMENT ME!
     * @param t2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Trivaluate or(Trivaluate t1, Trivaluate t2) {
        if ((t1 != null) && (t2 != null)) {
            if ((t1.isValuated()) && (t2.isValuated())) {
                if ((t1.getValue()) || (t2.getValue())) {
                    return Trivaluate.TRUE;
                } else {
                    return Trivaluate.FALSE;
                }
            } else {
                return Trivaluate.UNKNOWN;
            }
        } else {
            return Trivaluate.UNKNOWN;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Trivaluate not(Trivaluate t) {
        if ((t != null)) {
            if (t.isValuated()) {
                if (t.getValue()) {
                    return Trivaluate.FALSE;
                } else {
                    return Trivaluate.TRUE;
                }
            } else {
                return Trivaluate.UNKNOWN;
            }
        } else {
            return Trivaluate.UNKNOWN;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void not() {
        if (isValuated) {
            value = !value;
        }
    }

    //          Returns true if and only if the argument is not null and is a Boolean object that represents the same boolean value as this object.
    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        Trivaluate t;

        if (obj != null) {
            if (obj instanceof Trivaluate) {
                t = (Trivaluate) obj;

                return (isValuated() == t.isValuated) &&
                (getValue() == t.getValue());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //          Allocates a Trivaluate object representing the value true if the string argument is not null and is equal, ignoring case, to the string "true", the value false if the string argument is not null and is equal, ignoring case, to the string "false", unknown otherwise.
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Trivaluate getTrivaluate(String name) {
        return new Trivaluate(name);
    }

    //          Returns a hash code for this Trivaluate object.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int i;

        i = 0;

        if (isValuated) {
            i++;

            if (value) {
                i++;
            }
        }

        return i;
    }

    //          Returns a String object representing this Trivaluate value.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (isValuated) {
            if (value) {
                return new String("true");
            } else {
                return new String("false");
            }
        } else {
            return new String("unknown");
        }
    }

    //          Returns a String object representing the specified boolean.
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(Trivaluate t) {
        if (t != null) {
            return t.toString();
        } else {
            return null;
        }
    }

    //          Returns a Trivaluate instance representing the specified boolean value.
    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Trivaluate valueOf(boolean b) {
        if (b) {
            return Trivaluate.TRUE;
        } else {
            return Trivaluate.FALSE;
        }
    }

    //Returns a Trivaluate with a value represented by the specified String.
    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Trivaluate valueOf(String s) {
        return getTrivaluate(s);
    }
}
