/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface that every implementation of a GML attribute must
 * implement. A GML attribute is any attribute appearing in a GML document.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Attribute extends XMLDescribable {
    /**
     * Returns the GML construct that owns this XML attribute.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstruct getOwner();

    /**
     * Returns the value of this attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getValue();
}
