/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface that represents an abstract GML construct. Any element
 * that appears in a GML document is classified as a GML construct. In
 * addition to the known GML constructs such as Feature and Geometry, this
 * notion includes even "unknown" constructs.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface GMLConstruct extends GMLConstructOwner, XMLDescribable {
    /**
     * Returns the owner of this GML construct.
     *
     * @return In a valid GML structure, the owner should not be null.
     */
    public GMLConstructOwner getOwner();

    // methods for reading attributes
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAttributeCount();

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(int index);

    /**
     * DOCUMENT ME!
     *
     * @param namespace DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(String namespace, String localName);

    /**
     * DOCUMENT ME!
     *
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(String localName);
}
