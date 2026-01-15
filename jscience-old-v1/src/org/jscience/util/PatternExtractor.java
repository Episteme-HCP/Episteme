/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;


//import org.jscience.*;
/**
 * PatternExtractor provides a way to group objects by a pattern
 * defined by an implementation.
 * Examples for such a pattern would be a hash code, the toString()
 * method or more object specific things, like group Date objects
 * by their month.
 *
 * @author Holger Antelmann
 */
public interface PatternExtractor<T> {
    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Object extractPattern(T obj);
}
