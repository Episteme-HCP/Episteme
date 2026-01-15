/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface every GML property must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Property extends GMLConstruct, UnknownConstructOwner,
    FeatureOwner, GeometryOwner, PropertyOwner,
    XLinkable // it may use XLink links
 {
    /**
     * Returns the String value of this property. This should be used
     * for "simple" properties.
     *
     * @return Null if the property contains no String value or if it is null.
     */
    public String getValueAsString();
}
