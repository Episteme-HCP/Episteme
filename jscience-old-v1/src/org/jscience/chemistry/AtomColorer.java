package org.jscience.chemistry;

import java.awt.*;


/**
 * Interface to a class for coloring atoms.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface AtomColorer {
    /**
     * Returns the color for a certain element type
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getAtomColor(Element e);
}
