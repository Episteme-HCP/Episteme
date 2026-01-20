package org.jscience.linguistics;

import org.jscience.util.Tree;


/**
 * The Parser interface defines methods that all parsers for languages should
 * implement.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//parsed object isreturned as a tree whose node are applied rules and terminals the elements of the String
public interface Parser {
    /**
     * DOCUMENT ME!
     *
     * @param grammar DOCUMENT ME!
     * @param string DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Tree parse(Grammar grammar, String string);
}
