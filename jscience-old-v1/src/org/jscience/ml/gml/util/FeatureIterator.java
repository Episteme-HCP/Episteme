/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.util;

import org.jscience.ml.gml.infoset.Feature;

import java.util.Iterator;


/**
 * Defines the interface every feature iterator must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface FeatureIterator extends Iterator {
    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public Feature nextFeature();
}
