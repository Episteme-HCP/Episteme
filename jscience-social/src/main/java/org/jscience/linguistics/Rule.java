package org.jscience.linguistics;

/**
 * The Rule class defines a rule used by a grammar to parse a language.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Rule extends Object {
    //defines the transformation of a vector of symbols into another vector of symbols (terminals or not)
    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String[] transform(String string);
}
