/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface that every XML descriptor must implement. An XML
 * descriptor describes the XML "properties" of a GML element.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface XMLDescriptor {
    /**
     * Returns the XML local name of the GML element/attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getLocalName();

    /**
     * Returns the namespace of the GML element/attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getNamespace();

    /**
     * Returns the XML type name of the GML element/attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getTypeName();

    /**
     * Returns the namespace of the element/attribute type.
     *
     * @return DOCUMENT ME!
     */
    public String getTypeNamespace();

    /**
     * Returns the prefix of this XML element/attribute.
     *
     * @return DOCUMENT ME!
     */
    public String getPrefix();
}
