package org.jscience.util;

/**
 * An interface to mark up something that contains an extra field parameter to support additional information storage without (sometimes inconveninet subclassing).
 *
 * @author Silvere Martin-Michiellot
 */

//perhaps should be renamed parametrized

//this interface should rather be avoided as it is rather bad object oriented programming practice
//you should instead use the decorator pattern around object which currently implement this interface
//see http://en.wikipedia.org/wiki/Decorator_pattern
//you can also consider using this interface as an entry for a visitor pattern 
//although this is not the intented design
//http://en.wikipedia.org/wiki/Visitor_pattern
public interface Parameterized {
    /**
     * Defines the parameter in an unspecified manner.
     *
     * @return DOCUMENT ME!
     */
    public Object getExtraParameter();

    /**
     * Defines the parameter in an unspecified manner.
     *
     * @param parameter DOCUMENT ME!
     */
    public void setExtraParameter(Object parameter);
}
