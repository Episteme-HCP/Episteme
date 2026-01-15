/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name. Made it a property owner.
 */
package org.jscience.ml.gml.infoset;

/**
 * This interface symbolizes an XML element that cannot be mapped to a GML
 * construct.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface UnknownConstruct extends GMLConstruct, UnknownConstructOwner,
    FeatureOwner, GeometryOwner, PropertyOwner {
    /**
     * Converts the unknown construct to a form familiar to the client.
     * For example, an implementation of this method may return a
     * corresponding DOM element.
     *
     * @return DOCUMENT ME!
     */
    public Object convertToFamiliarForm();
}
