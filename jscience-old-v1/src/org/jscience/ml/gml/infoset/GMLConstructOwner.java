/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import org.jscience.ml.gml.util.GMLConstructIterator;


/**
 * Defines the interface every owner of GML constructs must implement. Note
 * that a class need not be a GML construct to own one of them.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface GMLConstructOwner {
    /**
     * Provide access to all GML constructs. This method may be used to
     * recursively scan the GML object model.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstructIterator getGMLConstructIterator();
}
