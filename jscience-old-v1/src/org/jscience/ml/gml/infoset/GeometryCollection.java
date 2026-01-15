/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface that every geometry collection must implement. Note
 * that a geometry collection may not contain geometries directly but only via
 * properties (such as geometryMember).
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface GeometryCollection extends Geometry,
    GeometryOwner // indirect ownership (via geometryMember prop.)
 {
    // no additional methods
}
