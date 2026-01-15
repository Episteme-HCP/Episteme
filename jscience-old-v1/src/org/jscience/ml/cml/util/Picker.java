package org.jscience.ml.cml.util;

/**
 * Pick an object
 * <p/>
 * An interface analagous to java.util.Comparator that is used to determine whether an object matches a particualr criteria or not
 */
public interface Picker {
    /**
     * Choose whether to accpet or reject a particular object.
     *
     * @param o the object to examine.
     *
     * @return true if the object matches the criteria, false otherwise.
     */
    boolean pick(Object o);
}
