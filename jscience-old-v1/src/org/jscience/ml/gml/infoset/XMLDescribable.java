/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface that every GML object that has a corresponding XML
 * element in the GML schema must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface XMLDescribable {
    /**
     * Returns the XML descriptor for this "object". XML descriptors
     * contain information such as XML element tag, namespace, type.
     *
     * @return Must return a valid XMLDescriptor object even if the object was
     *         not created from an XML source.
     */
    public XMLDescriptor getXMLDescriptor();
}
