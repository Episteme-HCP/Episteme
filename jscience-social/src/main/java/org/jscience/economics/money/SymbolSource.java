package org.jscience.economics.money;

import org.jscience.util.UnavailableDataException;

import java.util.Set;


/**
 * Interface for classes that perform symbol searches.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//inspired from http://www.neuro-tech.net/javadocs/jfl/index.html
public interface SymbolSource {
    //the substring to search
    /**
     * DOCUMENT ME!
     *
     * @param expression DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnavailableDataException DOCUMENT ME!
     */
    public Set search(String expression) throws UnavailableDataException;
}
